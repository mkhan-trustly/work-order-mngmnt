package se.work.command.domain.service;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.work.command.api.request.AssignWorkOrderDto;
import se.work.command.api.request.CreateWorkOrderDto;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@ExtendWith(MockitoExtension.class)
class WorkOrderServiceTest {

    @Mock
    private CommandGateway commandGateway;

    @InjectMocks
    private WorkOrderService workOrderService;

    private final UUID id = UUID.randomUUID();

    @Nested
    class CreateWorkOrder {

        @Test
        void shouldCreateWorkOrder() {
            var createWorkOrderRequest = new CreateWorkOrderDto("perform test operation");
            workOrderService.createWorkOrder(createWorkOrderRequest);
        }

        @ParameterizedTest
        @NullAndEmptySource
        void shouldThrowExceptionOnInvalidRequest(String input) {
            var createWorkOrderRequest = new CreateWorkOrderDto(input);

            assertThrowsExactly(IllegalArgumentException.class, () -> workOrderService.createWorkOrder(createWorkOrderRequest));
        }
    }

    @Nested
    class AssignWorkOrder {

        @Test
        void shouldAssignWorkOrder() {
            var assignWorkOrderRequest = new AssignWorkOrderDto("user007");
            workOrderService.assignWorkOrder(id, assignWorkOrderRequest);
        }

        @ParameterizedTest
        @NullAndEmptySource
        void shouldThrowExceptionOnInvalidRequest(String input) {
            var assignWorkOrderRequest = new AssignWorkOrderDto(input);

            assertThrowsExactly(IllegalArgumentException.class, () -> workOrderService.assignWorkOrder(id, assignWorkOrderRequest));
        }
    }

}