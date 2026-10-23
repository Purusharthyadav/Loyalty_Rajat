import java.util.ArrayList;

// main logic class - implements the interface
public class LoyaltyManager implements LoyaltyOperations {

    // tier limits (based on total spending)
    private static final double GOLD_LIMIT = 10000;
    private static final double PLATINUM_LIMIT = 50000;

    private ArrayList<Customer> customers = new ArrayList<>();
    private ArrayList<Reward> rewards = new ArrayList<>();
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private Campaign currentCampaign = null;
    private int nextCustomerId = 101;

    public LoyaltyManager() {
        // adding some default rewards
        rewards.add(new Reward(1, "Rs.100 Discount Coupon", 100, 50));
        rewards.add(new Reward(2, "Free Coffee Mug", 250, 20));
        rewards.add(new Reward(3, "Movie Ticket", 500, 10));
        rewards.add(new Reward(4, "Bluetooth Earphones", 1500, 5));
    }

    @Override
    public void registerCustomer(String name, String phone) {
        if (phone.length() != 10) {
            System.out.println("Invalid phone number! It should be 10 digits.");
            return;
        }
        Customer c = new SilverCustomer(nextCustomerId, name, phone);
        customers.add(c);
        c.addPoints(50);   // welcome bonus
        transactions.add(new Transaction(c.getCustomerId(), "EARN", 50, "Welcome Bonus"));
        System.out.println("Customer registered successfully! Your ID is " + nextCustomerId);
        System.out.println("You got 50 welcome bonus points :)");
        nextCustomerId++;
    }

    public Customer findCustomer(int id) {
        for (Customer c : customers) {
            if (c.getCustomerId() == id) {
                return c;
            }
        }
        return null;
    }

    private Reward findReward(int id) {
        for (Reward r : rewards) {
            if (r.getRewardId() == id) {
                return r;
            }
        }
        return null;
    }

    @Override
    public void makePurchase(int customerId, double amount) {
        Customer c = findCustomer(customerId);
        if (c == null) {
            System.out.println("Customer not found!");
            return;
        }
        if (amount <= 0) {
            System.out.println("Amount must be greater than 0");
            return;
        }

        // 1 point for every Rs.100, then multiply with tier multiplier
        double pts = (amount / 100) * c.getMultiplier();
        if (currentCampaign != null && currentCampaign.isActive()) {
            pts = pts * currentCampaign.getBonusMultiplier();
            System.out.println("Campaign '" + currentCampaign.getCampaignName() + "' applied!");
        }
        int earned = (int) pts;

        c.addSpent(amount);
        c.addPoints(earned);
        transactions.add(new Transaction(customerId, "EARN", earned, "Purchase of Rs." + amount));
        System.out.println("Purchase done. Points earned: " + earned + " | Total points: " + c.getPoints());

        checkUpgrade(c);
    }

    // checks if customer should move to next tier
    private void checkUpgrade(Customer c) {
        Customer upgraded = null;

        if (c instanceof GoldCustomer && c.getTotalSpent() >= PLATINUM_LIMIT) {
            upgraded = new PlatinumCustomer(c);
        } else if (c instanceof SilverCustomer && c.getTotalSpent() >= PLATINUM_LIMIT) {
            upgraded = new PlatinumCustomer(c);
        } else if (c instanceof SilverCustomer && c.getTotalSpent() >= GOLD_LIMIT) {
            upgraded = new GoldCustomer(c);
        }

        if (upgraded == null) {
            return;
        }
        int index = customers.indexOf(c);
        customers.set(index, upgraded);
        System.out.println("*** Congratulations! " + c.getName() + " is upgraded to " + upgraded.getTier() + " ***");
    }

    @Override
    public void redeemReward(int customerId, int rewardId) {
        Customer c = findCustomer(customerId);
        Reward r = findReward(rewardId);

        if (c == null || r == null) {
            System.out.println("Invalid customer ID or reward ID");
            return;
        }
        if (r.getStock() == 0) {
            System.out.println("Sorry, this reward is out of stock");
            return;
        }

        try {
            c.deductPoints(r.getPointsNeeded());
            r.reduceStock();
            transactions.add(new Transaction(customerId, "REDEEM", r.getPointsNeeded(), r.getRewardName()));
            System.out.println("Redeemed " + r.getRewardName() + " successfully! Remaining points: " + c.getPoints());
        } catch (InsufficientPointsException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void showHistory(int customerId) {
        System.out.println("---- Transaction History of " + customerId + " ----");
        boolean found = false;
        for (Transaction t : transactions) {
            if (t.getCustomerId() == customerId) {
                t.display();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No transactions found.");
        }
    }

    public void showAllCustomers() {
        if (customers.isEmpty()) {
            System.out.println("No customers registered yet.");
            return;
        }
        System.out.println("---- All Customers ----");
        for (Customer c : customers) {
            c.display();   // runtime polymorphism - getTier() of child class is called
        }
    }

    public void showRewards() {
        System.out.println("---- Reward Catalog ----");
        for (Reward r : rewards) {
            r.display();
        }
    }

    public void addReward(String name, int points, int stock) {
        int id = rewards.size() + 1;
        rewards.add(new Reward(id, name, points, stock));
        System.out.println("Reward added with ID " + id);
    }

    public void startCampaign(String name, double multiplier) {
        currentCampaign = new Campaign(name, multiplier);
        System.out.println("Campaign '" + name + "' started with " + multiplier + "x bonus points!");
    }

    public void stopCampaign() {
        if (currentCampaign == null || !currentCampaign.isActive()) {
            System.out.println("No campaign is running right now.");
            return;
        }
        currentCampaign.endCampaign();
        System.out.println("Campaign '" + currentCampaign.getCampaignName() + "' ended.");
    }
}
