package com.matsko.service;

import com.matsko.entity.AppDocument;
import com.matsko.entity.AppPhoto;
import com.matsko.entity.BinaryContent;
import org.springframework.core.io.FileSystemResource;

public interface FileService {
    AppDocument getDocument(String id);
    AppPhoto getPhoto(String id);
    FileSystemResource getFileSystemResource(BinaryContent binaryContent);
}