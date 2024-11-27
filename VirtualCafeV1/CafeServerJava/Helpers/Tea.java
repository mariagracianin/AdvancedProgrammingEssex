package Helpers;

public class Tea {
    private String customerName;
    private String state;

    public Tea(String customerName) {
        this.customerName = customerName;
        this.state = "waiting";  //"brewing" "tray"
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
