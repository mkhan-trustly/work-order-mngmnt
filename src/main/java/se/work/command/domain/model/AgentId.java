package se.work.command.domain.model;

import lombok.Getter;

import java.util.Objects;

@Getter
public class AgentId {

    private final String id;

    private AgentId(String id) {
        this.id = validateId(id);
    }

    public static AgentId of(String id) {
        return new AgentId(id);
    }

    private String validateId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }

        if (!id.matches("^[a-zA-Z]{4}\\d{3}")) {
            throw new IllegalArgumentException("Username(%s) is not valid".formatted(id));
        }
        return id.strip().toLowerCase();
    }

    public String toString() {
        return this.id;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;

        if (that == null || getClass() != that.getClass()) return false;
        AgentId user = (AgentId) that;
        return this.id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
