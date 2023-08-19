package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.domain.post.dto.PostCommand;
import com.example.shoppingmall.domain.post.dto.PostDto;
import com.example.shoppingmall.domain.post.entity.Post;
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
import org.springframework.web.bind.annotation.*;

@Api(tags = "지구를 지키는 팁 커뮤니티")
@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {
    private final PostWriteService postWriteService;
    private final PostReadService postReadService;

//    @GetMapping("/users/{userId}")
//    public PostDto readPosts(@PathVariable Long userId) {
//
//    }

    @ApiOperation(value = "게시글 작성")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "현재 사용자의 id",dataType = "Long"),
            @ApiImplicitParam(name = "title", value = "글 제목", dataType = "Long"),
            @ApiImplicitParam(name = "contents", value = "글 내용", dataType = "Long")
    })
    @Operation(description = "postCommand를 받아 커뮤니티 게시글 등록")
    @PostMapping("/add")
    public PostDto createPost(PostCommand postCommand){
        var post = postWriteService.createPost(postCommand);
        return postReadService.toDto(post);
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
    public PageCursor<Post> readAllPosts(CursorRequest cursorRequest){
        return postReadService.getPostsByCursor(cursorRequest);
    }

}
