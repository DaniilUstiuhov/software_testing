import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    private BankAccount account;

    @BeforeEach
    void setUp() {
        account = new BankAccount("Daniil", 1000);
    }

    // ── Basic state ──────────────────────────────────────────────────────────

    @Test
    void initialBalanceIsSet() {
        assertEquals(1000, account.getBalance());
    }

    @Test
    void ownerNameIsCorrect() {
        assertEquals("Daniil", account.getOwner());
    }

    @Test
    void defaultConstructorStartsWithZeroBalance() {
        BankAccount empty = new BankAccount("Daniil");
        assertEquals(0, empty.getBalance());
    }

    // ── Deposit ──────────────────────────────────────────────────────────────

    @Test
    void depositIncreasesBalance() {
        account.deposit(500);
        assertEquals(1500, account.getBalance());
    }

    @Test
    void depositReturnsNewBalance() {
        double result = account.deposit(200);
        assertEquals(1200, result);
    }

    @Test
    void depositZeroThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> account.deposit(0));
    }

    @Test
    void depositNegativeThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-100));
    }

    // ── Withdraw ─────────────────────────────────────────────────────────────

    @Test
    void withdrawDecreasesBalance() {
        account.withdraw(300);
        assertEquals(700, account.getBalance());
    }

    @Test
    void withdrawReturnsNewBalance() {
        double result = account.withdraw(400);
        assertEquals(600, result);
    }

    @Test
    void withdrawEntireBalanceLeavesZero() {
        account.withdraw(1000);
        assertEquals(0, account.getBalance());
    }

    @Test
    void withdrawMoreThanBalanceThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(9999));
    }

    @Test
    void withdrawZeroThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(0));
    }

    @Test
    void withdrawNegativeThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-50));
    }

    // ── Transaction history ──────────────────────────────────────────────────

    @Test
    void initialDepositAppearsInHistory() {
        assertTrue(account.getTransactionHistory().get(0).contains("Initial deposit"));
    }

    @Test
    void depositAppearsInHistory() {
        account.deposit(250);
        assertTrue(account.getTransactionHistory().stream().anyMatch(t -> t.contains("Deposit")));
    }

    @Test
    void withdrawalAppearsInHistory() {
        account.withdraw(100);
        assertTrue(account.getTransactionHistory().stream().anyMatch(t -> t.contains("Withdrawal")));
    }

    @Test
    void historyRecordsMultipleTransactions() {
        account.deposit(100);
        account.withdraw(50);
        account.deposit(200);
        assertEquals(4, account.getTransactionHistory().size()); // initial + 3 ops
    }

    // ── Edge cases ───────────────────────────────────────────────────────────

    @Test
    void negativeInitialBalanceThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new BankAccount("Daniil", -500));
    }

    @Test
    void historyIsImmutable() {
        account.getTransactionHistory().add("HACKED");
        assertEquals(1, account.getTransactionHistory().size());
    }
}
