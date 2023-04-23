package com.smart.board.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.smart.board.controller.dto.post.PostCreateDto;
import com.smart.board.controller.dto.post.PostReadDto;
import com.smart.board.controller.dto.post.PostUpdateDto;
import com.smart.board.domain.Post;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostDaoTest {

  @Autowired
  private PostDao postDao;

  Post post;

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
    postDao.deleteByPostId(post.getPostId());
  }

  @Test
  @DisplayName("게시물 생성 후 게시글 전체 조회 시 게시물이 담긴 List를 반환한다.")
  public void getAllBoard_BoardInfos_ExistingBoard() {
    postDao.createPost(post);

    List<PostReadDto> allPost = postDao.getAllPost();
  }

  @Test
  @DisplayName("존재하지 않는 ID로 게시글 조회 시 null을 반환한다.")
  public void getPostByPostId_Null_NotExistingPostId() {
    Optional<PostReadDto> optionalPostReadDto = postDao.getPostByPostId(-1L);

    assertThat(optionalPostReadDto.isEmpty()).isTrue();
  }

  @Test
  @DisplayName("게시물 생성 후 반환된 ID로 게시물을 조회한다.")
  public void getBoardByBoardId_BoardInfo_ExistingBoardId() {
    postDao.createPost(post);

    PostReadDto postReadDto = postDao.getPostByPostId(post.getPostId()).get();

    assertThat(postReadDto).isNotNull();
    assertThat(postReadDto.getPostId()).isEqualTo(post.getPostId());
  }

  @Test
  @DisplayName("존재하지 않는 게시물 조회수를 업데이트 한다.")
  public void updateViewCnt_NotExistingBoardId() {
    postDao.updateViewCnt(-1L);

    Optional<PostReadDto> optionalPostReadDto = postDao.getPostByPostId(-1L);

    assertThat(optionalPostReadDto.isEmpty()).isTrue();
  }

  @Test
  @DisplayName("게시물 생성 후 반환된 ID로 게시물 조회수를 업데이트 한다.")
  public void updateViewCnt_ExistingBoardId() {
    postDao.createPost(post);

    postDao.updateViewCnt(post.getPostId());
    PostReadDto postReadDto = postDao.getPostByPostId(post.getPostId()).get();

    assertThat(postReadDto.getViewCount()).isEqualTo(post.getViewCount() + 1);
  }

  @Test
  @DisplayName("생성하지 않은 게시글 업데이트하면 당연히 해당 게시물을 찾을 수 없다.")
  public void updateBoard_NotExistingBoard() {
    Post updatePost = PostUpdateDto.builder()
        .postId(-1L)
        .title("update title")
        .content("update content")
        .userId(post.getUserId())
        .build()
        .toEntity();

    postDao.updatePost(updatePost);

    Optional<PostReadDto> optionalPostReadDto = postDao.getPostByPostId(-1L);
    assertThat(optionalPostReadDto.isEmpty()).isTrue();
  }

  @Test
  @DisplayName("게시글 업데이트 후 해당하는 게시글이 정상적으로 수정되었는지 확인한다.")
  public void updateBoard_ExistingBoard() {
    postDao.createPost(post);
    Post updateBoard = PostUpdateDto.builder()
        .postId(post.getPostId())
        .title("update title")
        .content("update content")
        .userId(post.getUserId())
        .build()
        .toEntity();

    postDao.updatePost(updateBoard);

    PostReadDto postReadDto = postDao.getPostByPostId(post.getPostId()).get();
    assertThat(postReadDto.getPostId()).isEqualTo(updateBoard.getPostId());
    assertThat(postReadDto.getTitle()).isEqualTo(updateBoard.getTitle());
    assertThat(postReadDto.getContent()).isEqualTo(updateBoard.getContent());
  }

  @Test
  @DisplayName("게시글 삭제 후 해당 게시글이 삭제되었는지 확인한다.")
  public void deleteByBoardId_ExistingBoardId() {
    postDao.createPost(post);

    postDao.deleteByPostId(post.getPostId());

    assertThat(postDao.checkPostId(post.getPostId())).isFalse();
  }
}
