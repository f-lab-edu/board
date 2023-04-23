package com.smart.board.service;

import com.smart.board.controller.dto.BoardDetailDto;
import com.smart.board.controller.dto.BoardListDto;
import com.smart.board.controller.dto.comment.CommentCreateDto;
import com.smart.board.controller.dto.comment.CommentDeleteDto;
import com.smart.board.controller.dto.comment.CommentReadDto;
import com.smart.board.controller.dto.comment.CommentUpdateDto;
import com.smart.board.controller.dto.post.PostCreateDto;
import com.smart.board.controller.dto.post.PostDeleteDto;
import com.smart.board.controller.dto.post.PostReadDto;
import com.smart.board.controller.dto.post.PostUpdateDto;
import com.smart.board.repository.CommentRepository;
import com.smart.board.repository.PostRepository;
import com.smart.board.domain.Comment;
import com.smart.board.domain.Post;
import com.smart.global.error.NotFoundEntityException;
import com.smart.global.error.PermissionDeniedException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardService {

  private final PostRepository postRepository;
  private final CommentRepository commentRepository;

  public BoardService(PostRepository postRepository, CommentRepository commentRepository) {
    this.postRepository = postRepository;
    this.commentRepository = commentRepository;
  }

  public BoardListDto getBoardList() {
    List<PostReadDto> postDtos = postRepository.findAll();
    return BoardListDto.builder()
        .postDtos(postDtos)
        .build();
  }

  public BoardDetailDto getBoardDetailByPostId(Long postId) {
    postRepository.updateViewCnt(postId);
    PostReadDto postDto = postRepository.findByPostId(postId)
        .orElseThrow(() -> new NotFoundEntityException("게시물"));
    List<CommentReadDto> commentDtos = commentRepository.findByPostId(postId);
    return BoardDetailDto.builder()
        .postDto(postDto)
        .commentDtos(commentDtos)
        .build();
  }

  public PostReadDto getPostByPostId(Long postId) {
    return postRepository.findByPostId(postId)
        .orElseThrow(() -> new NotFoundEntityException("게시물"));
  }

  @Transactional
  public Long createPost(PostCreateDto createDto, Long loginUserId) {
    Post post = createDto.toEntity(loginUserId);
    postRepository.save(post);
    return post.getPostId();
  }

  @Transactional
  public void updatePost(PostUpdateDto updateDto, Long loginUserId) {
    checkPermission(updateDto.getUserId(), loginUserId);
    checkExistPost(updateDto.getPostId());
    postRepository.update(updateDto.toEntity());
  }

  @Transactional
  public void deletePost(PostDeleteDto deleteDto, Long loginUserId) {
    checkPermission(deleteDto.getUserId(), loginUserId);
    checkExistPost(deleteDto.getPostId());
    commentRepository.deleteByPostId(deleteDto.getPostId());
    postRepository.deleteByPostId(deleteDto.getPostId());
  }

  public CommentReadDto getCommentByCommentId(Long commentId) {
    return commentRepository.findByCommentId(commentId)
        .orElseThrow(() -> new NotFoundEntityException("댓글"));
  }

  @Transactional
  public Long createComment(CommentCreateDto createDto, Long loginUserId) {
    Comment comment = createDto.toEntity(loginUserId);
    commentRepository.save(comment);
    return comment.getCommentId();
  }

  @Transactional
  public void updateComment(CommentUpdateDto updateDto, Long loginUserId) {
    checkPermission(updateDto.getUserId(), loginUserId);
    checkExistComment(updateDto.getCommentId());
    commentRepository.update(updateDto.toEntity());
  }

  @Transactional
  public void deleteComment(CommentDeleteDto deleteDto, Long loginUserId) {
    checkPermission(deleteDto.getUserId(), loginUserId);
    checkExistComment(deleteDto.getCommentId());
    commentRepository.deleteByCommentId(deleteDto.getCommentId());
  }

  private void checkExistPost(Long postId) {
    if (!postRepository.existsByPostId(postId)) {
      throw new NotFoundEntityException("게시물");
    }
  }

  private void checkExistComment(Long commentId) {
    if (!commentRepository.existsByCommentId(commentId)) {
      throw new NotFoundEntityException("댓글");
    }
  }

  private void checkPermission(Long authorUserId, Long loginUserId) {
    if (!loginUserId.equals(authorUserId)) {
      throw new PermissionDeniedException();
    }
  }
}
