package Helpers;

import java.util.List;
import java.util.Queue;

public class CoffeeMaker implements Runnable{
    private String threadName;
    private VirtualCafe virtualCafe;
    private Queue<Coffee> waitingCoffees;
    private List<Coffee> brewingCoffees;
    private List<Coffee> readyCoffees;

    public CoffeeMaker(String name, VirtualCafe virtualCafe, Queue<Coffee> waitingCoffees, List<Coffee> brewingCoffees, List<Coffee> readyCoffees) {
        this.threadName = name;
        this.virtualCafe = virtualCafe;
        this.waitingCoffees = waitingCoffees;
        this.brewingCoffees = brewingCoffees;
        this.readyCoffees = readyCoffees;
    }

    @Override
    public void run() {
        while (true) {
            Coffee coffee = waitingCoffees.poll();
            if (coffee != null) {
                try {
                    synchronized (brewingCoffees){
                        coffee.setState("brewing");
                        brewingCoffees.add(coffee);
                    }

                    virtualCafe.showLog();

                    Thread.sleep(45000);

                    synchronized (brewingCoffees){
                        synchronized (readyCoffees) {
                            coffee.setState("tray");
                            if (brewingCoffees.remove(coffee)) {
                                readyCoffees.add(coffee);
                            }
                        }
                    }
                    virtualCafe.showLog();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}
