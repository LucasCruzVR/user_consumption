package com.senior.api.UserConsumption.service;

import com.senior.api.UserConsumption.domain.Order;
import com.senior.api.UserConsumption.domain.OrderItem;
import com.senior.api.UserConsumption.domain.ProductService;
import com.senior.api.UserConsumption.model.AllOrdersDTO;
import com.senior.api.UserConsumption.model.OrderDTO;
import com.senior.api.UserConsumption.repository.OrderItemRepository;
import com.senior.api.UserConsumption.repository.OrderRepository;
import com.senior.api.UserConsumption.repository.ProductServiceRepository;
import com.senior.api.UserConsumption.util.MapperClass;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hibernate.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductServiceRepository productServiceRepository;
    private final ModelMapper modelMapper;

    public List<AllOrdersDTO> findAll() {
        return MapperClass.converter(orderRepository.findAll(), AllOrdersDTO.class);
    }

    public Order findOne(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.orElseThrow(() -> new ObjectNotFoundException(id, "Not found"));
    }

    @Transactional
    public Order save(Order order) {
        try {
            Order orderSaved = orderRepository.save(order);
            for (OrderItem orderItem : orderSaved.getOrderItems()) {
                Optional<ProductService> productService = productServiceRepository.findById(orderItem.getProductService().getId());
                orderItem.setProductService(productService.get());
                orderItem.setOrder(orderSaved);
            }
            orderItemRepository.saveAll(orderSaved.getOrderItems());
            return orderSaved;
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Transactional
    public OrderDTO update(Long id, Order newOrder) {
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        Order order = orderRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, "Not found"));
        modelMapper.map(newOrder, order);
        return MapperClass.converter(orderRepository.save(order), OrderDTO.class);
    }

    @Transactional
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
}
