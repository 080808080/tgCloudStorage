package com.matsko.service;

import com.matsko.entity.AppUser;

/**
 * Service that checks user registration and validates the correctness of filling out the email form.
 */
public interface AppUserService {

    /**
     * Method that checks user registration.
     *
     * @param appUser table in the database.
     * @return response about the results of the check.
     */
    String registerUser(AppUser appUser);

    /**
     * Method that validates the correctness of filling out the email form.
     *
     * @param appUser table in the database.
     * @param email user email.
     * @return response about the results of the check.
     */
    String setEmail(AppUser appUser, String email);

}