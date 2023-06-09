package com.matsko.service.enums;

/**
 * Class containing available service commands.
 */
public enum ServiceCommand {
    HELP("/help"),
    REGISTRATION("/registration"),
    CANCEL("/cancel"),
    START("/start");

    /**
     * This field contains command value.
     */
    private final String value;

    /**
     * Constructor.
     *
     * @param value command value.
     */
    ServiceCommand(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * Determines the existence of an incoming command.
     *
     * @param v incoming command.
     * @return enum with this Value.
     */
    public static ServiceCommand fromValue(String v) {
        for (ServiceCommand c: ServiceCommand.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        return null;
    }
}