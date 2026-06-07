import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

    public enum Type {
        DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT, INTEREST
    }

    private final Type type;
    private final double amount;
    private final double balanceAfter;
    private final String description;
    private final LocalDateTime timestamp;

    public Transaction(Type type, double amount, double balanceAfter, String description) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    public Type getType()           { return type; }
    public double getAmount()       { return amount; }
    public double getBalanceAfter() { return balanceAfter; }
    public String getDescription()  { return description; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("[%s] %-14s %8.2f | Balance: %10.2f | %s",
                timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                type, amount, balanceAfter, description);
    }
}
