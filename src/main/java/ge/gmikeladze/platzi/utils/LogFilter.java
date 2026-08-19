package ge.gmikeladze.platzi.utils;
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

        reporter.info("მოთხოვნა: " + requestSpec.getMethod() + " " + requestSpec.getURI());
        if (requestSpec.getBody() != null) {
            reporter.info("მოთხოვნის სხეული: <pre>" + requestSpec.getBody() + "</pre>");
        }

        Response response = ctx.next(requestSpec, responseSpec);

        reporter.info("პასუხის სტატუს კოდი: " + response.getStatusCode());
        reporter.info("პასუხის სხეული: <pre>" + response.getBody().asPrettyString() + "</pre>");

        return response;
    }
}
