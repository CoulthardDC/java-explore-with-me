package ru.practicum.ewm.base.dto.compilation;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.base.util.notblanknull.NotBlankNull;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UpdateCompilationRequest {
    Set<Long> events;

    Boolean pinned;

    @NotBlankNull
    String title;

    public Set<Long> getEvents() {
        return events;
    }

    public void setEvents(Set<Long> events) {
        this.events = events;
    }

    public Boolean getPinned() {
        return pinned;
    }

    public void setPinned(Boolean pinned) {
        this.pinned = pinned;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "UpdateCompilationRequest{" +
                "events=" + events +
                ", pinned=" + pinned +
                ", title='" + title + '\'' +
                '}';
    }
}
