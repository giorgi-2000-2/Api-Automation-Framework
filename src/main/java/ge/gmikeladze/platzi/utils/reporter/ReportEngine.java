package ge.gmikeladze.platzi.utils.reporter;

import java.util.Arrays;

public enum ReportEngine {
    EXTENT,
    ALLURE;

    public static ReportEngine from(String value) {
        if (value == null || value.isBlank()) {
            return EXTENT;
        }
        try {
            return ReportEngine.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                    "report.engine-ის უცნობი მნიშვნელობა: " + value
                            + ". დასაშვებია: " + Arrays.toString(values()));
        }
    }
}
