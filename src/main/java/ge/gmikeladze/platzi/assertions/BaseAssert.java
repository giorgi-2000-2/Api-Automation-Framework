package ge.gmikeladze.platzi.assertions;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import ge.gmikeladze.platzi.utils.ExtentReportManager;
import org.testng.asserts.SoftAssert;

public abstract class BaseAssert {
    protected final SoftAssert softAssert;
    private final String nodeName;
    private ExtentTest node;

    protected BaseAssert(SoftAssert softAssert, String nodeName) {
        this.softAssert = softAssert;
        this.nodeName = nodeName;
    }

    protected void step(String message) { log(Status.INFO, message); }

    protected void log(Status status, String message) {
        ExtentTest current = node();
        if (current != null) current.log(status, message);
        else ExtentReportManager.log(status, message);
    }

    private ExtentTest node() {
        if (node == null) node = ExtentReportManager.createNode(nodeName);
        return node;
    }
}