package com.senior.api.UserConsumption.controller;

import com.senior.api.UserConsumption.domain.Order;
import com.senior.api.UserConsumption.domain.ProductService;
import com.senior.api.UserConsumption.model.AllOrdersDTO;
import com.senior.api.UserConsumption.model.OrderDTO;
import com.senior.api.UserConsumption.model.ProductServiceReqDTO;
import com.senior.api.UserConsumption.model.ProductServiceRespDTO;
import com.senior.api.UserConsumption.service.OrderService;
import com.senior.api.UserConsumption.util.MapperClass;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrderService orderService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<AllOrdersDTO>> listAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "size", defaultValue = "5") int size) {
        return ResponseEntity.ok().body(orderService.findAll(page, size));
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Order> read(@PathVariable Long id) {
        return ResponseEntity.ok().body(orderService.findOne(id));
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Order> create(@RequestBody Order order) {
        return ResponseEntity.ok().body(orderService.save(order));
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<OrderDTO> update(@PathVariable Long id, @RequestBody Order order) {
        return ResponseEntity.ok().body(orderService.update(id, order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        orderService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
