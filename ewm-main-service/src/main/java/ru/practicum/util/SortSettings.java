package ru.practicum.util;

import org.springframework.data.domain.Sort;

public interface SortSettings {
    Sort SORT_ID_DESC = Sort.by(Sort.Direction.DESC, "id");

    Sort SORT_EVENT_DATE_DESC = Sort.by(Sort.Direction.DESC, "eventDate");

    Sort SORT_ID_ASC = Sort.by(Sort.Direction.ASC, "id");
}
