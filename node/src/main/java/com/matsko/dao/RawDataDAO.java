package com.matsko.dao;

import com.matsko.entity.RawData;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data access object interface that inherits from an interface {@link JpaRepository}.
 */
public interface RawDataDAO extends JpaRepository<RawData, Long> {
}
