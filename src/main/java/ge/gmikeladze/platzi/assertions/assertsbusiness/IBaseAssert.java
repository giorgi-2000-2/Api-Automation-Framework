package ge.gmikeladze.platzi.assertions.assertsbusiness;

import io.restassured.response.Response;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public interface IBaseAssert<T, Self extends IBaseAssert<T, Self>> {
    
    Self assertThat(T dto);

    Self assertThat(List<T> dtoList);

    Self assertThat(Response response);

    <V> Self hasField(Function<T, V> extractor, V expected, String fieldName);

    <V> Self hasField(Function<T, V> extractor, V expected, String fieldName, String customMessage);

    <V> Self hasNotNullField(Function<T, V> extractor, String fieldName);

    Self hasSize(int expectedSize);

    Self allMatch(Predicate<T> predicate, String description);

    Self allHavePositiveId(Function<T, Integer> idExtractor);

    void isDeletedSuccessfully();

}