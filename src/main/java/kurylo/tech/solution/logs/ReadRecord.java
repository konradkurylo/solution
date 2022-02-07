package kurylo.tech.solution.logs;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
class ReadRecord {
    private final String id;
    private final String state;
    private final Long timestamp;
    private final String host;
    private final String type;
}
