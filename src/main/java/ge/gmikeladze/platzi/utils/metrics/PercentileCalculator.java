package ge.gmikeladze.platzi.utils.metrics;

import java.util.List;

public class PercentileCalculator {

    private PercentileCalculator() {}

    public static long calculate(List<Long> sortedList, double percentile) {
        if (sortedList == null || sortedList.isEmpty()) {
            return 0;
        }
        int index = (int) Math.ceil((percentile / 100.0) * sortedList.size()) - 1;
        index = Math.max(0, Math.min(index, sortedList.size() - 1));
        return sortedList.get(index);
    }
}