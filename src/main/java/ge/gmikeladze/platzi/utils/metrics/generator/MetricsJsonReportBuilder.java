package ge.gmikeladze.platzi.utils.metrics.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.Singleton;
import ge.gmikeladze.platzi.utils.metrics.MetricsRegistry;
import ge.gmikeladze.platzi.utils.metrics.PercentileCalculator;

import java.io.File;
import java.time.Instant;
import java.util.*;

@Singleton
public class MetricsJsonReportBuilder {

        private static final String RUN_ID = "run-" + System.currentTimeMillis();
        private static final String TIMESTAMP = Instant.now().toString();

        private final ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT);

        public Map<String, Object> build(MetricsRegistry metrics, long suiteDurationMs) {
            Map<String, Object> report = new LinkedHashMap<>();

            report.put("runId", RUN_ID);
            report.put("timestamp", TIMESTAMP);
            report.put("suiteDurationMs", suiteDurationMs);


        int total   = metrics.getTotalTests();
        int passed  = metrics.getPassedTests();
        int failed  = metrics.getFailedTests();
        int skipped = metrics.getSkippedTests();
        int flaky   = metrics.getFlakyTests();

        double passRate  = total == 0 ? 0 : Math.round(((double) (passed + flaky) / total) * 10000.0) / 100.0;
        double flakyRate = total == 0 ? 0 : Math.round(((double) flaky / total) * 10000.0) / 100.0;

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("totalTests", total);
        summary.put("passed", passed);
        summary.put("failed", failed);
        summary.put("skipped", skipped);
        summary.put("flaky", flaky);
        summary.put("flakyRatePercent", flakyRate);
        summary.put("passRatePercent", passRate);
        report.put("summary", summary);

        Map<String, Object> endpointsReport = new LinkedHashMap<>();
        for (var entry : metrics.getEndpointStatsMap().entrySet()) {
            String endpoint = entry.getKey();
            MetricsRegistry.EndpointStats stats = entry.getValue();

            Map<String, Object> endpointData = new LinkedHashMap<>();
            endpointData.put("totalCalls", stats.getCallsCount());
            endpointData.put("retriesCount", stats.getRetriesCount());

            Map<String, Integer> statusMap = new HashMap<>();
            for (var statusEntry : stats.getStatusCodes().entrySet()) {
                statusMap.put(String.valueOf(statusEntry.getKey()), statusEntry.getValue().intValue());
            }
            endpointData.put("statusCodes", statusMap);

            List<Long> times = new ArrayList<>(stats.getResponseTimes());
            Collections.sort(times);

            Map<String, Long> percentiles = new LinkedHashMap<>();
            if (!times.isEmpty()) {
                percentiles.put("min", times.get(0));
                percentiles.put("max", times.get(times.size() - 1));
                percentiles.put("p50", PercentileCalculator.calculate(times, 50));
                percentiles.put("p95", PercentileCalculator.calculate(times, 95));
                percentiles.put("p99", PercentileCalculator.calculate(times, 99));
            }
            endpointData.put("responseTimeMs", percentiles);

            endpointsReport.put(endpoint, endpointData);
        }
        report.put("endpoints", endpointsReport);

        return report;
    }

    public void writeToFile(Map<String, Object> report, File dir) {
        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            mapper.writeValue(new File(dir, "metrics.json"), report);
            System.out.println("Metrics report saved to target/metrics/metrics.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}