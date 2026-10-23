// Platinum tier - highest tier, customer gets 2x points
public class PlatinumCustomer extends Customer {

    public PlatinumCustomer(Customer old) {
        super(old.getCustomerId(), old.getName(), old.getPhone());
        copyData(old);
    }

    @Override
    public String getTier() {
        return "PLATINUM";
    }

    @Override
    public double getMultiplier() {
        return 2.0;
    }
}
