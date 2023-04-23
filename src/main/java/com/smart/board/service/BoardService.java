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
import com.smart.board.dao.CommentDao;
import com.smart.board.dao.PostDao;
import com.smart.board.domain.Comment;
import com.smart.board.domain.Post;
import com.smart.global.error.NotFoundEntityException;
import com.smart.global.error.PermissionDeniedException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardService {

  private final PostDao postDao;
  private final CommentDao commentDao;

  public BoardService(PostDao postDao, CommentDao commentDao) {
    this.postDao = postDao;
    this.commentDao = commentDao;
  }

  public BoardListDto getBoardList() {
    List<PostReadDto> postDtos = postDao.getAllPost();
    return BoardListDto.builder()
        .postDtos(postDtos)
        .build();
  }

  public BoardDetailDto getBoardDetailByPostId(Long postId) {
    postDao.updateViewCnt(postId);
    PostReadDto postDto = postDao.getPostByPostId(postId)
        .orElseThrow(() -> new NotFoundEntityException("게시물"));
    List<CommentReadDto> commentDtos = commentDao.getCommentsByPostId(postId);
    return BoardDetailDto.builder()
        .postDto(postDto)
        .commentDtos(commentDtos)
        .build();
  }

  public PostReadDto getPostByPostId(Long postId) {
    return postDao.getPostByPostId(postId)
        .orElseThrow(() -> new NotFoundEntityException("게시물"));
  }

  @Transactional
  public Long createPost(PostCreateDto createDto, Long loginUserId) {
    Post post = createDto.toEntity(loginUserId);
    postDao.createPost(post);
    return post.getPostId();
  }

  @Transactional
  public void updatePost(PostUpdateDto updateDto, Long loginUserId) {
    checkPermission(updateDto.getUserId(), loginUserId);
    checkExistPost(updateDto.getPostId());
    postDao.updatePost(updateDto.toEntity());
  }

  @Transactional
  public void deletePost(PostDeleteDto deleteDto, Long loginUserId) {
    checkPermission(deleteDto.getUserId(), loginUserId);
    checkExistPost(deleteDto.getPostId());
    commentDao.deleteByPostId(deleteDto.getPostId());
    postDao.deleteByPostId(deleteDto.getPostId());
  }

  public CommentReadDto getCommentByCommentId(Long commentId) {
    return commentDao.getCommentByCommentId(commentId)
        .orElseThrow(() -> new NotFoundEntityException("댓글"));
  }

  @Transactional
  public Long createComment(CommentCreateDto createDto, Long loginUserId) {
    Comment comment = createDto.toEntity(loginUserId);
    commentDao.createComment(comment);
    return comment.getCommentId();
  }

  @Transactional
  public void updateComment(CommentUpdateDto updateDto, Long loginUserId) {
    checkPermission(updateDto.getUserId(), loginUserId);
    checkExistComment(updateDto.getCommentId());
    commentDao.updateComment(updateDto.toEntity());
  }

  @Transactional
  public void deleteComment(CommentDeleteDto deleteDto, Long loginUserId) {
    checkPermission(deleteDto.getUserId(), loginUserId);
    checkExistComment(deleteDto.getCommentId());
    commentDao.deleteByCommentId(deleteDto.getCommentId());
  }

  private void checkExistPost(Long postId) {
    if (!postDao.checkPostId(postId)) {
      throw new NotFoundEntityException("게시물");
    }
  }

  private void checkExistComment(Long commentId) {
    if (!commentDao.checkCommentId(commentId)) {
      throw new NotFoundEntityException("댓글");
    }
  }

  private void checkPermission(Long authorUserId, Long loginUserId) {
    if (!loginUserId.equals(authorUserId)) {
      throw new PermissionDeniedException();
    }
  }
}
