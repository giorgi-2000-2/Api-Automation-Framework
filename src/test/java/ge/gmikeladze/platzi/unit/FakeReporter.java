package ge.gmikeladze.platzi.unit;

import ge.gmikeladze.platzi.utils.reporter.ITestReporter;
import ge.gmikeladze.platzi.utils.reporter.ReportStatus;

import java.util.ArrayList;
import java.util.List;

public class FakeReporter implements ITestReporter {

    public record LogEntry(ReportStatus status, String message) {}

    private final List<LogEntry> logs = new ArrayList<>();

    @Override
    public void createTest(String testName) {
        // no-op
    }

    @Override
    public void createNode(String nodeName) {
        // no-op
    }

    @Override
    public void log(ReportStatus status, String message) {
        logs.add(new LogEntry(status, message));
    }

    @Override
    public void info(String message) {
        logs.add(new LogEntry(ReportStatus.INFO, message));
    }

    @Override
    public void unload() {
        // no-op
    }

    @Override
    public void flush() {
        // no-op
    }

    public List<LogEntry> getLogs() {
        return List.copyOf(logs);
    }

    public boolean hasWarningContaining(String fragment) {
        return logs.stream()
                .anyMatch(e -> e.status() == ReportStatus.WARNING
                        && e.message() != null
                        && e.message().contains(fragment));
    }

    public void clear() {
        logs.clear();
    }
}