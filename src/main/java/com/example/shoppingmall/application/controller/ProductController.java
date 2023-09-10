package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.application.usecase.product.CreateProductUseCase;
import com.example.shoppingmall.application.usecase.product.DeleteProductUseCase;
import com.example.shoppingmall.domain.product.dto.ProductCommand;
import com.example.shoppingmall.domain.product.dto.ProductCommentCommand;
import com.example.shoppingmall.domain.product.dto.ProductCommentDto;
import com.example.shoppingmall.domain.product.dto.ProductDto;
import com.example.shoppingmall.domain.product.entity.Product;
import com.example.shoppingmall.domain.product.service.ProductCommentReadService;
import com.example.shoppingmall.domain.product.service.ProductCommentWriteService;
import com.example.shoppingmall.domain.product.service.ProductReadService;
import com.example.shoppingmall.domain.product.service.ProductWriteService;
import com.example.shoppingmall.util.CursorRequest;
import com.example.shoppingmall.util.PageCursor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "상품과 댓글 기능")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/product")
public class ProductController {
    private final ProductWriteService productWriteService;
    private final ProductReadService productReadService;
    private final ProductCommentWriteService productCommentWriteService;
    private final ProductCommentReadService productCommentReadService;
    private final CreateProductUseCase createProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;


    @ApiOperation(value = "상품 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", dataType = "String"),
            @ApiImplicitParam(name = "modelName", dataType = "String"),
            @ApiImplicitParam(name = "price", dataType = "int"),
            @ApiImplicitParam(name = "stock", dataType = "int"),
            @ApiImplicitParam(name = "description", dataType = "String"),
            @ApiImplicitParam(name = "categoryId", dataType = "Long")
    })
    @Operation(description = "name, modelName, price, stock, description, categoryId를 받아 ProductCommand class 변환 후 상품 등록")
    @PostMapping("/add")
    public ResponseEntity<Object> uploadProduct(
            ProductCommand productCommand,
            @RequestParam(value = "fileType") String fileType,
            @RequestPart(value = "files")List<MultipartFile> multipartFiles
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(createProductUseCase.execute(productCommand, fileType, multipartFiles));
    }

//    @ApiOperation(value = "모든 상품 조회-")
//    @Operation(responses = @ApiResponse(responseCode = "200", description = "UserDto class List형 반환"
//            , content = @Content(schema = @Schema(implementation = UserDto.class)))
//    )
//    @GetMapping("/all")
//    public List<ProductDto> getAllProducts(){
//        return productReadService.getAllProducts();
//    }

    @ApiOperation(value = "상품 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", dataType = "String"),
            @ApiImplicitParam(name = "modelName", dataType = "String"),
            @ApiImplicitParam(name = "price", dataType = "int"),
            @ApiImplicitParam(name = "stock", dataType = "int"),
            @ApiImplicitParam(name = "description", dataType = "String"),
            @ApiImplicitParam(name = "categoryId", dataType = "Long")
    })
    @Operation(description = "name, modelName, price, stock, description, categoryId를 받아 ProductCommand class 변환 후 상품 수정- 인라인 형식의 PutMapping 사용")
    @PutMapping("/{productId}")
    public ProductDto updateProduct(@PathVariable Long productId, ProductCommand productCommand){
        var product = productWriteService.updateProduct(productId, productCommand);
        return productReadService.toDto(product);
    }

    @ApiOperation(value = "상품 삭제")
    @ApiImplicitParam(name = "productId", value = "상품 id", dataType = "Long")
    @Operation(description = "상품 삭제", responses = @ApiResponse(responseCode = "200", description = "반환값 없음"))
    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable Long productId){
        deleteProductUseCase.execute(productId);
    }

    @ApiOperation(value = "모든 상품 조회 - cursor 기반 pagination")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "key 값은 productId + 2와 같음", dataType = "Long"),
            @ApiImplicitParam(name = "size", value = "설정한 size 의 갯수만큼 반환", dataType = "int")
    })
    @Operation(responses = @ApiResponse(responseCode = "200", description = "key 값을 받아 size 만큼 product 반환"))
    @GetMapping("/all")
    public PageCursor<Product> getProductsByCursor(CursorRequest cursorRequest){
        return productReadService.getProductsByCursor(cursorRequest);
    }

    @ApiOperation(value = "상품 댓글 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "현재 사용자의 id",dataType = "Long"),
            @ApiImplicitParam(name = "productId", value = "현재 댓글을 작성하는 상품의 id", dataType = "Long"),
            @ApiImplicitParam(name = "contents", value = "댓글 내용", dataType = "String")
    })
    @Operation(description = "userId, productId, contents 를 받아 productCommentCommand class 변환 후 상품 댓글 등록")
    @PostMapping("/{userId}/comment")
    public ProductCommentDto createComment(@PathVariable Long userId, ProductCommentCommand productCommentCommand){
        var comment = productCommentWriteService.createProductComment(userId, productCommentCommand);
        return productCommentReadService.toDto(comment);
    }

    @ApiOperation(value = "상품의 모든 댓글 조회")
    @Operation(description = "상품 id를 통해 해당 상품의 모든 댓글 조회 List<ProductCommentDto> 반환",
            responses = @ApiResponse(responseCode = "200", description = "ProductCommentDto List형 반환"))
    @GetMapping("/{productId}/comments")
    public List<ProductCommentDto> getAllCommentsByProductId(@PathVariable Long productId){
        return productCommentReadService.getAllComments(productId);
    }

    @ApiOperation(value = "상품 댓글 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commentId", value = "삭제하려는 댓글의 id",dataType = "Long"),
            @ApiImplicitParam(name = "userId", value = "현재 사용자의 id", dataType = "Long")
    })
    @Operation(description = "id를 비교해 댓글을 작성한 사용자가 맞다면 댓글 삭제")
    @DeleteMapping("/{commentId}/comments")
    public void deleteComment(@PathVariable Long commentId, @RequestParam Long userId){
        productCommentWriteService.deleteProductComment(commentId, userId);
    }
}
