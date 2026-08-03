package streamapis;

import java.util.Arrays;
import java.util.function.DoubleSupplier;
import java.util.stream.IntStream;

/**
 * Compares sequential and parallel-stream average calculations on a large data set.
 * The displayed timings are the best of several runs after a warm-up.
 */
public class ParallelAverageComparison {
    private static final int DEFAULT_SIZE = 10_000_000;
    private static final int REPETITIONS = 3;

    public static void main(String[] args) {
        int size = readSize(args, DEFAULT_SIZE);
        int[] data = IntStream.range(0, size)
                .map(index -> Math.floorMod(index * 31 + (index >>> 3), 1_000_000))
                .toArray();

        // Warm up both paths before measuring so class loading and JIT compilation
        // have less influence on the comparison.
        averageSequentially(data);
        averageInParallel(data);

        Measurement sequential = benchmark(() -> averageSequentially(data));
        Measurement parallel = benchmark(() -> averageInParallel(data));

        if (Double.compare(sequential.value, parallel.value) != 0) {
            throw new IllegalStateException("Sequential and parallel averages do not match.");
        }

        System.out.printf("Values: %,d%n", size);
        System.out.printf("Average: %.3f%n", parallel.value);
        System.out.printf("Sequential stream (best of %d): %.3f ms%n",
                REPETITIONS, sequential.nanoseconds / 1_000_000.0);
        System.out.printf("Parallel stream   (best of %d): %.3f ms%n",
                REPETITIONS, parallel.nanoseconds / 1_000_000.0);
        System.out.printf("Speed-up (sequential / parallel): %.2fx%n",
                (double) sequential.nanoseconds / parallel.nanoseconds);
        System.out.printf("Available processors: %d%n", Runtime.getRuntime().availableProcessors());
    }

    private static double averageSequentially(int[] data) {
        return Arrays.stream(data).average().orElseThrow();
    }

    private static double averageInParallel(int[] data) {
        return Arrays.stream(data).parallel().average().orElseThrow();
    }

    private static Measurement benchmark(DoubleSupplier operation) {
        double bestValue = 0.0;
        long bestTime = Long.MAX_VALUE;

        for (int run = 0; run < REPETITIONS; run++) {
            long start = System.nanoTime();
            double value = operation.getAsDouble();
            long elapsed = System.nanoTime() - start;

            if (elapsed < bestTime) {
                bestTime = elapsed;
                bestValue = value;
            }
        }
        return new Measurement(bestValue, bestTime);
    }

    private static int readSize(String[] args, int defaultSize) {
        int size = args.length == 0 ? defaultSize : Integer.parseInt(args[0]);
        if (size < 1) {
            throw new IllegalArgumentException("Size must be positive.");
        }
        return size;
    }

    private static class Measurement {
        final double value;
        final long nanoseconds;

        Measurement(double value, long nanoseconds) {
            this.value = value;
            this.nanoseconds = nanoseconds;
        }
    }
}
