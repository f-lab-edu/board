package com.smart.board.repository;

import com.smart.board.domain.Post;
import java.util.List;
import java.util.Optional;

public interface PostRepository {

  List<Post> findAll();

  Optional<Post> findByPostId(Long postId);

  Long save(Post post);

  void deleteByPostId(Long postId);
}
