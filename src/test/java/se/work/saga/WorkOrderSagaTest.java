package se.work.saga;

import org.axonframework.test.saga.SagaTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.work.api.commands.AssignWorkOrderCommand;
import se.work.api.commands.ExecuteWorkOrderCommand;
import se.work.api.events.WorkOrderAssignedEvent;
import se.work.api.events.WorkOrderCreatedEvent;
import se.work.api.events.WorkOrderExecutedEvent;
import se.work.command.domain.model.AgentId;

import java.util.UUID;

class WorkOrderSagaTest {

    private SagaTestFixture<WorkOrderSaga> fixture;

    @BeforeEach
    void setUp() {
        fixture = new SagaTestFixture<>(WorkOrderSaga.class);
    }

    @Test
    void shouldStartsSagaAndSendsAssignCommandOnWorkOrderCreatedEvent() {
        UUID workOrderId = UUID.randomUUID();
        AgentId assignTo = AgentId.of("user007");

        fixture.givenNoPriorActivity()
                .whenPublishingA(new WorkOrderCreatedEvent(workOrderId, "Repair the machine"))
                .expectActiveSagas(1)
                .expectDispatchedCommands(new AssignWorkOrderCommand(workOrderId, assignTo));
    }

    @Test
    void shouldDispatchExecuteCommandOnWorkOrderAssignedEvent() {
        UUID workOrderId = UUID.randomUUID();

        fixture.givenAPublished(new WorkOrderCreatedEvent(workOrderId, "Repair the machine"))
                .whenPublishingA(new WorkOrderAssignedEvent(workOrderId, "user007"))
                .expectActiveSagas(1)
                .expectDispatchedCommands(new ExecuteWorkOrderCommand(workOrderId));
    }

    @Test
    void shouldEndSagaOnWorkOrderExecutedEvent() {
        UUID workOrderId = UUID.randomUUID();

        fixture.givenAPublished(new WorkOrderCreatedEvent(workOrderId, "Repair the machine"))
                .andThenAPublished(new WorkOrderAssignedEvent(workOrderId, "user007"))
                .whenPublishingA(new WorkOrderExecutedEvent(workOrderId, "IN_PROGRESS"))
                .expectActiveSagas(0);
    }

    @Test
    void shouldNotDispatchCommandsIfSagaNotStarted() {
        UUID id = UUID.randomUUID();
        String assignedTo = "john123";

        fixture.givenNoPriorActivity()
                .whenPublishingA(new WorkOrderAssignedEvent(id, assignedTo))
                .expectNoDispatchedCommands();
    }

}