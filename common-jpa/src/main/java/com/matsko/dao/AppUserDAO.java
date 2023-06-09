package com.matsko.dao;

import com.matsko.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Data access object interface that inherits from an interface {@link JpaRepository}.
 */
public interface AppUserDAO extends JpaRepository<AppUser, Long> {

    /**
     * Method that the presence of the user in the database.
     *
     * @param id user id.
     * @return user.
     */
    Optional<AppUser> findByTelegramUserId(Long id);

    /**
     * Method that searches the database by user id.
     *
     * @param id user id.
     * @return user.
     */
    Optional<AppUser> findById(Long id);

    /**
     * Method that searches the database by user email.
     *
     * @param email user email.
     * @return user.
     */
    Optional<AppUser> findByEmail(String email);
}
