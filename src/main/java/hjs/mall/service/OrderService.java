package hjs.mall.service;

import hjs.mall.controller.dto.OrderResponseDto;
import hjs.mall.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Log4j2
public class OrderService {
    private OrderRepository orderRepository;

    public List<OrderResponseDto> getAllOrders(Long member_id) {
        return orderRepository.findAll(member_id)
                .stream()
                .map(OrderResponseDto::new)
                .collect(Collectors.toList());
    }

}
