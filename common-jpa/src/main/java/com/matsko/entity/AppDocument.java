package com.matsko.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Class that creates and connects to a table <b>"app_document"</b> in the database.
 * This table contains:
 * primary key, Telegram file ID, document name, reference to {@link BinaryContent} object,
 * MIME type, document size.
 */
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_document")
public class AppDocument {

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
     * Field that contains the document name.
     */
    private String docName;

    /**
     * Field that contains the reference to {@link BinaryContent} object.
     */
    @OneToOne
    private BinaryContent binaryContent;

    /**
     * Field that contains the MIME type.
     */
    private String mimeType;

    /**
     * Field that contains the document size.
     */
    private Long fileSize;
}
