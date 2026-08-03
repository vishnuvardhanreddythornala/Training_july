package OOPs.Relationships;

/**
 * A has-a relationship is created by keeping one object inside another object.
 * This example shows both composition and aggregation.
 */
public class HasARelationshipExample {

    private static class Engine {
        private final String type;

        Engine(String type) {
            this.type = type;
        }

        void start() {
            System.out.println(type + " engine started.");
        }
    }

    private static class Driver {
        private final String name;

        Driver(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }
    }

    private static class Car {
        // Composition: the car creates and owns its engine.
        private final Engine engine;

        // Aggregation: the driver is supplied from outside and can exist alone.
        private Driver driver;

        Car(String engineType) {
            engine = new Engine(engineType);
        }

        void assignDriver(Driver driver) {
            if (driver == null) {
                throw new IllegalArgumentException("A driver is required.");
            }
            this.driver = driver;
        }

        void drive() {
            if (driver == null) {
                throw new IllegalStateException("Assign a driver before driving.");
            }
            engine.start();
            System.out.println(driver.getName() + " is driving the car.");
        }
    }

    public static void main(String[] args) {
        Driver driver = new Driver("Meera");
        Car car = new Car("Petrol");

        car.assignDriver(driver);
        car.drive();

        System.out.println("The driver can still exist independently: " + driver.getName());
    }
}
