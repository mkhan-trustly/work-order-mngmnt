package se.work.query;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import se.work.api.events.WorkOrderAssignedEvent;
import se.work.api.events.WorkOrderCreatedEvent;
import se.work.api.events.WorkOrderExecutedEvent;
import se.work.query.exception.NoWorkOrderFoundException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WorkOrderQueryControllerTest {

    private WorkOrderQueryController workOrderController;
    private final UUID id = UUID.randomUUID();


    @BeforeEach
    void setUp() {
        workOrderController = new WorkOrderQueryController();
    }

    @Test
    void shouldListenToWorkOrderCreatedEvent() {
        var instruction = "repair the machine";
        workOrderController.onEvent(new WorkOrderCreatedEvent(id, instruction));
        WorkOrder workOrder = workOrderController.getWorkOrder(id);

        assertEquals(id, workOrder.id());
        assertEquals(instruction, workOrder.instruction());
    }

    @Test
    void shouldListenToWorkOrderAssignedEvent() {
        var instruction = "repair the machine";
        workOrderController.onEvent(new WorkOrderCreatedEvent(id, instruction));

        var agentId = "user007";
        workOrderController.onEvent(new WorkOrderAssignedEvent(id, agentId));
        WorkOrder workOrder = workOrderController.getWorkOrder(id);

        assertEquals(id, workOrder.id());
        assertEquals(agentId, workOrder.assignedTo());
    }

    @Test
    void shouldListenToWorkOrderExecutedEvent() {
        var instruction = "repair the machine";
        workOrderController.onEvent(new WorkOrderCreatedEvent(id, instruction));

        var state = "IN_PROGRESS";
        workOrderController.onEvent(new WorkOrderExecutedEvent(id, state));
        WorkOrder workOrder = workOrderController.getWorkOrder(id);

        assertEquals(id, workOrder.id());
        assertEquals(state, workOrder.state());
    }

    @Test
    void shouldThrowExceptionWhenIdNotFound() {
        workOrderController.onEvent(new WorkOrderCreatedEvent(id, "repair the machine"));

        assertThrowsExactly(NoWorkOrderFoundException.class, () -> workOrderController.getWorkOrder(UUID.randomUUID()));
    }
}