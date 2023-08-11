package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.domain.post.service.PostReadService;
import com.example.shoppingmall.domain.product.dto.ProductCommand;
import com.example.shoppingmall.domain.product.dto.ProductCommentCommand;
import com.example.shoppingmall.domain.product.dto.ProductCommentDto;
import com.example.shoppingmall.domain.product.dto.ProductDto;
import com.example.shoppingmall.domain.product.entity.Category;
import com.example.shoppingmall.domain.product.entity.Product;
import com.example.shoppingmall.domain.product.service.ProductCommentReadService;
import com.example.shoppingmall.domain.product.service.ProductCommentWriteService;
import com.example.shoppingmall.domain.product.service.ProductReadService;
import com.example.shoppingmall.domain.product.service.ProductWriteService;
import com.example.shoppingmall.util.CursorRequest;
import com.example.shoppingmall.util.PageCursor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/product")
public class ProductController {
    private final ProductWriteService productWriteService;
    private final ProductReadService productReadService;
    private final ProductCommentWriteService productCommentWriteService;
    private final ProductCommentReadService productCommentReadService;

    @PostMapping("/add")
    public ProductDto addProduct(ProductCommand productCommand){
        var product = productWriteService.createProduct(productCommand);
        return productReadService.toDto(product);
    }

    @GetMapping("/all")
    public List<ProductDto> getAllProducts(){
        return productReadService.getAllProducts();
    }

    @PutMapping("/{productId}")
    public ProductDto updateProduct(@PathVariable Long productId, ProductCommand productCommand){
        var product = productWriteService.updateProduct(productId, productCommand);
        return productReadService.toDto(product);
    }

    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable Long productId){
        productWriteService.deleteProduct(productId);
    }

    @GetMapping("/byCursor")
    public PageCursor<Product> getProductsByCursor(CursorRequest cursorRequest){
        return productReadService.getProductsByCursor(cursorRequest);
    }

    @PostMapping("/{userId}/comment")
    public ProductCommentDto createComment(@PathVariable Long userId, ProductCommentCommand productCommentCommand){
        var comment = productCommentWriteService.createProductComment(userId, productCommentCommand);
        return productCommentReadService.toDto(comment);
    }

    @GetMapping("/{productId}/comments")
    public List<ProductCommentDto> getAllCommentsByProductId(@PathVariable Long productId){
        return productCommentReadService.getAllComments(productId);
    }

    @DeleteMapping("/{commentId}/comments")
    public void deleteComment(@PathVariable Long commentId, @RequestParam Long userId){
        productCommentWriteService.deleteProductComment(commentId, userId);
    }
}
