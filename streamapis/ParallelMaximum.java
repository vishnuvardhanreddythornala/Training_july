package streamapis;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Finds the maximum value in a huge list with a parallel stream.
 */
public class ParallelMaximum {
    private static final int DEFAULT_SIZE = 5_000_000;

    public static void main(String[] args) {
        int size = readSize(args, DEFAULT_SIZE);
        List<Integer> numbers = IntStream.range(0, size)
                .map(index -> Math.floorMod(index * 1_103_515_245 + 12_345, 1_000_000_000))
                .boxed()
                .collect(Collectors.toCollection(() -> new ArrayList<>(size)));

        // A known maximum makes the result easy to verify.
        numbers.set(size / 2, Integer.MAX_VALUE);

        long start = System.nanoTime();
        int maximum = numbers.parallelStream()
                .mapToInt(Integer::intValue)
                .max()
                .orElseThrow();
        long elapsed = System.nanoTime() - start;

        if (maximum != Integer.MAX_VALUE) {
            throw new IllegalStateException("Maximum value was not found.");
        }

        System.out.printf("List size: %,d%n", numbers.size());
        System.out.printf("Maximum: %,d%n", maximum);
        System.out.printf("Parallel max time: %.3f ms%n", elapsed / 1_000_000.0);
    }

    private static int readSize(String[] args, int defaultSize) {
        int size = args.length == 0 ? defaultSize : Integer.parseInt(args[0]);
        if (size < 1) {
            throw new IllegalArgumentException("Size must be positive.");
        }
        return size;
    }
}
