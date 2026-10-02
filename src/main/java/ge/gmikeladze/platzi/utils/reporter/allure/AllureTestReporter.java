package ge.gmikeladze.platzi.utils.reporter.allure;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ge.gmikeladze.platzi.utils.reporter.IReportConfig;
import ge.gmikeladze.platzi.utils.reporter.ITestReporter;
import ge.gmikeladze.platzi.utils.reporter.ReportStatus;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
@Singleton
public class AllureTestReporter implements ITestReporter {

private final IReportConfig config;

    private final ThreadLocal<String> openNode = new ThreadLocal<>();
@Inject
    public AllureTestReporter(IReportConfig config) {
        this.config = config;
    }

    @Override
    public void createTest(String testName) {

    }

    @Override
    public void createNode(String nodeName) {
        closeNode();
        if (!hasRunningContext()) {
            return;
        }
        String uuid = UUID.randomUUID().toString();
        lifecycle().startStep(uuid, new StepResult()
                .setName(nodeName)
                .setStatus(Status.PASSED));
        openNode.set(uuid);
    }

    @Override
    public void log(ReportStatus status, String message) {
        if (!hasRunningContext() || message == null) {
            return;
        }
        Status allureStatus = toAllureStatus(status);
        writeStep(message, allureStatus);
        if (allureStatus == Status.FAILED) {
            markOpenNode(Status.FAILED);
        }
    }

    @Override
    public void info(String message) {
        log(ReportStatus.INFO, message);
    }

    @Override
    public void attach(String name, String content) {
        if (!hasRunningContext() || content == null) {
            return;
        }
        boolean json = looksLikeJson(content);
        lifecycle().addAttachment(
                name,
                json ? "application/json" : "text/plain",
                json ? ".json" : ".txt",
                content.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void endTest() {
        closeNode();
    }

    @Override
    public void unload() {
        closeNode();
        openNode.remove();
    }

    @Override
    public void flush() {

    }

    private void writeStep(String message, Status status) {
        boolean tooLong = message.length() > config.maxStepTitle();
        String title = tooLong ? message.substring(0, config.maxStepTitle()) + "…" : message;

        String uuid = UUID.randomUUID().toString();
        lifecycle().startStep(uuid, new StepResult().setName(title).setStatus(status));
        if (tooLong) {
            lifecycle().addAttachment("სრული ტექსტი", "text/plain", ".txt",
                    message.getBytes(StandardCharsets.UTF_8));
        }
        lifecycle().stopStep(uuid);
    }

    private void markOpenNode(Status status) {
        String uuid = openNode.get();
        if (uuid != null) {
            lifecycle().updateStep(uuid, step -> step.setStatus(status));
        }
    }

    private void closeNode() {
        String uuid = openNode.get();
        if (uuid != null) {
            openNode.remove();
            lifecycle().stopStep(uuid);
        }
    }

    private boolean hasRunningContext() {
        return lifecycle().getCurrentTestCaseOrStep().isPresent();
    }

    private static AllureLifecycle lifecycle() {
        return Allure.getLifecycle();
    }

    private static boolean looksLikeJson(String content) {
        String trimmed = content.trim();
        return trimmed.startsWith("{") || trimmed.startsWith("[");
    }

    private static Status toAllureStatus(ReportStatus status) {
        return switch (status) {
            case PASS, INFO -> Status.PASSED;
            case FAIL -> Status.FAILED;
            case SKIP -> Status.SKIPPED;
            case WARNING -> Status.BROKEN;
        };
    }
}
