// Campaign = engagement offer like "Diwali Dhamaka - extra 2x points"
public class Campaign {

    private String campaignName;
    private double bonusMultiplier;
    private boolean active;

    public Campaign(String campaignName, double bonusMultiplier) {
        this.campaignName = campaignName;
        this.bonusMultiplier = bonusMultiplier;
        this.active = true;
    }

    public String getCampaignName() { return campaignName; }
    public double getBonusMultiplier() { return bonusMultiplier; }
    public boolean isActive() { return active; }

    public void endCampaign() {
        active = false;
    }
}
