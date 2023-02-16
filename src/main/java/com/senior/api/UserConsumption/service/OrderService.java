package com.senior.api.UserConsumption.service;

import com.senior.api.UserConsumption.domain.Order;
import com.senior.api.UserConsumption.domain.OrderItem;
import com.senior.api.UserConsumption.domain.ProductService;
import com.senior.api.UserConsumption.itemize.OrderStatusEnum;
import com.senior.api.UserConsumption.itemize.ProductServiceStatusEnum;
import com.senior.api.UserConsumption.itemize.ProductServiceTypeEnum;
import com.senior.api.UserConsumption.model.AllOrdersDTO;
import com.senior.api.UserConsumption.model.OrderDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductServiceRepository productServiceRepository;
    private final ModelMapper modelMapper;

    public List<AllOrdersDTO> findAll(int page, int size) {
        if (page > 0) {
            page = page - 1;
        }
        Pageable pagination = PageRequest.of(page, size);
        Page<Order> orderPage = orderRepository.findAll(pagination);
        List<Order> orderList = orderPage.getContent();
        return MapperClass.converter(orderList, AllOrdersDTO.class);
    }

    public Order findOne(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.orElseThrow(() -> new ObjectNotFoundException(id, "Not found"));
    }

    @Transactional
    public Order save(Order order) {
        Order orderSaved = orderRepository.save(order);
        Double sumOrderPrice = 0.0;
        for (OrderItem orderItem : orderSaved.getOrderItems()) {
            Optional<ProductService> productService = productServiceRepository.findById(orderItem.getProductService().getId());
            orderItem.setProductService(productService.get());
            orderItem.setOrder(orderSaved);

            /* Check if product is active to be part of order */
            if (orderItem.getProductService().getStatus() == ProductServiceStatusEnum.DISABLE) {
                throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY);
            }

            /*
            if(orderSaved.getDiscountPercentage() != null) {
                if(orderSaved.getStatus() == OrderStatusEnum.ACTIVE) {
                    sumOrderPrice +=
                }
            }*/
        }
        orderItemRepository.saveAll(orderSaved.getOrderItems());
        return orderSaved;
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
