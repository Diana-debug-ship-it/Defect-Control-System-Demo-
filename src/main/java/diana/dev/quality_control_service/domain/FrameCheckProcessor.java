package diana.dev.quality_control_service.domain;

import diana.dev.quality_control_service.api.FrameResultEvent;
import diana.dev.quality_control_service.api.FrameUIDto;
import diana.dev.quality_control_service.api.LineStatsDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class FrameCheckProcessor {

    private final FrameCheckRepository repository;
    private final SseNotificationService notificationService;

    public LineStatsDto processStats() {

        Long totalChecked = repository.count();
        Long totalDefects = repository.countByStatus(QualityStatus.DEFECT);

        Double defectRate = totalChecked > 0
                ? ((double) totalDefects/totalChecked) * 100
                : 0.0;

        return LineStatsDto.builder()
                .totalDefects(totalDefects)
                .totalChecked(totalChecked)
                .defectRate(defectRate)
                .build();
    }

    private String formatTime(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return time.format(formatter);
    }

    public List<FrameUIDto> processHistory() {

        return repository.findTop10ByOrderByTimestampDesc()
                .stream()
                .map(entity -> new FrameUIDto(
                        entity.getFrameId(),
                        entity.getStatus(),
                        entity.getDefectType(),
                        formatTime(entity.getTimestamp()),
                        entity.getConfidence())
                ).toList();
    }

    @Transactional
    public void saveResult(FrameResultEvent event) {

        FrameCheckEntity entity = new FrameCheckEntity();
        entity.setFrameId(event.frameId());
        entity.setTimestamp(LocalDateTime.now());
        entity.setConfidence(event.confidence());
        entity.setDefectType(event.defectType());

        QualityStatus status = event.hasDefect() ? QualityStatus.DEFECT : QualityStatus.NORMAL;
        entity.setStatus(status);

        repository.save(entity);
        log.info("Рама {} успешно сохранена со статусом {}", event.frameId(), status);
    }

    public void processFrameResultCheckEvent(FrameResultEvent event) {
        FrameCheckEntity entity = new FrameCheckEntity();
        entity.setFrameId(event.frameId());
        entity.setTimestamp(LocalDateTime.now());
        entity.setConfidence(event.confidence());
        entity.setDefectType(event.defectType());

        QualityStatus status = event.hasDefect() ? QualityStatus.DEFECT : QualityStatus.NORMAL;
        entity.setStatus(status);

        repository.save(entity);
        log.info("Frame {} successfully saved. Status: {}", event.frameId(), status);
        sendToFrontend(entity);
    }

    public void sendToFrontend(FrameCheckEntity entity) {
        FrameUIDto uiDto = new FrameUIDto(
                entity.getFrameId(),
                entity.getStatus(),
                entity.getDefectType(),
                formatTime(entity.getTimestamp()),
                entity.getConfidence()
                );

        notificationService.broadcast(uiDto);
    }
}
