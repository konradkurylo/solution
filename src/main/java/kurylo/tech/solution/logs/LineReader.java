package kurylo.tech.solution.logs;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@AllArgsConstructor
class LineReader {
    private final ObjectMapper objectMapper;

    Optional<ReadRecord> readLine(String line) {
        if (line.trim().length() == 0){
            return Optional.empty();
        }
        try{
            return Optional.ofNullable(objectMapper.reader().readValue(line, ReadRecord.class));
        } catch (IOException e){
            return Optional.empty();
        }
    }
}
