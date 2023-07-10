package ru.practicum.util;

public enum LoggingMessages {
    CREATE("Creating data {}"),
    GET("Getting data with id {}"),
    GET_ALL("Getting all data"),
    UPDATE("Updating data {}"),
    DELETE("Deleting data with id {}"),
    SEARCH("Searching data");

    private String message;

    private LoggingMessages(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
