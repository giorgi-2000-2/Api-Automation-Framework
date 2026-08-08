package ge.gmikeladze.platzi.utils;

import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListenerManager implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.out.println("ტესტი დაიწყო: " + testName);
        ExtentReportManager.info("ტესტი დაიწყო: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("ტესტი წარმატებულია: " + result.getName());
        ExtentReportManager.log(Status.PASS, "ტესტი წარმატებულია");
        ExtentReportManager.unload();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.out.println("ტესტი ჩავარდა: " + testName);
        ExtentReportManager.log(Status.FAIL, "ტესტი ჩავარდა: " + describeThrowable(result));
        ExtentReportManager.unload();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("ტესტი გამოტოვებულია: " + result.getName());
        ExtentReportManager.log(Status.SKIP, "ტესტი გამოტოვებულია: " + describeThrowable(result));
        ExtentReportManager.unload();
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("ტესტების ნაკრები დაიწყო: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("ტესტების ნაკრები დასრულდა: " + context.getName());
        ExtentReportManager.flushReports();
    }

    private String describeThrowable(ITestResult result) {
        Throwable throwable = result.getThrowable();
        if (throwable == null) {
            return "მიზეზი მითითებული არ არის";
        }
        String message = throwable.getMessage();
        return (message == null || message.isBlank())
                ? throwable.getClass().getSimpleName()
                : message;
    }
}
