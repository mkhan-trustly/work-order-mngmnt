package se.work.api.commands;

public record CreateWorkOrderCommand(String instruction) {

    public CreateWorkOrderCommand {
        if (instruction == null || instruction.isBlank()) {
            throw new IllegalArgumentException("Instruction must be a valid input, null or empty not allowed");
        }
    }
}