package com.example.product.option;

import com.example.product.product.Product;
import com.example.product.product.ProductRepository;
import com.example.product.product.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OptionService {

    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;

    @Transactional
    public Option save(OptionRequest.saveDTO optionDTO) {

        Optional<Product> optionalOption = productRepository.findById(optionDTO.getProductId());

        if (optionalOption.isPresent()) {
            Product product = optionalOption.get();

            Option entity = optionDTO.toEntity(product);

            // Option 엔터티를 저장하고 반환
            return optionRepository.save(entity);
        } else {
            throw new IllegalArgumentException("해당 ID에 대한 Option을 찾을 수 없습니다. ID: " );
        }
    }


    // 옵션 개별 상품 검색
    public List<OptionResponse.FindByProductIdDTO> findByPorductId(Long id) {

        List<Option> optionList = optionRepository.findByProductId(id);

        List<OptionResponse.FindByProductIdDTO> optionResponses =
                optionList.stream().map(OptionResponse.FindByProductIdDTO::new)
                        .collect(Collectors.toList());

        return optionResponses;
    }


    // 옵션 전체 상품 검색
    public List<OptionResponse.FindAllDTO> findAll() {
        List<Option> optionList = optionRepository.findAll();

        List<OptionResponse.FindAllDTO> findAllDTOS =
                optionList.stream().map(OptionResponse.FindAllDTO::new)// optionResponse에 들어갈 new 생성
                        .collect(Collectors.toList());

        return findAllDTOS;
    }


    @Transactional
    public void update(OptionRequest.updateDTO updateDTO){
        Optional<Option> optionalOption = optionRepository.findById(updateDTO.getId());

        optionalOption.ifPresent(option -> {
            option.updateFromDTO(updateDTO);
        });
    }


    @Transactional
    public void delete(Long id, Long optionId) {
        // productTd에 해당하는 상품 찾기
        Product deleteProduct = productRepository.findById(id).get();

        // optionRepository를 사용하여 해당하는 상품이 속한 옵션들중 optionId와 일치하는 옵션 찾기
        Optional<Option> optionalOption = optionRepository.findByIdAndProduct(optionId, deleteProduct);

        // 찾는 옵션이 있다면 삭제
        if(optionalOption.isPresent()){
            optionRepository.delete(optionalOption.get());
        }
        // 없다면 예외처리
    }

}
