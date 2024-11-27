package Helpers;

import java.util.List;
import java.util.Queue;

class TeaMaker implements Runnable {
    private String threadName;
    private VirtualCafe virtualCafe;
    private Queue<Tea> waitingTeas;
    private List<Tea> brewingTeas;
    private List<Tea> readyTeas;

    public TeaMaker(String name, VirtualCafe virtualCafe, Queue<Tea> waitingTeas,List<Tea> brewingTeas, List<Tea> readyTeas) {
        this.threadName = name;
        this.virtualCafe = virtualCafe;
        this.waitingTeas = waitingTeas;
        this.brewingTeas = brewingTeas;
        this.readyTeas = readyTeas;
    }

    @Override
    public void run() {
        while (true) {
            Tea tea = waitingTeas.poll();
            if (tea != null) {
                try {
                    synchronized (brewingTeas){
                        tea.setState("brewing");
                        brewingTeas.add(tea);
                    }

                    virtualCafe.showLog();

                    Thread.sleep(30000);

                    synchronized (brewingTeas){
                        synchronized (readyTeas) {
                            tea.setState("tray");
                            if (brewingTeas.remove(tea)){
                                readyTeas.add(tea);
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

