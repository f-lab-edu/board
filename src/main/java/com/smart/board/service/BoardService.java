package com.smart.board.service;

import com.smart.board.controller.dto.comment.CommentCreateDto;
import com.smart.board.controller.dto.comment.CommentReadDto;
import com.smart.board.controller.dto.comment.CommentUpdateDto;
import com.smart.board.controller.dto.post.PostCreateDto;
import com.smart.board.controller.dto.post.PostReadDto;
import com.smart.board.controller.dto.post.PostUpdateDto;
import com.smart.board.domain.Comment;
import com.smart.board.domain.Post;
import com.smart.board.repository.CommentRepository;
import com.smart.board.repository.PostRepository;
import com.smart.global.error.NotFoundEntityException;
import com.smart.global.error.PermissionDeniedException;
import com.smart.user.domain.CustomUserDetails;
import com.smart.user.domain.User;
import com.smart.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardService {

  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final UserRepository userRepository;

  public BoardService(PostRepository postRepository, CommentRepository commentRepository,
      UserRepository userRepository) {
    this.postRepository = postRepository;
    this.commentRepository = commentRepository;
    this.userRepository = userRepository;
  }

  public List<PostReadDto> getPosts() {
    List<PostReadDto> postReadDtos = new ArrayList<>();
    for (Post post : postRepository.findAll()) {
      User user = findUserByUserId(post.getUserId());
      postReadDtos.add(PostReadDto.toDto(post, user));
    }
    return postReadDtos;
  }

  public PostReadDto getPostByPostId(Long postId) {
    Post post = findPostByPostId(postId);
    User user = findUserByUserId(post.getUserId());
    return PostReadDto.toDto(post, user);
  }

  @Transactional
  public void updateViewCount(Long postId, Long loginId) {
    Post post = findPostByPostId(postId);
    post.updateViewCount(loginId);
    postRepository.save(post);
  }

  @Transactional
  public Long createPost(PostCreateDto createDto, Long loginUserId) {
    Post post = createDto.toEntity(loginUserId);
    return postRepository.save(post);
  }

  @Transactional
  public void updatePost(PostUpdateDto updateDto, Long loginUserId) {
    Post post = findPostByPostId(updateDto.getPostId());
    checkPermission(post.getUserId(), loginUserId);
    postRepository.save(updateDto.toEntity(post));
  }

  @Transactional
  public void deletePost(Long postId, Long loginUserId) {
    Post post = findPostByPostId(postId);
    checkPermission(post.getUserId(), loginUserId);
    commentRepository.deleteByPostId(postId);
    postRepository.deleteByPostId(postId);
  }

  public List<CommentReadDto> getCommentsByPostId(Long postId) {
    List<CommentReadDto> commentReadDtos = new ArrayList<>();
    for (Comment comment : commentRepository.findByPostId(postId)) {
      User user = findUserByUserId(comment.getUserId());
      commentReadDtos.add(CommentReadDto.toDto(comment, user));
    }
    return commentReadDtos;
  }

  public CommentReadDto getCommentByCommentId(Long commentId) {
    Comment comment = findCommentByCommentId(commentId);
    Long userId = comment.getUserId();
    User user = findUserByUserId(userId);
    return CommentReadDto.toDto(comment, user);
  }

  @Transactional
  public Long createComment(CommentCreateDto createDto, Long loginUserId) {
    Comment comment = createDto.toEntity(loginUserId);
    commentRepository.save(comment);
    return comment.getCommentId();
  }

  @Transactional
  public void updateComment(CommentUpdateDto updateDto, Long loginUserId) {
    Comment comment = findCommentByCommentId(updateDto.getCommentId());
    checkPermission(comment.getUserId(), loginUserId);
    commentRepository.save(updateDto.toEntity(comment));
  }

  @Transactional
  public void deleteComment(Long commentId, Long loginUserId) {
    Comment comment = findCommentByCommentId(commentId);
    checkPermission(comment.getUserId(), loginUserId);
    commentRepository.deleteByCommentId(commentId);
  }

  private Post findPostByPostId(Long postId) {
    return postRepository.findByPostId(postId)
        .orElseThrow(() -> new NotFoundEntityException("게시물"));
  }

  private User findUserByUserId(Long userId) {
    return userRepository.findByUserId(userId)
        .orElseThrow(() -> new NotFoundEntityException("사용자"));
  }

  private Comment findCommentByCommentId(Long commentId) {
    return commentRepository.findByCommentId(commentId)
        .orElseThrow(() -> new NotFoundEntityException("댓글"));
  }

  private void checkPermission(Long authorUserId, Long loginUserId) {
    if (!loginUserId.equals(authorUserId)) {
      throw new PermissionDeniedException();
    }
  }
}
