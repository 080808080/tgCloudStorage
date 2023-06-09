package com.matsko.dto;

import lombok.*;

/**
 * Class that, when receiving a request in the controller,
 * establishes the correspondence of the user id and his mail.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailParams {

    /**
     * Field that contained user id.
     */
    private String id;

    /**
     * Field that contained users email.
     */
    private String emailTo;
}