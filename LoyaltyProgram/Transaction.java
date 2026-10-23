import java.time.LocalDate;

// stores history of every earn / redeem
public class Transaction {

    private static int counter = 1;   // static variable for auto id

    private int transId;
    private int customerId;
    private String type;   // EARN or REDEEM
    private int points;
    private String details;
    private LocalDate date;

    public Transaction(int customerId, String type, int points, String details) {
        this.transId = counter++;
        this.customerId = customerId;
        this.type = type;
        this.points = points;
        this.details = details;
        this.date = LocalDate.now();
    }

    public int getCustomerId() { return customerId; }

    public void display() {
        System.out.println("T" + transId + " | " + date + " | " + type + " | " + points + " pts | " + details);
    }
}
