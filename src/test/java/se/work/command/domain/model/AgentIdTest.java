package se.work.command.domain.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AgentIdTest {

    @ParameterizedTest
    @MethodSource("invalidUsername")
    void shouldThrowExceptionOnInvalidUsername(String username) {
        assertThrowsExactly(IllegalArgumentException.class, () -> AgentId.of(username));
    }

    private static Stream<Arguments> invalidUsername() {
        String username = null;
        return Stream.of(
                Arguments.of(username),
                Arguments.of(" "),
                Arguments.of("öäåö"),
                Arguments.of("12345"),
                Arguments.of("abcd-122"),
                Arguments.of("abcde012"),
                Arguments.of("abc0122")
        );
    }

    @ParameterizedTest
    @MethodSource("validUsername")
    void shouldCreateTaskUserOnValidUsername(String expectedUsername, String username) {
        Assertions.assertEquals(expectedUsername, AgentId.of(username).toString());
    }

    private static Stream<Arguments> validUsername() {
        return Stream.of(
                Arguments.of("kaaa001", "kaaa001"),
                Arguments.of("zoan999", "zoan999")
        );
    }

}