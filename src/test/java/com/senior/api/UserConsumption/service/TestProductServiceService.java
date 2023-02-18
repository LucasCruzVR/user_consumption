package com.senior.api.UserConsumption.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.senior.api.UserConsumption.domain.ProductService;
import com.senior.api.UserConsumption.domain.QOrder;
import com.senior.api.UserConsumption.domain.QProductService;
import com.senior.api.UserConsumption.dto.product_service.ProductServiceUpdateDTO;
import com.senior.api.UserConsumption.itemize.ProductServiceStatusEnum;
import com.senior.api.UserConsumption.itemize.ProductServiceTypeEnum;
import com.senior.api.UserConsumption.dto.product_service.ProductServiceCreateDTO;
import com.senior.api.UserConsumption.dto.product_service.ProductServiceDetailDTO;
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
import org.springframework.data.domain.*;

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
    BooleanBuilder booleanBuilder;

    @Mock
    private ProductServiceRepository productServiceRepository;

    private static QProductService qProductService = QProductService.productService;

    private final ProductService productService1 = new ProductService(1L, "test1", 10.0, ProductServiceTypeEnum.PRODUCT, ProductServiceStatusEnum.ACTIVE, null);
    private final ProductService productService2 = new ProductService(2L, "test2", 12.0, ProductServiceTypeEnum.SERVICE, ProductServiceStatusEnum.ACTIVE, null);
    private final ProductService productService3 = new ProductService(3L, "test3", 14.0, ProductServiceTypeEnum.PRODUCT, ProductServiceStatusEnum.ACTIVE, null);
    private List<ProductService> productServiceList = new ArrayList<>();

    @Test
    public void listAllProductServiceWithPagination() {
        Pageable pagination = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "id"));
        productServiceList.addAll(Arrays.asList(productService1, productService2, productService3));
        Page<ProductService> pages = new PageImpl<ProductService>(productServiceList.subList(0, 2), pagination, productServiceList.size());

        Mockito.when((productServiceRepository.findAll(pagination))).thenReturn(pages);
        List<ProductServiceDetailDTO> resp = productServiceService.findAll(1, 2, null, null, null);

        Assertions.assertNotNull(resp);
        Assertions.assertEquals(resp.getClass(), ArrayList.class);
        Assertions.assertEquals(2, resp.size());
    }

    @Test
    public void listAllProductServiceWithPaginationAndFilter() {
        Pageable pagination = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "id"));
        productServiceList.addAll(Arrays.asList(productService1, productService2, productService3));
        Page<ProductService> pages = new PageImpl<ProductService>(productServiceList.subList(0, 1), pagination, productServiceList.size());

        Mockito.when((productServiceRepository.findAll(Mockito.any(Predicate.class), Mockito.any(Pageable.class)))).thenReturn(pages);
        List<ProductServiceDetailDTO> resp = productServiceService.findAll(1,
                2,
                productService1.getName(),
                productService1.getType(),
                productService1.getStatus());

        Assertions.assertNotNull(resp);
        Assertions.assertEquals(resp.getClass(), ArrayList.class);
        Assertions.assertEquals(1, resp.size());
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
        ProductService p1 = new ProductService(10L, "test1", 12.0, ProductServiceTypeEnum.PRODUCT, ProductServiceStatusEnum.ACTIVE, null);
        ProductService p2 = new ProductService(10L, "test2", 10.0, ProductServiceTypeEnum.SERVICE, ProductServiceStatusEnum.ACTIVE, null);
        Mockito.when((productServiceRepository.save(Mockito.any(ProductService.class)))).thenReturn(p2);
        Mockito.when((productServiceRepository.findById(p1.getId()))).thenReturn(Optional.of(p1));
        ProductServiceDetailDTO resp = productServiceService.update(p1.getId(), MapperClass.converter(p2, ProductServiceUpdateDTO.class));
        assertEquals(resp.getPrice(), p2.getPrice());
        Assertions.assertEquals(resp.getClass(), ProductServiceDetailDTO.class);
    }

    @Test
    public void notFoundProductServiceIdToUpdate() {
        ProductService p1 = new ProductService(10L, "test1", 12.0, ProductServiceTypeEnum.PRODUCT, ProductServiceStatusEnum.ACTIVE, null);
        Mockito.when((productServiceRepository.findById(0L))).thenReturn(Optional.empty());
        assertThrows("Not Found", ObjectNotFoundException.class, () -> productServiceService.update(0L, Mockito.any(ProductServiceUpdateDTO.class)));
    }

    @Test
    public void deleteProductService() {
        ProductService p1 = new ProductService(10L, "test1", 12.0, ProductServiceTypeEnum.PRODUCT, ProductServiceStatusEnum.ACTIVE, null);
        Mockito.when((productServiceRepository.findById(Mockito.any(Long.class)))).thenReturn(Optional.of(p1));
        productServiceService.delete(p1.getId());
        Mockito.verify(productServiceRepository, Mockito.times(1)).deleteById(p1.getId());
    }

    @Test
    public void notFoundProductServiceIdToDelete() {
        ProductService p1 = new ProductService(10L, "test1", 12.0, ProductServiceTypeEnum.PRODUCT, ProductServiceStatusEnum.ACTIVE, null);
        Mockito.when((productServiceRepository.findById(0L))).thenReturn(Optional.empty());

        assertThrows("Not Found", ObjectNotFoundException.class, () -> productServiceService.delete(0L));
    }
}
