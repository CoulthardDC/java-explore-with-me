package ru.practicum.ewm.base.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class EventSearchCriteria {

    String text;

    List<Long> categories;

    Boolean paid;

    String rangeStart;

    String rangeEnd;

    Boolean onlyAvailable;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Long> getCategories() {
        return categories;
    }

    public void setCategories(List<Long> categories) {
        this.categories = categories;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public String getRangeStart() {
        return rangeStart;
    }

    public void setRangeStart(String rangeStart) {
        this.rangeStart = rangeStart;
    }

    public String getRangeEnd() {
        return rangeEnd;
    }

    public void setRangeEnd(String rangeEnd) {
        this.rangeEnd = rangeEnd;
    }

    public Boolean getOnlyAvailable() {
        return onlyAvailable;
    }

    public void setOnlyAvailable(Boolean onlyAvailable) {
        this.onlyAvailable = onlyAvailable;
    }
}
