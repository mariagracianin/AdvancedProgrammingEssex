package Helpers;

public class Coffee {
    private String customerName;
    private String state;

    public Coffee(String customerName) {
        this.customerName = customerName;
        this.state = "waiting";
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
