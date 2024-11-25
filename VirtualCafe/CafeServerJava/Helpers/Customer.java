package Helpers;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private final String customerName;
    private List<Tea> teasList = new ArrayList<>();
    private List<Coffee> coffeeList = new ArrayList<>();

    public Customer(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public List<Tea> getTeasList() {
        return teasList;
    }

    public List<Coffee> getCoffeeList() {
        return coffeeList;
    }

    public void addTea(Tea tea){
        teasList.add(tea);
    }

    public void replaceTea(Tea teaToRemove, Tea teaToAdd){
        teasList.remove(teaToRemove);
        teasList.add(teaToAdd);
    }

    public void addCoffee(Coffee coffee){
        coffeeList.add(coffee);
    }

    public void replaceCoffee(Coffee coffeeToRemove, Coffee coffeeToAdd){
        coffeeList.remove(coffeeToRemove);
        coffeeList.add(coffeeToAdd);
    }

    public boolean idleCustomer(){
        if (teasList.isEmpty() && coffeeList.isEmpty()){
            return true;
        }else {
            return false;
        }
    }

    public boolean orderReady(){
        for (Tea tea : teasList) {
            if (tea.getState().equals("waiting") || tea.getState().equals("brewing")){
                return false;
            }
        }
        for (Coffee coffee : coffeeList) {
            if (coffee.getState().equals("waiting") || coffee.getState().equals("brewing")){
                return false;
            }
        }
        return true;
    }

    public String getOrderStatusString(){
        int teasInWaitingArea = 0;
        int teasInBrewingArea = 0;
        int teasInTrayArea = 0;
        int coffeeInWaitingArea = 0;
        int coffeeInBrewingArea = 0;
        int coffeeInTrayArea = 0;

        if (teasList.isEmpty() && coffeeList.isEmpty()){
            return "No order found for " + this.customerName + "\nEND";
        }

        for (Tea tea : teasList) {
            if (tea.getState().equals("waiting")){
                teasInWaitingArea++;
            }else if (tea.getState().equals("brewing")){
                teasInBrewingArea++;
            }else{
                teasInTrayArea++;
            }
        }

        for (Coffee coffee : coffeeList) {
            if (coffee.getState().equals("waiting")){
                coffeeInWaitingArea++;
            }else if (coffee.getState().equals("brewing")){
                coffeeInBrewingArea++;
            }else{
                coffeeInTrayArea++;
            }
        }
        return generateOrderStatusString(this.customerName, teasInWaitingArea, teasInBrewingArea, teasInTrayArea, coffeeInWaitingArea, coffeeInBrewingArea, coffeeInTrayArea);
    }

    private String generateOrderStatusString(
            String customerName,
            int teasInWaitingArea, int teasInBrewingArea, int teasInTrayArea,
            int coffeeInWaitingArea, int coffeeInBrewingArea, int coffeeInTrayArea) {

        StringBuilder status = new StringBuilder();

        status.append("Order status for ").append(customerName).append(":\n");

        if (coffeeInWaitingArea > 0 || teasInWaitingArea > 0) {
            status.append("  - ");
            status.append(coffeeInWaitingArea).append(" coffee").append(coffeeInWaitingArea == 1 ? "" : "s");
            status.append(" and ");
            status.append(teasInWaitingArea).append(" tea").append(teasInWaitingArea == 1 ? "" : "s");
            status.append(" in waiting area\n");
        }

        if (coffeeInBrewingArea > 0 || teasInBrewingArea > 0) {
            status.append("  - ");
            status.append(coffeeInBrewingArea).append(" coffee").append(coffeeInBrewingArea == 1 ? "" : "s");
            status.append(" and ");
            status.append(teasInBrewingArea).append(" tea").append(teasInBrewingArea == 1 ? "" : "s");
            status.append(" currently being prepared\n");
        }

        if (coffeeInTrayArea > 0 || teasInTrayArea > 0) {
            status.append("  - ");
            status.append(coffeeInTrayArea).append(" coffee").append(coffeeInTrayArea == 1 ? "" : "s");
            status.append(" and ");
            status.append(teasInTrayArea).append(" tea").append(teasInTrayArea == 1 ? "" : "s");
            status.append(" currently in the tray\n");
        }

        status.append("END");
        return status.toString();
    }
}
