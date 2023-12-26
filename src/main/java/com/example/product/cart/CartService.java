package com.example.product.cart;

import com.example.product.error.exception.Exception400;
import com.example.product.error.exception.Exception404;
import com.example.product.error.exception.Exception500;
import com.example.product.option.Option;
import com.example.product.option.OptionRepository;
import com.example.product.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final OptionRepository optionRepository;

    public CartResponse.FindAllDTO findAll() {

        List<Cart> cartList = cartRepository.findAll();

        return new CartResponse.FindAllDTO(cartList);
    }


    // 카트에 상품 추가
    @Transactional
    public void addCartList(List<CartRequest.SaveDTO> saveDTO, User user) {

        // Set<> 동일한 데이터를 묶어줌. 동일한 상품 예외처리
        Set<Long> optionsId = new HashSet<>();

        for (CartRequest.SaveDTO cart : saveDTO) {
            if (!optionsId.add(cart.getOptionId())) {
                throw new Exception400("이미 동일한 상품 옵션이 있습니다." + cart.getOptionId());
            }
        }

        List<Cart> cartList = saveDTO.stream().map(cartDTO -> {
            Option option = optionRepository.findById(cartDTO.getOptionId()).orElseThrow(
                    () -> new Exception404("해당 상품 옵션을 찾을 수 없습니다." + cartDTO.getOptionId())
            );
            return cartDTO.toEntity(option, user);
        }).collect(Collectors.toList());

        cartList.forEach(cart -> {
            try {
                cartRepository.save(cart);
            } catch (Exception exception) {
                throw new Exception500("카트에 담던 중 오류가 발생했습니다." + exception.getMessage());
            }
        });

    }


    @Transactional
    public CartResponse.UpdateDTO update(List<CartRequest.UpdateDTO> requestDTO, User user) {

        // 카트에 있는 모든 정보를 가져옴
        List<Cart> cartList = cartRepository.findAllByUserId(user.getId());

        List<Long> cartIds = cartList.stream().map(cart -> cart.getId()).collect(Collectors.toList());
        List<Long> requestIds = requestDTO.stream().map(dto -> dto.getCartId()).collect(Collectors.toList());

        if (cartIds.size() == 0) { // 자료가 없음
            throw new Exception404("주문 가능한 상품이 없습니다.");
        }

        // distinct() = 동일한 키는 제거 ex) 1, 1, 3, 3, 4 -> 3개
        if (requestIds.stream().distinct().count() != requestIds.size()) {
            throw new Exception400("동일한 카트 아이디를 주문할 수 없습니다.");
        }

        for (Long requestId : requestIds) {
            if (!cartIds.contains(requestId)) {
                throw new Exception400("카트에 없는 상품은 주문할 수 없습니다." + requestId);
            }
        }

        for (CartRequest.UpdateDTO updateDTO : requestDTO) {
            for (Cart cart : cartList) {
                if (cart.getId() == updateDTO.getCartId()) {
                    cart.update(updateDTO.getQuantity(), cart.getOption().getPrice() * updateDTO.getQuantity());
                }
            }
        }

        return new CartResponse.UpdateDTO(cartList);
    }


    @Transactional
    public void clear(User user) {
        cartRepository.deleteAllByUserId(user.getId());
    }

}
