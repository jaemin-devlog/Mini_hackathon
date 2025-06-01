package com.example.tilproject.controller;

import com.example.tilproject.CommonResponse;
import com.example.tilproject.security.JwtTokenProvider;
import com.example.tilproject.security.UserPrincipal;
import com.example.tilproject.service.FriendshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import com.example.tilproject.dto.TILRequestDto;
import com.example.tilproject.dto.TILResponseDto;
import com.example.tilproject.entity.TIL;
import com.example.tilproject.service.TILService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@Tag(name = "TIL", description = "내용 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/til")
public class TILController {

    private final TILService tilService;
    private final JwtTokenProvider jwtTokenProvider;
    private final FriendshipService friendshipService;

    //토큰에서 userId 추출
    private Long getUserIdFormHeader(String token) {
        if(token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtTokenProvider.getUserIdFromToken(token);
    }

    //TIL 생성
    @Operation(summary = "TIL 생성", security = @SecurityRequirement(name = "Auth"))
    @PostMapping("/create")
    public ResponseEntity<CommonResponse<TILResponseDto>> tilCreate(@RequestBody TILRequestDto request,
                                                                    @RequestHeader("Authorization")String token) {
        Long userId = getUserIdFormHeader(token);
        TIL til = tilService.createTIL(request, userId);
        TILResponseDto response = new TILResponseDto(til);
        return ResponseEntity.ok()
                .body(CommonResponse.<TILResponseDto>builder()
                        .statusCode(HttpStatus.OK.value())
                        .msg("TIL 생성")
                        .data(response)
                        .build());
    }

    //TIL 조회
    @Operation(summary = "내용 조회", description = "내용을 조회합니다")
    @GetMapping("/{TILId}")
    public ResponseEntity<CommonResponse<TILResponseDto>> getTIL(@PathVariable Long TILId,
                                                                 @RequestHeader("Authorization")String token) {
        Long userId = getUserIdFormHeader(token);
        TIL til = tilService.getTIL(TILId, userId);
        TILResponseDto response = new TILResponseDto(til);
        return ResponseEntity.ok()
                .body(CommonResponse.<TILResponseDto>builder()
                        .statusCode(HttpStatus.OK.value())
                        .msg("TIL 조회 완료")
                        .data(response)
                        .build());
    }

    //TIL 전체 조회
    @GetMapping
    public ResponseEntity<CommonResponse<List<TILResponseDto>>> getAllTIL(@RequestHeader("Authorization")String token) {
        Long userId = getUserIdFormHeader(token);
        List<TIL> tils = tilService.getTILs(userId);
        List<TILResponseDto> response = tils.stream()
                .map(TILResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(CommonResponse.<List<TILResponseDto>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .msg("TIL 전체 조회 완료")
                        .data(response)
                        .build());
    }

    //TIL 수정
    @PutMapping("/{TILId}")
    public ResponseEntity<CommonResponse<TILResponseDto>> tilUpdate(@RequestBody TILRequestDto request, @PathVariable Long TILId,
                                                                    @RequestHeader("Authorization")String token) {
        Long userId = getUserIdFormHeader(token);
        TIL til = tilService.updateTIL(TILId, request, userId);
        TILResponseDto response = new TILResponseDto(til);

        return ResponseEntity.ok()
                .body(CommonResponse.<TILResponseDto>builder()
                        .statusCode(HttpStatus.OK.value())
                        .msg("메모 수정이 완료되었습니다.")
                        .data(response)
                        .build());
    }

    //TIL 삭제
    @DeleteMapping("/{TILId}")
    public ResponseEntity<CommonResponse<Void>> tilDelete(@PathVariable Long TILId,
                                                          @RequestHeader("Authorization")String token) {
        Long userId = getUserIdFormHeader(token);
        tilService.deleteTIL(TILId, userId);

        return ResponseEntity.ok()
                .body(CommonResponse.<Void>builder()
                        .statusCode(HttpStatus.OK.value())
                        .msg("메모 삭제가 완료되었습니다.")
                        .build());
    }
    //친구 TIL 조회
    @Operation(summary = "친구의 TIL 조회", security = @SecurityRequirement(name = "Auth"))
    @GetMapping("/friend/{friendId}/{TILId}")
    public ResponseEntity<CommonResponse<TILResponseDto>> getFriendTIL(@PathVariable Long friendId,
                                                                       @PathVariable Long TILId,
                                                                       @AuthenticationPrincipal UserPrincipal user) {
        Long myUserId = user.getUserId();

        // 1. 친구 관계인지 확인
        if (!friendshipService.isFriend(myUserId, friendId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(CommonResponse.<TILResponseDto>builder()
                            .statusCode(HttpStatus.FORBIDDEN.value())
                            .msg("해당 사용자와 친구 관계가 아닙니다.")
                            .build());
        }

        // 2. 친구의 TIL 조회
        TIL til = tilService.getTIL(TILId, friendId);  // 주의: friendId로 조회
        TILResponseDto response = new TILResponseDto(til);
        return ResponseEntity.ok()
                .body(CommonResponse.<TILResponseDto>builder()
                        .statusCode(HttpStatus.OK.value())
                        .msg("친구의 TIL 조회 완료")
                        .data(response)
                        .build());
    }

}
