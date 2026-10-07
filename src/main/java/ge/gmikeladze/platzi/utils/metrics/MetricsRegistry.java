package ge.gmikeladze.platzi.utils.metrics;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

@Singleton
public class MetricsRegistry {

    private final LongAdder totalTests = new LongAdder();
    private final LongAdder passedTests = new LongAdder();
    private final LongAdder failedTests = new LongAdder();
    private final LongAdder skippedTests = new LongAdder();
    private final LongAdder flakyTests = new LongAdder();

    @Getter
    private final Map<String, EndpointStats> endpointStatsMap = new ConcurrentHashMap<>();

    @Inject
    public MetricsRegistry() {}

    public void incrementTotal()   { totalTests.increment(); }
    public void incrementPassed()  { passedTests.increment(); }
    public void incrementFailed()  { failedTests.increment(); }
    public void incrementSkipped() { skippedTests.increment(); }
    public void incrementFlaky()   { flakyTests.increment(); }

    public void recordHttpCall(String endpointKey, int statusCode, long responseTimeMs) {
        endpointStatsMap
                .computeIfAbsent(endpointKey, k -> new EndpointStats())
                .recordCall(statusCode, responseTimeMs);
    }


    public int getTotalTests()   { return totalTests.intValue(); }
    public int getPassedTests()  { return passedTests.intValue(); }
    public int getFailedTests()  { return failedTests.intValue(); }
    public int getSkippedTests() { return skippedTests.intValue(); }
    public int getFlakyTests()   { return flakyTests.intValue(); }

    public static class EndpointStats {
        private final LongAdder callsCount = new LongAdder();
        private final LongAdder retriesCount = new LongAdder();

        @Getter
        private final Map<Integer, LongAdder> statusCodes = new ConcurrentHashMap<>();

        @Getter
        private final List<Long> responseTimes = Collections.synchronizedList(new ArrayList<>());

        public void recordCall(int statusCode, long responseTimeMs) {
            callsCount.increment();
            statusCodes.computeIfAbsent(statusCode, k -> new LongAdder()).increment();
            responseTimes.add(responseTimeMs);
        }

        public int getCallsCount()   { return callsCount.intValue(); }
        public int getRetriesCount() { return retriesCount.intValue(); }
    }
}