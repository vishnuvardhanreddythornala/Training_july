package streamapis;

import java.util.*;
import java.util.stream.Collectors;

public class SortingComplex {
    public static void main(String[] args) {
        // 1. Sort strings by length, then alphabetically
        List<String> strings = Arrays.asList("banana", "apple", "kiwi", "pear", "fig", "grape");
        List<String> sortedStrings = strings.stream()
                .sorted(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()))
                .collect(Collectors.toList());
        System.out.println("Sorted Strings: " + sortedStrings);

        // 2. Sort numbers by their absolute value
        List<Integer> numbers = Arrays.asList(-5, 2, 8, -1, 3, -10);
        List<Integer> sortedByAbs = numbers.stream()
                .sorted(Comparator.comparingInt(Math::abs))
                .collect(Collectors.toList());
        System.out.println("Sorted by Absolute Value: " + sortedByAbs);
    }
}
