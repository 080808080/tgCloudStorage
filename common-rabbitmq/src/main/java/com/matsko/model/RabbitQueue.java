package com.matsko.model;

/**
 * Class that contains constants with the names of the queues in the message broker.
 */
public class RabbitQueue {

    /**
     * Constant for the document queue.
     */
    public static final String DOC_MESSAGE_UPDATE = "doc_message_update";

    /**
     * Constant for the photo queue.
     */
    public static final String PHOTO_MESSAGE_UPDATE = "photo_message_update";

    /**
     * Constant for the text message queue.
     */
    public static final String TEXT_MESSAGE_UPDATE = "text_message_update";

    /**
     * Constant for the reply message queue.
     */
    public static final String ANSWER_MESSAGE = "answer_message";
}
