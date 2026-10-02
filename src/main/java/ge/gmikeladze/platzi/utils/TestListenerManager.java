package ge.gmikeladze.platzi.utils;

import ge.gmikeladze.platzi.utils.reporter.ITestReporter;
import ge.gmikeladze.platzi.utils.reporter.ReportStatus;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListenerManager implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.out.println("ტესტი დაიწყო: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("ტესტი წარმატებულია: " + result.getName());
        ITestReporter reporter = getReporter(result);
        if (reporter != null) {
            reporter.log(ReportStatus.PASS, "ტესტი წარმატებულია");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.out.println("ტესტი ჩავარდა: " + testName);
        ITestReporter reporter = getReporter(result);
        if (reporter != null) {
            reporter.log(ReportStatus.FAIL, "ტესტი ჩავარდა: " + describeThrowable(result));
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("ტესტი გამოტოვებულია: " + result.getName());
        ITestReporter reporter = getReporter(result);
        if (reporter != null) {
            reporter.log(ReportStatus.SKIP, "ტესტი გამოტოვებულია: " + describeThrowable(result));
        }
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("ტესტების ნაკრები დაიწყო: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("ტესტების ნაკრები დასრულდა: " + context.getName());
    }

    private ITestReporter getReporter(ITestResult result) {
        Object attr = result.getAttribute("reporter");
        if (attr instanceof ITestReporter) {
            return (ITestReporter) attr;
        }
        return null;
    }

    private String describeThrowable(ITestResult result) {
        Throwable throwable = result.getThrowable();
        if (throwable == null) {
            return "მიზეზი მითითებული არ არის";
        }
        String message = throwable.getMessage();
        if (message == null || message.isBlank()) {
            return throwable.getClass().getSimpleName();
        }
        return message;
    }
}