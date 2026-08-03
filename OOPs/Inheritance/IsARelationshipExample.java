package OOPs.Inheritance;

/**
 * Inheritance represents an is-a relationship: a Developer is an Employee and
 * receives the accessible behavior defined by Employee.
 */
public class IsARelationshipExample {

    private static class Employee {
        private final int id;
        private final String name;

        Employee(int id, String name) {
            this.id = id;
            this.name = name;
        }

        void introduce() {
            System.out.println("Employee " + id + ": " + name);
        }

        void work() {
            System.out.println(name + " is completing assigned work.");
        }

        String getName() {
            return name;
        }
    }

    private static class Developer extends Employee {
        private final String programmingLanguage;

        Developer(int id, String name, String programmingLanguage) {
            super(id, name);
            this.programmingLanguage = programmingLanguage;
        }

        @Override
        void work() {
            System.out.println(getName() + " is developing software with "
                    + programmingLanguage + ".");
        }

        void reviewCode() {
            System.out.println(getName() + " is reviewing a pull request.");
        }
    }

    public static void main(String[] args) {
        Developer developer = new Developer(101, "Ravi", "Java");

        developer.introduce(); // inherited method
        developer.work();      // specialized method
        developer.reviewCode();

        Employee employee = developer; // valid because Developer is an Employee
        System.out.println("Developer is an Employee: " + (employee instanceof Employee));
    }
}
