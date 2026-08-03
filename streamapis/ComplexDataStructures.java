package streamapis;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ComplexDataStructures {

    static class Employee {
        String name, department;
        double salary;
        public Employee(String name, String department, double salary) {
            this.name = name; this.department = department; this.salary = salary;
        }
    }

    static class Category {
        String name;
        List<Category> subCategories;
        public Category(String name, List<Category> subCategories) {
            this.name = name; this.subCategories = subCategories;
        }
        public Stream<Category> flattened() {
            return Stream.concat(Stream.of(this), 
                    subCategories == null ? Stream.empty() : subCategories.stream().flatMap(Category::flattened));
        }
    }

    static class PersonCSV {
        String name;
        int age;
        public PersonCSV(String name, int age) { this.name = name; this.age = age; }
        @Override
        public String toString() { return name + " (" + age + ")"; }
    }

    public static void main(String[] args) {
        // 1. Flatten a Map of String -> List<Integer> into a list of all integers.
        Map<String, List<Integer>> mapOfLists = Map.of(
                "A", Arrays.asList(1, 2),
                "B", Arrays.asList(3, 4, 5)
        );
        List<Integer> allInts = mapOfLists.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
        System.out.println("Flattened Map<String, List<Integer>>: " + allInts);

        // 2. Convert a nested Map (Map<String, Map<String, Integer>>) to a flat list of entries.
        Map<String, Map<String, Integer>> nestedMap = Map.of(
                "Group1", Map.of("Alice", 10, "Bob", 20),
                "Group2", Map.of("Charlie", 30)
        );
        List<Map.Entry<String, Integer>> flatEntries = nestedMap.values().stream()
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toList());
        System.out.println("Flattened Nested Map: " + flatEntries);

        // 3. Group employees by department and then by their salary range.
        List<Employee> employees = Arrays.asList(
                new Employee("E1", "IT", 40000),
                new Employee("E2", "IT", 60000),
                new Employee("E3", "IT", 120000),
                new Employee("E4", "HR", 45000),
                new Employee("E5", "HR", 80000)
        );
        Map<String, Map<String, List<Employee>>> groupedEmployees = employees.stream()
                .collect(Collectors.groupingBy(
                        e -> e.department,
                        Collectors.groupingBy(e -> {
                            if (e.salary < 50000) return "< 50000";
                            else if (e.salary <= 100000) return "50000-100000";
                            else return "> 100000";
                        })
                ));
        System.out.println("Grouped Employees by Dept and Salary Range: ");
        groupedEmployees.forEach((dept, salaryGroups) -> {
            System.out.println("  " + dept + ":");
            salaryGroups.forEach((range, list) -> System.out.println("    " + range + ": " + list.size() + " employees"));
        });

        // 4. Flatten a tree structure (like categories with subcategories).
        Category root = new Category("Root", Arrays.asList(
                new Category("Electronics", Arrays.asList(
                        new Category("Laptops", null),
                        new Category("Phones", null)
                )),
                new Category("Clothing", Arrays.asList(
                        new Category("Shirts", null)
                ))
        ));
        List<String> allCategoryNames = root.flattened()
                .map(c -> c.name)
                .collect(Collectors.toList());
        System.out.println("Flattened Tree Categories: " + allCategoryNames);

        // 5. Convert CSV data (List of String arrays) into objects and perform analysis.
        List<String[]> csvData = Arrays.asList(
                new String[]{"Alice", "25"},
                new String[]{"Bob", "30"},
                new String[]{"Charlie", "35"}
        );
        List<PersonCSV> people = csvData.stream()
                .map(arr -> new PersonCSV(arr[0], Integer.parseInt(arr[1])))
                .collect(Collectors.toList());
        double avgAge = people.stream().mapToInt(p -> p.age).average().orElse(0.0);
        System.out.println("Parsed CSV Objects: " + people);
        System.out.println("Average Age: " + avgAge);
    }
}
