package com.signumapp.service;

import org.springframework.web.multipart.MultipartFile;

import com.signumapp.dto.*;
import com.signumapp.entity.Comment;
import com.signumapp.entity.Post;
import com.signumapp.entity.User;
import com.signumapp.response.UserResponse;

import java.util.List;

public interface UserService {
    User getUserById(Long userId);
    List<User> getAllUser();
    User findByUserName(String name);
    User getUserByEmail(String email);
    List<UserResponse> getFollowerUsersPaginate(Long userId, Integer page, Integer size);
    List<UserResponse> getFollowingUsersPaginate(Long userId, Integer page, Integer size);
    List<UserResponse> getFollowerUsers(Long userId);
    public User createNewUser(SignupDto signupDto);
    User updateUserInfo(UpdateUserInfoDto updateUserInfoDto);
    User updateEmail(UpdateEmailDto updateEmailDto);
    User updatePassword(UpdatePasswordDto updatePasswordDto);
    User updateProfilePhoto(MultipartFile photo);
    User updateCoverPhoto(MultipartFile photo);
    User verifyEmail(String token);
    void forgotPassword(String email);
    User resetPassword(String token, ResetPasswordDto resetPasswordDto);
    void deleteUserAccount();
    void followUser(Long userId);
    void unfollowUser(Long userId);
    User getAuthenticatedUser();
    List<UserResponse> getUserSearchResult(String key, Integer page, Integer size);
    List<User> getLikesByPostPaginate(Post post, Integer page, Integer size);
    List<User> getLikesByCommentPaginate(Comment comment, Integer page, Integer size);
}
