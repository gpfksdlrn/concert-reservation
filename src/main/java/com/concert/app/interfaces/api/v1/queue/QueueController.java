package com.concert.app.interfaces.api.v1.queue;

import com.concert.app.domain.queue.QueueService;
import com.concert.app.domain.queue.SelectQueueTokenResult;
import com.concert.app.interfaces.api.common.CommonRes;
import com.concert.app.interfaces.api.v1.queue.req.CreateQueueTokenReq;
import com.concert.app.interfaces.api.v1.queue.res.CreateQueueTokenRes;
import com.concert.app.interfaces.api.v1.queue.res.SelectQueueTokenRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "대기열 API", description = "콘서트 대기열을 발급받는 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/queue")
public class QueueController {

    private final QueueService queueService;

    public CommonRes<CreateQueueTokenRes> createQueueToken(
            @RequestBody CreateQueueTokenReq req
    ) {
        return CommonRes.success(new CreateQueueTokenRes(queueService.enterQueue(req.userId())));
    }

    @Operation(summary = "유저 대기열 토큰 체크 API")
    @PostMapping("/token/check")
    public CommonRes<SelectQueueTokenRes> getQueueToken(
            @Schema(description = "대기열 토큰", defaultValue = "Bearer...") @RequestHeader("Authorization") String token
    ) {
        SelectQueueTokenResult res = queueService.checkQueue(token);
        return CommonRes.success(SelectQueueTokenRes.of(res.queuePosition(), res.status()));
    }
}
