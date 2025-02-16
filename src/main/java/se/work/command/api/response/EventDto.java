package se.work.command.api.response;

import java.time.Instant;

public record EventDto(String eventType, Instant timestamp, Object data) {}