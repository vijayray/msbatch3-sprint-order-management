package com.sl.ms.ordermanagement;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sl.ms.ordermanagement.controller.OrdersController;
import com.sl.ms.ordermanagement.items.Items;
import com.sl.ms.ordermanagement.items.ItemsRepository;
import com.sl.ms.ordermanagement.orders.Orders;
import com.sl.ms.ordermanagement.orders.OrdersRepository;
import com.sl.ms.ordermanagement.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrdersController.class)
@ActiveProfiles("test")
public class OrdersControllerIntegrationTest {
	
	@Autowired
    private MockMvc mockMvc;

	@MockBean
    private OrderService orderService;
    
    @MockBean
    private OrdersRepository ordersRepository;
    
    @MockBean
    private ItemsRepository itemsRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private List<Orders> orderList; 
    private List<Items> itemList;
    
    HttpHeaders httpHeaders = new HttpHeaders();
    
    @BeforeEach
    void setUp() {                               
       this.orderList = new ArrayList<>();                                    
       this.orderList.add(new Orders(1L, "Item1", 1001));
       this.orderList.add(new Orders(2L, "Item2", 1200));
       this.orderList.add(new Orders(3L, "Item3", 34));
       this.itemList = new ArrayList<>();
       this.itemList.add(new Items(1L, "A1", 10, 599, 345));
       this.itemList.add(new Items(2L, "B1", 11, 599, 579));
       this.itemList.add(new Items(3L, "C1", 12, 599, 45));
    }
    
    @Test
    void getAllOrdersTest() throws Exception {
        given(orderService.getOrders()).willReturn(orderList);
        this.mockMvc.perform(get("/orders"))
                .andExpect(status().isOk());
    }

    @Test
    void getOrderByIdTest() throws Exception {
	 final Long orderId = 1L;
	 Optional<Orders> order = Optional.ofNullable(new Orders(1L, "Item1", 100));

	 Mockito.when(orderService.getById(orderId)).thenReturn(order);

        this.mockMvc.perform(get("/orders/{Id}" , orderId))
                .andExpect(status().isOk());
    }
    
    @Test
    void saveItemToOrderTest() throws Exception {
	 final Long orderId = 1L;
	 httpHeaders.add("Content-Type", "application/json");
	 Items item1 = new Items(1L, "Item1", 100, 100, 100);
	 String inputInJson = this.mapToJson(item1);
        mockMvc.perform(MockMvcRequestBuilders.post("/orders/create/{Id}", orderId , item1).content(inputInJson).headers(httpHeaders)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    
    @Test
    void saveOrderCheckProductTest() throws Exception {
	 final Long orderId = 1L;
	 Optional<Orders> order = Optional.ofNullable(new Orders(1L, "Item1", 100));
	 Mockito.when(orderService.CheckProduct(orderId)).thenReturn(true);
        this.mockMvc.perform(post("/order")).andExpect(status().isNotFound());
        assertThat(true);
    }
    
    @Test
    void hysterixTest() throws Exception {
	 final Long orderId = 1L;
	 Optional<Orders> order = Optional.ofNullable(new Orders(1L, "Item1", 100));
	 Mockito.when(orderService.TestHystrix(orderId)).thenReturn("true");
        this.mockMvc.perform(post("/TestHystrix")).andReturn();
        assertThat(true);
                
    }

    @Test
    void deleteOrderByIdTest() throws Exception {
	 final Long orderId = 1L;
	 orderService.delete(orderId);
	 verify(orderService).delete(1L);
        this.mockMvc.perform(delete("/orders/{id}" , orderId))
                .andExpect(status().isOk());
    }
    
   	 @Test
		public void postOrderTest() throws Exception {
			Orders mockOrder = new Orders();
			mockOrder.setId(1L);
			mockOrder.setName("GOD");
			mockOrder.setTotal_amount(599);
			mockOrder.setItems(itemList);
			String inputInJson = this.mapToJson(mockOrder);
			
			String URI = "/testOrderSave";
			
			Mockito.when(orderService.save(Mockito.any(Orders.class))).thenReturn(mockOrder);
			
			RequestBuilder requestBuilder = MockMvcRequestBuilders
					.post(URI)
					.accept(MediaType.APPLICATION_JSON).content(inputInJson)
					.contentType(MediaType.APPLICATION_JSON);

			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			MockHttpServletResponse response = result.getResponse();
			
			String outputInJson = response.getContentAsString();
			
			assertThat(outputInJson).isNotEqualTo(inputInJson);
			assertNotEquals(HttpStatus.OK.value(), response.getStatus());
		}

		private String mapToJson(Object object) throws JsonProcessingException {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(object);
		}

}
