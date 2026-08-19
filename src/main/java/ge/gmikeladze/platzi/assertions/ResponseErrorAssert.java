package ge.gmikeladze.platzi.assertions;

import com.google.inject.Inject;
import ge.gmikeladze.platzi.annotations.TestScoped;
import ge.gmikeladze.platzi.assertions.assertsbusiness.BaseAssert;
import ge.gmikeladze.platzi.dtos.response.error.ApiError;
import org.testng.asserts.SoftAssert;
import java.util.List;

@TestScoped
public class ResponseErrorAssert extends BaseAssert {

    private ApiError error;

    @Inject
    ResponseErrorAssert(SoftAssert softAssert) {
        super(softAssert, " შეცდომის სხეულის შემოწმება ");
    }

    public ResponseErrorAssert assertThat(ApiError error) {
        this.error = error;
        return this;
    }

    public ResponseErrorAssert messageIsNotBlank() {
        step("შეცდომის ტექსტი არ არის ცარიელი");
        List<String> messages = error.messages();
        softAssert.assertFalse(messages.isEmpty(), "შეცდომის სხეულში message არ მოვიდა");
        boolean hasNonBlank = false;
        for (String m : messages) {
            if (m != null && !m.isBlank()) {
                hasNonBlank = true;
                break;
            }
        }
        softAssert.assertTrue(hasNonBlank, "შეცდომის ყველა message ცარიელია: " + messages);
        return this;
    }


    public ResponseErrorAssert messageMentions(String fragment) {
        step("შეცდომის ტექსტი მოიხსენიებს: " + fragment);
        String needle = fragment.toLowerCase();
        boolean found = false;
        for (String message : error.messages()) {
            if (message != null && message.toLowerCase().contains(needle)) {
                found = true;
                break;
            }
        }
        softAssert.assertTrue(found,
                "შეცდომაში ვერ მოიძებნა " + fragment + " " + error.messages());
        return this;
    }

    public ResponseErrorAssert messageMentionsAll(List<String> fragments) {
        for (String fragment : fragments) {
            messageMentions(fragment);
        }
        return this;
    }





}