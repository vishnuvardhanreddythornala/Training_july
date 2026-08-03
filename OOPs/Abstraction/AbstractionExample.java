package OOPs.Abstraction;

/**
 * Abstraction exposes a simple operation while hiding the implementation steps.
 * Callers use processPayment without knowing how each payment provider works.
 */
public class AbstractionExample {

    private abstract static class PaymentMethod {
        final void processPayment(double amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException("Payment amount must be positive.");
            }

            System.out.printf("Processing Rs. %.2f...%n", amount);
            authenticate();
            pay(amount);
            System.out.println("Payment completed.\n");
        }

        protected void authenticate() {
            System.out.println("Performing standard customer authentication.");
        }

        protected abstract void pay(double amount);
    }

    private static class UpiPayment extends PaymentMethod {
        private final String upiId;

        UpiPayment(String upiId) {
            this.upiId = upiId;
        }

        @Override
        protected void pay(double amount) {
            System.out.printf("Debited Rs. %.2f using UPI ID %s.%n", amount, upiId);
        }
    }

    private static class CardPayment extends PaymentMethod {
        private final String lastFourDigits;

        CardPayment(String lastFourDigits) {
            this.lastFourDigits = lastFourDigits;
        }

        @Override
        protected void authenticate() {
            System.out.println("Verifying the card number and CVV.");
        }

        @Override
        protected void pay(double amount) {
            System.out.printf("Charged Rs. %.2f to card ending in %s.%n",
                    amount, lastFourDigits);
        }
    }

    public static void main(String[] args) {
        PaymentMethod upi = new UpiPayment("asha@upi");
        PaymentMethod card = new CardPayment("4242");

        upi.processPayment(750.00);
        card.processPayment(1_250.00);
    }
}
