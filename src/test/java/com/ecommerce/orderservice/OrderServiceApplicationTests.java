package com.ecommerce.orderservice;

import com.ecommerce.orderservice.controller.OrderController;
import com.ecommerce.orderservice.models.Order;
import com.ecommerce.orderservice.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.mockito.BDDMockito.given;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
public class OrderServiceApplicationTests {

    private static final Long OrderId = 1L;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService orderService;

    @Test
    public void givenOrder_whenGetOrders_thenReturnJsonArrayAndHTTPStatusOk()
            throws Exception {

        // given
        Order order1 = new Order( OrderId, "1", "Alex", "alex@gmail.com");

        List<Order> allOrders = Arrays.asList(order1);

        // when
        given(orderService.getAllOrders()).willReturn(allOrders);

        // then
        mvc.perform(get("/api/orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].customerName", is(order1.getCustomerName())));
    }
}
