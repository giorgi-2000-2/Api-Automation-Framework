package ge.gmikeladze.platzi.utils;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
public class ExtentTestReporter implements ITestReporter {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> node = new ThreadLocal<>();

    private ExtentReports getExtentReports() {
        if (extent == null) { synchronized (ExtentTestReporter.class) {
                if (extent == null) {
                    String reportPath = System.getProperty("user.dir") + "/report/extentReport.html";
                    ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
                    sparkReporter.config().setReportName("Automation Tester: Giorgi Mikeladze - Reports");
                    sparkReporter.config().setDocumentTitle("Test Execution Report");
                    extent = new ExtentReports();
                    extent.attachReporter(sparkReporter);
                    extent.setSystemInfo("Environment", "QA");
                    extent.setSystemInfo("Automation Tester", "Giorgi Mikeladze");
                }
            }
        }
        return extent;
    }

    @Override
    public void createTest(String testName) {
        test.set(getExtentReports().createTest(testName));
        node.remove();
    }

    @Override
    public void createNode(String nodeName) {
        ExtentTest currentTest = test.get();

        if (currentTest != null) {
            node.set(currentTest.createNode(nodeName));
        }
    }

    @Override
    public void log(ReportStatus status, String message) {

        ExtentTest currentNode = node.get();

        if (currentNode != null) {
            currentNode.log(toExtentStatus(status), message);
            return;
        }
        ExtentTest currentTest = test.get();
        if (currentTest != null) {
            currentTest.log(toExtentStatus(status), message);
        } else {
            System.out.println("[" + status + "] " + message);
        }
    }

    @Override
    public void info(String message) {
        log(ReportStatus.INFO, message);
    }

    @Override
    public void unload() {
        node.remove();
        test.remove();
    }

    @Override
    public void flush() {
        if (extent != null) {
            extent.flush();
        }
    }

    private Status toExtentStatus(ReportStatus status) {
        return switch (status) {
            case PASS -> Status.PASS;
            case FAIL -> Status.FAIL;
            case SKIP -> Status.SKIP;
            case INFO -> Status.INFO;
            case WARNING -> Status.WARNING;
        };
    }
}