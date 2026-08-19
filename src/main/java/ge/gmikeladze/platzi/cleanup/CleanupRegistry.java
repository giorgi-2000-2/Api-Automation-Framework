package ge.gmikeladze.platzi.cleanup;

import com.google.inject.Inject;
import ge.gmikeladze.platzi.annotations.TestScoped;
import ge.gmikeladze.platzi.utils.ITestReporter;
import ge.gmikeladze.platzi.utils.ReportStatus;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

@TestScoped
public class CleanupRegistry {

    private final ITestReporter reporter;

    private final Deque<ResourceKey> order = new ArrayDeque<>();
    private final Map<ResourceKey, Runnable> pending = new HashMap<>();

    @Inject
    public CleanupRegistry(ITestReporter reporter) {
        this.reporter = reporter;
    }

    public void register(ResourceKey key, Runnable deleteAction) {
        if (pending.putIfAbsent(key, deleteAction) == null) {
            order.push(key);
        }
    }

    public void markCompleted(ResourceKey key) {
        pending.remove(key);
    }

    public void cleanup() {
        while (!order.isEmpty()) {
            ResourceKey key = order.pop();

            Runnable action = pending.remove(key);

            if (action == null) {
                continue;
            }

            try {
                action.run();
            } catch (Throwable t) {
                reporter.log(
                        ReportStatus.WARNING,
                        "cleanup ვერ შესრულდა: " + t.getMessage()
                );
            }
        }

        pending.clear();
    }
}