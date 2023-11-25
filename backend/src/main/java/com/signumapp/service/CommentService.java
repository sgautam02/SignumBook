package com.signumapp.service;

import java.util.List;

import com.signumapp.entity.Comment;
import com.signumapp.entity.Post;
import com.signumapp.response.CommentResponse;

public interface CommentService {
    Comment getCommentById(Long commentId);
    Comment createNewComment(String content, Post post);
    Comment updateComment(Long commentId, String content);
    Comment likeComment(Long commentId);
    Comment unlikeComment(Long commentId);
    void deleteComment(Long commentId);
    List<CommentResponse> getPostCommentsPaginate(Post post, Integer page, Integer size);
}
