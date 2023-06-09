package com.matsko.service.enums;

/**
 * Resource identifiers used in link generation.
 */
public enum LinkType {
    GET_DOC("file/get-doc"),
    GET_PHOTO("file/get-photo");

    /**
     * Field that contains a reference to an object.
     */
    private final String link;


    /**
     * Constructor.
     *
     * @param link reference to an object.
     */
    LinkType(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return link;
    }
}