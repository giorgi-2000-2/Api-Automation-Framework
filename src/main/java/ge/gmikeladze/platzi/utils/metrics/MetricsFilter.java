package ge.gmikeladze.platzi.utils.metrics;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

@Singleton
public class MetricsFilter implements Filter {

    private final MetricsRegistry metrics;

    @Inject
    public MetricsFilter(MetricsRegistry metrics) {
        this.metrics = metrics;
    }

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {
        String endpointKey = requestSpec.getMethod() + " " + requestSpec.getUserDefinedPath();
        long start = System.currentTimeMillis();
        Response response = ctx.next(requestSpec, responseSpec);
        long duration = System.currentTimeMillis() - start;
        metrics.recordHttpCall(endpointKey, response.statusCode(), duration);
        return response;
    }
}