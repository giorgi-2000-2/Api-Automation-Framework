package org.example.utils;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
public class TestListenerManager implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("Test Started : " + result.getTestClass().getName());
        String testName = result.getMethod().getMethodName();
        ExtentReportManager.getTest().info("Test Started : " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Test Success : " + result.getName());
        ExtentReportManager.getTest().pass("Test Passed");
        ExtentReportManager.unload();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.out.println("Test Failed : " + testName);
        ExtentReportManager.getTest().fail("Test Failed : " + result.getThrowable().getMessage());
        ExtentReportManager.unload();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReportManager.getTest().skip("Test Skipped");
        System.out.println("Test Skipped : " + result.getName());
        ExtentReportManager.unload();
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("Test suite Started : " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Test suite finished : " + context.getName());
        ExtentReportManager.flushReports();
    }


}