package ru.practicum.ewm.adminApi.dto;

import lombok.*;
import ru.practicum.ewm.base.enums.State;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestParamForEvent {

    private List<Long> users;

    private List<State> states;

    private List<Long> categories;

    private LocalDateTime rangeStart;

    private LocalDateTime rangeEnd;

    private int from;

    private int size;

    public List<Long> getUsers() {
        return users;
    }

    public void setUsers(List<Long> users) {
        this.users = users;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public List<Long> getCategories() {
        return categories;
    }

    public void setCategories(List<Long> categories) {
        this.categories = categories;
    }

    public LocalDateTime getRangeStart() {
        return rangeStart;
    }

    public void setRangeStart(LocalDateTime rangeStart) {
        this.rangeStart = rangeStart;
    }

    public LocalDateTime getRangeEnd() {
        return rangeEnd;
    }

    public void setRangeEnd(LocalDateTime rangeEnd) {
        this.rangeEnd = rangeEnd;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "RequestParamForEvent{" +
                "users=" + users +
                ", states=" + states +
                ", categories=" + categories +
                ", rangeStart=" + rangeStart +
                ", rangeEnd=" + rangeEnd +
                ", from=" + from +
                ", size=" + size +
                '}';
    }
}
