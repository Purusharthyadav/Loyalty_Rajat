import java.util.InputMismatchException;
import java.util.Scanner;

// Customer Loyalty Program Management System
// OOPs using Java - Mini Project
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LoyaltyManager lm = new LoyaltyManager();
        int choice = 0;

        System.out.println("==========================================");
        System.out.println("   CUSTOMER LOYALTY PROGRAM MANAGEMENT    ");
        System.out.println("==========================================");

        do {
            System.out.println("\n1. Register Customer");
            System.out.println("2. Make Purchase (Earn Points)");
            System.out.println("3. View Reward Catalog");
            System.out.println("4. Redeem Reward");
            System.out.println("5. View Customer Details");
            System.out.println("6. View Transaction History");
            System.out.println("7. View All Customers");
            System.out.println("8. Add New Reward (Admin)");
            System.out.println("9. Start Campaign (Admin)");
            System.out.println("10. Stop Campaign (Admin)");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Enter name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter phone number: ");
                        String phone = sc.nextLine();
                        lm.registerCustomer(name, phone);
                        break;

                    case 2:
                        System.out.print("Enter customer ID: ");
                        int id = sc.nextInt();
                        System.out.print("Enter purchase amount: ");
                        double amt = sc.nextDouble();
                        lm.makePurchase(id, amt);
                        break;

                    case 3:
                        lm.showRewards();
                        break;

                    case 4:
                        System.out.print("Enter customer ID: ");
                        int cid = sc.nextInt();
                        lm.showRewards();
                        System.out.print("Enter reward ID: ");
                        int rid = sc.nextInt();
                        lm.redeemReward(cid, rid);
                        break;

                    case 5:
                        System.out.print("Enter customer ID: ");
                        Customer c = lm.findCustomer(sc.nextInt());
                        if (c != null) c.display();
                        else System.out.println("Customer not found!");
                        break;

                    case 6:
                        System.out.print("Enter customer ID: ");
                        lm.showHistory(sc.nextInt());
                        break;

                    case 7:
                        lm.showAllCustomers();
                        break;

                    case 8:
                        System.out.print("Enter reward name: ");
                        String rname = sc.nextLine();
                        System.out.print("Enter points required: ");
                        int pts = sc.nextInt();
                        System.out.print("Enter stock: ");
                        int stock = sc.nextInt();
                        lm.addReward(rname, pts, stock);
                        break;

                    case 9:
                        System.out.print("Enter campaign name: ");
                        String cname = sc.nextLine();
                        System.out.print("Enter bonus multiplier (eg 2): ");
                        double mul = sc.nextDouble();
                        lm.startCampaign(cname, mul);
                        break;

                    case 10:
                        lm.stopCampaign();
                        break;

                    case 0:
                        System.out.println("Thank you for using the system!");
                        break;

                    default:
                        System.out.println("Invalid choice, try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter numbers only!");
                sc.nextLine();   // clear wrong input
                choice = -1;
            }

        } while (choice != 0);

        sc.close();
    }
}
