package com.example.shoppingmall.domain.product.product.service.cursor;

import com.example.shoppingmall.domain.product.product.dto.ProductResponse;
import com.example.shoppingmall.domain.product.product.entity.Product;
import com.example.shoppingmall.domain.product.product.repository.ProductRepository;
import com.example.shoppingmall.domain.product.product.service.ProductReadService;
import com.example.shoppingmall.domain.product.product.util.ProductCursorUtilService;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.UserReadService;
import com.example.shoppingmall.util.CursorRequest;
import com.example.shoppingmall.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductLikeCursorReadService {
    private final ProductRepository productRepository;

    private final ProductCursorUtilService productCursorUtilService;
    private final ProductReadService productReadService;
    private final UserReadService userReadService;

    public PageCursor<ProductResponse> getLikeProductsByCursor(Principal principal, Number key, int size) throws Exception {
        User user = userReadService.getUserByEmail(principal.getName());

        CursorRequest cursorRequest = new CursorRequest(key, size);
        var products = findLikeAll(cursorRequest, user.getId());

        var productDtoList = products.stream()
                .map(productReadService::toProductResponse)
                .collect(Collectors.toList());
        var nextKey = productCursorUtilService.getNextKey(products);

        return new PageCursor<>(cursorRequest.next(nextKey), productDtoList);
    }

    private List<Product> findLikeAll(CursorRequest cursorRequest, Long userId) {
        if (cursorRequest.hasKey()) {
            return productRepository.findAllByLikeOrderByIdDescHasKey(cursorRequest.getKey().longValue(), userId, cursorRequest.getSize());
        } else {
            return productRepository.findAllByLikeOrderByIdDescNoKey(userId, cursorRequest.getSize());
        }
    }
}
