package com.matsko.service.impl;

import com.matsko.dao.AppDocumentDAO;
import com.matsko.dao.AppPhotoDAO;
import com.matsko.dao.BinaryContentDAO;
import com.matsko.entity.AppDocument;
import com.matsko.entity.AppPhoto;
import com.matsko.entity.BinaryContent;
import com.matsko.exception.UploadFileException;
import com.matsko.service.FileService;
import com.matsko.service.enums.LinkType;
import com.matsko.utils.CryptoTool;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class that implements {@link FileService}.
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    /**
     * Field that accepts the key to Telegram Bot.
     */
    @Value("${token}")
    private String token;

    /**
     * Field that accepts address to which the file information from Telegram is requested.
     */
    @Value("${service.file_info.uri}")
    private String fileInfoUri;

    /**
     * Field that accepts address where you can contact and download content.
     */
    @Value("${service.file_storage.uri}")
    private String fileStorageUri;

    /**
     * Field that receives the host address.
     */
    @Value("${link.address}")
    private String LinkAddress;

    /**
     * Field that accepts {@link AppDocumentDAO}.
     */
    private final AppDocumentDAO appDocumentDAO;

    /**
     * Field that accepts {@link AppPhotoDAO}.
     */
    private final AppPhotoDAO appPhotoDAO;

    /**
     * Field that accepts {@link BinaryContentDAO}.
     */
    private final BinaryContentDAO binaryContentDAO;

    /**
     * Field that accepts {@link CryptoTool}.
     */
    private final CryptoTool cryptoTool;

    /**
     * Constructor.
     *
     * @param appDocumentDAO inherits from an interface {@link JpaRepository} for document processing.
     * @param appPhotoDAO inherits from an interface {@link JpaRepository} for photo processing.
     * @param binaryContentDAO inherits from an interface {@link JpaRepository} for {@link BinaryContent} processing.
     * @param cryptoTool encrypts the generated file reference.
     */
    public FileServiceImpl(AppDocumentDAO appDocumentDAO, AppPhotoDAO appPhotoDAO, BinaryContentDAO binaryContentDAO, CryptoTool cryptoTool) {
        this.appDocumentDAO = appDocumentDAO;
        this.appPhotoDAO = appPhotoDAO;
        this.binaryContentDAO = binaryContentDAO;
        this.cryptoTool = cryptoTool;
    }

    @Override
    public AppDocument processDoc(Message telegramMessage) {
        Document telegramDoc = telegramMessage.getDocument();
        String fileId = telegramDoc.getFileId();
        ResponseEntity<String> response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {
            BinaryContent persistentBinaryContent = getPersistentBinaryContent(response);
            AppDocument transientAppDoc = buildTransientAppDoc(telegramDoc, persistentBinaryContent);
            return appDocumentDAO.save(transientAppDoc);
        } else {
            throw new UploadFileException("Bad response from telegram service: " + response);
        }
    }

    @Override
    public AppPhoto processPhoto(Message telegramMessage) {
        var photoSizeCount = telegramMessage.getPhoto().size();
        var photoIndex = photoSizeCount > 1 ? telegramMessage.getPhoto().size() - 1 : 0;
        PhotoSize telegramPhoto = telegramMessage.getPhoto().get(photoIndex);
        String fileId = telegramPhoto.getFileId();
        ResponseEntity<String> response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {
            BinaryContent persistentBinaryContent = getPersistentBinaryContent(response);
            AppPhoto transientAppPhoto = buildTransientAppPhoto(telegramPhoto, persistentBinaryContent);
            return appPhotoDAO.save(transientAppPhoto);
        } else {
            throw new UploadFileException("Bad response from telegram service: " + response);
        }
    }

    /**
     * Retrieving an object stored in a database and having a primary key.
     *
     * @param response request response.
     * @return save the object in the database.
     */
    private BinaryContent getPersistentBinaryContent(ResponseEntity<String> response) {
        String filePath = getFilePath(response);
        byte[] fileInByte = downloadFile(filePath);
        BinaryContent transientBinaryContent = BinaryContent.builder()
                .fileAsArrayOfBytes(fileInByte)
                .build();
        return binaryContentDAO.save(transientBinaryContent);
    }

    /**
     * Getting the file path from the request response.
     *
     * @param response request response.
     * @return file path.
     */
    private String getFilePath(ResponseEntity<String> response) {
        JSONObject jsonObject = new JSONObject(response.getBody());
        return String.valueOf(jsonObject
                .getJSONObject("result")
                .getString("file_path"));
    }

    /**
     * This method takes values from the fields of a Telegram document object
     * and enters them into our object.
     *
     * @param telegramDoc Telegram document object.
     * @param persistentBinaryContent object containing the generated primary key.
     * @return ready document object.
     */
    private AppDocument buildTransientAppDoc(Document telegramDoc, BinaryContent persistentBinaryContent) {
        return AppDocument.builder()
                .telegramFileId(telegramDoc.getFileId())
                .docName(telegramDoc.getFileName())
                .binaryContent(persistentBinaryContent)
                .mimeType(telegramDoc.getMimeType())
                .fileSize(telegramDoc.getFileSize())
                .build();
    }

    /**
     * This method takes values from the fields of a Telegram photo object
     * and enters them into our object.
     *
     * @param telegramPhoto Telegram photo object.
     * @param persistentBinaryContent object containing the generated primary key.
     * @return ready photo object.
     */
    private AppPhoto buildTransientAppPhoto(PhotoSize telegramPhoto, BinaryContent persistentBinaryContent) {
        return AppPhoto.builder()
                .telegramFileId(telegramPhoto.getFileId())
                .binaryContent(persistentBinaryContent)
                .fileSize(telegramPhoto.getFileSize())
                .build();
    }

    /**
     * Method that allows to make an HTTP request.
     *
     * @param fileId Telegram file id.
     * @return request URI, request parameters, return value type, request object and HTTP method.
     */
    private ResponseEntity<String> getFilePath(String fileId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);

        return restTemplate.exchange(
                fileInfoUri,
                HttpMethod.GET,
                request,
                String.class,
                token, fileId
        );
    }

    /**
     * The final URI on which the stream is started to download content is formed.
     *
     * @param filePath file path derived from the Telegram object.
     * @return file, which is one big whole piece of.
     */
    private byte[] downloadFile(String filePath) {
        String fullUri = fileStorageUri.replace("{token}", token)
                .replace("{filePath}", filePath);
        URL urlObj = null;
        try {
            urlObj = new URL(fullUri);
        } catch (MalformedURLException e) {
            throw new UploadFileException(e);
        }
        try (InputStream is = urlObj.openStream()) {
            return is.readAllBytes();
        } catch (IOException e) {
            throw new UploadFileException(urlObj.toExternalForm(), e);
        }
    }

    @Override
    public String generationLink(Long docId, LinkType linkType) {
        var hash = cryptoTool.hashOf(docId);
        return "http://" + LinkAddress + "/" + linkType + "?id=" + hash;
    }
}