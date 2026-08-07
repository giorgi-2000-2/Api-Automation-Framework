package ge.gmikeladze.platzi.steps;

import ge.gmikeladze.platzi.assertions.ResponseValidator;
import ge.gmikeladze.platzi.utils.ExtentReportManager;

public abstract class BaseSteps {

    protected final ResponseValidator validator;

    protected BaseSteps(ResponseValidator validator) {
        this.validator = validator;
    }


    protected void step(String description) {
        ExtentReportManager.info("ნაბიჯი: " + description);
    }
}
