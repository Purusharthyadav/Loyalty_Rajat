// Silver is the default tier for every new customer
public class SilverCustomer extends Customer {

    public SilverCustomer(int id, String name, String phone) {
        super(id, name, phone);
    }

    @Override
    public String getTier() {
        return "SILVER";
    }

    @Override
    public double getMultiplier() {
        return 1.0;
    }
}
