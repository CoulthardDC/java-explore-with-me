package ru.practicum.dto.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestStatusUpdateResult {
    List<ParticipationRequestDto> confirmedRequests;
    List<ParticipationRequestDto> rejectedRequests;

    public List<ParticipationRequestDto> getConfirmedRequests() {
        return confirmedRequests;
    }

    public void setConfirmedRequests(List<ParticipationRequestDto> confirmedRequests) {
        this.confirmedRequests = confirmedRequests;
    }

    public List<ParticipationRequestDto> getRejectedRequests() {
        return rejectedRequests;
    }

    public void setRejectedRequests(List<ParticipationRequestDto> rejectedRequests) {
        this.rejectedRequests = rejectedRequests;
    }

    @Override
    public String toString() {
        return "EventRequestStatusUpdateResult{" +
                "confirmedRequests=" + confirmedRequests +
                ", rejectedRequests=" + rejectedRequests +
                '}';
    }
}
