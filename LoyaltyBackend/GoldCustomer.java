// Gold tier - customer gets 1.5x points
public class GoldCustomer extends Customer {

    public GoldCustomer(int id, String name, String phone) {
        super(id, name, phone);
    }

    // constructor used during upgrade from silver
    public GoldCustomer(Customer old) {
        super(old.getCustomerId(), old.getName(), old.getPhone());
        copyData(old);
    }

    @Override
    public String getTier() {
        return "GOLD";
    }

    @Override
    public double getMultiplier() {
        return 1.5;
    }
}
