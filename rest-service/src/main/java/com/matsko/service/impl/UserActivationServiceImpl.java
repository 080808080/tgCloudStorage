package com.matsko.service.impl;

import com.matsko.dao.AppUserDAO;
import com.matsko.service.UserActivationService;
import com.matsko.utils.CryptoTool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * Class that implements {@link UserActivationService}.
 */
@Service
public class UserActivationServiceImpl implements UserActivationService {

    /**
     * Field that accepts {@link AppUserDAO}.
     */
    private final AppUserDAO appUserDAO;

    /**
     * Field that accepts {@link CryptoTool}.
     */
    private final CryptoTool cryptoTool;

    /**
     * Constructor.
     *
     * @param appUserDAO data access object interface that inherits from an interface {@link JpaRepository}.
     * @param cryptoTool encrypts the generated file reference.
     */
    public UserActivationServiceImpl(AppUserDAO appUserDAO, CryptoTool cryptoTool) {
        this.appUserDAO = appUserDAO;
        this.cryptoTool = cryptoTool;
    }

    @Override
    public boolean activation(String cryptoUserId) {
        var userId = cryptoTool.idOf(cryptoUserId);
        var optional = appUserDAO.findById(userId);
        if(optional.isPresent()){
            var user = optional.get();
            user.setIsActive(true);
            appUserDAO.save(user);
            return true;
        }
        return false;
    }
}
