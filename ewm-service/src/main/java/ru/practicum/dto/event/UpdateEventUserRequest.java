package ru.practicum.dto.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventUserRequest {
    UserStateActions userStateActions;

    public UserStateActions getUserStateActions() {
        return userStateActions;
    }

    public void setUserStateActions(UserStateActions userStateActions) {
        this.userStateActions = userStateActions;
    }
}
