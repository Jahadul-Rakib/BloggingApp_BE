package com.rakib.controller.api;

import com.google.common.collect.ImmutableMap;
import com.rakib.domain.Comments;
import com.rakib.domain.LikeDislike;
import com.rakib.service.CommentService;
import com.rakib.service.LikeDislikeService;
import com.rakib.service.dto.CommentDTO;
import com.rakib.service.dto.LikeDislikeDTO;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/")
public class BlogInfoController {
    private final CommentService commentService;
    private final LikeDislikeService likeDislikeService;

    public BlogInfoController(CommentService commentService, LikeDislikeService likeDislikeService) {
        this.commentService = commentService;
        this.likeDislikeService = likeDislikeService;
    }

    @PostMapping("comment")
    //@PreAuthorize("permitAll()")
    public ResponseEntity<?> saveComment(@RequestBody CommentDTO commentDTO) throws NotFoundException {
        Comments comments = commentService.saveComment(commentDTO);
        return ResponseEntity.ok().body(ImmutableMap.of("data", comments));
    }
    @PutMapping("comment/{id}")
    //@PreAuthorize("permitAll()")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO) throws NotFoundException {
        Comments comments = commentService.updateComment(id, commentDTO);
        return ResponseEntity.ok().body(ImmutableMap.of("data", comments));
    }
    @PostMapping("likedislike")
    //@PreAuthorize("permitAll()")
    public ResponseEntity<?> saveLikeDislike(@RequestBody LikeDislikeDTO likeDislikeDTO) throws NotFoundException {
        LikeDislike likeDislike = likeDislikeService.saveLikeDislike(likeDislikeDTO);
        return ResponseEntity.ok().body(ImmutableMap.of("data", likeDislike));
    }
}
