package ge.gmikeladze.platzi.utils.metrics;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

@Singleton
public class SuiteMetricsListener implements ISuiteListener, ITestListener {

    private final MetricsReportService reportService;

    private volatile long startTime = System.currentTimeMillis();

    @Inject
    public SuiteMetricsListener(MetricsReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public void onStart(ISuite suite) {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        MetricsRegistry metrics = metrics(result);
        if (metrics == null) return;

        metrics.incrementTotal();
        if (result.wasRetried()) {
            metrics.incrementFlaky();
        } else {
            metrics.incrementPassed();
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        MetricsRegistry metrics = metrics(result);
        if (metrics == null) return;

        metrics.incrementTotal();
        metrics.incrementFailed();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        MetricsRegistry metrics = metrics(result);
        if (metrics == null || result.wasRetried()) return;

        metrics.incrementTotal();
        metrics.incrementSkipped();
    }

    @Override
    public void onFinish(ISuite suite) {
        MetricsRegistry metrics = (MetricsRegistry) suite.getAttribute(MetricsRegistry.class.getName());
        if (metrics == null) {
            System.out.println("MetricsRegistry suite-ზე ვერ მოიძებნა — მეტრიკების რეპორტი არ დაგენერირდა");
            return;
        }
        try {
            reportService.generate(metrics, System.currentTimeMillis() - startTime);
        } catch (RuntimeException e) {
            System.err.println("მეტრიკების რეპორტი ვერ დაგენერირდა: " + e);
            e.printStackTrace();
        }
    }

    private MetricsRegistry metrics(ITestResult result) {
        return (MetricsRegistry) result.getTestContext().getSuite()
                .getAttribute(MetricsRegistry.class.getName());
    }
}