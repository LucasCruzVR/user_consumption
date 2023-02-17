package com.senior.api.UserConsumption.service;

import com.senior.api.UserConsumption.domain.Order;
import com.senior.api.UserConsumption.domain.OrderItem;
import com.senior.api.UserConsumption.domain.ProductService;
import com.senior.api.UserConsumption.itemize.OrderStatusEnum;
import com.senior.api.UserConsumption.itemize.ProductServiceStatusEnum;
import com.senior.api.UserConsumption.itemize.ProductServiceTypeEnum;
import com.senior.api.UserConsumption.model.order.OrderCreateDTO;
import com.senior.api.UserConsumption.model.order.OrderDetailDTO;
import com.senior.api.UserConsumption.model.order.OrderListDTO;
import com.senior.api.UserConsumption.repository.OrderItemRepository;
import com.senior.api.UserConsumption.repository.OrderRepository;
import com.senior.api.UserConsumption.repository.ProductServiceRepository;
import com.senior.api.UserConsumption.util.MapperClass;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductServiceRepository productServiceRepository;
    private final ModelMapper modelMapper;
    private final MapperClass mapperClass;

    public List<OrderListDTO> findAll(int page, int size) {
        if (page > 0) {
            page = page - 1;
        }
        Pageable pagination = PageRequest.of(page, size);
        Page<Order> orderPage = orderRepository.findAll(pagination);
        List<Order> orderList = orderPage.getContent();
        return MapperClass.converter(orderList, OrderListDTO.class);
    }

    public OrderDetailDTO findOne(Long id) {
        return mapperClass.toObject(getByOrder(id), OrderDetailDTO.class);
    }

    @Transactional
    public OrderDetailDTO save(OrderCreateDTO orderCreateDTO) {
        if (orderCreateDTO.getDiscountPercentage() == null) {
            orderCreateDTO.setDiscountPercentage(0.0);
        }
        if(!applyDiscountIfOrderIsOpen(orderCreateDTO)) {
            throw new IllegalArgumentException("Can't apply discount because order is inactive");
        }
        /* define order base attributes */
        Order order = Order.builder()
                .orderCode(orderCreateDTO.getOrderCode())
                .discountPercentage(orderCreateDTO.getDiscountPercentage())
                .status(orderCreateDTO.getStatus())
                .build();
        final Order orderStatic = order;

        /* Get orderItems and associate with products */
        Set<OrderItem> items = MapperClass.converter(orderCreateDTO.getOrderItem(), OrderItem.class);
        items.forEach(o -> o.setOrder(orderStatic));
        items = items.stream().map(item -> {
            item = item.toBuilder()
                    .productService(getByProductService(item.getProductService().getId())).build();
            return item;
        }).collect(Collectors.toSet());

        /* Add the quantity of Products and apply discount */
        Set<OrderItem> orderItem = new HashSet<>();
        orderItem.addAll(items.stream()
                .filter(item -> item.getProductService().getType().equals(ProductServiceTypeEnum.PRODUCT))
                .map(item -> {
                    item.setPrice(item.getAmount() * item.getProductService().getPrice() * (100 - orderCreateDTO.getDiscountPercentage()) / 100);
                    return item;
                }).collect(Collectors.toSet()));

        /* Apply price on Service item */
        orderItem.addAll(items.stream()
                .filter(item -> item.getProductService().getType().equals(ProductServiceTypeEnum.SERVICE))
                .map(item -> {
                    item.setPrice(item.getProductService().getPrice());
                    return item;
                })
                .collect(Collectors.toSet()));

        /* Add all prices of Products/Services linked to the Order */
        order.setFinalPrice(orderItem.stream().mapToDouble(oi -> oi.getPrice()).sum());

        order.getOrderItem().clear();
        order.getOrderItem().addAll(orderItem);

        order = orderRepository.save(order);
        //orderItemRepository.saveAll(orderItem);
        return mapperClass.toObject(order, OrderDetailDTO.class);
    }

    @Transactional
    public OrderDetailDTO update(Long id, OrderCreateDTO orderCreateDTO) {
        Order order = orderRepository.findById(id).orElse(Order.builder().build());
        if (orderCreateDTO.getDiscountPercentage() == null) {
            orderCreateDTO.setDiscountPercentage(0.0);
        }
        if(!applyDiscountIfOrderIsOpen(orderCreateDTO)) {
            throw new IllegalArgumentException("Can't apply discount because order is inactive");
        }
        /* define order base attributes */
        order = order.toBuilder()
                .orderCode(orderCreateDTO.getOrderCode())
                .discountPercentage(orderCreateDTO.getDiscountPercentage())
                .status(orderCreateDTO.getStatus())
                .build();
        final Order orderStatic = order;

        /* Get orderItems and associate with products */
        Set<OrderItem> items = MapperClass.converter(orderCreateDTO.getOrderItem(), OrderItem.class);
        items.forEach(o -> o.setOrder(orderStatic));
        items = items.stream().map(item -> {
            item = item.toBuilder()
                    .productService(getByProductService(item.getProductService().getId())).build();
            return item;
        }).collect(Collectors.toSet());

        /* Add the quantity of Products and apply discount */
        Set<OrderItem> orderItem = new HashSet<>();
        orderItem.addAll(items.stream()
                .filter(item -> item.getProductService().getType().equals(ProductServiceTypeEnum.PRODUCT))
                .map(item -> {
                    item.setPrice(item.getAmount() * item.getProductService().getPrice() * (100 - orderCreateDTO.getDiscountPercentage()) / 100);
                    return item;
                }).collect(Collectors.toSet()));

        /* Apply price on Service item */
        orderItem.addAll(items.stream()
                .filter(item -> item.getProductService().getType().equals(ProductServiceTypeEnum.SERVICE))
                .map(item -> {
                    item.setPrice(item.getProductService().getPrice());
                    return item;
                })
                .collect(Collectors.toSet()));

        /* Add all prices of Products/Services linked to the Order */
        order.setFinalPrice(orderItem.stream().mapToDouble(oi -> oi.getPrice()).sum());

        order.getOrderItem().clear();
        order.getOrderItem().addAll(orderItem);

        order = orderRepository.save(order);
        //orderItemRepository.saveAll(orderItem);
        return mapperClass.toObject(order, OrderDetailDTO.class);
    }

    @Transactional
    public void delete(Long id) {
        orderRepository.deleteById(id);
        //PSQLException
    }

    /* PRIVATE METHODS */

    private ProductService getByProductService(Long id) {
        ProductService productService = productServiceRepository.findById(id).orElseThrow(() -> new RuntimeException("product/service not found"));
        if(productService.getStatus().equals(ProductServiceStatusEnum.ACTIVE)) {
            return productService;
        }
        throw new IllegalArgumentException("Product "+ productService.getName() + " is inactive");
    }

    private Order getByOrder(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("product/service not found"));
    }

    private Boolean applyDiscountIfOrderIsOpen(OrderCreateDTO order) {
        if(order.getDiscountPercentage() > 0 && order.getStatus().equals(OrderStatusEnum.INACTIVE)) {
            return false;
        }
        return true;
    }

    //private Set<OrderItem> orderItemsWithCalculatedPrices(Set<OrderItem> items) {

    //}

    private void setAttributesInCreateOrder(Order order, OrderCreateDTO orderCreateDTO) {
        if(order.getId() == 0) {

        }
    }
}
