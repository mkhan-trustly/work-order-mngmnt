package se.work.api.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import se.work.command.domain.model.AgentId;

import java.util.UUID;

public record AssignWorkOrderCommand(@TargetAggregateIdentifier UUID id, AgentId assignedTo) {}