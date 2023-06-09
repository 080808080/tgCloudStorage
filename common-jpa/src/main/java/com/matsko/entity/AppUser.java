package com.matsko.entity;

import com.matsko.entity.enums.UserState;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Class that creates and connects to a table <b>"app_user"</b> in the database.
 * This table contains:
 * primary key, Telegram user ID, logging date in the bot, user first name, user last name, username,
 * user e-mail, account activation flag, available user states.
 */
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class AppUser {

    /**
     * Field that contains the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Field that contains the Telegram user ID.
     */
    private Long telegramUserId;

    /**
     * Field that contains the logging date in the bot.
     */
    @CreationTimestamp
    private LocalDateTime firstLoginDate;

    /**
     * Field that contains the user first name.
     */
    private String firstName;

    /**
     * Field that contains the user last name.
     */
    private String lastName;

    /**
     * Field that contains the username.
     */
    private String username;

    /**
     * Field that contains the user e-mail.
     */
    private String email;

    /**
     * Field that contains the account activation flag.
     */
    private Boolean isActive;

    /**
     * Field that contains the available user states.
     */
    @Enumerated(EnumType.STRING)
    private UserState state;
}
