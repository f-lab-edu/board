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
      User user = userRepository.findByUserId(post.getUserId())
          .orElseThrow(() -> new NotFoundEntityException("사용자"));
      postReadDtos.add(PostReadDto.toDto(post, user));
    }
    return postReadDtos;
  }

  public void updatePostViewCount(Long postId) {
    Post post = postRepository.findByPostId(postId)
        .orElseThrow(() -> new NotFoundEntityException("게시물"));
    post.updateViewCount();
    postRepository.update(post);
  }

  public PostReadDto getPostByPostId(Long postId) {
    Post post = postRepository.findByPostId(postId)
        .orElseThrow(() -> new NotFoundEntityException("게시물"));
    User user = userRepository.findByUserId(post.getUserId())
        .orElseThrow(() -> new NotFoundEntityException("사용자"));
    return PostReadDto.toDto(post, user);
  }

  @Transactional
  public Long createPost(PostCreateDto createDto, Long loginUserId) {
    Post post = createDto.toEntity(loginUserId);
    return postRepository.save(post);
  }

  @Transactional
  public void updatePost(PostUpdateDto updateDto, Long loginUserId) {
    Post post = postRepository.findByPostId(updateDto.getPostId())
        .orElseThrow(() -> new NotFoundEntityException("게시물"));
    checkPermission(post.getUserId(), loginUserId);
    postRepository.update(updateDto.toEntity(post));
  }

  @Transactional
  public void deletePost(Long postId, Long loginUserId) {
    Post post = postRepository.findByPostId(postId)
        .orElseThrow(() -> new NotFoundEntityException("게시물"));
    checkPermission(post.getUserId(), loginUserId);
    commentRepository.deleteByPostId(postId);
    postRepository.deleteByPostId(postId);
  }

  public List<CommentReadDto> getCommentsByPostId(Long postId) {
    List<CommentReadDto> commentReadDtos = new ArrayList<>();
    for (Comment comment : commentRepository.findByPostId(postId)) {
      User user = userRepository.findByUserId(comment.getUserId())
          .orElseThrow(() -> new NotFoundEntityException("사용자"));
      commentReadDtos.add(CommentReadDto.toDto(comment, user));
    }
    return commentReadDtos;
  }

  public CommentReadDto getCommentsByCommentId(Long commentId) {
    Comment comment = commentRepository.findByCommentId(commentId)
        .orElseThrow(() -> new NotFoundEntityException("댓글"));
    User user = userRepository.findByUserId(comment.getUserId())
        .orElseThrow(() -> new NotFoundEntityException("사용자"));
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
    Comment comment = commentRepository.findByCommentId(updateDto.getCommentId())
        .orElseThrow(() -> new NotFoundEntityException("댓글"));
    checkPermission(comment.getUserId(), loginUserId);
    commentRepository.update(updateDto.toEntity(comment));
  }

  @Transactional
  public void deleteComment(Long commentId, Long loginUserId) {
    Comment comment = commentRepository.findByCommentId(commentId)
        .orElseThrow(() -> new NotFoundEntityException("댓글"));
    checkPermission(comment.getUserId(), loginUserId);
    commentRepository.deleteByCommentId(commentId);
  }

  private void checkPermission(Long authorUserId, Long loginUserId) {
    if (!loginUserId.equals(authorUserId)) {
      throw new PermissionDeniedException();
    }
  }
}
