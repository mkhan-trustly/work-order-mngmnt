package se.work.command.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import se.work.command.api.request.AssignWorkOrderDto;
import se.work.command.api.request.CreateWorkOrderDto;
import se.work.command.api.response.EventDto;
import se.work.command.domain.model.WorkOrderId;
import se.work.command.domain.service.WorkOrderService;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/v1/work-orders")
@RequiredArgsConstructor
public class WorkOrderCommandController {

    private final WorkOrderService workOrderService;

    @PostMapping
    CompletableFuture<String> createWorkOrder(@RequestBody CreateWorkOrderDto createWorkOrder) {
        CompletableFuture<WorkOrderId> futureWorkOrderId = workOrderService.createWorkOrder(createWorkOrder);
        return futureWorkOrderId.thenApply(WorkOrderId::toString);
    }

    @PutMapping("/{id}/assign")
    CompletableFuture<Object> assignWorkOrder(@PathVariable UUID id, @RequestBody AssignWorkOrderDto assignWorkOrder) {
        return workOrderService.assignWorkOrder(id, assignWorkOrder);
    }

    @PutMapping("/{id}/execute")
    CompletableFuture<Object> executeWorkOrder(@PathVariable UUID id) {
        return workOrderService.executeWorkOrder(id);
    }

    @GetMapping("/{id}/events")
    List<EventDto> getWorkOrder(@PathVariable UUID id) {
        return workOrderService.getAllEvents(id);
    }

}
