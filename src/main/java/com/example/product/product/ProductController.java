package com.example.product.product;

import com.example.product.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody ProductResponse.SaveDTO product){
        Product save = productService.save(product);

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(save);
        return ResponseEntity.ok(apiResult);
    }

    // RestController이기 때문에 에러 적어 줘야 함

    // 전체 상품 확인
    // 페이지를 int로 사용. db까지 접근을 한다면 Long을 쓰는 게 더 유리함.
    @GetMapping("/products")
    public ResponseEntity<?> findAll(@RequestParam(defaultValue = "0") int page) {
        List<ProductResponse.FindAllDTO> productDTOS = productService.findAll(page);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(productDTOS);
        return ResponseEntity.ok(apiResult);
    }


    // 개별 상품 확인
    @GetMapping("/products/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        ProductResponse.FindByIdDTO productDTOS = productService.findById(id);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(productDTOS);
        return ResponseEntity.ok(apiResult);
    }


    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@RequestBody ProductResponse.FindAllDTO findAllDTO){
        productService.update(findAllDTO);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(null);
        return ResponseEntity.ok(apiResult);

    }



    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success("Product with ID: " + id + " deleted successfully");
        return ResponseEntity.ok(apiResult);
    }

}
