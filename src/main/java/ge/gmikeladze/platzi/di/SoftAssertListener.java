package ge.gmikeladze.platzi.di;

import com.aventstack.extentreports.Status;
import ge.gmikeladze.platzi.utils.ExtentReportManager;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.asserts.SoftAssert;


public class SoftAssertListener implements IInvokedMethodListener {

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
        if (!method.isTestMethod()) return;
        if (!(result.getAttribute("softAssert") instanceof SoftAssert soft)) return;

        try {
            soft.assertAll();
        } catch (AssertionError softError) {

            if (result.getStatus() == ITestResult.SUCCESS) {

                result.setStatus(ITestResult.FAILURE);
                result.setThrowable(softError);
                return;
            }

            Throwable primary = result.getThrowable();
            if (primary != null && primary != softError) {
                primary.addSuppressed(softError);
            } else if (primary == null) {
                result.setThrowable(softError);
            }

            ExtentReportManager.log(Status.WARNING,
                    "დამატებითი soft-assert შეცდომები (ტესტი უკვე ჩავარდნილი იყო):\n"
                            + softError.getMessage());
        }
    }
}
