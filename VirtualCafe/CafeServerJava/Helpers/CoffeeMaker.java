package Helpers;

import java.util.List;
import java.util.Queue;

public class CoffeeMaker implements Runnable{
    private String threadName;
    private Queue<Coffee> waitingCoffees;
    private List<Coffee> brewingCoffees;
    private List<Coffee> readyCoffees;

    public CoffeeMaker(String name, Queue<Coffee> waitingCoffees, List<Coffee> brewingCoffees, List<Coffee> readyCoffees) {
        this.threadName = name;
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
                        brewingCoffees.add(coffee);
                    }
                    coffee.setState("brewing");
                    //System.out.println(threadName + " está preparando cafe para " + coffee.getCustomerName());

                    Thread.sleep(3000); // Simula 2 segundos para preparar un té

                    synchronized (brewingCoffees){
                        synchronized (readyCoffees) {
                            coffee.setState("tray");
                            if (brewingCoffees.remove(coffee)) {
                                readyCoffees.add(coffee);
                            }
                        }
                    }
                    //System.out.println(threadName + " ha terminado el cafe para " + coffee.getCustomerName());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}
