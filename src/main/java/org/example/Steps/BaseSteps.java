package org.example.Steps;

import org.example.AssertionManager.ResponseValidator;
import org.example.Utils.ExtentReportManager;

public abstract class BaseSteps {

    protected final ResponseValidator validator;

    protected BaseSteps(ResponseValidator validator) {
        this.validator = validator;
    }

    protected void step(String description) {
        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest().info("STEP: " + description);
        }

    }

}