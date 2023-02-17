package com.senior.api.service;

import com.senior.api.UserConsumption.domain.ProductService;
import com.senior.api.UserConsumption.itemize.ProductServiceStatusEnum;
import com.senior.api.UserConsumption.itemize.ProductServiceTypeEnum;
import com.senior.api.UserConsumption.model.product_service.ProductServiceCreateDTO;
import com.senior.api.UserConsumption.model.product_service.ProductServiceDetailDTO;
import com.senior.api.UserConsumption.repository.ProductServiceRepository;
import com.senior.api.UserConsumption.service.ProductServiceService;
import com.senior.api.UserConsumption.util.MapperClass;
import org.hibernate.ObjectNotFoundException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TestProductServiceService {

    @InjectMocks
    private ProductServiceService productServiceService;

    @Mock
    private ProductServiceRepository productServiceRepository;

    private final ProductService productService1 = new ProductService(1L, "test1", 10.0, ProductServiceTypeEnum.PRODUCT, ProductServiceStatusEnum.ACTIVE, null);
    private final ProductService productService2 = new ProductService(2L, "test2", 12.0, ProductServiceTypeEnum.SERVICE, ProductServiceStatusEnum.ACTIVE, null);
    private final ProductService productService3 = new ProductService(3L, "test3", 14.0, ProductServiceTypeEnum.PRODUCT, ProductServiceStatusEnum.ACTIVE, null);
    private List<ProductService> productServiceList = new ArrayList<>();

    @Test
    public void listAllProductServiceWithPagination() {
        Pageable pagination = PageRequest.of(0, 2);
        productServiceList.addAll(Arrays.asList(productService1, productService2, productService3));
        Page<ProductService> pages = new PageImpl<ProductService>(productServiceList.subList(0, 2), pagination, productServiceList.size());
        Mockito.when((productServiceRepository.findAll(pagination))).thenReturn(pages);
        List<ProductServiceDetailDTO> resp = productServiceService.findAll(1, 2);
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(resp.getClass(), ArrayList.class);
        Assertions.assertEquals(2, resp.size());
    }

    @Test
    public void findOneProductService() {
        Mockito.when((productServiceRepository.findById(productService1.getId()))).thenReturn(Optional.of(productService1));
        ProductService resp = productServiceService.findOne(1L);
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(resp.getClass(), ProductService.class);
    }

    @Test
    public void notFoundProductService() {
        Mockito.when((productServiceRepository.findById(5L))).thenReturn(Optional.empty());
        assertThrows("Not Found", ObjectNotFoundException.class, () -> productServiceService.findOne(5L));
    }

    @Test
    public void createProductService() {
        ProductServiceCreateDTO productServiceCreateDTO = new ProductServiceCreateDTO("test1", 10.0, ProductServiceTypeEnum.PRODUCT, ProductServiceStatusEnum.ACTIVE);
        Mockito.when((productServiceRepository.save(Mockito.any(ProductService.class)))).thenReturn(MapperClass.converter(productServiceCreateDTO, ProductService.class));
        ProductServiceDetailDTO resp = productServiceService.save(productServiceCreateDTO);
        assertEquals(resp.getName(), productService1.getName());
        assertEquals(resp.getPrice(), productService1.getPrice());
        assertEquals(resp.getType(), productService1.getType());
        assertEquals(resp.getStatus(), productService1.getStatus());
        Assertions.assertEquals(resp.getClass(), ProductServiceDetailDTO.class);
    }

    @Test
    public void UpdateProductService() {
        ProductServiceCreateDTO productServiceCreateDTO = new ProductServiceCreateDTO("test1", 12.0, ProductServiceTypeEnum.PRODUCT, ProductServiceStatusEnum.ACTIVE);
        Mockito.when((productServiceRepository.save(Mockito.any(ProductService.class)))).thenReturn(MapperClass.converter(productServiceCreateDTO, ProductService.class));
        ProductServiceDetailDTO resp = productServiceService.update(productService1.getId(), productServiceCreateDTO);
        assertEquals(resp.getPrice(), productServiceCreateDTO.getPrice());
        Assertions.assertEquals(resp.getClass(), ProductServiceDetailDTO.class);
    }

    @Test
    public void deleteProductService() {
        final ProductService ps1 = new ProductService(1L, "test1", 10.0, ProductServiceTypeEnum.PRODUCT, ProductServiceStatusEnum.ACTIVE, null);
        final ProductService ps2 = new ProductService(1L, "test2", 12.0, ProductServiceTypeEnum.SERVICE, ProductServiceStatusEnum.ACTIVE, null);

        productServiceService.delete(ps1.getId());
        assertThrows("Not Found", ObjectNotFoundException.class, () -> productServiceService.findOne(5L));
    }
}
