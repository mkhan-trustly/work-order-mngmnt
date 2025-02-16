package se.work.api.events;

import java.util.UUID;

public record WorkOrderAssignedEvent(UUID id, String assignedTo) {
}
