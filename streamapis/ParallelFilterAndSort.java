package streamapis;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Filters and sorts a large in-memory data set with a parallel stream.
 */
public class ParallelFilterAndSort {
    private static final int DEFAULT_SIZE = 2_000_000;
    private static final int MINIMUM_VALUE = 1_000_000_000;

    public static void main(String[] args) {
        int size = readSize(args, DEFAULT_SIZE);
        int[] data = IntStream.range(0, size)
                .map(ParallelFilterAndSort::mix)
                .toArray();

        long start = System.nanoTime();
        int[] result = Arrays.stream(data)
                .parallel()
                .filter(value -> value > MINIMUM_VALUE && value % 3 == 0)
                .sorted()
                .toArray();
        long elapsed = System.nanoTime() - start;

        verifyResult(result);

        System.out.printf("Input values: %,d%n", size);
        System.out.printf("Matching values: %,d%n", result.length);
        System.out.printf("First results: %s%n",
                Arrays.toString(Arrays.copyOf(result, Math.min(result.length, 10))));
        System.out.printf("Parallel filter + sort time: %.3f ms%n", elapsed / 1_000_000.0);
    }

    private static int mix(int value) {
        value ^= value << 13;
        value ^= value >>> 17;
        value ^= value << 5;
        return value & Integer.MAX_VALUE;
    }

    private static void verifyResult(int[] result) {
        for (int index = 0; index < result.length; index++) {
            int value = result[index];
            if (value <= MINIMUM_VALUE || value % 3 != 0) {
                throw new IllegalStateException("Filter condition was not preserved.");
            }
            if (index > 0 && result[index - 1] > value) {
                throw new IllegalStateException("Result is not sorted.");
            }
        }
    }

    private static int readSize(String[] args, int defaultSize) {
        int size = args.length == 0 ? defaultSize : Integer.parseInt(args[0]);
        if (size < 1) {
            throw new IllegalArgumentException("Size must be positive.");
        }
        return size;
    }
}
