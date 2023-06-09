package com.matsko.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Class that creates and connects to a table <b>"binary_content"</b> in the database.
 * This table stores the foreign key ID of a record from another table, depending on the type of message file (<b>"app_photo"</b> or <b>"app_document"</b>)
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
@Table(name = "binary_content")
public class BinaryContent {

    /**
     * Field that contains the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Field storing an array of bytes.
     */
    private byte[] fileAsArrayOfBytes;
}