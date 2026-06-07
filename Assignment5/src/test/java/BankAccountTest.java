import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    private BankAccount savings;
    private BankAccount checking;

    @BeforeEach
    void setUp() {
        savings  = new BankAccount("Daniil", BankAccount.AccountType.SAVINGS,  1000);
        checking = new BankAccount("Daniil", BankAccount.AccountType.CHECKING, 500);
    }

    // ── Account creation ─────────────────────────────────────────────────────

    @Test void initialBalanceIsSet()            { assertEquals(1000, savings.getBalance()); }
    @Test void ownerIsSet()                     { assertEquals("Daniil", savings.getOwner()); }
    @Test void typeIsSet()                      { assertEquals(BankAccount.AccountType.SAVINGS, savings.getType()); }
    @Test void statusIsActiveByDefault()        { assertEquals(BankAccount.Status.ACTIVE, savings.getStatus()); }
    @Test void accountNumberIsGenerated()       { assertNotNull(savings.getAccountNumber()); }
    @Test void accountNumberStartsWithACC()     { assertTrue(savings.getAccountNumber().startsWith("ACC-")); }
    @Test void zeroInitialBalanceAllowed()      { assertEquals(0, new BankAccount("Test", BankAccount.AccountType.CHECKING).getBalance()); }
    @Test void negativeInitialBalanceThrows()   { assertThrows(IllegalArgumentException.class, () -> new BankAccount("X", BankAccount.AccountType.SAVINGS, -1)); }
    @Test void emptyOwnerThrows()               { assertThrows(IllegalArgumentException.class, () -> new BankAccount("", BankAccount.AccountType.SAVINGS)); }
    @Test void savingsHasInterestRate()         { assertEquals(0.03, savings.getInterestRate()); }
    @Test void checkingHasZeroInterestRate()    { assertEquals(0.0,  checking.getInterestRate()); }

    // ── Deposit ──────────────────────────────────────────────────────────────

    @Test void depositIncreasesBalance()        { savings.deposit(500);  assertEquals(1500, savings.getBalance()); }
    @Test void depositReturnsNewBalance()        { assertEquals(1200, savings.deposit(200)); }
    @Test void multipleDepositsAccumulate()     { savings.deposit(100); savings.deposit(200); assertEquals(1300, savings.getBalance()); }
    @Test void depositZeroThrows()              { assertThrows(IllegalArgumentException.class, () -> savings.deposit(0)); }
    @Test void depositNegativeThrows()          { assertThrows(IllegalArgumentException.class, () -> savings.deposit(-50)); }

    // ── Withdraw ─────────────────────────────────────────────────────────────

    @Test void withdrawDecreasesBalance()       { savings.withdraw(300); assertEquals(700, savings.getBalance()); }
    @Test void withdrawReturnsNewBalance()       { assertEquals(600, savings.withdraw(400)); }
    @Test void withdrawEntireBalance()          { savings.withdraw(1000); assertEquals(0, savings.getBalance()); }
    @Test void withdrawMoreThanBalanceThrows()  { assertThrows(IllegalArgumentException.class, () -> savings.withdraw(9999)); }
    @Test void withdrawZeroThrows()             { assertThrows(IllegalArgumentException.class, () -> savings.withdraw(0)); }
    @Test void withdrawNegativeThrows()         { assertThrows(IllegalArgumentException.class, () -> savings.withdraw(-10)); }

    // ── Transfer ─────────────────────────────────────────────────────────────

    @Test
    void transferMovesMoneyBetweenAccounts() {
        savings.transfer(checking, 200);
        assertEquals(800, savings.getBalance());
        assertEquals(700, checking.getBalance());
    }

    @Test
    void transferRecordedInBothHistories() {
        savings.transfer(checking, 100);
        assertTrue(savings.getTransactionHistory().stream()
                .anyMatch(t -> t.getType() == Transaction.Type.TRANSFER_OUT));
        assertTrue(checking.getTransactionHistory().stream()
                .anyMatch(t -> t.getType() == Transaction.Type.TRANSFER_IN));
    }

    @Test void transferExceedingBalanceThrows() { assertThrows(IllegalArgumentException.class, () -> savings.transfer(checking, 9999)); }
    @Test void transferZeroThrows()             { assertThrows(IllegalArgumentException.class, () -> savings.transfer(checking, 0)); }

    // ── Interest ─────────────────────────────────────────────────────────────

    @Test
    void applyInterestIncreasesBalance() {
        double interest = savings.applyInterest();
        assertEquals(30.0, interest, 0.001);
        assertEquals(1030.0, savings.getBalance(), 0.001);
    }

    @Test void interestOnCheckingThrows()       { assertThrows(IllegalStateException.class, () -> checking.applyInterest()); }

    @Test
    void customInterestRateApplied() {
        savings.setInterestRate(0.05);
        double interest = savings.applyInterest();
        assertEquals(50.0, interest, 0.001);
    }

    @Test void negativeInterestRateThrows()     { assertThrows(IllegalArgumentException.class, () -> savings.setInterestRate(-0.1)); }
    @Test void interestRateAboveOneThrows()     { assertThrows(IllegalArgumentException.class, () -> savings.setInterestRate(1.5)); }

    // ── Account status ───────────────────────────────────────────────────────

    @Test
    void frozenAccountCannotDeposit() {
        savings.freeze();
        assertThrows(IllegalStateException.class, () -> savings.deposit(100));
    }

    @Test
    void frozenAccountCannotWithdraw() {
        savings.freeze();
        assertThrows(IllegalStateException.class, () -> savings.withdraw(100));
    }

    @Test
    void frozenAccountCannotTransfer() {
        savings.freeze();
        assertThrows(IllegalStateException.class, () -> savings.transfer(checking, 100));
    }

    @Test
    void unfreezeRestoresAccess() {
        savings.freeze();
        savings.unfreeze();
        assertDoesNotThrow(() -> savings.deposit(100));
    }

    @Test
    void closeAccountWithZeroBalance() {
        savings.withdraw(1000);
        savings.close();
        assertEquals(BankAccount.Status.CLOSED, savings.getStatus());
    }

    @Test void closeAccountWithBalanceThrows()  { assertThrows(IllegalStateException.class, () -> savings.close()); }
    @Test void closedAccountCannotDeposit()     { savings.withdraw(1000); savings.close(); assertThrows(IllegalStateException.class, () -> savings.deposit(1)); }
    @Test void freezeClosedAccountThrows()      { savings.withdraw(1000); savings.close(); assertThrows(IllegalStateException.class, () -> savings.freeze()); }

    // ── Transaction history ──────────────────────────────────────────────────

    @Test
    void initialDepositInHistory() {
        assertEquals(1, savings.getTransactionHistory().size());
        assertEquals(Transaction.Type.DEPOSIT, savings.getTransactionHistory().get(0).getType());
    }

    @Test
    void historyGrowsWithOperations() {
        savings.deposit(100);
        savings.withdraw(50);
        assertEquals(3, savings.getTransactionHistory().size());
    }

    @Test
    void historyIsImmutable() {
        assertThrows(UnsupportedOperationException.class,
                () -> savings.getTransactionHistory().add(null));
    }

    @Test
    void totalDepositedCalculated() {
        savings.deposit(500);
        assertEquals(1500, savings.getTotalDeposited(), 0.001);
    }

    @Test
    void totalWithdrawnCalculated() {
        savings.withdraw(200);
        savings.withdraw(300);
        assertEquals(500, savings.getTotalWithdrawn(), 0.001);
    }
}
