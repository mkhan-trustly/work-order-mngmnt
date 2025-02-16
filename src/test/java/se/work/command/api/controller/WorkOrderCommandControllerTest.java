package se.work.command.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import se.work.command.api.request.CreateWorkOrderDto;
import se.work.command.domain.model.WorkOrderId;
import se.work.command.domain.service.WorkOrderService;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkOrderCommandController.class)
@ExtendWith(MockitoExtension.class)
class WorkOrderCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WorkOrderService workOrderService;

    /**
     *  WorkOrderCommandController is a delegating controller. So doesn't need much testing.
     */

    @Test
    void shouldCreateWorkOrder() throws Exception {

        var id = CompletableFuture.completedFuture(WorkOrderId.of(UUID.randomUUID()));
        when(workOrderService.createWorkOrder(any())).thenReturn(id);

        var createWorkOrderRequest = new CreateWorkOrderDto("repair the machine");
        mockMvc.perform(
                        post("/api/v1/work-orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(createWorkOrderRequest))

                )
                .andExpect(status().isOk());
    }


    @Autowired
    private ObjectMapper objectMapper;

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}