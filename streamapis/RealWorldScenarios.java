package streamapis;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class RealWorldScenarios {

    // Shopping Cart
    static class Item {
        String name;
        double price;
        public Item(String name, double price) { this.name = name; this.price = price; }
    }
    static class Order {
        String customerName;
        List<Item> items;
        public Order(String customerName, List<Item> items) { this.customerName = customerName; this.items = items; }
    }

    // Library System
    static class Book {
        String title, author, genre;
        int year;
        public Book(String title, String author, int year, String genre) {
            this.title = title; this.author = author; this.year = year; this.genre = genre;
        }
    }

    // Employee Analysis
    static class Employee {
        int id;
        String name, department;
        double salary;
        LocalDate joiningDate;
        public Employee(int id, String name, String department, double salary, LocalDate joiningDate) {
            this.id = id; this.name = name; this.department = department; this.salary = salary; this.joiningDate = joiningDate;
        }
    }

    // Student Report
    static class Student {
        String name;
        List<Integer> marks;
        public Student(String name, List<Integer> marks) { this.name = name; this.marks = marks; }
    }
    
    static class StudentRank {
        String name;
        double percentage;
        public StudentRank(String name, double percentage) { this.name = name; this.percentage = percentage; }
        @Override
        public String toString() { return name + ": " + percentage + "%"; }
    }

    // Hotel Booking
    static class Booking {
        LocalDate checkIn, checkOut;
        String roomType, guestName;
        public Booking(LocalDate checkIn, LocalDate checkOut, String roomType, String guestName) {
            this.checkIn = checkIn; this.checkOut = checkOut; this.roomType = roomType; this.guestName = guestName;
        }
        public long getDays() { return ChronoUnit.DAYS.between(checkIn, checkOut); }
    }

    public static void main(String[] args) {
        // 1. Shopping Cart
        List<Order> orders = Arrays.asList(
                new Order("Alice", Arrays.asList(new Item("Pen", 10), new Item("Book", 50))),
                new Order("Bob", Arrays.asList(new Item("Laptop", 1000))),
                new Order("Alice", Arrays.asList(new Item("Mouse", 20)))
        );
        Map<String, Double> totalSpentByCustomer = orders.stream()
                .collect(Collectors.groupingBy(
                        o -> o.customerName,
                        Collectors.summingDouble(o -> o.items.stream().mapToDouble(i -> i.price).sum())
                ));
        System.out.println("Total Spent by Customer: " + totalSpentByCustomer);

        // 2. Library System
        List<Book> books = Arrays.asList(
                new Book("B1", "Author1", 2020, "Sci-Fi"),
                new Book("B2", "Author2", 2021, "Sci-Fi"),
                new Book("B3", "Author1", 2019, "Fantasy"),
                new Book("B4", "Author3", 2022, "Sci-Fi")
        );
        String mostPublishedGenre = books.stream()
                .collect(Collectors.groupingBy(b -> b.genre, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("Unknown");
        String authorWithMostBooks = books.stream()
                .collect(Collectors.groupingBy(b -> b.author, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("Unknown");
        System.out.println("Most Published Genre: " + mostPublishedGenre);
        System.out.println("Author with Most Books: " + authorWithMostBooks);

        // 3. Employee Analysis
        LocalDate fiveYearsAgo = LocalDate.now().minusYears(5);
        List<Employee> employees = Arrays.asList(
                new Employee(1, "E1", "IT", 60000, LocalDate.now().minusYears(2)),
                new Employee(2, "E2", "IT", 80000, LocalDate.now().minusYears(6)),
                new Employee(3, "E3", "HR", 50000, LocalDate.now().minusYears(1))
        );
        Map<String, Double> avgSalaryLast5Years = employees.stream()
                .filter(e -> e.joiningDate.isAfter(fiveYearsAgo))
                .collect(Collectors.groupingBy(e -> e.department, Collectors.averagingDouble(e -> e.salary)));
        System.out.println("Avg Salary (Hired Last 5 Yrs) by Dept: " + avgSalaryLast5Years);

        // 4. Student Report
        List<Student> students = Arrays.asList(
                new Student("S1", Arrays.asList(80, 90, 85, 70, 95)),
                new Student("S2", Arrays.asList(60, 70, 65, 80, 75))
        );
        List<StudentRank> rankedStudents = students.stream()
                .map(s -> new StudentRank(s.name, s.marks.stream().mapToInt(Integer::intValue).sum() / 5.0))
                .sorted(Comparator.comparingDouble((StudentRank sr) -> sr.percentage).reversed())
                .collect(Collectors.toList());
        System.out.println("Ranked Students: " + rankedStudents);

        // 5. Hotel Booking
        Map<String, Double> roomPrices = Map.of("Single", 100.0, "Double", 150.0, "Suite", 300.0);
        List<Booking> bookings = Arrays.asList(
                new Booking(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 5), "Single", "G1"),
                new Booking(LocalDate.of(2023, 2, 1), LocalDate.of(2023, 2, 3), "Double", "G2"),
                new Booking(LocalDate.of(2023, 3, 1), LocalDate.of(2023, 3, 10), "Single", "G3")
        );
        String popularRoomType = bookings.stream()
                .collect(Collectors.groupingBy(b -> b.roomType, Collectors.counting()))
                .entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("Unknown");
        Map<String, Double> revenuePerRoomType = bookings.stream()
                .collect(Collectors.groupingBy(
                        b -> b.roomType,
                        Collectors.summingDouble(b -> b.getDays() * roomPrices.getOrDefault(b.roomType, 0.0))
                ));
        System.out.println("Most Popular Room: " + popularRoomType);
        System.out.println("Revenue per Room Type: " + revenuePerRoomType);
    }
}
