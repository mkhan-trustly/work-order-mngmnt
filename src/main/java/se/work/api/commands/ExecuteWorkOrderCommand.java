package se.work.api.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

public record ExecuteWorkOrderCommand(@TargetAggregateIdentifier UUID id) {}