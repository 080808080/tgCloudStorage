package com.matsko.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Class that creates and connects to a table <b>"app_photo"</b> in the database.
 * This table contains:
 * primary key, Telegram file ID, reference to {@link BinaryContent} object, photo size.
 */
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_photo")
public class AppPhoto {

    /**
     * Field that contains the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Field that contains the Telegram file ID.
     */
    private String telegramFileId;

    /**
     * Field that contains the reference to {@link BinaryContent} object.
     */
    @OneToOne
    private BinaryContent binaryContent;

    /**
     * Field that contains the photo size.
     */
    private Integer fileSize;
}