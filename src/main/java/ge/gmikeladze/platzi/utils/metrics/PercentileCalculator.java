package ge.gmikeladze.platzi.utils.metrics;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;

@Singleton
public class PercentileCalculator {

    @Inject
    public PercentileCalculator() {}

    public long calculate(List<Long> sortedList, double percentile) {
        if (sortedList == null || sortedList.isEmpty()) {
            return 0;
        }
        int index = (int) Math.ceil((percentile / 100.0) * sortedList.size()) - 1;
        index = Math.max(0, Math.min(index, sortedList.size() - 1));
        return sortedList.get(index);
    }
}