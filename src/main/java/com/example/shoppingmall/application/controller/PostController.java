package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.application.usecase.post.CreatePostUseCase;
import com.example.shoppingmall.domain.post.dto.PostCommand;
import com.example.shoppingmall.domain.post.dto.PostDto;
import com.example.shoppingmall.domain.post.service.PostLikeWriteService;
import com.example.shoppingmall.domain.post.service.PostReadService;
import com.example.shoppingmall.domain.post.service.PostWriteService;
import com.example.shoppingmall.util.CursorRequest;
import com.example.shoppingmall.util.PageCursor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "지구를 지키는 팁 커뮤니티")
@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {
    private final PostWriteService postWriteService;
    private final PostReadService postReadService;
    private final PostLikeWriteService postLikeWriteService;
    private final CreatePostUseCase createPostUseCase;

    @ApiOperation(value = "게시글 작성")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "현재 사용자의 id",dataType = "Long"),
            @ApiImplicitParam(name = "title", value = "글 제목", dataType = "Long"),
            @ApiImplicitParam(name = "contents", value = "글 내용", dataType = "Long")
    })
    @Operation(description = "postCommand를 받아 커뮤니티 게시글 등록")
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

    @ApiOperation(value = "게시글 상세 조회")
    @Operation(description = "postId를 통해 게시글 상세 조회", responses = @ApiResponse(responseCode = "200", description = "PostDto 반환"))
    @GetMapping("/{postId}")
    public PostDto readPost(@PathVariable Long postId){
        return postReadService.getPost(postId);
    }

    @ApiOperation(value = "모든 게시글 조회 - cursor 기반 pagination")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "key 값 요청 후 +1을 하여 요청하면 그 다음 id의 게시글 조회", dataType = "Long"),
            @ApiImplicitParam(name = "size", value = "설정한 size 의 갯수만큼 반환", dataType = "int")
    })
    @Operation(responses = @ApiResponse(responseCode = "200", description = "key 값을 받아 size 만큼 post 반환"))
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
    @ApiOperation(value = "게시글 좋아요")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", value = "현재 게시글의 id", dataType = "Long"),
            @ApiImplicitParam(name = "userId", value = "현재 사용자의 id", dataType = "Long"),
    })
    @Operation(description = "게시글과 사용자의 id를 받아 좋아요 생성")
    @PostMapping("/{postId}/like")
    public void createPostLike(@PathVariable Long postId, @RequestParam Long userId){
        postLikeWriteService.createPostLike(userId, postId);
    }

}
