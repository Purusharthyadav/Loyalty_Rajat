// interface which tells what operations our system must support
public interface LoyaltyOperations {

    void registerCustomer(String name, String phone);
    void makePurchase(int customerId, double amount);
    void redeemReward(int customerId, int rewardId);
    void showHistory(int customerId);
}
