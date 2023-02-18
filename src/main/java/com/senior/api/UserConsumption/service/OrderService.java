package com.senior.api.UserConsumption.service;

import com.querydsl.core.BooleanBuilder;
import com.senior.api.UserConsumption.domain.Order;
import com.senior.api.UserConsumption.domain.OrderItem;
import com.senior.api.UserConsumption.domain.ProductService;
import com.senior.api.UserConsumption.domain.QOrder;
import com.senior.api.UserConsumption.dto.order.OrderCreateDTO;
import com.senior.api.UserConsumption.dto.order.OrderListDTO;
import com.senior.api.UserConsumption.dto.order.OrderUpdateDTO;
import com.senior.api.UserConsumption.itemize.OrderStatusEnum;
import com.senior.api.UserConsumption.itemize.ProductServiceStatusEnum;
import com.senior.api.UserConsumption.itemize.ProductServiceTypeEnum;
import com.senior.api.UserConsumption.repository.OrderRepository;
import com.senior.api.UserConsumption.repository.ProductServiceRepository;
import com.senior.api.UserConsumption.util.MapperClass;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final ProductServiceRepository productServiceRepository;
    private final MapperClass mapperClass;
    private static QOrder qOrder = QOrder.order;

    public List<OrderListDTO> findAll(int page, int size, String orderCode, OrderStatusEnum status) {
        if (page > 0) {
            page = page - 1;
        }
        BooleanBuilder where = new BooleanBuilder();
        if (orderCode != null) {
            where.and(qOrder.orderCode.like("%" + orderCode + "%"));
        }
        if (status != null) {
            where.and(qOrder.status.eq(status));
        }
        Pageable pagination = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        Page<Order> orderPage;
        if (where.hasValue()) {
            orderPage = orderRepository.findAll(where.getValue(), pagination);
        } else {
            orderPage = orderRepository.findAll(pagination);
        }

        List<Order> orderList = orderPage.getContent();
        return MapperClass.converter(orderList, OrderListDTO.class);
    }

    public Order findOne(Long id) {
        return getByOrder(id);
    }

    @Transactional
    public Order save(OrderCreateDTO orderCreateDTO) {
        if (orderCreateDTO.getDiscountPercentage() == null) {
            orderCreateDTO.setDiscountPercentage(0.0);
        }
        if (!applyDiscountIfOrderIsOpen(orderCreateDTO)) {
            throw new IllegalArgumentException("Can't apply discount because order is inactive");
        }
        /* define order base attributes */
        Order order = Order.builder()
                .orderCode(orderCreateDTO.getOrderCode())
                .discountPercentage(orderCreateDTO.getDiscountPercentage())
                .status(orderCreateDTO.getStatus())
                .build();

        /* get orderItems from DTO and calculate final price */
        Set<OrderItem> orderItem = MapperClass.converter(orderCreateDTO.getOrderItem(), OrderItem.class);
        orderItem = orderItemsWithCalculatedPrices(orderItem, order, orderCreateDTO.getDiscountPercentage());

        /* Add all prices of Products/Services linked to the Order */
        order.setFinalPrice(orderItem.stream().mapToDouble(oi -> oi.getPrice()).sum());

        order.getOrderItem().clear();
        order.getOrderItem().addAll(orderItem);

        return orderRepository.save(order);
    }

    @Transactional
    public Order update(Long id, OrderUpdateDTO orderUpdateDTO) {
        Order order = getByOrder(id);
        if (orderUpdateDTO.getDiscountPercentage() == null) {
            orderUpdateDTO.setDiscountPercentage(0.0);
        }
        if (!applyDiscountIfOrderIsOpen(MapperClass.converter(orderUpdateDTO, OrderCreateDTO.class))) {
            throw new IllegalArgumentException("Can't apply discount because order is inactive");
        }
        /* define order base attributes */
        order = order.toBuilder()
                .discountPercentage(orderUpdateDTO.getDiscountPercentage())
                .status(orderUpdateDTO.getStatus())
                .build();

        /* get orderItems from DTO and calculate final price */
        Set<OrderItem> orderItem = MapperClass.converter(orderUpdateDTO.getOrderItem(), OrderItem.class);
        orderItem = orderItemsWithCalculatedPrices(orderItem, order, orderUpdateDTO.getDiscountPercentage());

        /* Add all prices of Products/Services linked to the Order */
        order.setFinalPrice(orderItem.stream().mapToDouble(oi -> oi.getPrice()).sum());

        order.getOrderItem().clear();
        order.getOrderItem().addAll(orderItem);

        return orderRepository.save(order);
    }

    @Transactional
    public void delete(Long id) {
        orderRepository.deleteById(id);
        //PSQLException
    }

    /* PRIVATE METHODS */

    private ProductService getByProductService(Long id) {
        ProductService productService = productServiceRepository.findById(id).orElseThrow(() -> new RuntimeException("Product/Service not found"));
        if (productService.getStatus().equals(ProductServiceStatusEnum.ACTIVE)) {
            return productService;
        }
        throw new IllegalArgumentException("Product " + productService.getName() + " is inactive");
    }

    private Order getByOrder(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, "Order not found"));
    }

    private Boolean applyDiscountIfOrderIsOpen(OrderCreateDTO order) {
        if (order.getDiscountPercentage() > 0 && order.getStatus().equals(OrderStatusEnum.INACTIVE)) {
            return false;
        }
        return true;
    }

    private Set<OrderItem> orderItemsWithCalculatedPrices(Set<OrderItem> items, Order order, Double discount) {
        /* Get orderItems and associate with products */
        items.forEach(o -> o.setOrder(order));
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
                    item.setPrice(item.getAmount() * item.getProductService().getPrice() * (100 - discount) / 100);
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
        return orderItem;
    }
}
