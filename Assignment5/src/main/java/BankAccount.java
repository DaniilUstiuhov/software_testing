import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class BankAccount {

    public enum AccountType { SAVINGS, CHECKING }
    public enum Status      { ACTIVE, FROZEN, CLOSED }

    private final String accountNumber;
    private final String owner;
    private final AccountType type;
    private double balance;
    private double interestRate;
    private Status status;
    private final List<Transaction> history = new ArrayList<>();

    public BankAccount(String owner, AccountType type, double initialBalance) {
        if (owner == null || owner.isBlank())
            throw new IllegalArgumentException("Owner name cannot be empty");
        if (initialBalance < 0)
            throw new IllegalArgumentException("Initial balance cannot be negative");

        this.accountNumber = "ACC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.owner         = owner;
        this.type          = type;
        this.balance       = initialBalance;
        this.status        = Status.ACTIVE;
        this.interestRate  = (type == AccountType.SAVINGS) ? 0.03 : 0.0;

        if (initialBalance > 0)
            record(Transaction.Type.DEPOSIT, initialBalance, "Account opened with initial balance");
    }

    public BankAccount(String owner, AccountType type) {
        this(owner, type, 0);
    }

    // ── Core operations ──────────────────────────────────────────────────────

    public double deposit(double amount) {
        requireActive();
        if (amount <= 0) throw new IllegalArgumentException("Deposit amount must be positive");
        balance += amount;
        record(Transaction.Type.DEPOSIT, amount, "Deposit");
        return balance;
    }

    public double withdraw(double amount) {
        requireActive();
        if (amount <= 0)    throw new IllegalArgumentException("Withdrawal amount must be positive");
        if (amount > balance) throw new IllegalArgumentException("Insufficient funds");
        balance -= amount;
        record(Transaction.Type.WITHDRAWAL, amount, "Withdrawal");
        return balance;
    }

    public void transfer(BankAccount target, double amount) {
        requireActive();
        target.requireActive();
        if (amount <= 0)      throw new IllegalArgumentException("Transfer amount must be positive");
        if (amount > balance) throw new IllegalArgumentException("Insufficient funds for transfer");

        balance -= amount;
        history.add(new Transaction(Transaction.Type.TRANSFER_OUT, amount, balance,
                "Transfer to " + target.owner));

        target.balance += amount;
        target.history.add(new Transaction(Transaction.Type.TRANSFER_IN, amount, target.balance,
                "Transfer from " + owner));
    }

    // ── Interest ─────────────────────────────────────────────────────────────

    public double applyInterest() {
        requireActive();
        if (type != AccountType.SAVINGS)
            throw new IllegalStateException("Interest applies only to savings accounts");
        double interest = balance * interestRate;
        balance += interest;
        record(Transaction.Type.INTEREST, interest,
                String.format("Interest %.0f%%", interestRate * 100));
        return interest;
    }

    public void setInterestRate(double rate) {
        if (rate < 0 || rate > 1) throw new IllegalArgumentException("Rate must be between 0 and 1");
        this.interestRate = rate;
    }

    // ── Account status ───────────────────────────────────────────────────────

    public void freeze() {
        if (status == Status.CLOSED) throw new IllegalStateException("Cannot freeze a closed account");
        status = Status.FROZEN;
    }

    public void unfreeze() {
        if (status == Status.CLOSED) throw new IllegalStateException("Cannot unfreeze a closed account");
        status = Status.ACTIVE;
    }

    public void close() {
        if (balance > 0) throw new IllegalStateException("Cannot close account with remaining balance");
        status = Status.CLOSED;
    }

    // ── Reporting ────────────────────────────────────────────────────────────

    public double getTotalDeposited() {
        return history.stream()
                .filter(t -> t.getType() == Transaction.Type.DEPOSIT
                          || t.getType() == Transaction.Type.TRANSFER_IN)
                .mapToDouble(Transaction::getAmount).sum();
    }

    public double getTotalWithdrawn() {
        return history.stream()
                .filter(t -> t.getType() == Transaction.Type.WITHDRAWAL
                          || t.getType() == Transaction.Type.TRANSFER_OUT)
                .mapToDouble(Transaction::getAmount).sum();
    }

    public void printStatement() {
        System.out.println("========================================");
        System.out.println("  BANK ACCOUNT STATEMENT");
        System.out.println("========================================");
        System.out.printf("  Account : %s%n", accountNumber);
        System.out.printf("  Owner   : %s%n", owner);
        System.out.printf("  Type    : %s%n", type);
        System.out.printf("  Status  : %s%n", status);
        System.out.printf("  Balance : %.2f%n", balance);
        System.out.println("----------------------------------------");
        history.forEach(System.out::println);
        System.out.println("========================================");
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public String      getAccountNumber()     { return accountNumber; }
    public String      getOwner()             { return owner; }
    public AccountType getType()              { return type; }
    public double      getBalance()           { return balance; }
    public Status      getStatus()            { return status; }
    public double      getInterestRate()      { return interestRate; }

    public List<Transaction> getTransactionHistory() {
        return Collections.unmodifiableList(history);
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    private void requireActive() {
        if (status == Status.FROZEN) throw new IllegalStateException("Account is frozen");
        if (status == Status.CLOSED) throw new IllegalStateException("Account is closed");
    }

    private void record(Transaction.Type type, double amount, String desc) {
        history.add(new Transaction(type, amount, balance, desc));
    }
}
