# Customer Loyalty Program Management System

**Subject:** Object Oriented Programming using Java
**Branch:** B.Tech CSE (Data Science), 2nd Year
**Type:** Mini Project (Console based)

---

## 1. Problem Statement

Organizations require structured management of customer rewards and engagement initiatives. Design a Java based loyalty management platform.

## 2. What Our Project Does (In Simple Words)

Think of it like the points system at a supermarket or a coffee shop. When a customer buys something, they get points. When they have enough points, they can exchange them for rewards like coupons or gifts. Customers who spend more move to a higher level (tier) and get more points on every purchase. The shop can also run special offers (campaigns) like "Diwali Dhamaka" where everyone gets extra points.

Our system does all of this:

| Feature | Description |
|---|---|
| Customer Registration | New customer gets an ID (starting from 101) and 50 welcome bonus points |
| Earn Points | 1 point for every Rs.100 spent, multiplied by the tier multiplier |
| Tier System | Silver (1x) → Gold (1.5x) → Platinum (2x), upgrade is automatic |
| Reward Catalog | List of rewards with points required and stock |
| Redeem Reward | Customer exchanges points for a reward (checks points and stock) |
| Campaigns | Admin can start/stop a bonus campaign (e.g. 2x points) |
| Transaction History | Every earn and redeem is saved with date |
| Input Validation | Phone number must be 10 digits, wrong menu input is handled |

---

## 3. Folder Structure

The project is divided into two parts: **Backend** (logic and data) and **Frontend** (menu that the user sees).

```
LoyaltyProgram/                      ← Full project (backend + frontend)
│
├── Main.java                         ← FRONTEND: menu, takes input from user
│
├── LoyaltyOperations.java            ← BACKEND: interface
├── LoyaltyManager.java               ← BACKEND: main logic
├── Customer.java                     ← BACKEND: abstract parent class
├── SilverCustomer.java               ← BACKEND: child class
├── GoldCustomer.java                 ← BACKEND: child class
├── PlatinumCustomer.java             ← BACKEND: child class
├── Reward.java                       ← BACKEND: reward model
├── Transaction.java                  ← BACKEND: transaction model
├── Campaign.java                     ← BACKEND: campaign model
└── InsufficientPointsException.java  ← BACKEND: custom exception

LoyaltyBackend/                      ← Same as above but WITHOUT Main.java
```

**Why we split it?** The backend does not print menus or read input. It only does the work. The frontend only talks to the user and calls backend methods. So if tomorrow we want a GUI (Swing / JavaFX) instead of a console menu, we only change `Main.java`. The backend stays the same. This is called **separation of concerns**.

---

## 4. How to Run

### Step 1: Install Java

You need JDK 8 or above (we used JDK 21). Check if it is installed:

```
java -version
javac -version
```

If not installed, download JDK from https://www.oracle.com/java/technologies/downloads/ or https://adoptium.net and install it.

### Step 2: Run from Command Prompt / Terminal

Open the terminal inside the `LoyaltyProgram` folder and run:

```
javac *.java
java Main
```

`javac *.java` compiles all the Java files into `.class` files.
`java Main` runs the program, because `Main` has the `main()` method.

### Step 3 (Optional): Run in an IDE

**Eclipse / IntelliJ IDEA**
1. Create a new Java Project.
2. Copy all `.java` files into the `src` folder.
3. Right click `Main.java` → Run.

**VS Code**
1. Install the "Extension Pack for Java".
2. Open the `LoyaltyProgram` folder.
3. Open `Main.java` and click "Run" above the `main` method.

### Running the Backend Only

The `LoyaltyBackend` folder has no `main()` method, so it cannot run alone. It can only be compiled to check that there are no errors:

```
javac *.java
```

To use it, any frontend just creates an object of `LoyaltyManager` and calls its methods (see Section 7).

---

## 5. Sample Output

```
==========================================
   CUSTOMER LOYALTY PROGRAM MANAGEMENT
==========================================

1. Register Customer
2. Make Purchase (Earn Points)
...
0. Exit
Enter your choice: 1
Enter name: Rahul Sharma
Enter phone number: 9876543210
Customer registered successfully! Your ID is 101
You got 50 welcome bonus points :)

Enter your choice: 2
Enter customer ID: 101
Enter purchase amount: 12000
Purchase done. Points earned: 120 | Total points: 170
*** Congratulations! Rahul Sharma is upgraded to GOLD ***

Enter your choice: 4
Enter customer ID: 101
Enter reward ID: 3
Error: Not enough points! Available: 170, Required: 500

Enter your choice: 9
Enter campaign name: Diwali Dhamaka
Enter bonus multiplier (eg 2): 2
Campaign 'Diwali Dhamaka' started with 2.0x bonus points!

Enter your choice: 2
Enter customer ID: 101
Enter purchase amount: 5000
Campaign 'Diwali Dhamaka' applied!
Purchase done. Points earned: 150 | Total points: 320

Enter your choice: 6
Enter customer ID: 101
---- Transaction History of 101 ----
T1 | 2026-10-23 | EARN | 50 pts | Welcome Bonus
T2 | 2026-10-23 | EARN | 120 pts | Purchase of Rs.12000.0
T3 | 2026-10-23 | EARN | 150 pts | Purchase of Rs.5000.0
```

**Points check for the 5000 purchase:** 5000 / 100 = 50 → Gold multiplier 1.5 → 75 → Campaign 2x → **150 points**.

---

## 6. Explanation of Every Class

### 6.1 `Customer.java` (Abstract Class)

This is the **parent class** of all customers.

**Fields (all private):** `customerId`, `name`, `phone`, `points`, `totalSpent`

**Methods:**

| Method | What it does |
|---|---|
| `getTier()` | **abstract**, each child returns its own tier name |
| `getMultiplier()` | **abstract**, each child returns its own point multiplier |
| `addPoints(int p)` | Adds points |
| `deductPoints(int p)` | Removes points, **throws** `InsufficientPointsException` if points are less |
| `addSpent(double amount)` | Adds to total spending (used for tier upgrade) |
| `copyData(Customer old)` | Copies points and spending from old object (used during upgrade) |
| `display()` | Prints customer details |

**Why abstract?** A customer must always be Silver, Gold or Platinum. There is no "plain" customer. So we make `Customer` abstract, and Java will not allow `new Customer(...)`. Common things (name, phone, points) are written once in the parent, and the different things (tier, multiplier) are left to the child classes.

**Why `copyData()` is `protected`?** Only child classes should use it. Outside classes should not be able to copy points into a customer directly.

### 6.2 `SilverCustomer`, `GoldCustomer`, `PlatinumCustomer`

These three **extend** `Customer` and **override** `getTier()` and `getMultiplier()`.

| Class | Tier | Multiplier | When customer reaches it |
|---|---|---|---|
| `SilverCustomer` | SILVER | 1.0 | At registration (default) |
| `GoldCustomer` | GOLD | 1.5 | Total spending ≥ Rs.10,000 |
| `PlatinumCustomer` | PLATINUM | 2.0 | Total spending ≥ Rs.50,000 |

`GoldCustomer` and `PlatinumCustomer` have a special constructor that takes an old `Customer` object. It calls `super(...)` with the same ID, name and phone, then calls `copyData(old)` so points and spending are not lost.

**Why separate classes and not just a `String tier` variable?** With a string we would need `if (tier.equals("GOLD"))` checks everywhere. With separate classes, each class knows its own multiplier. If we want to add a "Diamond" tier later, we just add one new class. This is the real use of **inheritance and polymorphism**.

### 6.3 `Reward.java`

Stores one reward: `rewardId`, `rewardName`, `pointsNeeded`, `stock`.
`reduceStock()` reduces stock by 1 when someone redeems it.

Four default rewards are added in the `LoyaltyManager` constructor:

| ID | Reward | Points | Stock |
|---|---|---|---|
| 1 | Rs.100 Discount Coupon | 100 | 50 |
| 2 | Free Coffee Mug | 250 | 20 |
| 3 | Movie Ticket | 500 | 10 |
| 4 | Bluetooth Earphones | 1500 | 5 |

### 6.4 `Transaction.java`

Stores the history of every earn or redeem: `transId`, `customerId`, `type` (EARN / REDEEM), `points`, `details`, `date`.

**Static counter:** `private static int counter = 1;`
A static variable belongs to the class, not to the object. So it is shared by all transactions. Every time a new `Transaction` is created, it takes `counter++` as its ID. That is how IDs come automatically as T1, T2, T3...

**Date:** `LocalDate.now()` from the `java.time` package gives today's date.

### 6.5 `Campaign.java`

Represents an engagement offer like "Diwali Dhamaka".
Fields: `campaignName`, `bonusMultiplier`, `active`.
`endCampaign()` sets `active = false`.

Only one campaign runs at a time. While it is active, every purchase gets extra points.

### 6.6 `InsufficientPointsException.java` (User Defined Exception)

```java
public class InsufficientPointsException extends Exception
```

It extends `Exception`, so it is a **checked exception**. That means whoever calls `deductPoints()` **must** handle it with `try-catch`, otherwise the code will not compile.

**Why custom exception?** The error has a clear meaning ("not enough points") and gives a proper message. It is better than returning `false` or `-1`, because the caller cannot ignore it.

### 6.7 `LoyaltyOperations.java` (Interface)

```java
void registerCustomer(String name, String phone);
void makePurchase(int customerId, double amount);
void redeemReward(int customerId, int rewardId);
void showHistory(int customerId);
```

**Why interface?** It is like a contract. It says "any loyalty system must have these 4 operations". `LoyaltyManager` implements it. If later someone writes a database-based version, it also implements the same interface and the frontend does not need to change.

### 6.8 `LoyaltyManager.java` (Main Logic, Backend Brain)

This class implements `LoyaltyOperations` and holds all the data:

```java
ArrayList<Customer> customers
ArrayList<Reward> rewards
ArrayList<Transaction> transactions
Campaign currentCampaign
```

**Constants:** `GOLD_LIMIT = 10000` and `PLATINUM_LIMIT = 50000` are `static final`, so they cannot be changed and are the same for all objects.

**Why ArrayList and not array?** We don't know in advance how many customers will register. An array has a fixed size, but an ArrayList grows automatically.

**Why `ArrayList<Customer>` can store Silver, Gold and Platinum objects together?** Because all of them are child classes of `Customer`. A parent reference can hold a child object. This is **upcasting**.

### 6.9 `Main.java` (Frontend)

Uses `Scanner` to read input and a `do-while` loop with `switch-case` to show the menu again and again until the user enters 0. It does not contain any business logic. It only calls `LoyaltyManager` methods.

**Why `do-while`?** The menu must be shown at least once, so `do-while` is the right loop.

**Why `sc.nextLine()` after `sc.nextInt()`?** `nextInt()` reads the number but leaves the Enter key (`\n`) in the input. If we don't clear it, the next `nextLine()` (for name) will read an empty line.

**Why `try-catch (InputMismatchException)`?** If the user types `abc` instead of a number, the program would crash. We catch it, print "Please enter numbers only!", clear the wrong input and show the menu again.

---

## 7. How Each Feature Works (Step by Step)

### 7.1 Register Customer: `registerCustomer(name, phone)`

1. Check that the phone has 10 digits. If not, print an error and **return early**.
2. Create `new SilverCustomer(nextCustomerId, name, phone)`. Every new customer starts as Silver.
3. Add it to the `customers` list.
4. Give 50 welcome points and save a transaction "Welcome Bonus".
5. Increase `nextCustomerId` (101 → 102 → 103...).

### 7.2 Make Purchase: `makePurchase(customerId, amount)`

1. Find the customer using `findCustomer(id)` (loops through the list and matches the ID). If not found, return.
2. Check that amount > 0.
3. Calculate points:
   ```
   points = (amount / 100) × tier multiplier × campaign multiplier (if active)
   ```
4. Convert to `int` (decimal part is removed).
5. Add the amount to total spent, add the points, save an EARN transaction.
6. Call `checkUpgrade(c)`.

Here, `c.getMultiplier()` is called on a `Customer` reference, but Java runs the child class version (1.0, 1.5 or 2.0) depending on the actual object. **This is runtime polymorphism (dynamic method dispatch).**

### 7.3 Tier Upgrade: `checkUpgrade(c)`

1. Uses `instanceof` to check what type of customer it is right now.
2. If Silver and spending ≥ 50,000 → make Platinum directly.
3. If Silver and spending ≥ 10,000 → make Gold.
4. If Gold and spending ≥ 50,000 → make Platinum.
5. If no upgrade, return.
6. Otherwise, find the position of the old object in the list using `indexOf()` and **replace** it with the new object using `set()`.

**Why replace the object?** In Java, an object's class cannot change after it is created. A `SilverCustomer` object can never become a `GoldCustomer` object. So we create a new `GoldCustomer` with the same data (using the copy constructor) and put it in place of the old one.

### 7.4 Redeem Reward: `redeemReward(customerId, rewardId)`

1. Find the customer and the reward. If either is not found, return.
2. If stock is 0, print "out of stock" and return.
3. Inside `try`: call `deductPoints()`. If points are enough, reduce stock and save a REDEEM transaction.
4. If points are not enough, `deductPoints()` throws `InsufficientPointsException` and the `catch` block prints the error message.

### 7.5 Campaign: `startCampaign()` / `stopCampaign()`

`startCampaign()` creates a new `Campaign` object and stores it in `currentCampaign`. `stopCampaign()` calls `endCampaign()` so it becomes inactive. In `makePurchase()` we check `currentCampaign != null && currentCampaign.isActive()` before applying the bonus.

### 7.6 Transaction History: `showHistory(customerId)`

Loops through all transactions and prints the ones with a matching customer ID. If none are found, prints "No transactions found."

---

## 8. OOP Concepts Used (Most Important for Viva)

| Concept | Where in our code | Why we used it |
|---|---|---|
| **Class & Object** | Every `.java` file is a class; `new SilverCustomer(...)`, `new Reward(...)` | To model real world things like customer and reward |
| **Encapsulation** | All fields are `private`, accessed using getters | Data is protected. No one can directly do `points = 99999` |
| **Abstraction** | `abstract class Customer`, `interface LoyaltyOperations` | Hide details and show only what is needed |
| **Inheritance** | `SilverCustomer extends Customer` etc. | Reuse common code, write name/phone/points only once |
| **Polymorphism (Runtime)** | `getTier()` and `getMultiplier()` overridden; `c.display()` in `showAllCustomers()` | Same method call gives different result based on object |
| **Method Overriding** | `@Override` in all three tier classes | Child gives its own version of parent's method |
| **Constructor Overloading** | `GoldCustomer` has two constructors | One for new customer, one for upgrade from old customer |
| **`super` keyword** | `super(id, name, phone)` in child constructors | To call parent's constructor |
| **`this` keyword** | `this.name = name` in constructors | To separate field from parameter with same name |
| **Interface** | `LoyaltyManager implements LoyaltyOperations` | Contract of what the system must do |
| **Exception Handling** | `try-catch` in `redeemReward()` and `Main` | Program should not crash on errors |
| **Custom Exception** | `InsufficientPointsException extends Exception` | Meaningful error for our own case |
| **`throws` / `throw`** | `deductPoints() throws ...`, `throw new ...` | `throw` creates the error, `throws` tells caller to handle it |
| **Static** | `static int counter` in `Transaction`, `static final` limits | Shared among all objects / constant values |
| **`final`** | `GOLD_LIMIT`, `PLATINUM_LIMIT` | Values cannot be changed |
| **Upcasting** | `Customer c = new SilverCustomer(...)` | Store all tiers in one `ArrayList<Customer>` |
| **`instanceof`** | `checkUpgrade()` | Check actual type of object at runtime |
| **Collections** | `ArrayList` | Dynamic size list |
| **Access Modifiers** | `private`, `protected`, `public` | Control who can access what |

---

## 9. Flow of the Program

```
           User
            │
            ▼
   Main.java (Frontend)
   - shows menu
   - reads input
            │  calls methods
            ▼
   LoyaltyManager (Backend)  ── implements ──▶  LoyaltyOperations
   - customers list                               (interface)
   - rewards list
   - transactions list
   - currentCampaign
            │  uses
            ▼
   Customer (abstract) ◀── Silver / Gold / Platinum
   Reward, Transaction, Campaign
   InsufficientPointsException
```

---

## 10. Possible Viva Questions & Answers

**Q1. What is the difference between abstract class and interface? Why did you use both?**
An abstract class can have normal methods, variables and constructors along with abstract methods. An interface only defines what methods must be there (a contract). We used an abstract class for `Customer` because all customers share common fields and methods. We used an interface for `LoyaltyOperations` because it only defines what operations the system must support.

**Q2. Where is polymorphism in your project?**
In `getTier()` and `getMultiplier()`. They are overridden in each tier class. When we call `c.getMultiplier()` on a `Customer` reference, Java decides at runtime which version to run based on the actual object. This is runtime polymorphism.

**Q3. Can we create an object of `Customer` class?**
No, because it is abstract. We can only create objects of its child classes.

**Q4. How does a customer get upgraded?**
After every purchase, `checkUpgrade()` checks total spending. If it crosses the limit, we create a new object of the higher tier using the copy constructor (so points are not lost) and replace the old object in the ArrayList.

**Q5. Why didn't you just change a variable for the tier?**
Java objects cannot change their class. Also, using separate classes means each tier has its own behaviour, and adding a new tier is easy.

**Q6. Is your exception checked or unchecked?**
Checked, because it extends `Exception` (not `RuntimeException`). So the compiler forces us to handle it.

**Q7. What is the difference between `throw` and `throws`?**
`throw` is used inside a method to actually throw an exception object. `throws` is written in the method signature to tell the caller that this method may throw that exception.

**Q8. Why is `counter` static in `Transaction`?**
So that it is shared by all `Transaction` objects. If it were not static, every object would start from 1 and all IDs would be the same.

**Q9. Why did you use ArrayList instead of array?**
Because the number of customers is not fixed. ArrayList grows automatically and has ready methods like `add()`, `set()`, `indexOf()`.

**Q10. How is encapsulation achieved?**
All fields are private. Points can only be changed through `addPoints()` and `deductPoints()`, and `deductPoints()` checks that points don't go negative.

**Q11. What happens if the user enters letters instead of numbers in the menu?**
`nextInt()` throws `InputMismatchException`. We catch it, show a message, clear the input with `sc.nextLine()` and show the menu again. The program does not crash.

**Q12. What does `super()` do in child classes?**
It calls the parent class (`Customer`) constructor to set ID, name and phone.

**Q13. What is the use of `@Override`?**
It tells the compiler that we are overriding a parent method. If we make a spelling mistake in the method name, the compiler gives an error.

**Q14. Why is `copyData()` protected?**
So only child classes can use it. Other classes cannot directly copy points into a customer.

**Q15. Why did you separate backend and frontend?**
So that logic and UI are independent. We can replace the console menu with a GUI without changing any backend code.

---

## 11. Limitations

- Data is stored in memory (ArrayList), so it is lost when the program is closed.
- Only one campaign can be active at a time.
- No login or password for admin.
- Tier only goes up, never goes down.

## 12. Future Scope

- Save data permanently using **File Handling** or a **MySQL database with JDBC**.
- Build a **GUI using Java Swing or JavaFX**.
- Admin login with password.
- Points expiry after some months.
- Send SMS / email notifications on upgrade or reward redemption.
- Reports like top customers and most redeemed rewards.

---

## 13. Conclusion

This project shows how real life loyalty programs (like those used by supermarkets, airlines and coffee shops) can be designed using Object Oriented Programming in Java. Using abstraction, inheritance, polymorphism, encapsulation, interfaces and exception handling, we built a system that is easy to understand, easy to extend and handles errors properly.
