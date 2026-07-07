package diana.dev.quality_control_service.api;

public record LineStatsDto(
        Long totalChecked,
        Long totalDefects,
        Double defectRate
) {
}
