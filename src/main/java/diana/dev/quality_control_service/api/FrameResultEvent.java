package diana.dev.quality_control_service.api;

public record FrameResultEvent(
        String frameId,
        Boolean hasDefect,
        Double confidence
) {
}
