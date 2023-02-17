package com.senior.api.UserConsumption.service;

import com.senior.api.UserConsumption.domain.ProductService;
import com.senior.api.UserConsumption.model.product_service.ProductServiceCreateDTO;
import com.senior.api.UserConsumption.model.product_service.ProductServiceDetailDTO;
import com.senior.api.UserConsumption.repository.ProductServiceRepository;
import com.senior.api.UserConsumption.util.MapperClass;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceService {

    private final ProductServiceRepository productServiceRepository;
    private final ModelMapper modelMapper;

    public List<ProductServiceDetailDTO> findAll(int page, int size) {
        if (page > 0) {
            page = page - 1;
        }
        Pageable pagination = PageRequest.of(page, size);
        Page<ProductService> productServicePage = productServiceRepository.findAll(pagination);
        List<ProductService> productServiceList = productServicePage.getContent();
        return MapperClass.converter(productServiceList, ProductServiceDetailDTO.class);
    }

    public ProductService findOne(Long id) {
        Optional<ProductService> productService = productServiceRepository.findById(id);
        return productService.orElseThrow(() -> new ObjectNotFoundException(id, "Not found"));
    }

    @Transactional
    public ProductServiceDetailDTO save(ProductServiceCreateDTO productService) {
        return MapperClass.converter(productServiceRepository.save(MapperClass.converter(productService, ProductService.class)), ProductServiceDetailDTO.class);
    }

    @Transactional
    public ProductServiceDetailDTO update(Long id, ProductServiceCreateDTO newProductService) {
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        ProductService productService = productServiceRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, "Not found"));
        modelMapper.map(newProductService, productService);
        return MapperClass.converter(productServiceRepository.save(productService), ProductServiceDetailDTO.class);
    }

    @Transactional
    public void delete(Long id) {
        productServiceRepository.deleteById(id);
    }
}
