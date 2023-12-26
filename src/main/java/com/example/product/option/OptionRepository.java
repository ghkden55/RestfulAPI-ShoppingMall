package com.example.product.option;

import com.example.product.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OptionRepository extends JpaRepository<Option, Long> {

    // 이름이 쿼리문이 돼서 findById 이름이 겹치기 때문에 findByProductId로 변경
    List<Option> findByProductId(Long id);

    Optional<Option> findByIdAndProduct(Long optionId, Product deleteProduct);

}
