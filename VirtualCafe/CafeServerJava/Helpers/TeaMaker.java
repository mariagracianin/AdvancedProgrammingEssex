package Helpers;

import java.util.List;
import java.util.Queue;

class TeaMaker implements Runnable {
    private String threadName;
    private Queue<Tea> waitingTeas;
    private List<Tea> brewingTeas;
    private List<Tea> readyTeas;

    public TeaMaker(String name, Queue<Tea> waitingTeas,List<Tea> brewingTeas, List<Tea> readyTeas) {
        this.threadName = name;
        this.waitingTeas = waitingTeas;
        this.brewingTeas = brewingTeas;
        this.readyTeas = readyTeas;
    }

    @Override
    public void run() {
        while (true) {
            //todo tendria que estar la queue y la lista de brewing sincronizadas como brwing y ready?
            Tea tea = waitingTeas.poll();
            if (tea != null) {
                try {
                    synchronized (brewingTeas){
                        tea.setState("brewing");
                        brewingTeas.add(tea);
                    }
                    //System.out.println(threadName + " está preparando té para " + tea.getCustomerName());

                    Thread.sleep(2000); // Simula 2 segundos para preparar un té

                    //TODO ESTA ESTO BIEN SINCRONIZADO?
                    synchronized (brewingTeas){
                        synchronized (readyTeas) {
                            tea.setState("tray");
                            if (brewingTeas.remove(tea)){
                                readyTeas.add(tea);
                            }
                        }
                    }
                    //System.out.println(threadName + " ha terminado el té para " + tea.getCustomerName());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}

