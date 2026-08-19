package ge.gmikeladze.platzi.steps;
import ge.gmikeladze.platzi.assertions.ResponseValidator;
import ge.gmikeladze.platzi.utils.ITestReporter;

public abstract class BaseSteps {
    private final ITestReporter reporter;
    protected final ResponseValidator validator;

    public BaseSteps(ITestReporter reporter, ResponseValidator validator) {
        this.reporter = reporter;
        this.validator = validator;
    }

    public void step(String description) {
        reporter.info("ნაბიჯი: " + description);
    }
}