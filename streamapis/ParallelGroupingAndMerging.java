package streamapis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Groups a large sales data set in parallel. Each worker builds a partial map,
 * and the collector's combiner explicitly merges those partial results.
 */
public class ParallelGroupingAndMerging {
    private static final int DEFAULT_SIZE = 1_000_000;
    private static final String[] CATEGORIES = {
            "Books", "Clothing", "Electronics", "Garden", "Grocery",
            "Health", "Home", "Sports", "Toys", "Travel"
    };

    public static void main(String[] args) {
        int size = readSize(args, DEFAULT_SIZE);
        List<Sale> sales = IntStream.range(0, size)
                .mapToObj(index -> new Sale(
                        CATEGORIES[Math.floorMod(index * 31, CATEGORIES.length)],
                        100 + Math.floorMod(index * 7_919, 100_000)))
                .collect(Collectors.toList());

        long start = System.nanoTime();
        Map<String, GroupStats> grouped = sales.parallelStream().collect(
                HashMap::new,
                (partialResult, sale) -> partialResult
                        .computeIfAbsent(sale.category, ignored -> new GroupStats())
                        .add(sale.amountInCents),
                ParallelGroupingAndMerging::mergePartialResults);
        long elapsed = System.nanoTime() - start;

        long groupedCount = grouped.values().stream()
                .mapToLong(stats -> stats.count)
                .sum();
        if (groupedCount != sales.size()) {
            throw new IllegalStateException("Some sales were lost while merging partial results.");
        }

        System.out.printf("Sales grouped: %,d%n", groupedCount);
        System.out.printf("Merged groups: %d%n", grouped.size());
        System.out.printf("Parallel grouping + merging time: %.3f ms%n", elapsed / 1_000_000.0);
        grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> System.out.printf(
                        "%-12s count=%,7d total=$%,12.2f average=$%,8.2f%n",
                        entry.getKey(),
                        entry.getValue().count,
                        entry.getValue().totalInCents / 100.0,
                        entry.getValue().averageInDollars()));
    }

    private static void mergePartialResults(
            Map<String, GroupStats> left,
            Map<String, GroupStats> right) {
        right.forEach((category, stats) -> left.merge(category, stats, GroupStats::merge));
    }

    private static int readSize(String[] args, int defaultSize) {
        int size = args.length == 0 ? defaultSize : Integer.parseInt(args[0]);
        if (size < 1) {
            throw new IllegalArgumentException("Size must be positive.");
        }
        return size;
    }

    private static class Sale {
        final String category;
        final long amountInCents;

        Sale(String category, long amountInCents) {
            this.category = category;
            this.amountInCents = amountInCents;
        }
    }

    private static class GroupStats {
        long count;
        long totalInCents;

        void add(long amountInCents) {
            count++;
            totalInCents += amountInCents;
        }

        GroupStats merge(GroupStats other) {
            count += other.count;
            totalInCents += other.totalInCents;
            return this;
        }

        double averageInDollars() {
            return totalInCents / (count * 100.0);
        }
    }
}
