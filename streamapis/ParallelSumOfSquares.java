package streamapis;

import java.util.stream.LongStream;

/**
 * Calculates the sum of the squares from 1 to one million with a parallel stream.
 */
public class ParallelSumOfSquares {
    private static final long DEFAULT_SIZE = 1_000_000L;
    private static final long MAX_SAFE_SIZE = 3_000_000L;

    public static void main(String[] args) {
        long size = readSize(args);

        long start = System.nanoTime();
        long sum = LongStream.rangeClosed(1, size)
                .parallel()
                .map(number -> number * number)
                .sum();
        long elapsed = System.nanoTime() - start;

        long expected = sumOfSquaresFormula(size);
        if (sum != expected) {
            throw new IllegalStateException("Unexpected result: " + sum);
        }

        System.out.printf("Numbers: %,d%n", size);
        System.out.printf("Parallel sum of squares: %,d%n", sum);
        System.out.printf("Time: %.3f ms%n", elapsed / 1_000_000.0);
        System.out.printf("Common-pool parallelism: %d%n",
                java.util.concurrent.ForkJoinPool.getCommonPoolParallelism());
    }

    private static long readSize(String[] args) {
        long size = args.length == 0 ? DEFAULT_SIZE : Long.parseLong(args[0]);
        if (size < 1 || size > MAX_SAFE_SIZE) {
            throw new IllegalArgumentException(
                    "Size must be between 1 and " + MAX_SAFE_SIZE + " so the long result cannot overflow.");
        }
        return size;
    }

    private static long sumOfSquaresFormula(long size) {
        long first = size;
        long second = size + 1;
        long third = 2 * size + 1;

        if (first % 2 == 0) {
            first /= 2;
        } else {
            second /= 2;
        }

        if (first % 3 == 0) {
            first /= 3;
        } else if (second % 3 == 0) {
            second /= 3;
        } else {
            third /= 3;
        }

        return Math.multiplyExact(Math.multiplyExact(first, second), third);
    }
}
