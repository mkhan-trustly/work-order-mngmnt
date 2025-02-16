package se.work.command.domain.service;

import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.DomainEventMessage;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.stereotype.Service;
import se.work.command.api.request.AssignWorkOrderDto;
import se.work.command.api.request.CreateWorkOrderDto;
import se.work.command.api.response.EventDto;
import se.work.api.commands.AssignWorkOrderCommand;
import se.work.api.commands.CreateWorkOrderCommand;
import se.work.api.commands.ExecuteWorkOrderCommand;
import se.work.command.domain.model.AgentId;
import se.work.command.domain.model.WorkOrderId;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class WorkOrderService {

    private final CommandGateway commandGateway;
    private final EventStore eventStore;

    public CompletableFuture<WorkOrderId> createWorkOrder(CreateWorkOrderDto createWorkOrder) {
        var createCommand = new CreateWorkOrderCommand(createWorkOrder.instruction());
        return commandGateway.send(createCommand);
    }

    public CompletableFuture<Object> assignWorkOrder(UUID id, AssignWorkOrderDto assignWorkOrder) {
        AgentId agent = AgentId.of(assignWorkOrder.assignTo());
        var assignCommand = new AssignWorkOrderCommand(id, agent);
        return commandGateway.send(assignCommand);
    }

    public CompletableFuture<Object> executeWorkOrder(UUID id) {
        var assignCommand = new ExecuteWorkOrderCommand(id);
        return commandGateway.send(assignCommand);
    }

    public List<EventDto> getAllEvents(UUID id) {
        return eventStore
                .readEvents(id.toString())
                .asStream()
                .map(this::map)
                .toList();
    }

    private EventDto map(DomainEventMessage<?> event) {
        return new EventDto(
                event.getPayloadType().getSimpleName(),
                event.getTimestamp(),
                event.getPayload()
        );
    }


}
