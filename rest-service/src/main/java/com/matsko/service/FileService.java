package com.matsko.service;

import com.matsko.entity.AppDocument;
import com.matsko.entity.AppPhoto;

/**
 * Interface for getting a file.
 */
public interface FileService {

    /**
     * Method that get the document.
     *
     * @param id document identifier.
     * @return encrypted documents id.
     */
    AppDocument getDocument(String id);

    /**
     * Method that get the photo.
     *
     * @param id photo identifier.
     * @return encrypted photos id.
     */
    AppPhoto getPhoto(String id);
}