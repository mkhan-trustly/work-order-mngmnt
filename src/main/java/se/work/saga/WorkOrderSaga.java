package se.work.saga;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import se.work.api.commands.AssignWorkOrderCommand;
import se.work.api.commands.ExecuteWorkOrderCommand;
import se.work.api.events.WorkOrderAssignedEvent;
import se.work.api.events.WorkOrderCreatedEvent;
import se.work.api.events.WorkOrderExecutedEvent;
import se.work.command.domain.model.AgentId;

@Saga
@Slf4j
public class WorkOrderSaga {

    private static final String ID_ASSOCIATION = "id";

    public WorkOrderSaga() {}

    @StartSaga
    @SagaEventHandler(associationProperty = ID_ASSOCIATION)
    public void onEvent(WorkOrderCreatedEvent event, CommandGateway commandGateway) {
        log.info("Received(Saga): WorkOrderCreatedEvent: {}", event.id());
        AgentId assignTo = AgentId.of(getAvailableAgent());
        commandGateway.send(new AssignWorkOrderCommand(event.id(), assignTo));
    }

    @SagaEventHandler(associationProperty = ID_ASSOCIATION)
    public void onEvent(WorkOrderAssignedEvent event, CommandGateway commandGateway) {
        log.info("Received(Saga): WorkOrderAssignedEvent: {}", event.id());
        commandGateway.send(new ExecuteWorkOrderCommand(event.id()));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = ID_ASSOCIATION)
    public void onEvent(WorkOrderExecutedEvent event) {
        log.info("Received(Saga): WorkOrderExecutedEvent: {}, current state {}.", event.id(), event.state());
    }

    private String getAvailableAgent() {
        return "user007";
    }
}