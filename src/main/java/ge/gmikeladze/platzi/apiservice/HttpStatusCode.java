package ge.gmikeladze.platzi.apiservice;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * FIX L-7 (dead code): წაშლილია 6 გამოუყენებელი კონსტანტა —
 * ACCEPTED, NO_CONTENT, UNAUTHORIZED, FORBIDDEN, NOT_FOUND, INTERNAL_SERVER_ERROR.
 * enum ინახავს მხოლოდ იმ სტატუსებს, რომლებსაც რეალურად იყენებს ტესტების ამჟამინდელი ნაკრები (YAGNI).
 * ავტორიზაციის ტესტების დამატებისას UNAUTHORIZED/FORBIDDEN აქვე უნდა დაბრუნდეს.
 */
@Getter
@AllArgsConstructor
public enum HttpStatusCode {
    OK(200, "OK"),
    CREATED(201, "Created"),
    BAD_REQUEST(400, "Bad Request");

    private final int code;
    private final String description;
}
