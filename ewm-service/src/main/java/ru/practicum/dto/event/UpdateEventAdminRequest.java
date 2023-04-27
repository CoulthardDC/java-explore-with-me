package ru.practicum.dto.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventAdminRequest {
    AdminStateActions adminStateActions;

    public AdminStateActions getAdminStateActions() {
        return adminStateActions;
    }

    public void setAdminStateActions(AdminStateActions adminStateActions) {
        this.adminStateActions = adminStateActions;
    }
}
