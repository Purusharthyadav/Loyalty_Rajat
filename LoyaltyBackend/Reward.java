// Reward class - things customer can redeem using points
public class Reward {

    private int rewardId;
    private String rewardName;
    private int pointsNeeded;
    private int stock;

    public Reward(int rewardId, String rewardName, int pointsNeeded, int stock) {
        this.rewardId = rewardId;
        this.rewardName = rewardName;
        this.pointsNeeded = pointsNeeded;
        this.stock = stock;
    }

    public int getRewardId() { return rewardId; }
    public String getRewardName() { return rewardName; }
    public int getPointsNeeded() { return pointsNeeded; }
    public int getStock() { return stock; }

    public void reduceStock() {
        stock--;
    }

    public void display() {
        System.out.println(rewardId + ". " + rewardName + " - " + pointsNeeded + " points (Stock: " + stock + ")");
    }
}
