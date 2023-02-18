package com.senior.api.UserConsumption.service;

import com.querydsl.core.BooleanBuilder;
import com.senior.api.UserConsumption.domain.ProductService;
import com.senior.api.UserConsumption.domain.QProductService;
import com.senior.api.UserConsumption.dto.product_service.ProductServiceCreateDTO;
import com.senior.api.UserConsumption.dto.product_service.ProductServiceDetailDTO;
import com.senior.api.UserConsumption.dto.product_service.ProductServiceUpdateDTO;
import com.senior.api.UserConsumption.itemize.ProductServiceStatusEnum;
import com.senior.api.UserConsumption.itemize.ProductServiceTypeEnum;
import com.senior.api.UserConsumption.repository.ProductServiceRepository;
import com.senior.api.UserConsumption.util.MapperClass;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceService {

    private final ProductServiceRepository productServiceRepository;
    private final ModelMapper modelMapper;
    private static QProductService qProductService = QProductService.productService;

    public List<ProductServiceDetailDTO> findAll(int page, int size, String name, ProductServiceTypeEnum type, ProductServiceStatusEnum status) {
        if (page > 0) {
            page = page - 1;
        }
        BooleanBuilder where = new BooleanBuilder();
        if (name != null) {
            where.and(qProductService.name.like("%" + name + "%"));
        }
        if (type != null) {
            where.and(qProductService.type.eq(type));
        }
        if (status != null) {
            where.and(qProductService.status.eq(status));
        }
        Pageable pagination = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        Page<ProductService> productServicePage;
        if (where.hasValue()) {
            productServicePage = productServiceRepository.findAll(where.getValue(), pagination);
        } else {
            productServicePage = productServiceRepository.findAll(pagination);
        }
        List<ProductService> productServiceList = productServicePage.getContent();
        return MapperClass.converter(productServiceList, ProductServiceDetailDTO.class);
    }

    public ProductService findOne(Long id) {
        Optional<ProductService> productService = productServiceRepository.findById(id);
        return productService.orElseThrow(() -> new ObjectNotFoundException(id, "Product/Service Not found"));
    }

    @Transactional
    public ProductServiceDetailDTO save(ProductServiceCreateDTO productService) {
        return MapperClass.converter(productServiceRepository.save(MapperClass.converter(productService, ProductService.class)), ProductServiceDetailDTO.class);
    }

    @Transactional
    public ProductServiceDetailDTO update(Long id, ProductServiceUpdateDTO productServiceUpdateDTO) {
        ProductService productService = productServiceRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, "Product/Service Not found"));
        productService = productService.toBuilder()
                .name(productServiceUpdateDTO.getName())
                .price(productServiceUpdateDTO.getPrice())
                .status(productServiceUpdateDTO.getStatus())
                .type(productServiceUpdateDTO.getType()).build();
        return MapperClass.converter(productServiceRepository.save(productService), ProductServiceDetailDTO.class);
    }

    public void delete(Long id) {
        try {
            productServiceRepository.deleteById(id);
        } catch (Exception ex) {
            throw new ConstraintViolationException("Unable to delete this Product/Service. Check if it is linked to an order", null, "");
        }
    }
}
