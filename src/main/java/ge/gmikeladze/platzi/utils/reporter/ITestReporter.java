package ge.gmikeladze.platzi.utils;

public interface ITestReporter {
        void createTest(String testName);
        void createNode(String nodeName);
        void log(ReportStatus status, String message);
        void info(String message);
        void unload();
        void flush();
}