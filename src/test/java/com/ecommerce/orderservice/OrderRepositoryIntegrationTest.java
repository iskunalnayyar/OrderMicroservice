package com.ecommerce.orderservice;

import com.ecommerce.orderservice.models.Order;
import com.ecommerce.orderservice.repository.OrderRepository;
import com.ecommerce.orderservice.service.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class OrderRepositoryIntegrationTest {

    private static final Long OrderId = 1L;

	@TestConfiguration
	static class OrderRepositoryIntegrationTestContextConfiguration {

		@Bean
		public OrderService orderService() {
			return new OrderService();
		}
	}
	@Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @Before
    public void setUp() {
        Order order = new Order(OrderId, "1", "Alex", "alex@gmail.com");

        Mockito.when(orderRepository.findById(order.getOrderId()))
                .thenReturn(java.util.Optional.of(order));
    }

    @Test
    public void whenValidOrderId_thenOrderShouldBeFound() {
        Optional<Order> found = orderService.findByOrderId(OrderId);

        assertThat(found.get().getOrderId())
                .isEqualTo(OrderId);
    }
}
