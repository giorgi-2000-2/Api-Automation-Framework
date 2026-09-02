package ge.gmikeladze.platzi.utils.config;

public interface IConfigForRequest {
    String baseUrl();
    int responseTimeLimit();
    int maxBodyLengthInMessage();
}
