package ru.practicum.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.category.model.Category;
import ru.practicum.user.model.User;
import ru.practicum.util.EwmServiceConstants;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Embedded;
import javax.persistence.AttributeOverride;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "events")
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private Boolean paid;

    @Column(name = "event_date")
    @DateTimeFormat(pattern = EwmServiceConstants.DATE_TIME_PATTERN)
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    private String description;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column(name = "created_on")
    @DateTimeFormat(pattern = EwmServiceConstants.DATE_TIME_PATTERN)
    private LocalDateTime createdOn;

    @Column(name = "published_on")
    @DateTimeFormat(pattern = EwmServiceConstants.DATE_TIME_PATTERN)
    private LocalDateTime publishedOn;

    @Embedded
    @AttributeOverride(name = "lat", column = @Column(name = "latitude"))
    @AttributeOverride(name = "lon", column = @Column(name = "longitude"))
    private Location location;

    @Column(name = "request_moderation")
    private boolean requestModeration;
}
