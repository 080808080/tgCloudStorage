package com.matsko.service;

/**
 * Service for processing a user activation request.
 */
public interface UserActivationService {

    /**
     * Method that activate user.
     *
     * @param cryptoUserId encrypted user id.
     * @return activation completion response.
     */
    boolean activation(String cryptoUserId);
}
