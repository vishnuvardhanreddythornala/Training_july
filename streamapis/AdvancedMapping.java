package streamapis;

import java.util.*;
import java.util.stream.Collectors;

public class AdvancedMapping {

    static class Employee {
        String name;
        double salary;
        String department;

        public Employee(String name, double salary, String department) {
            this.name = name;
            this.salary = salary;
            this.department = department;
        }
    }

    static class Product {
        String name;
        double price;

        public Product(String name, double price) {
            this.name = name;
            this.price = price;
        }
    }

    public static void main(String[] args) {
        // 1. Extract names of employees earning more than average salary
        List<Employee> employees = Arrays.asList(
                new Employee("Alice", 50000, "IT"),
                new Employee("Bob", 60000, "HR"),
                new Employee("Charlie", 70000, "IT"),
                new Employee("David", 40000, "Finance")
        );

        double avgSalary = employees.stream()
                .mapToDouble(e -> e.salary)
                .average()
                .orElse(0.0);

        List<String> highEarners = employees.stream()
                .filter(e -> e.salary > avgSalary)
                .map(e -> e.name)
                .collect(Collectors.toList());
        System.out.println("High Earners: " + highEarners);

        // 2. Get the length of the longest string in a list
        List<String> strings = Arrays.asList("apple", "banana", "kiwi", "strawberry");
        int maxLength = strings.stream()
                .mapToInt(String::length)
                .max()
                .orElse(0);
        System.out.println("Max length: " + maxLength);

        // 3. Convert a list of strings to a map (string -> length)
        Map<String, Integer> stringLengthMap = strings.stream()
                .collect(Collectors.toMap(s -> s, String::length));
        System.out.println("String to Length Map: " + stringLengthMap);

        // 4. Convert a list of products to a map (product name -> price)
        List<Product> products = Arrays.asList(
                new Product("Laptop", 1200.0),
                new Product("Phone", 800.0),
                new Product("Tablet", 400.0)
        );
        Map<String, Double> productPriceMap = products.stream()
                .collect(Collectors.toMap(p -> p.name, p -> p.price));
        System.out.println("Product Price Map: " + productPriceMap);

        // 5. Transform a list of employees to a list of their departments (unique)
        List<String> uniqueDepartments = employees.stream()
                .map(e -> e.department)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Unique Departments: " + uniqueDepartments);
    }
}
