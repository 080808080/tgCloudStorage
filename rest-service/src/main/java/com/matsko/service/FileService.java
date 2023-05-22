package com.matsko.service;

import com.matsko.entity.AppDocument;
import com.matsko.entity.AppPhoto;

public interface FileService {
    AppDocument getDocument(String id);
    AppPhoto getPhoto(String id);
}