package com.example.product.order;

import com.example.product.cart.Cart;
import com.example.product.cart.CartRepository;
import com.example.product.error.exception.Exception404;
import com.example.product.error.exception.Exception500;
import com.example.product.order.item.Item;
import com.example.product.order.item.ItemRepository;
import com.example.product.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public OrderResponse.FindByIdDTO save(User user) {

        // 장바구니 조회
        List<Cart> cartList = cartRepository.findAllByUserId(user.getId());

        if (cartList.size() == 0) {
            throw new Exception404("장바구니에 상품 내역이 존재하지 않습니다.");
        }

        // 주문 생성
        Order order = orderRepository.save(Order.builder().user(user).build());

        // 아이템을 장바구니에 저장?
        List<Item> itemList = new ArrayList<>();

        for (Cart cart : cartList) {
            Item item = Item.builder()
                    .option(cart.getOption())
                    .order(order)
                    .quantity(cart.getQuantity())
                    .price(cart.getOption().getPrice() * cart.getQuantity())
                    .build();

            itemList.add(item);
        }

        try {
            itemRepository.saveAll(itemList);
        } catch (Exception exception) {
            throw new Exception500("결제 실패"); // 저장하다 생긴 오류는 전부 500번 오류.
        }

        return new OrderResponse.FindByIdDTO(order, itemList);
    }


    public OrderResponse.FindByIdDTO findById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new Exception404("해당 주문 내역을 찾을 수 없습니다.: " + id)
        );
        List<Item> itemList = itemRepository.findAllByOrderId(id);
        return new OrderResponse.FindByIdDTO(order, itemList);
    }


    @Transactional
    public void clear() {
        try {
            itemRepository.deleteAll();
        } catch (Exception exception) {
            throw new Exception500("아이템 삭제 오류: " + exception.getMessage());
        }
    }

}
