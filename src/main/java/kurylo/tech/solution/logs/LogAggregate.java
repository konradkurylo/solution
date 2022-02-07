package kurylo.tech.solution.logs;

import lombok.*;

import javax.persistence.*;
@Entity(name = "LogAggregate")
@Table(name = "logs")
@Builder(toBuilder = true)
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Getter
class LogAggregate {
    private final static int EVENT_THRESHOLD_DURATION = 4;
    private final static String FINISHED_STATE = "FINISHED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String businessId;
    private Long start;
    private Long stop;
    private String host;
    private String type;
    private Long duration;
    private Boolean alert;


    static LogAggregate of(ReadRecord readRecord){
        String state = readRecord.getState();
        boolean finished = FINISHED_STATE.equals(state);
        return LogAggregate.builder()
                .businessId(readRecord.getId())
                .start(finished ? null : readRecord.getTimestamp())
                .stop(finished ? readRecord.getTimestamp() : null)
                .host(readRecord.getHost())
                .type(readRecord.getType())
                .build();
    }

    LogAggregate updateWithSecondRecord(ReadRecord readRecord){
        LogAggregate logAggregate = updateStartOrStop(readRecord);
        long duration = logAggregate.getStop() - logAggregate.getStart();
        return logAggregate.toBuilder().duration(duration).alert(duration > EVENT_THRESHOLD_DURATION).build();
    }

    private LogAggregate updateStartOrStop(ReadRecord readRecord){
        return FINISHED_STATE.equals(readRecord.getState())
                ? this.toBuilder().stop(readRecord.getTimestamp()).build()
                : this.toBuilder().start(readRecord.getTimestamp()).build();
    }
}
