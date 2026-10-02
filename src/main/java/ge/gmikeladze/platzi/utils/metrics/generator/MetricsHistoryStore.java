package ge.gmikeladze.platzi.utils.metrics.generator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.Singleton;

import java.io.File;
import java.util.*;

@Singleton
public class MetricsHistoryStore {

    private final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    public List<Map<String, Object>> loadAndAppend(File dir, String runId, String timestamp,
                                                   long durationMs, int totalTests,
                                                   double passRate, double flakyRate) {
        List<Map<String, Object>> history = new ArrayList<>();
        File historyFile = new File(dir, "history.json");

        try {
            if (historyFile.exists()) {
                history = mapper.readValue(historyFile, new TypeReference<>() {});
            }

            history.removeIf(run -> runId.equals(run.get("runId")));

            Map<String, Object> runSummary = new LinkedHashMap<>();
            runSummary.put("runId", runId);
            runSummary.put("timestamp", timestamp);
            runSummary.put("durationMs", durationMs);
            runSummary.put("totalTests", totalTests);
            runSummary.put("passRatePercent", passRate);
            runSummary.put("flakyRatePercent", flakyRate);

            history.add(runSummary);
            mapper.writeValue(historyFile, history);
        } catch (Exception e) {
            System.err.println("Could not update history.json: " + e.getMessage());
        }

        return history;
    }
}