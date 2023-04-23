package com.smart.board.dao;

import com.smart.board.controller.dto.post.PostReadDto;
import com.smart.board.domain.Post;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostDao {

  List<PostReadDto> getAllPost();

  Optional<PostReadDto> getPostByPostId(Long postId);

  void updateViewCnt(Long postId);

  void createPost(Post post);

  void updatePost(Post post);

  void deleteByPostId(Long postId);

  boolean checkPostId(Long postId);
}
