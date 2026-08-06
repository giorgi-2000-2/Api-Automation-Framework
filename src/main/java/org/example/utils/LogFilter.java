package org.example.utils;

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

        ExtentReportManager.getTest().info(" HTTP Request: " + requestSpec.getMethod() + " " + requestSpec.getURI());
        if (requestSpec.getBody() != null) {
            ExtentReportManager.getTest().info("Request Body: <pre>" + requestSpec.getBody() + "</pre>");
        }


        Response response = ctx.next(requestSpec, responseSpec);


        ExtentReportManager.getTest().info(" Response Status Code: " + response.getStatusCode());
        ExtentReportManager.getTest().info(" Response Body: <pre>" + response.getBody().asPrettyString() + "</pre>");

        return response;
    }
}  