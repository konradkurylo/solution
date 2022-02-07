package kurylo.tech.solution.logs;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
@Slf4j
class LogProcess {
    private final LogRepository logRepository;
    private final LineReader lineReader;

    @Async
    CompletableFuture<LogAggregate> saveInfoFromOneLine(String line){
        return lineReader.readLine(line)
                .map(this::retryOnDataViolation)
                .orElseGet(() -> CompletableFuture.completedFuture(null));

    }

    CompletableFuture<LogAggregate> retryOnDataViolation(ReadRecord readRecord) {
        try{
            log.debug("first try with: " + readRecord.getId());
            return CompletableFuture.completedFuture(insertOrUpdate(readRecord));
        } catch (DataIntegrityViolationException e){
            try {
                log.debug("second try with: " + readRecord.getId());
                return CompletableFuture.completedFuture(insertOrUpdate(readRecord));
            } catch (DataIntegrityViolationException f){
                log.debug("give up with: " + readRecord.getId());
                return CompletableFuture.completedFuture(null);
            }
        }
    }

    @Transactional
    LogAggregate insertOrUpdate(ReadRecord readRecord) {
        if (logRepository.existsByBusinessId(readRecord.getId())) {
            LogAggregate logAggregate = logRepository.findByBusinessId(readRecord.getId())
                    .orElseThrow(RuntimeException::new)
                    .updateWithSecondRecord(readRecord);
            log.debug("update record in database");
            return logRepository.save(logAggregate);
        } else {
            log.debug("insert record in database");
            return logRepository.save(LogAggregate.of(readRecord));
        }
    }
}
