package OOPs.Polymorphism;

/**
 * Demonstrates compile-time polymorphism with method overloading and runtime
 * polymorphism with method overriding.
 */
public class PolymorphismExample {

    private interface Notification {
        void send(String recipient, String message);
    }

    private static class EmailNotification implements Notification {
        @Override
        public void send(String recipient, String message) {
            System.out.println("Email to " + recipient + ": " + message);
        }
    }

    private static class SmsNotification implements Notification {
        @Override
        public void send(String recipient, String message) {
            System.out.println("SMS to " + recipient + ": " + message);
        }
    }

    private static class MessageFormatter {
        String format(String message) {
            return "[INFO] " + message;
        }

        String format(String level, String message) {
            return "[" + level.toUpperCase() + "] " + message;
        }
    }

    private static void notifyUser(
            Notification notification,
            String recipient,
            String message) {
        notification.send(recipient, message);
    }

    public static void main(String[] args) {
        MessageFormatter formatter = new MessageFormatter();

        // Compile-time polymorphism: Java selects an overloaded format method.
        System.out.println(formatter.format("Application started"));
        System.out.println(formatter.format("warning", "Storage is almost full"));

        // Runtime polymorphism: the actual object selects the overridden method.
        Notification notification = new EmailNotification();
        notifyUser(notification, "asha@example.com", "Your order was shipped.");

        notification = new SmsNotification();
        notifyUser(notification, "+91-9876543210", "Your OTP is 4821.");
    }
}
