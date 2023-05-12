package com.matsko.dao;

import com.matsko.entity.AppPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppPhotoDAO extends JpaRepository <AppPhoto, Long>{
}
