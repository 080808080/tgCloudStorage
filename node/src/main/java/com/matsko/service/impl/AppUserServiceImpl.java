package com.matsko.service.impl;

import com.matsko.dao.AppUserDAO;
import com.matsko.dto.MailParams;
import com.matsko.entity.AppUser;
import com.matsko.service.AppUserService;
import com.matsko.utils.CryptoTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import static com.matsko.entity.enums.UserState.BASIC_STATE;
import static com.matsko.entity.enums.UserState.WAIT_FOR_EMAIL_STATE;

/**
 * Class that implements {@link AppUserService}.
 */
@Slf4j
@Service
public class AppUserServiceImpl implements AppUserService {

    /**
     * Field that accepts {@link AppUserDAO}.
     */
    private final AppUserDAO appUserDAO;

    /**
     * Field that accepts {@link CryptoTool}.
     */
    private final CryptoTool cryptoTool;

    /**
     * Field that accepts address to which requests are sent to the mailing service
     * to send a letter for user activation.
     */
    @Value("${service.mail.uri}")
    private String mailServiceUri;

    /**
     * Constructor.
     *
     * @param appUserDAO data access object interface that inherits from an interface {@link JpaRepository}.
     * @param cryptoTool encrypts the generated file reference.
     */
    public AppUserServiceImpl(AppUserDAO appUserDAO, CryptoTool cryptoTool) {
        this.appUserDAO = appUserDAO;
        this.cryptoTool = cryptoTool;
    }

    @Override
    public String registerUser(AppUser appUser) {
        if (appUser.getIsActive()) {
            return "Вы уже зарегистрированы";
        } else if (appUser.getEmail() != null) {
            return "Вам на почту было отправлено письмо." +
                    " Перейдите по ссылке в письме для подтверждения регистрации.";
        }
        appUser.setState(WAIT_FOR_EMAIL_STATE);
        appUserDAO.save(appUser);
        return "Введите e-mail: ";
    }

    @Override
    public String setEmail(AppUser appUser, String email) {
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException e) {
            return "Введите корректный e-mail. Для отмены команды введите /cancel";
        }
        var optional = appUserDAO.findByEmail(email);
        if (optional.isEmpty()) {
            appUser.setEmail(email);
            appUser.setState(BASIC_STATE);
            appUser = appUserDAO.save(appUser);

            var cryptoUserId = cryptoTool.hashOf(appUser.getId());
            var response = sendRequestToMailService(cryptoUserId, email);
            if (response.getStatusCode() != HttpStatus.OK) {
                var msg = String.format("Отправка электронного письма на почту %s не удалась.", email);
                log.error(msg);
                appUser.setEmail(null);
                appUserDAO.save(appUser);
                return msg;
            }
            return "Вам на почту было отправлено письмо." +
                    " Перейдите по ссылке в письме для подтверждения регистрации";
        } else {
            return "Этот е-mail уже используется." +
                    "Введите корректный е-mail." +
                    "Для отмены команды введите /cancel";
        }
    }

    /**
     * Method that generates and sends a POST request to the mail service.
     *
     * @param cryptoUserId encrypted user id.
     * @param email user email.
     * @return ready request.
     */
    private ResponseEntity<String> sendRequestToMailService(String cryptoUserId, String email) {
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var mailParams = MailParams.builder()
                .id(cryptoUserId)
                .emailTo(email)
                .build();
        var request = new HttpEntity<>(mailParams, headers);
        return restTemplate.exchange(mailServiceUri,
                HttpMethod.POST,
                request,
                String.class);
    }
}
