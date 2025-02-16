package se.work.command.domain.model;

import java.util.UUID;

public class WorkOrderId {

    private final UUID value;

    private WorkOrderId(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("WorkOrderId cannot be null or empty");
        }
        value = UUID.fromString(input);
    }

    public static WorkOrderId of(String id) {
        return new WorkOrderId(id);
    }

    public static WorkOrderId of(UUID id) {
        return new WorkOrderId(id.toString());
    }

    public String toString() {
        return value.toString();
    }
}
