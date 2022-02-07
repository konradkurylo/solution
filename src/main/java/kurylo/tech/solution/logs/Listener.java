package kurylo.tech.solution.logs;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@AllArgsConstructor
@Slf4j
class Listener {
    private final LogProcess logProcess;

    @EventListener(value = ContextRefreshedEvent.class)
    public void processOnSpringContextRefreshEvent() throws IOException {
        log.debug("listen");
        Path path = Paths.get("logfile.txt");
        BufferedReader reader = Files.newBufferedReader(path);

        String line;
        while ((line = reader.readLine()) != null) {
            logProcess.saveInfoFromOneLine(line);
        }
    }
}
