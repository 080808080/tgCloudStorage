package com.matsko.dao;

import com.matsko.entity.AppDocument;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data access object interface that inherits from an interface {@link JpaRepository}
 * for document processing.
 */
public interface AppDocumentDAO extends JpaRepository<AppDocument, Long> {
}