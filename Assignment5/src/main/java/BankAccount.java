import java.util.ArrayList;
import java.util.List;

public class BankAccount {

    private final String owner;
    private double balance;
    private final List<String> transactions = new ArrayList<>();

    public BankAccount(String owner, double initialBalance) {
        if (initialBalance < 0) throw new IllegalArgumentException("Initial balance cannot be negative");
        this.owner = owner;
        this.balance = initialBalance;
        if (initialBalance > 0) transactions.add("Initial deposit: +" + initialBalance);
    }

    public BankAccount(String owner) {
        this(owner, 0);
    }

    public double deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit amount must be positive");
        balance += amount;
        transactions.add("Deposit: +" + amount);
        return balance;
    }

    public double withdraw(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Withdrawal amount must be positive");
        if (amount > balance) throw new IllegalArgumentException("Insufficient funds");
        balance -= amount;
        transactions.add("Withdrawal: -" + amount);
        return balance;
    }

    public double getBalance() { return balance; }
    public String getOwner()   { return owner; }

    public List<String> getTransactionHistory() {
        return new ArrayList<>(transactions);
    }
}
