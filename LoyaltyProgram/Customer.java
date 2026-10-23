// Abstract class for Customer
// Every customer has a tier (Silver / Gold / Platinum) which is decided by child class
public abstract class Customer {

    private int customerId;
    private String name;
    private String phone;
    private int points;
    private double totalSpent;

    public Customer(int customerId, String name, String phone) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.points = 0;
        this.totalSpent = 0;
    }

    // abstract methods - child classes will give their own implementation
    public abstract String getTier();
    public abstract double getMultiplier();

    public void addPoints(int p) {
        points = points + p;
    }

    public void deductPoints(int p) throws InsufficientPointsException {
        if (p > points) {
            throw new InsufficientPointsException("Not enough points! Available: " + points + ", Required: " + p);
        }
        points = points - p;
    }

    public void addSpent(double amount) {
        totalSpent = totalSpent + amount;
    }

    // used when customer gets upgraded to next tier
    protected void copyData(Customer old) {
        this.points = old.points;
        this.totalSpent = old.totalSpent;
    }

    // getters
    public int getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public int getPoints() { return points; }
    public double getTotalSpent() { return totalSpent; }

    public void display() {
        System.out.println("ID: " + customerId + " | Name: " + name + " | Phone: " + phone
                + " | Tier: " + getTier() + " | Points: " + points + " | Total Spent: Rs." + totalSpent);
    }
}
