package ge.gmikeladze.platzi.steps;
import ge.gmikeladze.platzi.assertions.ResponseValidator;
import ge.gmikeladze.platzi.utils.ExtentReportManager;

public abstract class BaseSteps {

    protected final ResponseValidator validator;

    public BaseSteps(ResponseValidator validator) {
        this.validator = validator;
    }

    public void step(String description) {
       ExtentReportManager.info("ნაბიჯი: " + description);
    }
}