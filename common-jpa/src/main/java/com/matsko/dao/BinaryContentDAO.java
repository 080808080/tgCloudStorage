package com.matsko.dao;

import com.matsko.entity.BinaryContent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data access object interface that inherits from an interface {@link JpaRepository}
 * for {@link BinaryContent} processing.
 */
public interface BinaryContentDAO extends JpaRepository<BinaryContent, Long> {
}
