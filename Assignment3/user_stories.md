# Assignment 3 — Zoo Management System: User Stories

---

## User Story 1 — Zookeeper: Budget Overview

| Title | Priority | Estimate |
|-------|----------|----------|
| View Animal Feed Budget Overview | High | 3 points |

**User Story:**

As a zookeeper,
I want to see a clear breakdown of my animal's monthly feed budget
so that I can track spending and plan orders without exceeding the allocated funds.

**Acceptance Criteria:**

Given a zookeeper is logged in and selects an animal they are responsible for,
When they open the budget overview page,
Then the system displays the total monthly budget, amount already spent, amount reserved for pending deliveries, and amount not yet spent — along with a graphical representation of these figures.

---

## User Story 2 — Zoo Director: Handle Budget Increase Requests

| Title | Priority | Estimate |
|-------|----------|----------|
| Review and Respond to Budget Increase Requests | High | 5 points |

**User Story:**

As a zoo director,
I want to receive budget increase requests from zookeepers with relevant context such as feed stock levels and average order cost,
so that I can make informed decisions about granting or denying additional funds.

**Acceptance Criteria:**

Given a zookeeper has submitted a budget increase request with a short statement,
When the zoo director opens the request notification,
Then the system shows the request message, a link to the animal's current feed stock, the average cost per order, and options to either grant the request (entering the approved amount) or deny it (with a written statement) — with an automatic notification sent to all affected zookeepers in both cases.

---

## User Story 3 — Secretary: Manage Employee Holiday Requests

| Title | Priority | Estimate |
|-------|----------|----------|
| Approve or Deny Employee Holiday Requests | Medium | 3 points |

**User Story:**

As a secretary,
I want to review employee holiday requests and check for scheduling conflicts,
so that the zoo always has enough qualified staff available for every task.

**Acceptance Criteria:**

Given an employee has submitted a holiday request,
When the secretary opens the request in the system,
Then the system shows any scheduling conflicts with other employees who share the same tasks, the employee's remaining unused holidays, and allows the secretary to approve or deny the request — sending an automatic notification to the employee with the decision and, in case of denial, a short statement explaining the reason.
