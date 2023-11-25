package com.signumapp.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import com.signumapp.entity.Comment;
import com.signumapp.entity.Post;
import com.signumapp.entity.User;
import com.signumapp.shared.MockResourceRepo;

@DataJpaTest
class CommentRepositoryTest {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    private final User USER_JOHN = MockResourceRepo.getMockUserJohn();
    private final Post POST_ONE = MockResourceRepo.getPostOne();
    private final Comment COMMENT_ONE = MockResourceRepo.getCommentOne();


    @BeforeEach
    void setUp() {
        User userJohn = userRepository.save(USER_JOHN);

        POST_ONE.setAuthor(userJohn);
        Post postOne = postRepository.save(POST_ONE);

        COMMENT_ONE.setAuthor(userJohn);
        COMMENT_ONE.setPost(postOne);
        commentRepository.save(COMMENT_ONE);
    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldReturnListComments_whenPostIsGiven() {
        User userJohn = userRepository.findByEmail(USER_JOHN.getEmail()).get();
        Post targetPost = postRepository.findPostsByAuthor(userJohn, PageRequest.of(0, 5)).get(0);

        List<Comment> returnedCommentList = commentRepository.findByPost(targetPost,  PageRequest.of(0, 5));

        assertThat(returnedCommentList.size()).isEqualTo(1);
    }
}