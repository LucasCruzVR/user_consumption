package com.senior.api.UserConsumption.controller;

import com.senior.api.UserConsumption.dto.product_service.ProductServiceCreateDTO;
import com.senior.api.UserConsumption.dto.product_service.ProductServiceDetailDTO;
import com.senior.api.UserConsumption.dto.product_service.ProductServiceUpdateDTO;
import com.senior.api.UserConsumption.itemize.ProductServiceStatusEnum;
import com.senior.api.UserConsumption.itemize.ProductServiceTypeEnum;
import com.senior.api.UserConsumption.service.ProductServiceService;
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

@Api(tags = "Products and Services", value = "Product/Service")
@RestController
@RequestMapping("product-service")
@RequiredArgsConstructor
public class ProductServiceController {

    private final ProductServiceService productServiceService;

    @ApiOperation(value = "List all products and services", notes = "May have filters", protocols = "Accept=application/json", response = ProductServiceDetailDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ProductServiceDetailDTO.class)})
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<ProductServiceDetailDTO>> listAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                 @RequestParam(value = "size", defaultValue = "5") int size,
                                                                 @RequestParam(value = "name", required = false) String name,
                                                                 @RequestParam(value = "type", required = false)ProductServiceTypeEnum type,
                                                                 @RequestParam(value = "status", required = false) ProductServiceStatusEnum status) {
        return ResponseEntity.ok().body(productServiceService.findAll(page, size, name, type, status));
    }

    @ApiOperation(value = "Return one project/service by id", notes = "Return only one product/service", protocols = "Accept=application/json", response = ProductServiceDetailDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ProductServiceDetailDTO.class)})
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ProductServiceDetailDTO> read(@PathVariable Long id) {
        return ResponseEntity.ok().body(MapperClass.converter(productServiceService.findOne(id), ProductServiceDetailDTO.class));
    }

    @ApiOperation(value = "Create a product or service", notes = "Create a product or service", protocols = "Accept=application/json", response = ProductServiceDetailDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = ProductServiceDetailDTO.class)})
    @PostMapping
    @ResponseBody
    public ResponseEntity<ProductServiceDetailDTO> create(@RequestBody @Valid ProductServiceCreateDTO productService) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productServiceService.save(productService));
    }

    @ApiOperation(value = "Update a product or service", notes = "Update a product or service", protocols = "Accept=application/json", response = ProductServiceDetailDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Updated", response = ProductServiceDetailDTO.class)})
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ProductServiceDetailDTO> update(@PathVariable Long id, @RequestBody @Valid ProductServiceUpdateDTO productService) {
        return ResponseEntity.ok().body(productServiceService.update(id, productService));
    }

    @ApiOperation(value = "Delete a product or service", notes = "Delete a product or service", protocols = "Accept=application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deleted")})
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        productServiceService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
