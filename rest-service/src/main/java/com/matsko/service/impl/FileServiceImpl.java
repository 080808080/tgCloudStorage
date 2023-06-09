package com.matsko.service.impl;

import com.matsko.dao.AppDocumentDAO;
import com.matsko.dao.AppPhotoDAO;
import com.matsko.entity.AppDocument;
import com.matsko.entity.AppPhoto;
import com.matsko.service.FileService;
import com.matsko.utils.CryptoTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * Class that implements {@link FileService}.
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    /**
     * Field that accepts {@link AppDocumentDAO}.
     */
    private final AppDocumentDAO appDocumentDAO;

    /**
     * Field that accepts {@link AppPhotoDAO}.
     */
    private final AppPhotoDAO appPhotoDAO;

    /**
     * Field that accepts {@link CryptoTool}.
     */
    private final CryptoTool cryptoTool;

    /**
     * Constructor.
     *
     * @param appDocumentDAO inherits from an interface {@link JpaRepository} for document processing.
     * @param appPhotoDAO inherits from an interface {@link JpaRepository} for photo processing.
     * @param cryptoTool encrypts the generated file reference.
     */
    public FileServiceImpl(AppDocumentDAO appDocumentDAO, AppPhotoDAO appPhotoDAO, CryptoTool cryptoTool) {
        this.appDocumentDAO = appDocumentDAO;
        this.appPhotoDAO = appPhotoDAO;
        this.cryptoTool = cryptoTool;
    }

    @Override
    public AppDocument getDocument(String hash) {
        var id = cryptoTool.idOf(hash);
        if (id == null) {
            return null;
        }
        return appDocumentDAO.findById(id).orElse(null);
    }

    @Override
    public AppPhoto getPhoto(String hash) {
        var id = cryptoTool.idOf(hash);
        if (id == null) {
            return null;
        }
        return appPhotoDAO.findById(id).orElse(null);
    }
}