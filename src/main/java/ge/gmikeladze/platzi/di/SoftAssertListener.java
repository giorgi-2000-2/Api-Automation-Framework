package ge.gmikeladze.platzi.di;

import com.aventstack.extentreports.Status;
import ge.gmikeladze.platzi.utils.ExtentReportManager;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.asserts.SoftAssert;

/**
 * ტესტის დასრულებისას აგროვებს SoftAssert-ის შედეგებს.
 *
 * FIX C-2: ადრე პირველივე ხაზი იყო `if (result.getStatus() != SUCCESS) return;`
 *          ანუ assertAll() მხოლოდ მაშინ იძახებოდა, თუ ტესტი უკვე მწვანე იყო.
 *          თუ ტესტი hard assertion-ზე ჩავარდებოდა, ყველა დაგროვილი soft შეცდომა ჩუმად იკარგებოდა.
 *
 *          ეს რეალურად მოხდა testCreateCategoryBadRequest-ზე: შეტყობინება
 *          "მოსალოდნელი 400, მიღებული 201" წაიშალა, ხოლო რეპორტში დარჩა მხოლოდ
 *          გაუგებარი სქემის შეცდომა.
 *
 *          ახლა assertAll() ყოველთვის სრულდება:
 *            • თუ ტესტი მწვანე იყო — soft შეცდომა მას აწითლებს (ძველი ქცევა);
 *            • თუ ტესტი უკვე წითელი იყო — soft შეცდომა ემატება addSuppressed()-ით
 *              თავდაპირველ გამონაკლისს და პარალელურად რეპორტშიც იწერება.
 *              ასე არც ერთი დიაგნოსტიკა აღარ იკარგება.
 */
public class SoftAssertListener implements IInvokedMethodListener {

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
        if (!method.isTestMethod()) return;
        if (!(result.getAttribute("softAssert") instanceof SoftAssert soft)) return;

        try {
            soft.assertAll();
        } catch (AssertionError softError) {

            if (result.getStatus() == ITestResult.SUCCESS) {
                // ტესტი მწვანე იყო — soft შეცდომა ხდება ჩავარდნის მიზეზი
                result.setStatus(ITestResult.FAILURE);
                result.setThrowable(softError);
                return;
            }

            // ტესტი უკვე ჩავარდნილია — soft შეცდომებს ვინახავთ, არ ვკარგავთ
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
