package OOPs.Encapsulation;

/**
 * Encapsulation keeps an object's data private and allows access only through
 * methods that protect the object's rules.
 */
public class EncapsulationExample {

    private static class BankAccount {
        private final String accountNumber;
        private String ownerName;
        private double balance;

        BankAccount(String accountNumber, String ownerName, double openingBalance) {
            if (accountNumber == null || accountNumber.isBlank()) {
                throw new IllegalArgumentException("Account number is required.");
            }
            if (openingBalance < 0) {
                throw new IllegalArgumentException("Opening balance cannot be negative.");
            }
            this.accountNumber = accountNumber;
            changeOwnerName(ownerName);
            this.balance = openingBalance;
        }

        void deposit(double amount) {
            requirePositiveAmount(amount);
            balance += amount;
        }

        void withdraw(double amount) {
            requirePositiveAmount(amount);
            if (amount > balance) {
                throw new IllegalArgumentException("Insufficient balance.");
            }
            balance -= amount;
        }

        void changeOwnerName(String newOwnerName) {
            if (newOwnerName == null || newOwnerName.isBlank()) {
                throw new IllegalArgumentException("Owner name is required.");
            }
            ownerName = newOwnerName;
        }

        String getMaskedAccountNumber() {
            int visibleDigits = Math.min(4, accountNumber.length());
            return "*".repeat(accountNumber.length() - visibleDigits)
                    + accountNumber.substring(accountNumber.length() - visibleDigits);
        }

        String getOwnerName() {
            return ownerName;
        }

        double getBalance() {
            return balance;
        }

        private static void requirePositiveAmount(double amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be positive.");
            }
        }
    }

    public static void main(String[] args) {
        BankAccount account = new BankAccount("1234567890", "Asha", 1_000.00);
        account.deposit(500.00);
        account.withdraw(275.00);

        System.out.println("Account: " + account.getMaskedAccountNumber());
        System.out.println("Owner: " + account.getOwnerName());
        System.out.printf("Balance: Rs. %.2f%n", account.getBalance());

        try {
            account.withdraw(2_000.00);
        } catch (IllegalArgumentException exception) {
            System.out.println("Rejected transaction: " + exception.getMessage());
        }
    }
}
