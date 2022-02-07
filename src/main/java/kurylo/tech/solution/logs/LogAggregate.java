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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String businessId;
    @Embedded
    private AdditionalInfo additionalInfo;
    @Embedded
    private Alert alert;

    static LogAggregate of(ReadRecord readRecord){
        return LogAggregate.builder()
                .businessId(readRecord.getId())
                .additionalInfo(AdditionalInfo.of(readRecord))
                .alert(Alert.of(readRecord))
                .build();
    }

    LogAggregate updateWithSecondRecord(ReadRecord readRecord){
        return this.toBuilder()
                .alert(alert.updateWithSecondRecord(readRecord))
                .build();
    }


    @AllArgsConstructor
    @Getter
    @Setter
    @Embeddable
    @NoArgsConstructor
    private static class AdditionalInfo {
        private String host;
        private String type;

        public static AdditionalInfo of(ReadRecord readRecord) {
            return new AdditionalInfo(readRecord.getHost(), readRecord.getType());
        }
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @Embeddable
    @NoArgsConstructor
    @Builder(toBuilder = true)
    private static class Alert {
        private final static int EVENT_THRESHOLD_DURATION = 4;
        private final static String FINISHED_STATE = "FINISHED";
        private Long start;
        private Long stop;
        private Long duration;
        private Boolean alert;

        public static Alert of(ReadRecord readRecord) {
            String state = readRecord.getState();
            boolean finished = FINISHED_STATE.equals(state);
            return Alert.builder()
                    .start(finished ? null : readRecord.getTimestamp())
                    .stop(finished ? readRecord.getTimestamp() : null)
                    .build();
        }

        public Alert updateWithSecondRecord(ReadRecord readRecord){
            Alert logAggregate = updateStartOrStop(readRecord);
            long duration = logAggregate.getStop() - logAggregate.getStart();
            return logAggregate.toBuilder().duration(duration).alert(duration > EVENT_THRESHOLD_DURATION).build();
        }

        private Alert updateStartOrStop(ReadRecord readRecord){
            return FINISHED_STATE.equals(readRecord.getState())
                    ? this.toBuilder().stop(readRecord.getTimestamp()).build()
                    : this.toBuilder().start(readRecord.getTimestamp()).build();
        }
    }
}
