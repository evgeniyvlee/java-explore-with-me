package ru.practicum.exception;

public interface ExceptionMessages {
    String CATEGORY_NAME_TOO_SHORT = "Category name length is too short";
    String CATEGORY_NAME_TOO_LONG = "Category name length is too long";
    String CATEGORY_NOT_FOUND = "Category not found";
    String CATEGORY_NAME_CONFLICT = "Category with this name is already exists";
    String CATEGORY_IS_NOT_EMPTY = "Category is not empty";

    String USER_NOT_FOUND = "User not found";
    String USER_NAME_TOO_SHORT = "User name length is too short";
    String USER_NAME_TOO_LONG = "User name length is too long";
    String USER_EMAIL_TOO_SHORT = "User email length is too short";
    String USER_EMAIL_TOO_LONG = "User email length is too long";
    String USER_IS_NOT_EVENT_INITIATOR = "User is noy initiator";
    String USER_NAME_CONFLICT = "User with this name is already exists";

    String EVENT_NOT_FOUND = "Event not found";
    String EVENT_ANNOTATION_TOO_SHORT = "Event annotation is too short";
    String EVENT_ANNOTATION_TOO_LONG = "Event annotation is too long";
    String EVENT_DESCRIPTION_TOO_SHORT = "Event description is too short";
    String EVENT_DESCRIPTION_TOO_LONG = "Event description is too long";
    String EVENT_TITLE_TOO_SHORT = "Event title is too short";
    String EVENT_TITLE_TOO_LONG = "Event title is too long";
    String EVENT_INITIATOR_IS_SAME_REQUESTER = "Event initiator is the same requester";
    String EVENT_NOT_PUBLISHED = "Event is not published";
    String EVENT_REQUEST_LIMIT = "Event request limit";
    String EVENT_NOT_PENDING_OR_CANCELED = "Event is not pending or canceled";
    String EVENT_DATE_EARLY_2_HOURS_FROM_NOW = "Event begins earlier than 2 hours later from now";
    String INVALID_EVENT_RANGE_DATE = "Range date for getting event is invalid";

    String REQUEST_ALREADY_EXISTS = "Request already exists";
    String REQUEST_NOT_FOUND = "Request not found";
    String IS_NOT_REQUESTER = "User is not requester";

    String COMPILATION_NOT_FOUND = "Compilation not found";
    String COMPILATION_TITLE_TOO_SHORT = "Compilation title is too short";
    String COMPILATION_TITLE_TOO_LONG = "Compilation title is too long";
    String UNKNOWN_STATE_ACTION = "Unknown state action";
}
