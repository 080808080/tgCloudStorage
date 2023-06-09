package com.matsko.service.impl;

import com.matsko.dao.AppUserDAO;
import com.matsko.dao.RawDataDAO;
import com.matsko.entity.AppDocument;
import com.matsko.entity.AppPhoto;
import com.matsko.entity.AppUser;
import com.matsko.entity.RawData;
import com.matsko.exception.UploadFileException;
import com.matsko.service.*;
import com.matsko.service.enums.LinkType;
import com.matsko.service.enums.ServiceCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static com.matsko.entity.enums.UserState.BASIC_STATE;
import static com.matsko.entity.enums.UserState.WAIT_FOR_EMAIL_STATE;
import static com.matsko.service.enums.ServiceCommand.*;

/**
 * Class that implements {@link MainService}.
 */
@Slf4j
@Service
public class MainServiceImpl implements MainService {

    /**
     * Field that accepts {@link RawDataDAO}.
     */
    private final RawDataDAO rawDataDAO;

    /**
     * Field that accepts {@link ProducerService}.
     */
    private final ProducerService producerService;

    /**
     * Field that accepts {@link AppUserDAO}.
     */
    private final AppUserDAO appUserDAO;

    /**
     * Field that accepts {@link FileService}.
     */
    private final FileService fileService;

    /**
     * Field that accepts {@link AppUserService}.
     */
    private final AppUserService appUserService;

    /**
     * Constructor.
     *
     * @param rawDataDAO inherits from an interface {@link JpaRepository}.
     * @param producerService service for sending responses from a node in the broker RabbitMQ.
     * @param appUserDAO data access object interface that inherits from an interface {@link JpaRepository}.
     * @param fileService service that receives the message
     *                    and performs actions to download the file and store it in the database.
     * @param appUserService service that checks user registration
     *                       and validates the correctness of filling out the email form.
     */
    public MainServiceImpl(RawDataDAO rawDataDAO,
                           ProducerService producerService,
                           AppUserDAO appUserDAO,
                           FileService fileService, AppUserService appUserService) {
        this.rawDataDAO = rawDataDAO;
        this.producerService = producerService;
        this.appUserDAO = appUserDAO;
        this.fileService = fileService;
        this.appUserService = appUserService;
    }

    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);
        var appUser = findOrSaveAppUser(update);
        var userState = appUser.getState();
        var text = update.getMessage().getText();
        var output = "";

        var serviceCommand = ServiceCommand.fromValue(text);
        if (CANCEL.equals(serviceCommand)) {
            output = cancelProcess(appUser);
        } else if (BASIC_STATE.equals(userState)) {
            output = processServiceCommand(appUser, text);
        } else if (WAIT_FOR_EMAIL_STATE.equals(userState)) {
            output = appUserService.setEmail(appUser, text);
        } else {
            log.error("Unknown user state: " + userState);
            output = "Неизвестная ошибка! Введите /cancel и попробуйте снова!";
        }

        var chatId = update.getMessage().getChatId();
        sendAnswer(output, chatId);
    }

    @Override
    public void processDocMessage(Update update) {
        saveRawData(update);
        var appUser = findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId();
        if (isNotAllowToSendContent(chatId, appUser)) {
            return;
        }

        try {
            AppDocument doc = fileService.processDoc(update.getMessage());
            String link = fileService.generationLink(doc.getId(), LinkType.GET_DOC);
            var answer = "Документ успешно загружен! "
                    + "Ссылка для скачивания: \n" + link;
            sendAnswer(answer, chatId);
        } catch (UploadFileException e) {
            log.error(e.getMessage());
            String error = "К сожалению, загрузка файла не удалась. Повторите попытку позже.";
            sendAnswer(error, chatId);
        }
    }

    @Override
    public void processPhotoMessage(Update update) {
        saveRawData(update);
        var appUser = findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId();
        if (isNotAllowToSendContent(chatId, appUser)) {
            return;
        }

        try {
            AppPhoto photo = fileService.processPhoto(update.getMessage());
            String link = fileService.generationLink(photo.getId(), LinkType.GET_PHOTO);
            var answer = "Фото успешно загружено! "
                    + "Ссылка для скачивания: \n" + link;
            sendAnswer(answer, chatId);
        } catch (UploadFileException e) {
            log.error(e.getMessage());
            String error = "К сожалению, загрузка фото не удалась. Повторите попытку позже.";
            sendAnswer(error, chatId);
        }
    }

    /**
     * Method that checks the user's registration and status.
     *
     * @param chatId chat identifier.
     * @param appUser current user.
     * @return registration confirmation and basic status of the current user.
     */
    private boolean isNotAllowToSendContent(Long chatId, AppUser appUser) {
        var userState = appUser.getState();
        if (!appUser.getIsActive()) {
            var error = "Зарегистрируйтесь или активируйте "
                    + "свою учетную запись для загрузки контента.\n" +
                    "/registration";
            sendAnswer(error, chatId);
            return true;
        } else if (!BASIC_STATE.equals(userState)) {
            var error = "Отмените текущую команду с помощью /cancel для отправки файлов.";
            sendAnswer(error, chatId);
            return true;
        }
        return false;
    }

    /**
     * Method for sending the chat reply text to a user.
     *
     * @param output response text.
     * @param chatId chat identifier.
     */
    private void sendAnswer(String output, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        producerService.producerAnswer(sendMessage);
    }

    /**
     * Method that compares the command received in chat with the list of bot commands.
     *
     * @param appUser current user.
     * @param cmd command
     * @return help message text.
     */
    private String processServiceCommand(AppUser appUser, String cmd) {
        var serviceCommand = ServiceCommand.fromValue(cmd);
        if (REGISTRATION.equals(serviceCommand)) {
            return appUserService.registerUser(appUser);
        } else if (HELP.equals(serviceCommand)) {
            return help();
        } else if (START.equals(serviceCommand)) {
            return "Приветствую! Чтобы посмотреть список доступных команд введите /help";
        } else {
            return "Неизвестная команда! Чтобы посмотреть список доступных команд введите /help";
        }
    }

    /**
     * Description of the help command.
     *
     * @return list of available commands.
     */
    private String help() {
        return "Список доступных команд:\n"
                + "/cancel - отмена выполнения текущей команды;\n"
                + "/registration - регистрация пользователя.";
    }

    /**
     * Method that sets the base state of the current user and stores the updated data in the database.
     *
     * @param appUser current user.
     * @return text message about operation cancellation.
     */
    private String cancelProcess(AppUser appUser) {
        appUser.setState(BASIC_STATE);
        appUserDAO.save(appUser);
        return "Команда отменена!";
    }

    /**
     * Method that searches for a user in the database and saves it if he's not there.
     *
     * @param update update in the chat.
     * @return the non-null value.
     */
    private AppUser findOrSaveAppUser(Update update) {
        User telegramUser = update.getMessage().getFrom();
        var optional = appUserDAO.findByTelegramUserId(telegramUser.getId());
        if (optional.isEmpty()) {
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .username(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    .isActive(false)
                    .state(BASIC_STATE)
                    .build();
            return appUserDAO.save(transientAppUser);
        }
        return optional.get();
    }

    /**
     * Method to which the update is passed and the {@link RawData} object is created.
     *
     * @param update update in the chat.
     */
    private void saveRawData(Update update) {
        RawData rawData = RawData.builder()
                .event(update)
                .build();
        rawDataDAO.save(rawData);
    }
}
