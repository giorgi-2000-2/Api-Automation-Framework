package ge.gmikeladze.platzi.utils;
public final class TestReporterContext {

    private static final ThreadLocal<ITestReporter> REPORTER = new ThreadLocal<>();

    private TestReporterContext() {}
    public static void set(ITestReporter reporter) {
        REPORTER.set(reporter);
    }

    public static ITestReporter get() {
        ITestReporter reporter = REPORTER.get();
        if (reporter == null) {
            throw new IllegalStateException(
                    "ITestReporter არ არის ინიციალიზებული"
            );}
        return reporter;}

    public static void remove() {
        REPORTER.remove();
    }




}