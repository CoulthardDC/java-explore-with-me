package ru.practicum.ewm.base.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.base.enums.State;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Table(name = "events")
public class Event {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "annotation", nullable = false)
    String annotation;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    Category category;

    @Column(name = "confirmedRequests")
    Long confirmedRequests;

    @Column(name = "createdOn")
    LocalDateTime createdOn;

    @Column(name = "description", nullable = false)
    String description;

    @Column(name = "date")
    LocalDateTime date;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    User initiator;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "lat", column = @Column(name = "lat")),
            @AttributeOverride(name = "lon", column = @Column(name = "lon"))
    })
    Location location;

    @Column(name = "paid", nullable = false)
    Boolean paid;

    @Column(name = "participantLimit")
    Long participantLimit;

    @Column(name = "publishedOn")
    LocalDateTime publishedOn;

    @Column(name = "requestModeration")
    Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    State state;

    @Column(name = "title", nullable = false)
    String title;

    @Column(name = "views")
    Long views;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        return id != null && id.equals(((Event) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
