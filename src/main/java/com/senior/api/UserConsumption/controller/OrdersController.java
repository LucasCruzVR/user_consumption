package com.senior.api.UserConsumption.controller;

import com.senior.api.UserConsumption.dto.order.OrderCreateDTO;
import com.senior.api.UserConsumption.dto.order.OrderDetailDTO;
import com.senior.api.UserConsumption.dto.order.OrderListDTO;
import com.senior.api.UserConsumption.dto.order.OrderUpdateDTO;
import com.senior.api.UserConsumption.itemize.OrderStatusEnum;
import com.senior.api.UserConsumption.service.OrderService;
import com.senior.api.UserConsumption.util.MapperClass;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "Orders", value = "Order")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrderService orderService;
    private final MapperClass mapperClass;

    @ApiOperation(value = "List all orders", notes = "May have filters", protocols = "Accept=application/json", response = OrderListDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = OrderListDTO.class)})
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<OrderListDTO>> listAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "size", defaultValue = "5") int size,
                                                      @RequestParam(value = "orderCode", required = false) String orderCode,
                                                      @RequestParam(value = "status", required = false) OrderStatusEnum status) {
        return ResponseEntity.ok().body(orderService.findAll(page, size, orderCode, status));
    }

    @ApiOperation(value = "Return one order", notes = "Return only one order", protocols = "Accept=application/json", response = OrderDetailDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = OrderDetailDTO.class)})
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<OrderDetailDTO> read(@PathVariable Long id) {
        return ResponseEntity.ok().body(mapperClass.toObject(orderService.findOne(id), OrderDetailDTO.class));
    }

    @ApiOperation(value = "Create one order", notes = "Create one order", protocols = "Accept=application/json", response = OrderDetailDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success", response = OrderDetailDTO.class)})
    @PostMapping
    @ResponseBody
    public ResponseEntity<OrderDetailDTO> create(@RequestBody @Valid OrderCreateDTO order) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapperClass.toObject(orderService.save(order), OrderDetailDTO.class));
    }

    @ApiOperation(value = "Update one order", notes = "Update only one order", protocols = "Accept=application/json", response = OrderDetailDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = OrderDetailDTO.class)})
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<OrderDetailDTO> update(@PathVariable Long id, @RequestBody @Valid OrderUpdateDTO order) {
        return ResponseEntity.ok().body(mapperClass.toObject(orderService.update(id, order), OrderDetailDTO.class));
    }

    @ApiOperation(value = "Delete one order", notes = "Delete only one order", protocols = "Accept=application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")})
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        orderService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
