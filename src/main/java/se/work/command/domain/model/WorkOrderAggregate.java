package se.work.command.domain.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import se.work.api.commands.AssignWorkOrderCommand;
import se.work.api.commands.CreateWorkOrderCommand;
import se.work.api.commands.ExecuteWorkOrderCommand;
import se.work.api.events.WorkOrderAssignedEvent;
import se.work.api.events.WorkOrderCreatedEvent;
import se.work.api.events.WorkOrderExecutedEvent;

import java.util.UUID;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;
import static se.work.command.domain.model.WorkOrderState.IN_PROGRESS;

@Aggregate
@Getter
@Slf4j
public class WorkOrderAggregate {

    @AggregateIdentifier
    private WorkOrderId id;
    private String instruction;
    private WorkOrderState state;
    private AgentId assignedTo;

    protected WorkOrderAggregate() {}

    @CommandHandler
    public WorkOrderAggregate(CreateWorkOrderCommand command) {
        UUID id = UUID.randomUUID();
        log.info("CreateWorkOrderCommand: {}", id);
        apply(new WorkOrderCreatedEvent(id, command.instruction()));
    }

    @EventSourcingHandler
    public void onEvent(WorkOrderCreatedEvent event) {
        this.id = WorkOrderId.of(event.id());
        this.instruction = event.instruction();
        this.state = WorkOrderState.CREATED;
    }

    @CommandHandler
    public void assignWorkOrder(AssignWorkOrderCommand command) {
        log.info("AssignWorkOrderCommand: {}", command.id());
        if (state != WorkOrderState.CREATED) {
            throw new IllegalStateException("WorkOrder must be in state CREATED to be assigned.");
        }
        apply(new WorkOrderAssignedEvent(command.id(), command.assignedTo().getId()));
    }

    @EventSourcingHandler
    public void onEvent(WorkOrderAssignedEvent event) {
        this.state = WorkOrderState.ASSIGNED;
        this.assignedTo = AgentId.of(event.assignedTo());
    }

    @CommandHandler
    public void executeWorkOrder(ExecuteWorkOrderCommand command) {
        log.info("ExecuteWorkOrderCommand: {}", command.id());
        if (state != WorkOrderState.ASSIGNED) {
            throw new IllegalStateException("WorkOrder must be in state ASSIGNED to be executed.");
        }
        apply(new WorkOrderExecutedEvent(command.id(), IN_PROGRESS.name()));
    }

    @EventSourcingHandler
    public void onEvent(WorkOrderExecutedEvent event) {
        this.state = WorkOrderState.valueOf(event.state());
    }

}
