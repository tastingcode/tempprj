package taco.board.comment.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import taco.board.comment.service.CommentServiceV2;
import taco.board.comment.service.request.CommentCreateRequestV2;
import taco.board.comment.service.response.CommentPageResponse;
import taco.board.comment.service.response.CommentResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentControllerV2 {
    private final CommentServiceV2 commentService;

    @GetMapping({"/v2/comments/{commentId}"})
    public CommentResponse read(@PathVariable("commentId") Long commentId) {
        return this.commentService.read(commentId);
    }

    @PostMapping({"/v2/comments"})
    public CommentResponse create(@RequestBody CommentCreateRequestV2 request) {
        return this.commentService.create(request);
    }

    @DeleteMapping({"/v2/comments/{commentId}"})
    public void delete(@PathVariable("commentId") Long commentId) {
        this.commentService.delete(commentId);
    }

    @GetMapping({"/v2/comments"})
    public CommentPageResponse readAll(@RequestParam("articleId") Long articleId, @RequestParam("page") Long page, @RequestParam("pageSize") Long pageSize) {
        return this.commentService.readAll(articleId, page, pageSize);
    }

    @GetMapping({"/v2/comments/infinite-scroll"})
    public List<CommentResponse> readAllInfiniteScroll(@RequestParam("articleId") Long articleId, @RequestParam(value = "lastPath",required = false) String lastPath, @RequestParam("pageSize") Long pageSize) {
        return this.commentService.readAllInfiniteScroll(articleId, lastPath, pageSize);
    }
}