package com.ecommerce.orderservice;

import com.ecommerce.orderservice.controller.OrderController;
import com.ecommerce.orderservice.models.Order;
import com.ecommerce.orderservice.service.OrderService;
import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

//import static org.assertj.core.api.BDDAssumptions.given;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.BDDMockito.given;

///----

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
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
    public void givenOrders_whenGetOrders_thenReturnJsonArray()
            throws Exception {

    	// given
        Order order1 = new Order(OrderId, "1", "Alex", "alex@gmail.com");

        List<Order> allOrders = Arrays.asList(order1);

        // when
        given(orderService.getAllOrders()).willReturn(allOrders);

        // then
//        mvc.perform(get("/api/orders")
        mvc.perform(get("/orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].customerName", is(order1.getCustomerName())));
    }

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before()
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	public void  createOrder_shouldCreateAndSaveOrder_thenReturnJsonArray() throws Exception {

		// given
		Order order = new Order(OrderId, "1", "Alex", "alex@gmail.com");

		// when
		when(orderService.saveOrder(new Order())).thenReturn(order);

		// then
		mockMvc.perform(post("/create"))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.createdDate").exists());

		verify(orderService, times(1)).saveOrder(any(Order.class));
		verifyNoMoreInteractions(orderService);

	}
}
