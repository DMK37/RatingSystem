package org.example.ratingsystem.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ratingsystem.config.auth.TokenProvider;
import org.example.ratingsystem.dtos.comment.CommentDTO;
import org.example.ratingsystem.dtos.comment.CommentRequestDTO;
import org.example.ratingsystem.dtos.comment.CommentResponseDTO;
import org.example.ratingsystem.dtos.user.UserDTO;
import org.example.ratingsystem.services.interfaces.CommentService;
import org.example.ratingsystem.services.interfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final CommentService commentService;
    private final TokenProvider tokenProvider;

    @PostMapping("/{sellerId}/comments")
    public ResponseEntity<CommentResponseDTO> addComment(@PathVariable String sellerId,
                                                         @RequestBody CommentRequestDTO commentRequestDTO,
                                                         @RequestHeader(name = "Authorization", required = false)
                                                         String token) {
        String userId = extractUserId(token);

        return ResponseEntity.ok(commentService.addComment(commentRequestDTO, sellerId, userId));
    }

    private String extractUserId(String token) {
        if (token == null)
            return null;
        token = token.replace("Bearer ", "");
        return tokenProvider.extractUserId(token);
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentDTO> getComment(@PathVariable String commentId) {
        return ResponseEntity.ok(commentService.getCommentById(commentId));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable String commentId,
                                              @RequestHeader(name = "Authorization") String token) {
        String userId = extractUserId(token);
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{sellerId}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable String sellerId) {
        return ResponseEntity.ok(commentService.getCommentsBySellerId(sellerId));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDTO> updateComment(@PathVariable String commentId,
                                                    @RequestBody CommentRequestDTO commentRequestDTO,
                                                    @RequestHeader(name = "Authorization") String token) {
        String userId = extractUserId(token);
        return ResponseEntity.ok(commentService.updateComment(commentId, commentRequestDTO, userId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }
}
