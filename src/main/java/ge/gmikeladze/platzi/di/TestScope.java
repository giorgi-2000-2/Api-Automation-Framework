package ge.gmikeladze.platzi.di;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

import java.util.HashMap;
import java.util.Map;

public final class TestScope implements Scope {

    private final ThreadLocal<Map<Key<?>, Object>> scopedObjects = new ThreadLocal<>();


    public void enter() {
        if (scopedObjects.get() != null) {
            throw new IllegalStateException(
                    "TestScope უკვე გახსნილია ამ თრედზე — წინა ტესტს exit() არ გამოუძახებია");
        }
        scopedObjects.set(new HashMap<>());
    }


    public void exit() {
        scopedObjects.remove();
    }

    @Override
    public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped) {
        return () -> {
            Map<Key<?>, Object> objects = scopedObjects.get();
            if (objects == null) {
                throw new IllegalStateException(
                        "TestScope-ის გარეთ მოთხოვნილია: " + key +
                                "\nდარწმუნდი, რომ @BeforeMethod-ში TEST_SCOPE.enter() გამოიძახე " +
                                "და რომ ეს კოდი ტესტის თრედზე სრულდება.");
            }
            @SuppressWarnings("unchecked")
            T existing = (T) objects.get(key);
            if (existing == null && !objects.containsKey(key)) {
                existing = unscoped.get();
                objects.put(key, existing);
            }
            return existing;
        };
    }

    @Override
    public String toString() {
        return "TestScoped";
    }
}