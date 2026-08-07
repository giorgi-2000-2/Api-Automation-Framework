package ge.gmikeladze.platzi.utils;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class LogFilter implements Filter {

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {

        ExtentReportManager.info("მოთხოვნა: " + requestSpec.getMethod() + " " + requestSpec.getURI());
        if (requestSpec.getBody() != null) {
            ExtentReportManager.info("მოთხოვნის სხეული: <pre>" + requestSpec.getBody() + "</pre>");
        }

        Response response = ctx.next(requestSpec, responseSpec);

        ExtentReportManager.info("პასუხის სტატუს კოდი: " + response.getStatusCode());
        ExtentReportManager.info("პასუხის სხეული: <pre>" + response.getBody().asPrettyString() + "</pre>");

        return response;
    }
}
