package ge.gmikeladze.platzi.steps;

import ge.gmikeladze.platzi.assertions.ResponseValidator;
import ge.gmikeladze.platzi.utils.ExtentReportManager;

public abstract class BaseSteps {

    protected final ResponseValidator validator;

    protected BaseSteps(ResponseValidator validator) {
        this.validator = validator;
    }

    /**
     * FIX C-5: ადრე აქ ორჯერ იძახებოდა ExtentReportManager.getTest() null-შემოწმებით,
     *          ხოლო ExtentTest-ის არარსებობისას ნაბიჯი უბრალოდ იკარგებოდა.
     *          ახლა გამოიყენება null-safe ExtentReportManager.info(...), რომელიც
     *          ასეთ შემთხვევაში ჩანაწერს კონსოლში გადაიტანს და ინფორმაცია არ დაიკარგება.
     */
    protected void step(String description) {
        ExtentReportManager.info("ნაბიჯი: " + description);
    }
}
