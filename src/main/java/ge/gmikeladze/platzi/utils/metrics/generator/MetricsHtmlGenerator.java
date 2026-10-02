package ge.gmikeladze.platzi.utils.metrics.generator;

import com.google.inject.Singleton;
import ge.gmikeladze.platzi.utils.metrics.MetricsRegistry;
import ge.gmikeladze.platzi.utils.metrics.PercentileCalculator;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

@Singleton
public class MetricsHtmlGenerator {

    public void generate(int total, int allTimeTotalTests, int passed, int failed, int skipped, int flaky,
                         double passRate, double flakyRate, long currentDurationMs,
                         double avgPassRate, double avgFlakyRate, long avgDurationPerTestMs, int totalRuns,
                         Map<String, MetricsRegistry.EndpointStats> endpointStatsMap) {

        StringBuilder tableRows = new StringBuilder();
        for (var entry : endpointStatsMap.entrySet()) {
            String endpoint = entry.getKey();
            MetricsRegistry.EndpointStats stats = entry.getValue();

            List<Long> times = new ArrayList<>(stats.getResponseTimes());
            Collections.sort(times);

            long p95 = PercentileCalculator.calculate(times, 95);
            long max = times.isEmpty() ? 0 : times.get(times.size() - 1);

            tableRows.append(String.format(
                    "<tr><td>%s</td><td>%d</td><td>%d ms</td><td>%d ms</td></tr>\n",
                    endpoint, stats.getCallsCount(), p95, max
            ));
        }

        double currentAvgDurationSec = total > 0
                ? Math.round(((double) currentDurationMs / total / 1000.0) * 100.0) / 100.0
                : 0.0;

        double avgDurationPerTestSec = Math.round((avgDurationPerTestMs / 1000.0) * 100.0) / 100.0;
        String html = """
                <!DOCTYPE html>
                <html lang="ka">
                <head>
                    <meta charset="UTF-8">
                    <title>API Test Execution Dashboard</title>
                    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                    <style>
                        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f8f9fa; margin: 0; padding: 30px; color: #333; }
                        .container { max-width: 1000px; margin: 0 auto; }
                        h1, h2 { text-align: center; color: #2c3e50; }
                        h1 { margin-bottom: 10px; }
                        h2 { font-size: 18px; color: #7f8c8d; margin-bottom: 30px; margin-top: 30px; border-bottom: 1px solid #ddd; padding-bottom: 10px; }
                        .cards-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(150px, 1fr)); gap: 15px; margin-bottom: 30px; }
                        .card { background: white; padding: 20px; border-radius: 10px; box-shadow: 0 4px 6px rgba(0,0,0,0.05); text-align: center; }
                        .card h3 { margin: 0; color: #7f8c8d; font-size: 13px; text-transform: uppercase; }
                        .card p { margin: 10px 0 0; font-size: 22px; font-weight: bold; color: #2c3e50; }
                        .chart-and-table { display: grid; grid-template-columns: 350px 1fr; gap: 20px; align-items: start; }
                        .box { background: white; padding: 20px; border-radius: 10px; box-shadow: 0 4px 6px rgba(0,0,0,0.05); }
                        table { width: 100%%; border-collapse: collapse; margin-top: 10px; }
                        th, td { text-align: left; padding: 10px; border-bottom: 1px solid #eee; font-size: 14px; }
                        th { background-color: #f1f3f5; color: #495057; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>API Test Dashboard</h1>

                        <h2>Current Run Statistics</h2>
                        <div class="cards-grid" style="grid-template-columns: repeat(6, 1fr);">
                            <div class="card"><h3>Total Tests</h3><p>%d</p></div>
                            <div class="card"><h3>Pass Rate</h3><p style="color: #2ecc71;">%.2f%%</p></div>
                            <div class="card"><h3>Passed</h3><p style="color: #2ecc71;">%d</p></div>
                            <div class="card"><h3>Failed</h3><p style="color: #e74c3c;">%d</p></div>
                            <div class="card"><h3>Flaky Rate</h3><p style="color: #f39c12;">%.2f%%</p></div>
                            <!-- აქ შევცვალეთ სახელი და მნიშვნელობა -->
                            <div class="card"><h3>Avg Duration/Test</h3><p style="color: #3498db;">%.2f s</p></div>
                        </div>

                        <h2>Historical Averages (Total Runs: %d)</h2>
                        <div class="cards-grid" style="grid-template-columns: repeat(4, 1fr);">
                            <div class="card"><h3>All-Time Total Tests</h3><p style="color: #3498db;">%d</p></div>
                            <div class="card"><h3>Avg Pass Rate</h3><p style="color: #2ecc71;">%.2f%%</p></div>
                            <div class="card"><h3>Avg Flaky Rate</h3><p style="color: #f39c12;">%.2f%%</p></div>
                            <!-- აქ დავაკონკრეტეთ რომ ეს სუითის დროა -->
                            <div class="card"><h3>Avg Suite Duration</h3><p>%.2f s</p></div>
                        </div>

                        <h2>Endpoint Performance (Current Run)</h2>
                        <div class="chart-and-table">
                            <div class="box">
                                <h3>Results Distribution</h3>
                                <canvas id="resultsChart"></canvas>
                            </div>
                            <div class="box">
                                <h3>Latency (p95)</h3>
                                <table>
                                    <thead><tr><th>Endpoint</th><th>Calls</th><th>p95</th><th>Max</th></tr></thead>
                                    <tbody>
                                        %s
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <script>
                        var ctx = document.getElementById('resultsChart').getContext('2d');
                        new Chart(ctx, {
                            type: 'doughnut',
                            data: {
                                labels: ['Passed', 'Failed', 'Skipped', 'Flaky'],
                                datasets: [{
                                    data: [%d, %d, %d, %d],
                                    backgroundColor: ['#2ecc71', '#e74c3c', '#95a5a6', '#f39c12']
                                }]
                            },
                            options: { responsive: true, plugins: { legend: { position: 'bottom' } } }
                        });
                    </script>
                </body>
                </html>
                """.formatted( total, passRate, passed, failed, flakyRate, currentAvgDurationSec,
                totalRuns, allTimeTotalTests, avgPassRate, avgFlakyRate, avgDurationPerTestSec,
                tableRows.toString(),
                passed, failed, skipped, flaky);

        try {
            File dir = new File("target/metrics");
            if (!dir.exists()) dir.mkdirs();
            File file = new File(dir, "metrics.html");
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(html);
            }
            System.out.println("HTML Dashboard generated: " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}