package com.sl.ms.ordermanagement.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.sl.ms.ordermanagement.items.Items;
import com.sl.ms.ordermanagement.items.ItemsRepository;
import com.sl.ms.ordermanagement.orders.Orders;
import com.sl.ms.ordermanagement.orders.OrdersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class.getName());

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    ItemsRepository itemsRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Orders save(Orders order) {
        return ordersRepository.save(order);
    }

    public Items saveItems(Items items) {
        return itemsRepository.save(items);
    }

    public boolean CheckProduct(Long Id) {
        log.info("Calling the inventory API");
        String url = "http://localhost:7777/dev/checkProducts/";
        log.info("After calling the inventory API");
        String result = restTemplate.getForObject(url + Id, String.class);
		log.info("Result: " + result);
        return Boolean.parseBoolean(result);
    }

    @HystrixCommand(fallbackMethod = "TestHystrixFallback")
    public String TestHystrix(Long Id) {
        String url = "http://localhost:7777/dev/checkProducts/";
        String result = restTemplate.getForObject(url + Id, String.class);
        return result;
    }

    public String TestHystrixFallback(Long Id) {
        return "Looks like service unavailable. Please try later";
    }

    public Optional<Orders> getById(Long id) {
        return ordersRepository.findById(id);
    }

    public void delete(Long id) {
        ordersRepository.deleteById(id);
    }

    public List<Orders> getOrders() {
        return ordersRepository.findAll();
    }

    public Optional<Orders> getByIdTest(Long id) {
        return ordersRepository.findById(id);
    }

}