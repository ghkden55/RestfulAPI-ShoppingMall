package com.example.product.order;

import com.example.product.order.item.Item;
import com.example.product.product.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    @Getter
    @Setter
    public static class FindByIdDTO {

        private Long id;

        private List<ProductDTO> productDTOS;

        private Long totalPrice;

        public FindByIdDTO(Order order, List<Item> itemList) {
            this.id = order.getId();
            this.productDTOS = itemList.stream()
                    .map(item -> item.getOption().getProduct()).distinct()
                    .map(product -> new ProductDTO(itemList, product)).collect(Collectors.toList());
            this.totalPrice = itemList.stream().mapToLong(item -> item.getOption().getPrice() * item.getQuantity()).sum();
        }


        @Getter
        @Setter
        public class ProductDTO {

            private String productName;

            private List<ItemDTO> itemDTOS;

            public ProductDTO(List<Item> items, Product product) {
                this.productName = product.getProductName();
                this.itemDTOS = items.stream().filter(item -> item.getOption().getProduct().getId() == product.getId())
                        .map(ItemDTO::new)
                        .collect(Collectors.toList());
            }


            @Getter
            @Setter
            public class ItemDTO {

                private Long id;

                private String optionName;

                private Long quantity;

                private Long price;

                public ItemDTO(Item item) {
                    this.id = item.getId();
                    this.optionName = item.getOption().getOptionName();
                    this.quantity = item.getQuantity();
                    this.price = item.getPrice();
                }
            }
        }
    }
}
