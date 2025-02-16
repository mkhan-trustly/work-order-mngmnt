package se.work.api.events;

import java.util.UUID;

public record WorkOrderExecutedEvent(UUID id, String state) {}
