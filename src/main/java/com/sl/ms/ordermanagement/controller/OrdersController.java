package com.sl.ms.ordermanagement.controller;

import com.sl.ms.ordermanagement.items.Items;
import com.sl.ms.ordermanagement.orders.Orders;
import com.sl.ms.ordermanagement.orders.OrdersRepository;
import com.sl.ms.ordermanagement.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class OrdersController {

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    OrderService orderservice;

    private static final Logger log = LoggerFactory.getLogger(OrdersController.class.getName());

    @GetMapping("/orders")
    public List<Orders> getOrders() {
        log.info("Fetching All Orders: ");
        List<Orders> orders = (List<Orders>) orderservice.getOrders();
        return orders;
    }

    @RequestMapping("/orders/{Id}")
    public Optional<Orders> getOrdersById(@PathVariable("Id") Long Id) throws RuntimeException{
        Optional<Orders> orders = orderservice.getById(Id);
        log.info("Fetching orders for Id: " + orders);
        if (!orders.isPresent())
             throw new RuntimeException("Item  " + Id  + " not found in inventory. ");
        else {
            return orders;
        }
    }

    @PostMapping(path = "/orders/{Id}", consumes = {"application/json"})
    @ResponseBody
    public ResponseEntity<Object> createItemsForOrderId(@PathVariable("Id") Long Id, @RequestBody Items items)  throws RuntimeException{
        log.info("Creating Items For OrderId: "+Id);
        Optional<Orders> OrdersId = ordersRepository.findById(Id);
        if (!OrdersId.isPresent()) {
            throw new RuntimeException("Item  " + Id  + " not found in inventory. ");
        } else {
            Orders orders = OrdersId.get();
            ((Items) items).setOrders(orders);
            orderservice.saveItems(items);
        }
        return new ResponseEntity<Object>(items, HttpStatus.OK);
    }

    @DeleteMapping("/orders/{id}")
    private ResponseEntity<Object> deleteForOrdersId(@PathVariable("id") Long id) {
        log.info("Deleting Items For OrderId: "+id);
        Optional<Orders> delete = orderservice.getById(id);
        orderservice.delete(id);
        return new ResponseEntity<Object>(delete, HttpStatus.OK);
    }

    @GetMapping("/orderHystrix/{id}")
    public String getOrdersHystrix(@PathVariable("id") Long id) {
        log.info("orderHystrix Items For OrderId: "+id);
        return orderservice.TestHystrix(id);

    }
}
