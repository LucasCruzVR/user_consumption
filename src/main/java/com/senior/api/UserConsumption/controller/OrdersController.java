package com.senior.api.UserConsumption.controller;

import com.senior.api.UserConsumption.dto.order.OrderCreateDTO;
import com.senior.api.UserConsumption.dto.order.OrderDetailDTO;
import com.senior.api.UserConsumption.dto.order.OrderListDTO;
import com.senior.api.UserConsumption.dto.order.OrderUpdateDTO;
import com.senior.api.UserConsumption.itemize.OrderStatusEnum;
import com.senior.api.UserConsumption.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrderService orderService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<OrderListDTO>> listAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "size", defaultValue = "5") int size,
                                                      @RequestParam(value = "orderCode", required = false) String orderCode,
                                                      @RequestParam(value = "status", required = false) OrderStatusEnum status) {
        return ResponseEntity.ok().body(orderService.findAll(page, size, orderCode, status));
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<OrderDetailDTO> read(@PathVariable Long id) {
        return ResponseEntity.ok().body(orderService.findOne(id));
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<OrderDetailDTO> create(@RequestBody @Valid OrderCreateDTO order) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.save(order));
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<OrderDetailDTO> update(@PathVariable Long id, @RequestBody @Valid OrderUpdateDTO order) {
        return ResponseEntity.ok().body(orderService.update(id, order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        orderService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
