package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.application.usecase.post.CreatePostUseCase;
import com.example.shoppingmall.domain.post.dto.PostCommand;
import com.example.shoppingmall.domain.post.dto.PostDto;
import com.example.shoppingmall.domain.post.service.PostLikeWriteService;
import com.example.shoppingmall.domain.post.service.PostReadService;
import com.example.shoppingmall.domain.post.service.PostWriteService;
import com.example.shoppingmall.util.CursorRequest;
import com.example.shoppingmall.util.PageCursor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {
    private final PostWriteService postWriteService;
    private final PostReadService postReadService;
    private final PostLikeWriteService postLikeWriteService;
    private final CreatePostUseCase createPostUseCase;


    @Operation(summary = "게시글 생성"
            , description = "PostCommand와 fileType(=image), multipartFiles(이미지들)을 받아 게시글 생성", tags = {"USER_ROLE"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = PostDto.class)))
    })
    @PostMapping("/add")
    public ResponseEntity<Object> uploadProduct(
            PostCommand postCommand,
            @RequestParam(value = "fileType") String fileType,
            @RequestPart(value = "files") List<MultipartFile> multipartFiles
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(createPostUseCase.execute(postCommand, fileType, multipartFiles));
    }


    @Operation(summary = "게시글 상세 조회", description = "postId를 받아 게시글 상세보기", tags = {"USER_ROLE"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = PostDto.class)))
    })
    @GetMapping("/{postId}")
    public PostDto readPost(@PathVariable Long postId){
        return postReadService.getPost(postId);
    }


    @Operation(summary = "모든 게시글 조회", description = "모든 게시글 조회(Cursor 기반 무한 스크롤 방식)", tags = {"USER_ROLE"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = PageCursor.class)))
    })
    @GetMapping("/all")
    public PageCursor<PostDto> readAllPosts(CursorRequest cursorRequest){
        return postReadService.getPostsByCursor(cursorRequest);
    }


    /**
     * Like 테이블을 따로 분리 했기에 조회 시 toDto 함수를 통해 count 가 집계되며 이는 쓰기 성능을 향상시키지만
     * 동시에 읽기 성능은 나빠진다. -> redis나 스케쥴러를 통해 배치로 업로드 하면 해결 가능!
     * @param postId
     * @param userId
     */
    @Operation(summary = "게시글 좋아요 생성", description = "postId와 userId를 받아 좋아요 관계 생성", tags = {"USER_ROLE"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PostMapping("/{postId}/like")
    public void createPostLike(@PathVariable Long postId, @RequestParam Long userId){
        postLikeWriteService.createPostLike(userId, postId);
    }

}
