package com.smart.board.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.smart.board.controller.dto.post.PostCreateDto;
import com.smart.board.controller.dto.post.PostUpdateDto;
import com.smart.board.domain.Post;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PostRepositoryTest {

  private PostRepositoryImpl postRepository = new PostRepositoryImpl();

  private Post post;

  @BeforeEach
  public void create_Test_Data() {
    post = PostCreateDto
        .builder()
        .title("title")
        .content("content")
        .build()
        .toEntity(1L);
  }

  @AfterEach
  public void delete_Test_Data() {
    postRepository.deleteByPostId(post.getPostId());
  }

  @Test
  @DisplayName("게시물 생성 후 게시글 전체 조회 시 게시물이 담긴 List를 반환한다.")
  public void findAll_Posts_ExistingPost() {
    postRepository.save(post);

    List<Post> retPosts = postRepository.findAll();
    assertThat(retPosts).hasSize(1);
    assertThat(post).isIn(retPosts);
  }

  @Test
  @DisplayName("존재하지 않는 ID로 게시글 조회 시 null을 반환한다.")
  public void findByPostId_Null_NotExistingPostId() {
    Optional<Post> optionalPost = postRepository.findByPostId(-1L);

    assertThat(optionalPost.isEmpty()).isTrue();
  }

  @Test
  @DisplayName("게시물 생성 후 반환된 ID로 게시물을 조회한다.")
  public void findByPostId_PostReadDto_ExistingPostId() {
    postRepository.save(post);

    Post retPost = postRepository.findByPostId(post.getPostId()).get();

    assertThat(retPost).isNotNull();
    assertThat(retPost.getPostId()).isEqualTo(post.getPostId());
  }

  @Test
  @DisplayName("생성하지 않은 게시글 업데이트하면 당연히 해당 게시물을 찾을 수 없다.")
  public void update_NotExistingPost() {
    Post updatePost = PostUpdateDto.builder()
        .postId(-1L)
        .title("update title")
        .content("update content")
        .build()
        .toEntity(post);

    postRepository.update(updatePost);

    Optional<Post> optionalPost = postRepository.findByPostId(-1L);
    assertThat(optionalPost.isEmpty()).isTrue();
  }

  @Test
  @DisplayName("게시글 업데이트 후 해당하는 게시글이 정상적으로 수정되었는지 확인한다.")
  public void update_ExistingPost() {
    postRepository.save(post);
    Post updateBoard = PostUpdateDto.builder()
        .postId(post.getPostId())
        .title("update title")
        .content("update content")
        .build()
        .toEntity(post);

    postRepository.update(updateBoard);

    Post retPost = postRepository.findByPostId(post.getPostId()).get();
    assertThat(retPost.getPostId()).isEqualTo(updateBoard.getPostId());
    assertThat(retPost.getTitle()).isEqualTo(updateBoard.getTitle());
    assertThat(retPost.getContent()).isEqualTo(updateBoard.getContent());
  }

  @Test
  @DisplayName("게시글 삭제 후 해당 게시글이 삭제되었는지 확인한다.")
  public void deleteByPostId_ExistingPost() {
    postRepository.save(post);

    postRepository.deleteByPostId(post.getPostId());

    assertThat(postRepository.existsByPostId(post.getPostId())).isFalse();
  }
}
