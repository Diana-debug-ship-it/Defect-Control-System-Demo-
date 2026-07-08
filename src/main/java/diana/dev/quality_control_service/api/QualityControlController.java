package diana.dev.quality_control_service.api;

import diana.dev.quality_control_service.domain.FrameCheckProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/v1/quality-control")
@RequiredArgsConstructor
public class QualityControlController {

    private final FrameCheckProcessor processor;

    @GetMapping("/stats")
    public ResponseEntity<LineStatsDto> getStats() {
        log.info("Getting stats");
        return ResponseEntity.ok().body(processor.processStats());
    }


    @GetMapping("/history")
    public ResponseEntity<List<FrameUIDto>> getHistory() {
        log.info("Getting history of frame checks");
        return ResponseEntity.ok().body(processor.processHistory());
    }

}
