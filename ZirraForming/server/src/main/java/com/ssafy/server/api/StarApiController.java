package com.ssafy.server.api;

import com.ssafy.server.api.dto.star.StarRankingResponse;
import com.ssafy.server.api.dto.star.StarSaveResponse;
import com.ssafy.server.api.dto.star.StarTodayResultResponse;
import com.ssafy.server.api.dto.star.StarsResultResponse;
import com.ssafy.server.domain.entity.Trash;
import com.ssafy.server.domain.service.StarService;
import lombok.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class StarApiController {
    
    private final StarService starService;

    @GetMapping(value = "/stars", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StarsResultResponse> getStarsResult() {
        StarsResultResponse result = new StarsResultResponse();
        result.changeStars(starService.getStarsResult());
        result.changeTotalCount(starService.getStarsTotalCount());
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/stars/ranking", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StarRankingResponse>> getRanking() {
        List<StarRankingResponse> result = starService.getRankCountResponse().stream().map(s -> new StarRankingResponse(s)).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/stars", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StarSaveResponse> savaStar(@ModelAttribute StarSaveRequest request) throws IOException {
        Trash result = starService.saveStar(request.memberId, request.getImage());
        return ResponseEntity.ok().body(new StarSaveResponse(result));
    }

    @GetMapping(value = "/todayresult", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StarTodayResultResponse> savaStar() throws IOException {
        return ResponseEntity.ok().body(new StarTodayResultResponse(starService.getTodayResult()));
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StarsRankingResponse{
        List<String> rank = new ArrayList<>();
        List<Integer> count = new ArrayList<>();

        public void changeRank(List<String> rank) {
            this.rank = rank;
        }

        public void changeCount(List<Integer> count) {
            this.count = count;
        }
    }

    @Data
    public static class StarSaveRequest {
        private Long memberId;
        private MultipartFile image;
    }
}
