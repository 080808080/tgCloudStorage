package com.matsko.controller;

import com.matsko.service.UserActivationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class for processing a request for a link from an email.
 */
@RequestMapping("/user")
@RestController
public class ActivationController {

    /**
     * Field that contains {@link UserActivationService}.
     */
    private final UserActivationService userActivationService;

    /**
     * Constructor.
     *
     * @param userActivationService service for processing a user activation request.
     */
    public ActivationController(UserActivationService userActivationService) {
        this.userActivationService = userActivationService;
    }

    /**
     * Method that accepts GET-requests and id.
     *
     * @param id user id.
     * @return response about registration results.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/activation")
    public ResponseEntity<?> activation(@RequestParam("id") String id) {
        var res = userActivationService.activation(id);
        if(res){
            return ResponseEntity.ok().body("Регистрация успешно завершена!");
        }
        return ResponseEntity.internalServerError().build();
    }
}
