package diana.dev.quality_control_service.api;

import diana.dev.quality_control_service.domain.QualityStatus;

public record FrameUIDto(
        String frameId,
        QualityStatus status,
        String formattedTime,
        double confidence
) {
}
