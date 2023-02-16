package com.senior.api.UserConsumption.service;

import com.senior.api.UserConsumption.domain.Order;
import com.senior.api.UserConsumption.domain.OrderItem;
import com.senior.api.UserConsumption.domain.ProductService;
import com.senior.api.UserConsumption.itemize.ProductServiceTypeEnum;
import com.senior.api.UserConsumption.model.order.OrderListDTO;
import com.senior.api.UserConsumption.model.order.OrderCreateDTO;
import com.senior.api.UserConsumption.model.order.OrderDetailDTO;
import com.senior.api.UserConsumption.repository.OrderItemRepository;
import com.senior.api.UserConsumption.repository.OrderRepository;
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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductServiceRepository productServiceRepository;
    private final ModelMapper modelMapper;

    public List<OrderListDTO> findAll(int page, int size) {
        if (page > 0) {
            page = page - 1;
        }
        Pageable pagination = PageRequest.of(page, size);
        Page<Order> orderPage = orderRepository.findAll(pagination);
        List<Order> orderList = orderPage.getContent();
        return MapperClass.converter(orderList, OrderListDTO.class);
    }

    public Order findOne(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.orElseThrow(() -> new ObjectNotFoundException(id, "Not found"));
    }

    @Transactional
    public OrderDetailDTO save(OrderCreateDTO orderCreateDTO) {
        Order order = Order.builder()
                .orderCode(orderCreateDTO.getOrderCode())
                .discountPercentage(orderCreateDTO.getDiscountPercentage())
                .finalPrice(orderCreateDTO.getFinalPrice())
                .status(orderCreateDTO.getStatus())
                //.orderItems(MapperClass.converter(orderCreateDTO.getProductServiceList(),OrderItem.class))
                .build();
        final Order orderStatic = order;

        Set<OrderItem> items = MapperClass.converter(orderCreateDTO.getProductServiceList(), OrderItem.class);
        items.forEach(o -> o.setOrder(orderStatic));
        items = items.stream().map(item -> {
            item = item.toBuilder()
                    .productService(getByProductService(item.getProductService().getId())).build();
            //item.setPrice(item.getAmount() * item.getProductService().getPrice());
            return item;
        }).collect(Collectors.toSet());
        Set<OrderItem> orderItem = new HashSet<>();
        orderItem.addAll(items.stream()
                .filter(item -> item.getProductService().getType().equals(ProductServiceTypeEnum.PRODUCT))
                .map(item -> {
                    item.setPrice(item.getAmount() * item.getProductService().getPrice() * (100 - orderCreateDTO.getDiscountPercentage()) / 100);
                    return item;
                }).collect(Collectors.toSet()));
        orderItem.addAll(items.stream()
                .filter(item -> item.getProductService().getType().equals(ProductServiceTypeEnum.SERVICE))
                        .map(item -> {
                            item.setPrice(item.getProductService().getPrice());
                            return item;
                        })
                    .collect(Collectors.toSet()));
        order.setFinalPrice(orderItem.stream().mapToDouble(oi -> oi.getPrice()).sum());

        order = orderRepository.save(order);
        orderItemRepository.saveAll(orderItem);
        return MapperClass.converter(order, OrderDetailDTO.class);
    }

    @Transactional
    public OrderCreateDTO update(Long id, Order newOrder) {
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        Order order = orderRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, "Not found"));
        modelMapper.map(newOrder, order);
        return MapperClass.converter(orderRepository.save(order), OrderCreateDTO.class);
    }

    @Transactional
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    private ProductService getByProductService(Long id) {
        return productServiceRepository.findById(id).orElseThrow(() -> new RuntimeException("product/service not found"));
    }
}
