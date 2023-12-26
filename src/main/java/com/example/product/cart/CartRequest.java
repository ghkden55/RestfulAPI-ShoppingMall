package com.example.product.cart;

import com.example.product.option.Option;
import com.example.product.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

public class CartRequest {

    @Getter
    @Setter
    public static class SaveDTO {

        private Long optionId;

        private Long quantity;

        // 인증 정보가 필요하기 때문에 User는 매개변수로 항상 존재 해야함.
        public Cart toEntity(Option option, User user) {
            return Cart.builder()
                    .user(user)
                    .option(option)
                    .quantity(quantity)
                    .price(option.getPrice() * quantity)
                    .build();
        }

    }


    @Getter
    @Setter
    public static class UpdateDTO {

        private Long cartId;

        private Long quantity;

        public UpdateDTO(Long cartId, Long quantity) {
            this.cartId = cartId;
            this.quantity = quantity;
        }
    }

}
