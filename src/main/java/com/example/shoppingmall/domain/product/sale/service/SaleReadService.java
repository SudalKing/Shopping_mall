package com.example.shoppingmall.domain.product.sale.service;

import com.example.shoppingmall.domain.product.product.entity.Product;
import com.example.shoppingmall.domain.product.sale.entity.ProductSale;
import com.example.shoppingmall.domain.product.sale.repository.ProductSaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SaleReadService {
    private final ProductSaleRepository productSaleRepository;

    public int getDiscountPrice(final Product product) {
        Optional<ProductSale> productSale = productSaleRepository.findByProductId(product.getId());

        if (product.isSaled() && productSale.isPresent()) {
            return (int) Math.round(product.getPrice() * productSale.get().getDiscountRate());
        } else {
            return 0;
        }
    }

}
