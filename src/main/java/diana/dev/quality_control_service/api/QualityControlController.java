package diana.dev.quality_control_service.api;

import diana.dev.quality_control_service.domain.FrameCheckProcessor;
import diana.dev.quality_control_service.domain.SseNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


@Slf4j
@RestController
@RequestMapping("/api/v1/quality-control")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class QualityControlController {

    private final FrameCheckProcessor processor;
    private final SseNotificationService notificationService;

    @PostMapping("/test-receive")
    public ResponseEntity<String> testReceive(@RequestBody FrameResultEvent event) {
        log.info("Тестовый запуск: получили данные от нейросети: {}", event);

        processor.saveResult(event);

        return ResponseEntity.ok("Данные успешно обработаны процессором!");
    }

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

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamEvents() {
        log.info("Streaming to frontend");
        return notificationService.createConnection();
    }


}
