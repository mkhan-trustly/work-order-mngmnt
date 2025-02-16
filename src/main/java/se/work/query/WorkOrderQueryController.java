package se.work.query;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.work.query.exception.NoWorkOrderFoundException;
import se.work.api.events.WorkOrderAssignedEvent;
import se.work.api.events.WorkOrderCreatedEvent;
import se.work.api.events.WorkOrderExecutedEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/query/work-order")
@Slf4j
public class WorkOrderQueryController {

    private final Map<UUID, WorkOrder> repository = new HashMap<>();

    @GetMapping("/{id}")
    public WorkOrder getWorkOrder(@PathVariable UUID id) {
        return Optional.ofNullable(repository.get(id))
                .orElseThrow(() -> new NoWorkOrderFoundException("No WorkOrder found with id(%s).".formatted(id)));
    }

    @EventHandler
    public void onEvent(WorkOrderCreatedEvent event) {
        log.info("Received(Query): WorkOrderCreatedEvent: {}", event.id());
        var workOrder = WorkOrder.builder()
                .id(event.id())
                .instruction(event.instruction())
                .build();
        repository.put(event.id(), workOrder);
    }

    @EventHandler
    public void onEvent(WorkOrderAssignedEvent event) {
        log.info("Received(Query): WorkOrderAssignedEvent: {}", event.id());
        var workOrder = getWorkOrder(event.id());
        var updatedWorkOrder = copy(workOrder)
                .assignedTo(event.assignedTo())
                .build();

        repository.put(event.id(), updatedWorkOrder);
    }

    @EventHandler
    public void onEvent(WorkOrderExecutedEvent event) {
        log.info("Received(Query): WorkOrderExecutedEvent: {}", event.id());
        var workOrder = getWorkOrder(event.id());
        var updatedWorkOrder = copy(workOrder)
                .state(event.state())
                .build();

        repository.put(event.id(), updatedWorkOrder);
    }

    private WorkOrder.WorkOrderBuilder copy(WorkOrder workOrder) {
        return WorkOrder.builder()
                .id(workOrder.id())
                .instruction(workOrder.instruction())
                .assignedTo(workOrder.assignedTo())
                .state(workOrder.state());
    }
}
