package com.senior.api.UserConsumption.controller;

import com.senior.api.UserConsumption.model.product_service.ProductServiceCreateDTO;
import com.senior.api.UserConsumption.model.product_service.ProductServiceDetailDTO;
import com.senior.api.UserConsumption.service.ProductServiceService;
import com.senior.api.UserConsumption.util.MapperClass;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("product-service")
@RequiredArgsConstructor
public class ProductServiceController {

    private final ProductServiceService productServiceService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<ProductServiceDetailDTO>> listAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                 @RequestParam(value = "size", defaultValue = "5") int size) {
        return ResponseEntity.ok().body(productServiceService.findAll(page, size));
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ProductServiceDetailDTO> read(@PathVariable Long id) {
        return ResponseEntity.ok().body(MapperClass.converter(productServiceService.findOne(id), ProductServiceDetailDTO.class));
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<ProductServiceDetailDTO> create(@RequestBody ProductServiceCreateDTO productService) {
        return ResponseEntity.ok().body(productServiceService.save(productService));
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ProductServiceDetailDTO> update(@PathVariable Long id, @RequestBody ProductServiceCreateDTO productService) {
        return ResponseEntity.ok().body(productServiceService.update(id, productService));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        productServiceService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
