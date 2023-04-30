package ru.practicum.ewm.base.dto.event;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class EventRequestStatusUpdateRequest {
    @NotNull
    List<Long> requestIds;

    @NotBlank
    String status;

    public List<Long> getRequestIds() {
        return requestIds;
    }

    public void setRequestIds(List<Long> requestIds) {
        this.requestIds = requestIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "EventRequestStatusUpdateRequest{" +
                "requestIds=" + requestIds.toString() +
                ", status='" + status + '\'' +
                '}';
    }
}
