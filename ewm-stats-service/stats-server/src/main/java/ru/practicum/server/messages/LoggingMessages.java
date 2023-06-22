package ru.practicum.server.messages;

public enum LoggingMessages {
    GET_VIEW_STATS("Getting view statistics"),
    CREATE_ENDPOINT_HIT("Creating endpoint hit");

    private String message;

    LoggingMessages(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
