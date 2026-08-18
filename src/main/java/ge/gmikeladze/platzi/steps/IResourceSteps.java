package ge.gmikeladze.platzi.steps;

import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import io.restassured.response.Response;

public interface IResourceSteps<TRequest, TResponse, TUpdate> {


    TResponse create(TRequest body);

    TResponse create(TRequest body, HttpStatusCode expectedStatus);

    TResponse getById(int id);

    TResponse update(int id, TUpdate body);

    Response delete(int id);

    <T> T createExpectingError(TRequest body,
                               HttpStatusCode expectedStatus,
                               Class<T> errorDto);

    <T> T getExpectingError(int id,
                            HttpStatusCode expectedStatus,
                            Class<T> errorDto);

    <T> T updateExpectingError(int id,
                               Object body,
                               HttpStatusCode expectedStatus,
                               Class<T> errorDto);

    <T> T deleteExpectingError(int id,
                               HttpStatusCode expectedStatus,
                               Class<T> errorDto);


}
