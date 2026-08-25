package ge.gmikeladze.platzi.assertions.validator;
import com.google.inject.Inject;
import ge.gmikeladze.platzi.annotations.TestScoped;
import ge.gmikeladze.platzi.utils.reporter.ITestReporter;
import ge.gmikeladze.platzi.utils.reporter.ReportStatus;

@TestScoped
public abstract class Validator {
    private final ITestReporter reporter;

    @Inject
    public Validator(ITestReporter reporter) {
        this.reporter = reporter;
    }


    protected void reportPass(String message) {
        reporter.log(ReportStatus.PASS, message);
    }

    protected void reportFail(String message) {
        reporter.log(ReportStatus.FAIL, message);
    }




}
