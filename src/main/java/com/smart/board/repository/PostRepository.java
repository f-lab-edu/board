package com.smart.board.repository;

import com.smart.board.controller.dto.post.PostReadDto;
import com.smart.board.domain.Post;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostRepository {

  List<PostReadDto> findAll();

  Optional<PostReadDto> findByPostId(Long postId);

  void updateViewCnt(Long postId);

  void save(Post post);

  void update(Post post);

  void deleteByPostId(Long postId);

  boolean existsByPostId(Long postId);
}
