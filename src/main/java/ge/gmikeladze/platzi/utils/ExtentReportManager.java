package ge.gmikeladze.platzi.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

/**
 * Extent რეპორტის მენეჯერი.
 *
 * FIX C-5: დამატებულია null-safe log()/createNode() მეთოდები.
 *          ადრე ყველგან პირდაპირ getTest().info(...) იძახებოდა და თუ ExtentTest ჯერ არ იყო შექმნილი
 *          (მაგ. @BeforeMethod ჩავარდა createTest()-მდე), ვიღებდით NullPointerException-ს ლისენერში —
 *          რაც ნამდვილ მიზეზს სრულიად მალავდა. ახლა ასეთ დროს ლოგი კონსოლში გადადის და ინფორმაცია არ იკარგება.
 */
public final class ExtentReportManager {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    private ExtentReportManager() {
        // utility კლასი
    }

    public static synchronized ExtentReports getExtentReports() {
        if (extent == null) {
            String reportPath = System.getProperty("user.dir") + "/report/extentReport.html";
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setReportName("Automation Tester: Giorgi Mikeladze - Reports");
            sparkReporter.config().setDocumentTitle("Test Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Environment ", "QA");
            extent.setSystemInfo("Automation Tester", "Giorgi Mikeladze");
        }
        return extent;
    }

    public static ExtentTest createTest(String testName) {
        ExtentTest extentTest = getExtentReports().createTest(testName);
        test.set(extentTest);
        return extentTest;
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    /**
     * FIX C-5: ლოგირების ერთადერთი უსაფრთხო შესასვლელი.
     * თუ მიმდინარე თრედზე ExtentTest არ არსებობს, შეტყობინება კონსოლში გადადის
     * და არა NullPointerException-ში.
     */
    public static void log(Status status, String message) {
        ExtentTest currentTest = test.get();
        if (currentTest != null) {
            currentTest.log(status, message);
        } else {
            System.out.println("[" + status + "] " + message);
        }
    }

    /** მოკლე ჩანაწერი Status.INFO-თი. */
    public static void info(String message) {
        log(Status.INFO, message);
    }

    /**
     * FIX C-5: null-safe კვანძის შექმნა.
     * აბრუნებს null-ს, თუ მიმდინარე თრედზე ტესტი ჯერ არ არსებობს —
     * გამომძახებელი ამ შემთხვევაში log()-ზე უნდა გადავიდეს.
     */
    public static ExtentTest createNode(String nodeName) {
        ExtentTest currentTest = test.get();
        return currentTest == null ? null : currentTest.createNode(nodeName);
    }

    public static void unload() {
        test.remove();
    }

    public static void flushReports() {
        if (extent != null) {
            extent.flush();
        }
    }
}
