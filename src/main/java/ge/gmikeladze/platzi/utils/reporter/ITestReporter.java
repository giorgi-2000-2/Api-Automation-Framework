package ge.gmikeladze.platzi.utils.reporter;

public interface ITestReporter {
        void createTest(String testName);
        void createNode(String nodeName);
        void log(ReportStatus status, String message);
        void info(String message);
        void unload();
        void flush();

        default void attach(String name, String content) {
                info(name + ": " + content);
        }

        default void endTest() {
        }
}