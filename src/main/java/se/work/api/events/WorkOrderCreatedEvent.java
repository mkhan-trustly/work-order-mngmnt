package se.work.api.events;

import java.util.UUID;

public record WorkOrderCreatedEvent(UUID id, String instruction) {
}
