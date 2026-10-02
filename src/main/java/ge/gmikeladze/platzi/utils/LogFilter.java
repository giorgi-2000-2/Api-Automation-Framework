package ge.gmikeladze.platzi.utils;
import ge.gmikeladze.platzi.utils.reporter.ITestReporter;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class LogFilter implements Filter {
    private final ITestReporter reporter;

    public LogFilter(ITestReporter reporter) {
        this.reporter = reporter;
    }


    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {

        String call = requestSpec.getMethod() + " " + requestSpec.getURI();
        reporter.info("მოთხოვნა: " + call);
        if (requestSpec.getBody() != null) {
            reporter.attach("მოთხოვნის სხეული — " + call, String.valueOf((Object) requestSpec.getBody()));
        }

        Response response = ctx.next(requestSpec, responseSpec);

        reporter.info("პასუხი: " + response.getStatusCode() + " (" + response.getTime() + "ms) — " + call);
        reporter.attach("პასუხის სხეული — " + response.getStatusCode() + " " + call,
                response.getBody().asPrettyString());

        return response;
    }
}
