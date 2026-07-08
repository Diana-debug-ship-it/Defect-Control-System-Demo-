package diana.dev.quality_control_service.domain;

import diana.dev.quality_control_service.api.FrameUIDto;
import diana.dev.quality_control_service.api.LineStatsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class FrameCheckProcessor {

    private final FrameCheckRepository repository;

    public LineStatsDto processStats() {

        Long totalChecked = repository.count();
        Long totalDefects = repository.countByHasDefectTrue();

        Double defectRate = totalChecked > 0
                ? ((double) totalDefects/totalChecked) * 100
                : 0.0;

        return LineStatsDto.builder()
                .totalDefects(totalDefects)
                .totalChecked(totalChecked)
                .defectRate(defectRate)
                .build();

    }

    public List<FrameUIDto> processHistory() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        return repository.findTop10ByOrderByTimestampDesc()
                .stream()
                .map(entity -> new FrameUIDto(
                        entity.getFrameId(),
                        entity.getStatus(),
                        entity.getTimestamp().format(formatter),
                        entity.getConfidence())
                ).toList();
    }
}
