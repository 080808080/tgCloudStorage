package com.matsko.dao;

import com.matsko.entity.AppPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data access object interface that inherits from an interface {@link JpaRepository}
 * for photo processing.
 */
public interface AppPhotoDAO extends JpaRepository<AppPhoto, Long> {
}