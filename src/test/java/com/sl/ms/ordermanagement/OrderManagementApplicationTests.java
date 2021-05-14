package com.sl.ms.ordermanagement;

import com.sl.ms.ordermanagement.orders.Orders;
import com.sl.ms.ordermanagement.orders.OrdersRepository;
import com.sl.ms.ordermanagement.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class OrderManagementApplicationTests {
	@Test
	void contextLoads() {
	}

	@InjectMocks
	private OrderService orderService;

	@Mock
	private OrdersRepository ordersRepository;

	private static final Logger log = LoggerFactory.getLogger(OrderManagementApplicationTests.class);

	@Test
	void getOrdersById(){
		final Long id = 1L;
		final Orders orders = new Orders(1L, "A1",100);
		given(ordersRepository.findById(id)).willReturn(Optional.of(orders));
		final Optional<Orders> expected = orderService.getById(id);
		assertThat(expected).isNotNull();
	}

	@Test
	public void getOrdersTest() {
		when(ordersRepository.findAll()).thenReturn(Stream.of(new Orders(1L, "Item1", 499), new Orders(2L, "Item 2", 599)).
				collect(Collectors.toList()));
		assertEquals(2, orderService.getOrders().size());
	}

	@Test
	public void saveOrdersByIdTest()	{
		Orders or = new Orders(2L,"B2",500);
		when(orderService.save(or)).thenReturn(or);
		assertEquals(or, orderService.save(or));
	}


}
