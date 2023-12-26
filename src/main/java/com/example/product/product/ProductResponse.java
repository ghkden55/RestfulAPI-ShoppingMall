package com.example.product.product;

import com.example.product.option.Option;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

public class ProductResponse {

    @Getter
    @Setter
    public static class SaveDTO {

        private Long id;

        private String productName;

        private String description;

        private String image;

        private int price;

        public Product toEntity() {
            return Product.builder()
                    .productName(productName)
                    .description(description)
                    .image(image)
                    .price(price)
                    .build();
        }

    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class FindAllDTO {

        private Long id;

        private String productName;

        private String description;

        private String image;

        private int price;

        public FindAllDTO(Product product) {
            this.id = product.getId();
            this.productName = product.getProductName();
            this.description = product.getDescription();
            this.image = product.getImage();
            this.price = product.getPrice();
        }

    }


    @Getter
    @Setter
    public static class FindByIdDTO{
        private Long id;

        private String productName;

        private String description;

        private String image;

        private int price;

        private List<OptionDTO> optionList;

        public FindByIdDTO(Product product, List<Option> optionList) {
            this.id = product.getId();
            this.productName = product.getProductName();
            this.description = product.getDescription();
            this.image = product.getImage();
            this.price = product.getPrice();
            this.optionList = optionList.stream().map(OptionDTO::new)
                    .collect(Collectors.toList());
        }

    }


    @Getter
    @Setter
    public static class OptionDTO {
        private Long id;
        private String optionName;
        private Long Price;
        private Long quantity;

        public OptionDTO(Option option) {
            this.id = option.getId();
            this.optionName = option.getOptionName();
            this.Price = option.getPrice();
            quantity = option.getQuantity();
        }

    }

}
