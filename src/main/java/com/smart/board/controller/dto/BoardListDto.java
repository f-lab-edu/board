package com.smart.board.controller.dto;

import com.smart.board.controller.dto.post.PostReadDto;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BoardListDto {

  List<PostReadDto> postDtos;
}
