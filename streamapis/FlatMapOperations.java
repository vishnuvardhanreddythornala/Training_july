package streamapis;

import java.util.*;
import java.util.stream.Collectors;

public class FlatMapOperations {

    static class Order {
        List<String> items;
        public Order(List<String> items) {
            this.items = items;
        }
    }

    public static void main(String[] args) {
        // 1. Flatten a list of lists of integers
        List<List<Integer>> listOfLists = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5),
                Arrays.asList(6, 7, 8)
        );
        List<Integer> flattenedInts = listOfLists.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
        System.out.println("Flattened Integers: " + flattenedInts);

        // 2. Flatten a list of arrays of strings
        List<String[]> listOfArrays = Arrays.asList(
                new String[]{"apple", "banana"},
                new String[]{"orange", "kiwi"},
                new String[]{"grape"}
        );
        List<String> flattenedStrings = listOfArrays.stream()
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
        System.out.println("Flattened Strings: " + flattenedStrings);

        // 3. Split sentences into individual words and collect all unique words
        List<String> sentences = Arrays.asList("Hello world", "Hello Java", "Java is fun");
        List<String> uniqueWords = sentences.stream()
                .flatMap(sentence -> Arrays.stream(sentence.split(" ")))
                .map(String::toLowerCase)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Unique Words: " + uniqueWords);

        // 4. Given a list of orders (each with list of items), get all item names
        List<Order> orders = Arrays.asList(
                new Order(Arrays.asList("Laptop", "Mouse")),
                new Order(Arrays.asList("Keyboard", "Monitor")),
                new Order(Arrays.asList("Phone"))
        );
        List<String> allItems = orders.stream()
                .flatMap(order -> order.items.stream())
                .collect(Collectors.toList());
        System.out.println("All Items: " + allItems);

        // 5. Flatten a map of lists into a single list
        Map<String, List<Integer>> mapOfLists = new HashMap<>();
        mapOfLists.put("Evens", Arrays.asList(2, 4, 6));
        mapOfLists.put("Odds", Arrays.asList(1, 3, 5));
        
        List<Integer> allNumbers = mapOfLists.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
        System.out.println("Flattened Map Values: " + allNumbers);
    }
}
