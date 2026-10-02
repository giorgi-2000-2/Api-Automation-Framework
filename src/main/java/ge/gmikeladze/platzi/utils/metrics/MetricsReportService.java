package ge.gmikeladze.platzi.utils.metrics;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ge.gmikeladze.platzi.utils.metrics.generator.MetricsHistoryStore;
import ge.gmikeladze.platzi.utils.metrics.generator.MetricsHtmlGenerator;
import ge.gmikeladze.platzi.utils.metrics.generator.MetricsJsonReportBuilder;

import java.io.File;
import java.util.List;
import java.util.Map;

@Singleton
public class MetricsReportService {

    private final MetricsJsonReportBuilder jsonBuilder;
    private final MetricsHistoryStore historyStore;
    private final MetricsHtmlGenerator htmlGenerator;

    @Inject
    public MetricsReportService(MetricsJsonReportBuilder jsonBuilder,
                                MetricsHistoryStore historyStore,
                                MetricsHtmlGenerator htmlGenerator) {
        this.jsonBuilder = jsonBuilder;
        this.historyStore = historyStore;
        this.htmlGenerator = htmlGenerator;
    }

    public void generate(MetricsRegistry metrics, long durationMs) {
        File dir = new File("target/metrics");

        Map<String, Object> jsonReport = jsonBuilder.build(metrics, durationMs);
        jsonBuilder.writeToFile(jsonReport, dir);

        String runId     = (String) jsonReport.get("runId");
        String timestamp = (String) jsonReport.get("timestamp");

        @SuppressWarnings("unchecked")
        Map<String, Object> summary = (Map<String, Object>) jsonReport.get("summary");

        int total      = ((Number) summary.get("totalTests")).intValue();
        double passRate  = ((Number) summary.get("passRatePercent")).doubleValue();
        double flakyRate = ((Number) summary.get("flakyRatePercent")).doubleValue();

        List<Map<String, Object>> history = historyStore.loadAndAppend(
                dir, runId, timestamp, durationMs, total, passRate, flakyRate
        );

        double totalPassRate = 0;
        double totalFlakyRate = 0;
        long totalDuration = 0;
        int historySize = history.size();
        int allTimeTotalTests = 0;



        for (Map<String, Object> run : history) {
            totalPassRate  += ((Number) run.get("passRatePercent")).doubleValue();
            totalFlakyRate += ((Number) run.get("flakyRatePercent")).doubleValue();
            totalDuration  += ((Number) run.get("durationMs")).longValue();

            allTimeTotalTests += ((Number) run.get("totalTests")).intValue();

        }

        double avgPassRate  = historySize > 0 ? Math.round((totalPassRate / historySize) * 100.0) / 100.0 : passRate;
        double avgFlakyRate = historySize > 0 ? Math.round((totalFlakyRate / historySize) * 100.0) / 100.0 : flakyRate;
        long avgDurationPerTestMs = allTimeTotalTests > 0 ? totalDuration / allTimeTotalTests : 0;
        htmlGenerator.generate(
                total,
                allTimeTotalTests,
                ((Number) summary.get("passed")).intValue(),
                ((Number) summary.get("failed")).intValue(),
                ((Number) summary.get("skipped")).intValue(),
                ((Number) summary.get("flaky")).intValue(),
                passRate,
                flakyRate,
                durationMs,
                avgPassRate,
                avgFlakyRate,
                avgDurationPerTestMs,
                historySize,
                metrics.getEndpointStatsMap()
        );
    }
}