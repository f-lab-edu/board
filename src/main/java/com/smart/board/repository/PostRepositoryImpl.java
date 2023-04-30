package com.smart.board.repository;

import com.smart.board.domain.Post;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class PostRepositoryImpl implements PostRepository {

  private Map<Long, Post> posts = new HashMap<>();
  private static Long sequence = 0L;

  @Override
  public List<Post> findAll() {
    return new ArrayList<>(posts.values());
  }

  @Override
  public Optional<Post> findByPostId(Long postId) {
    return Optional.ofNullable(posts.get(postId));
  }

  @Override
  public Long save(Post post) {
    post.setPostId(sequence++);
    posts.put(post.getPostId(), post);
    return post.getPostId();
  }

  @Override
  public void update(Post post) {
    if (posts.containsKey(post.getPostId())) {
      posts.put(post.getPostId(), post);
    }
  }

  @Override
  public void deleteByPostId(Long postId) {
    posts.remove(postId);
  }

  @Override
  public boolean existsByPostId(Long postId) {
    return posts.containsKey(postId);
  }
}
