package se.work.command.domain.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class WorkOrderIdTest {

    @ParameterizedTest
    @ValueSource(strings = {"f03e044b-d7d0-441a-9894-67b973a46916", "c275ad27-33b0-4740-bf8a-62ec18651930"})
    void shouldCreateWorkOrderIdInstanceOnValidInput(String value) {
        assertNotNull(WorkOrderId.of(value));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowExceptionOnNullAndEmptyInput(String value) {
        assertThrowsExactly(IllegalArgumentException.class, () -> WorkOrderId.of(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"test", "c275ad27-33b0-4740-62ec18651930"})
    void shouldThrowExceptionOnInvalidInput(String value) {
        assertThrowsExactly(IllegalArgumentException.class, () -> WorkOrderId.of(value));
    }

}