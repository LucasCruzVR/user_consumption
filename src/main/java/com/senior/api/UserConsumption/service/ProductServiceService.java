package com.senior.api.UserConsumption.service;

import com.senior.api.UserConsumption.domain.ProductService;
import com.senior.api.UserConsumption.model.ProductServiceReqDTO;
import com.senior.api.UserConsumption.model.ProductServiceRespDTO;
import com.senior.api.UserConsumption.repository.ProductServiceRepository;
import com.senior.api.UserConsumption.util.MapperClass;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
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

    public List<ProductServiceRespDTO> findAll(int page, int size) {
        if (page > 0) {
            page = page - 1;
        }
        Pageable pagination = PageRequest.of(page, size);
        Page<ProductService> productServicePage = productServiceRepository.findAll(pagination);
        List<ProductService> productServiceList = productServicePage.getContent();
        return MapperClass.converter(productServiceList, ProductServiceRespDTO.class);
    }

    public ProductService findOne(Long id) {
        Optional<ProductService> productService = productServiceRepository.findById(id);
        return productService.orElseThrow(() -> new ObjectNotFoundException(id, "Not found"));
    }

    @Transactional
    public ProductServiceRespDTO save(ProductService productService) {
        return MapperClass.converter(productServiceRepository.save(productService), ProductServiceRespDTO.class);
    }

    @Transactional
    public ProductServiceRespDTO update(Long id, ProductServiceReqDTO newProductService) {
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        ProductService productService = productServiceRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, "Not found"));
        modelMapper.map(newProductService, productService);
        return MapperClass.converter(productServiceRepository.save(productService), ProductServiceRespDTO.class);
    }

    @Transactional
    public void delete(Long id) {
        productServiceRepository.deleteById(id);
    }
}
