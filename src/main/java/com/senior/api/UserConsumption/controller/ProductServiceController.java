package com.senior.api.UserConsumption.controller;

import com.senior.api.UserConsumption.dto.product_service.ProductServiceCreateDTO;
import com.senior.api.UserConsumption.dto.product_service.ProductServiceDetailDTO;
import com.senior.api.UserConsumption.itemize.ProductServiceStatusEnum;
import com.senior.api.UserConsumption.itemize.ProductServiceTypeEnum;
import com.senior.api.UserConsumption.service.ProductServiceService;
import com.senior.api.UserConsumption.util.MapperClass;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("product-service")
@RequiredArgsConstructor
public class ProductServiceController {

    private final ProductServiceService productServiceService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<ProductServiceDetailDTO>> listAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                 @RequestParam(value = "size", defaultValue = "5") int size,
                                                                 @RequestParam(value = "name", required = false) String name,
                                                                 @RequestParam(value = "type", required = false)ProductServiceTypeEnum type,
                                                                 @RequestParam(value = "status", required = false) ProductServiceStatusEnum status) {
        return ResponseEntity.ok().body(productServiceService.findAll(page, size, name, type, status));
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ProductServiceDetailDTO> read(@PathVariable Long id) {
        return ResponseEntity.ok().body(MapperClass.converter(productServiceService.findOne(id), ProductServiceDetailDTO.class));
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<ProductServiceDetailDTO> create(@RequestBody @Valid ProductServiceCreateDTO productService) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productServiceService.save(productService));
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ProductServiceDetailDTO> update(@PathVariable Long id, @RequestBody @Valid ProductServiceCreateDTO productService) {
        return ResponseEntity.ok().body(productServiceService.update(id, productService));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        productServiceService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
