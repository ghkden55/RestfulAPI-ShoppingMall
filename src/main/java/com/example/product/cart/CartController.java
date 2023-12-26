package com.example.product.cart;

import com.example.product.security.CustomUserDetails;
import com.example.product.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 카트에 상품 추가
    @PostMapping("/carts/add")
    public ResponseEntity<?> addCartList(@RequestBody @Valid List<CartRequest.SaveDTO> requestDTO,
                                        @AuthenticationPrincipal CustomUserDetails customUserDetails, // User의 인증 정보.
                                        Error error) {

        cartService.addCartList(requestDTO, customUserDetails.getUser());
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(null);
        return ResponseEntity.ok(apiResult);
    }


    // 카트 전체 상품 확인
    // @AuthenticationPrincipal CustomUserDetails customUserDetails 인증 받은 유저만 접근 가능. 미인증 유저의 경우 401에러 발생.
    @GetMapping("/carts")
    public ResponseEntity<?> carts(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        CartResponse.FindAllDTO findAllDTO = cartService.findAll();
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(findAllDTO);
        return ResponseEntity.ok(apiResult);
    }


    // 카트 업데이트
    @PutMapping("/carts/update")
    public ResponseEntity<?> update(@RequestBody @Valid List<CartRequest.UpdateDTO> requestDTO,
                                   @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                   Error error) {

        CartResponse.UpdateDTO updateDTO = cartService.update(requestDTO, customUserDetails.getUser());

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(updateDTO);
        return ResponseEntity.ok(apiResult);
    }


    @DeleteMapping("/carts/clear")
    public ResponseEntity<?> clear(@AuthenticationPrincipal CustomUserDetails customUserDetails, Error error) {
        cartService.clear(customUserDetails.getUser());
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(null);
        return ResponseEntity.ok(apiResult);
    }

}
