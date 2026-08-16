package ge.gmikeladze.platzi.datafactories;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.dtos.response.ApiError;
import lombok.Getter;
import lombok.Setter;


import java.util.List;
@Getter
@Setter

public class NegativeCase<T> {
    private  String name;
    private  T payload;
    private  HttpStatusCode expectedStatus;
    private  Class<? extends ApiError> errorDto;
    private  List<String> messageFragments;

    public NegativeCase(String name, T payload,
                        HttpStatusCode expectedStatus,
                        Class<? extends ApiError> errorDto,
                        List<String> messageFragments) {
        this.name = name;
        this.payload = payload;
        this.expectedStatus = expectedStatus;
        this.errorDto = errorDto;
        this.messageFragments = List.copyOf(messageFragments);
    }


    public static  <T> NegativeCase<T> of(String name, T payload,
                                         HttpStatusCode expectedStatus,
                                         Class<? extends ApiError> errorDto,
                                         String... messageFragments) {
        return new NegativeCase<>(name, payload, expectedStatus, errorDto, List.of(messageFragments));
    }

    @Override public String toString()        { return name; }
}