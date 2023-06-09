package com.matsko.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.persistence.*;

/**
 * Class that saves all incoming updates and assigns them a unique ID,
 * by creates and connects to a table <b>"raw_data"</b> in the database.
 * This table contains:
 * primary key and object of an update that comes from Telegram.
 */
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "raw_data")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class RawData {

    /**
     * Field containing the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Field containing the object of an update that comes from Telegram.
     */
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Update event;
}

