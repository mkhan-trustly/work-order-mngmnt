package se.work.query;

import lombok.Builder;

import java.util.UUID;

@Builder
public record WorkOrder(UUID id, String instruction, String state, String assignedTo) {}
