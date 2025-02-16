package se.work.command.domain.model;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.axonframework.test.matchers.IgnoreField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.work.api.commands.AssignWorkOrderCommand;
import se.work.api.commands.CreateWorkOrderCommand;
import se.work.api.commands.ExecuteWorkOrderCommand;
import se.work.api.events.WorkOrderAssignedEvent;
import se.work.api.events.WorkOrderCreatedEvent;
import se.work.api.events.WorkOrderExecutedEvent;

import java.util.UUID;

class WorkOrderAggregateTest {

    private FixtureConfiguration<WorkOrderAggregate> fixture;

    @BeforeEach
    void setUp() {
        fixture = new AggregateTestFixture<>(WorkOrderAggregate.class);
    }

    @Test
    void shouldCreateWorkOrder() {
        UUID id = UUID.randomUUID();
        String instruction = "Repair the machine";

        fixture.registerFieldFilter(new IgnoreField(WorkOrderCreatedEvent.class, "id"));
        fixture.givenNoPriorActivity()
                .when(new CreateWorkOrderCommand(instruction))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new WorkOrderCreatedEvent(id, instruction));
    }

    @Nested
    class AssignWorkOrder {

        @Test
        void shouldAssignWorkOrder() {
            UUID id = UUID.randomUUID();
            String instruction = "Repair the machine";
            AgentId assignTo = AgentId.of("john123");

            fixture.given(new WorkOrderCreatedEvent(id, instruction))
                    .when(new AssignWorkOrderCommand(id, assignTo))
                    .expectSuccessfulHandlerExecution()
                    .expectEvents(new WorkOrderAssignedEvent(id, assignTo.getId()));
        }

        @Test
        void shouldThrowExceptionIfWorkOrderNotCreated() {
            UUID id = UUID.randomUUID();
            String instruction = "Repair the machine";
            AgentId assignTo = AgentId.of("john123");

            fixture.given(
                            new WorkOrderCreatedEvent(id, instruction),
                            new WorkOrderAssignedEvent(id, assignTo.getId())
                    )
                    .when(new AssignWorkOrderCommand(id, assignTo))
                    .expectException(IllegalStateException.class)
                    .expectExceptionMessage("WorkOrder must be in state CREATED to be assigned.");
        }
    }

    @Nested
    class ExecuteWorkOrder {

        @Test
        void testExecuteWorkOrder() {
            UUID id = UUID.randomUUID();
            String instruction = "Repair the machine";
            AgentId assignTo = AgentId.of("john123");

            fixture.given(
                            new WorkOrderCreatedEvent(id, instruction),
                            new WorkOrderAssignedEvent(id, assignTo.getId())
                    )
                    .when(new ExecuteWorkOrderCommand(id))
                    .expectSuccessfulHandlerExecution()
                    .expectEvents(new WorkOrderExecutedEvent(id, "IN_PROGRESS"));
        }

        @Test
        void shouldThrowExceptionIfWorkOrderNotAssigned() {
            UUID id = UUID.randomUUID();
            String instruction = "Repair the machine";

            fixture.given(new WorkOrderCreatedEvent(id, instruction))
                    .when(new ExecuteWorkOrderCommand(id))
                    .expectException(IllegalStateException.class)
                    .expectExceptionMessage("WorkOrder must be in state ASSIGNED to be executed.");
        }

    }

}