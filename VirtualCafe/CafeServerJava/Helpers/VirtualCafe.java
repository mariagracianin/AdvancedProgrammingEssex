package Helpers;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class VirtualCafe implements Runnable{
    private Map<String, Customer> customers = new TreeMap<>();

    private Queue<Tea> waitingTeas = new ConcurrentLinkedQueue<>();
    private Queue<Coffee> waitingCoffees = new ConcurrentLinkedQueue<>();

    private List<Tea> brewingTeas = new ArrayList<>();
    private List<Coffee> brewingCoffees = new ArrayList<>();

    private List<Tea> readyTeas = new ArrayList<>();
    private List<Coffee> readyCoffees = new ArrayList<>();


    @Override
    public void run() {
        new Thread(new TeaMaker("TeaMaker-1", waitingTeas, brewingTeas, readyTeas)).start();
        new Thread(new TeaMaker("TeaMaker-2", waitingTeas, brewingTeas, readyTeas)).start();
        new Thread(new CoffeeMaker("CoffeeMaker-1", waitingCoffees, brewingCoffees, readyCoffees)).start();
        new Thread(new CoffeeMaker("CoffeeMaker-2", waitingCoffees, brewingCoffees, readyCoffees)).start();

        while (true) {
            try {

                showLog(); //TODO cambiarlo de lugar a donde corresponda
                Thread.sleep(2000 );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Map<String, Customer> getCustomers() {
        return customers;
    }

    public Queue<Tea> getWaitingTeas() {
        return waitingTeas;
    }

    public Queue<Coffee> getWaitingCoffees() {
        return waitingCoffees;
    }

    public List<Tea> getReadyTeas() {
        return readyTeas;
    }

    public List<Coffee> getReadyCoffees() {
        return readyCoffees;
    }

    public String getOrderStatus(String customerName) {
        return customers.get(customerName).getOrderStatusString();
    }

    // TODO tengo q sincronizarlo? que solo un cliente pueda agregar ordenes? o sincronizarlo como los otros?
    public synchronized void addOrder(String customerName, int numTeas, int numCoffes){
        Customer customer = customers.get(customerName);
        if (customer == null){
            customer = new Customer(customerName);
            customers.put(customerName, customer);
        }
        for (int i = 0; i < numTeas; i++) {
            Tea newTea = new Tea(customerName);
            customer.addTea(newTea);
            waitingTeas.add(newTea);
        }
        for (int i = 0; i < numCoffes; i++) {
            Coffee newCoffee = new Coffee(customerName);
            customer.addCoffee(newCoffee);
            waitingCoffees.add(newCoffee);
        }
    }

    public String collect(String customerName){
        Customer customer = customers.get(customerName);
        if (customer.orderReady()){
            List<Tea> teasList = customer.getTeasList();
            List<Coffee> coffeeList = customer.getCoffeeList();
            for (int index = 0; index < teasList.size(); ) {
                Tea tea = teasList.get(index);
                synchronized (readyTeas) {
                    readyTeas.remove(tea);
                }
                teasList.remove(tea);
            }
            for (int index = 0; index < coffeeList.size();) {
                Coffee coffee = coffeeList.get(index);
                synchronized (readyCoffees) {
                    readyCoffees.remove(coffee);
                }
                coffeeList.remove(coffee);
            }
            return "Order has been collected";
        }else {
            return "Order is still pending";
        }
    }

    public String exit(String customerName){
        waitingTeas.removeIf(tea -> tea.getCustomerName().equals(customerName));

        synchronized (readyTeas) {
            for (int index = 0; index < readyTeas.size(); index++) {
                Tea teaReady = readyTeas.get(index);
                if (teaReady.getCustomerName().equals(customerName)) {
                    Tea teaWaiting = waitingTeas.poll();
                    if (teaWaiting != null) {
                        synchronized (customers) {
                            teaReady.setCustomerName(teaWaiting.getCustomerName());
                            Customer customerStay = customers.get(teaWaiting.getCustomerName());
                            customerStay.replaceTea(teaWaiting, teaReady);
                            System.out.println("1 tea in " + customerName + "'s tray has been transferred to " + teaWaiting.getCustomerName() + "'s tray");
                        }
                    } else {
                        readyTeas.remove(teaReady);
                        index--;
                    }
                }
            }
        }

        synchronized (brewingTeas) {
            for (int index = 0; index < brewingTeas.size(); index++) {
                Tea teaBrewing = brewingTeas.get(index);
                if (teaBrewing.getCustomerName().equals(customerName)) {
                    Tea teaWaiting = waitingTeas.poll();
                    if (teaWaiting != null) {
                        synchronized (customers) {
                            teaBrewing.setCustomerName(teaWaiting.getCustomerName());
                            Customer customerStay = customers.get(teaWaiting.getCustomerName());
                            customerStay.replaceTea(teaWaiting, teaBrewing);
                            System.out.println("1 tea currently brewing for " + customerName + " has been transferred to " + teaWaiting.getCustomerName() + "'s order");
                        }
                    } else {
                        brewingTeas.remove(teaBrewing);
                        index--;
                    }
                }
            }
        }

        synchronized (readyCoffees) {
            for (int index = 0; index < readyCoffees.size(); index++) {
                Coffee coffeeReady = readyCoffees.get(index);
                if (coffeeReady.getCustomerName().equals(customerName)) {
                    Coffee coffeeWaiting = waitingCoffees.poll();
                    if (coffeeWaiting != null) {
                        synchronized (customers) {
                            coffeeReady.setCustomerName(coffeeWaiting.getCustomerName());
                            Customer customerStay = customers.get(coffeeWaiting.getCustomerName());
                            customerStay.replaceCoffee(coffeeWaiting, coffeeReady);
                            System.out.println("1 coffee in " + customerName + "'s tray has been transferred to " + coffeeWaiting.getCustomerName() + "'s tray");
                        }
                    } else {
                        readyCoffees.remove(coffeeReady);
                        index--;
                    }
                }
            }
        }

        synchronized (brewingCoffees) {
            for (int index = 0; index < brewingCoffees.size(); index++) {
                Coffee coffeeBrewing = brewingCoffees.get(index);
                if (coffeeBrewing.getCustomerName().equals(customerName)) {
                    Coffee coffeeWaiting = waitingCoffees.poll();
                    if (coffeeWaiting != null) {
                        synchronized (customers) {
                            coffeeBrewing.setCustomerName(coffeeWaiting.getCustomerName());
                            Customer customerStay = customers.get(coffeeWaiting.getCustomerName());
                            customerStay.replaceCoffee(coffeeWaiting, coffeeBrewing);
                            System.out.println("1 coffee currently brewing for " + customerName + " has been transferred to " + coffeeWaiting.getCustomerName() + "'s order");
                        }
                    } else {
                        brewingCoffees.remove(coffeeBrewing);
                        index--;
                    }
                }
            }
        }

        synchronized (customers){
            customers.remove(customerName);
        }
        return "Goodbye, " + customerName + "! Disconnecting...";
    }

    public void showLog(){
        System.out.println("---> SERVER CURRENT STATE");
        System.out.println("Number of clients in the café: " + this.customers.size());
        int count = 0;
        for (var entry : customers.entrySet()) {
            if (!entry.getValue().idleCustomer()) {
                count++;
            }
        }
        System.out.println("Number of clients waiting for orders: " + count);
        System.out.println("-> Teas");
        System.out.println("Number of teas in waiting area: " + this.waitingTeas.size());
        System.out.println("Number of teas in brewing area: " + this.brewingTeas.size());
        System.out.println("Number of teas in tray area: " + this.readyTeas.size());
        System.out.println("-> Coffees");
        System.out.println("Number of coffees in waiting area: " + this.waitingCoffees.size());
        System.out.println("Number of coffees in brewing area: " + this.brewingCoffees.size());
        System.out.println("Number of coffees in tray area: " + this.readyCoffees.size());
        System.out.println("-----------------------------------------------");
    }
}
