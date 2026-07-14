package diana.dev.quality_control_service.api;

import lombok.Builder;

@Builder
public record LineStatsDto(
        Long totalChecked,
        Long totalDefects,
        Long totalPassed,
        Double defectRate
) {
}
