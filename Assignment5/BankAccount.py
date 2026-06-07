class BankAccount:
    def __init__(self, owner, initial_balance=0):
        if initial_balance < 0:
            raise ValueError("Initial balance cannot be negative")
        self.owner = owner
        self._balance = initial_balance
        self._transactions = []
        if initial_balance > 0:
            self._transactions.append(f"Initial deposit: +{initial_balance}")

    def deposit(self, amount):
        if amount <= 0:
            raise ValueError("Deposit amount must be positive")
        self._balance += amount
        self._transactions.append(f"Deposit: +{amount}")
        return self._balance

    def withdraw(self, amount):
        if amount <= 0:
            raise ValueError("Withdrawal amount must be positive")
        if amount > self._balance:
            raise ValueError("Insufficient funds")
        self._balance -= amount
        self._transactions.append(f"Withdrawal: -{amount}")
        return self._balance

    def get_balance(self):
        return self._balance

    def get_history(self):
        return list(self._transactions)


if __name__ == "__main__":
    account = BankAccount("Daniil", 1000)
    account.deposit(500)
    account.withdraw(200)
    print(f"Owner:   {account.owner}")
    print(f"Balance: {account.get_balance()}")
    print("History:")
    for entry in account.get_history():
        print(f"  {entry}")
