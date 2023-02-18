package com.senior.api.UserConsumption.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.senior.api.UserConsumption.domain.Order;
import com.senior.api.UserConsumption.domain.ProductService;
import com.senior.api.UserConsumption.domain.QOrder;
import com.senior.api.UserConsumption.dto.order.OrderCreateDTO;
import com.senior.api.UserConsumption.dto.order.OrderDetailDTO;
import com.senior.api.UserConsumption.dto.order.OrderListDTO;
import com.senior.api.UserConsumption.dto.order.OrderUpdateDTO;
import com.senior.api.UserConsumption.itemize.OrderStatusEnum;
import com.senior.api.UserConsumption.repository.OrderRepository;
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

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class TestOrderService {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    private static QOrder qOrder = QOrder.order;

    private final Order order1 = new Order(1L, "123", 10.0, OrderStatusEnum.ACTIVE, 10.0, null);
    private final Order order2 = new Order(2L, "123", 10.0, OrderStatusEnum.ACTIVE, 10.0, null);

    @Test
    public void listAllOrdersWithPagination() {
        List<Order> orderList = new ArrayList<>();
        orderList.addAll(Arrays.asList(order1, order2));
        Pageable pagination = PageRequest.of(1, 2, Sort.by(Sort.Direction.ASC, "id"));
        Page<Order> pages = new PageImpl<>(orderList, pagination, orderList.size());
        Mockito.when(orderRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pages);

        List<OrderListDTO> resp = orderService.findAll(1, 2, null, null);
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(resp.getClass(), ArrayList.class);
    }

    @Test
    public void findOneOrder() {
        Mockito.when((orderRepository.findById(Mockito.anyLong()))).thenReturn(Optional.of(order1));
        Order resp = orderService.findOne(order1.getId());
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(resp.getClass(), Order.class);
    }

    @Test
    public void listAllOrdersWithPaginationAndQueryDSL() {
        List<Order> orderList = new ArrayList<>();
        orderList.addAll(Arrays.asList(order1, order2));
        Pageable pagination = PageRequest.of(1, 2, Sort.by(Sort.Direction.ASC, "id"));
        Page<Order> pages = new PageImpl<>(orderList, pagination, orderList.size());
        Mockito.when(orderRepository.findAll(Mockito.any(Predicate.class), Mockito.any(Pageable.class))).thenReturn(pages);

        List<OrderListDTO> resp = orderService.findAll(1, 2, order1.getOrderCode(), order1.getStatus());
        Assertions.assertNotNull(resp);
        Assertions.assertEquals(resp.getClass(), ArrayList.class);
    }

    @Test
    public void notFoundOrder() {
        Mockito.when((orderRepository.findById(5L))).thenReturn(Optional.empty());
        assertThrows("Not Found", ObjectNotFoundException.class, () -> orderService.findOne(5L));
    }


    @Test
    public void createOrder() {
        OrderCreateDTO orderCreateDTO = new OrderCreateDTO("test1", 10.0, OrderStatusEnum.ACTIVE, new HashSet<>());
        Mockito.when((orderRepository.save(Mockito.any(Order.class)))).thenReturn(MapperClass.converter(orderCreateDTO, Order.class));
        Order resp = orderService.save(orderCreateDTO);
        assertEquals(resp.getOrderCode(), orderCreateDTO.getOrderCode());
        Assertions.assertEquals(resp.getClass(), Order.class);
    }

    @Test
    public void UpdateOrder() {
        Order order1 = new Order(10L, "test1", 10.0, OrderStatusEnum.ACTIVE, 10.0, new HashSet<>());
        Order order2 = new Order(10L, "test2", 10.0, OrderStatusEnum.ACTIVE, 10.0, new HashSet<>());
        Mockito.when((orderRepository.save(Mockito.any(Order.class)))).thenReturn(order2);
        Mockito.when((orderRepository.findById(Mockito.anyLong()))).thenReturn(Optional.of(order1));
        Order resp = orderService.update(order1.getId(), MapperClass.converter(order2, OrderUpdateDTO.class));
        assertEquals(resp.getOrderCode(), order2.getOrderCode());
        Assertions.assertEquals(resp.getClass(), Order.class);
    }

    @Test
    public void notFoundOrderIdToUpdate() {
        Order order1 = new Order(10L, "test1", 10.0, OrderStatusEnum.ACTIVE, 10.0, new HashSet<>());
        Mockito.when((orderRepository.findById(Mockito.anyLong()))).thenReturn(Optional.empty());
        assertThrows("Not Found", ObjectNotFoundException.class, () -> orderService.update(0L, Mockito.any(OrderUpdateDTO.class)));
    }

    @Test
    public void deleteOrder() {
        orderService.delete(order1.getId());
        Mockito.verify(orderRepository, Mockito.times(1)).deleteById(order1.getId());
    }
}
