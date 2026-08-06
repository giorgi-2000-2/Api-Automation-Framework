package org.example.steps;

import org.example.assertionmanager.ResponseValidator;
import org.example.utils.ExtentReportManager;

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