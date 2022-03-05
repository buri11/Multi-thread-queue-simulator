import java.io.*;
import java.util.*;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class Store implements Runnable{
    private List<Customer> shoppingCustomers = Collections.synchronizedList(new ArrayList<>());
    private List<Server> shopQueues = Collections.synchronizedList(new ArrayList<>());
    private List<Thread> queueThreads = Collections.synchronizedList(new ArrayList<>());

    private int totalSimTime;
    private AtomicInteger currentSimTime = new AtomicInteger();

    private AtomicInteger totalWaitingTime = new AtomicInteger();
    private int nbOfClients = -1;
    private float avgServTime = 0;
    private int peakHour = -1;
    private int maxWaitTime = Integer.MIN_VALUE;

    private FileWriter fileWriter = new FileWriter("logs.txt");
    private PrintWriter printWriter = new PrintWriter(fileWriter);

    private ViewSim vSim;

    public Store(ViewSim v, int nbOfClients, int nbOfQueues, int simInterval, int tMinArr, int tMaxArr, int tMinService, int tMaxService) throws IOException {
        vSim = v;
        totalSimTime = simInterval;
        currentSimTime.set(0);
        this.nbOfClients = nbOfClients;
        for ( int i = 0; i < nbOfClients; i++){
            int tArrival = (int)(Math.random() * (tMaxArr - tMinArr + 1) + tMinArr);
            int tService = (int)(Math.random() * (tMaxService - tMinService + 1) + tMinService);
            //System.out.println("Client " + (i+1) + " has tArrival " + tArrival + " and tService " + tService);
            shoppingCustomers.add(new Customer(i+1, tArrival, tService));
            avgServTime += (float)tService;
        }
        avgServTime /= (float)nbOfClients;
        for ( int i = 0; i < nbOfQueues; i++){
            shopQueues.add(new Server(i+1));
        }
    }

    private void startQueues(){
        for ( int i = 0; i < shopQueues.size(); i++){
            queueThreads.add(new Thread(shopQueues.get(i)));
            queueThreads.get(i).start();
        }
    }

    private void stopQueues(){
        for ( int i = 0; i < queueThreads.size(); i++){
            queueThreads.get(i).stop();
        }
    }
    
    private Server getMinWaitTime(){
        int minWait = Integer.MAX_VALUE;
        Server result = null;
        for (Server s : shopQueues ){
            //System.out.println("Iterated queue id: " + s.getQueueID() + " with waiting time: " + s.getWaitingTime().get());
            if ( s.getWaitingTime().get() < minWait ){
                minWait = s.getWaitingTime().get();
                result = s;
            }
        }
        return result;
    }

    private boolean runningVerif(){
        if ( currentSimTime.get() < totalSimTime )
            return true;

        if ( !shoppingCustomers.isEmpty() )
            return true;

        for ( Server s : shopQueues ){
            if ( !s.getShoppingCustomers().isEmpty() )
                return true;
        }

        return false;
    }

    private void calcPeakHour(int time){
        int sum = 0;
        for ( Server s : shopQueues ){
            sum += s.getWaitingTime().get();
        }
        if ( sum > maxWaitTime ){
            maxWaitTime = sum;
            peakHour = time;
        }
    }

    public void run() {
        this.startQueues();
        while( runningVerif() ){
            //increment simulation time
            currentSimTime.incrementAndGet();

            //adding clients to queues if the right time has come
            for ( int i = 0; i < shoppingCustomers.size(); i++ ){
                Customer c = shoppingCustomers.get(i);
                if (c.gettArrival() <= currentSimTime.get()) {
                    Server s = this.getMinWaitTime();
                    totalWaitingTime.addAndGet(s.getWaitingTime().get() + c.gettService());
                    s.addCustomer(c);
                    //System.out.println("Clientul " + c.getID() + " with tArriv " + c.gettArrival() + " and with tService " + c.gettService() + " a fost adaugat in coada " + s.getQueueID());
                    shoppingCustomers.remove(i);
                    i--;
                }
            }

            calcPeakHour(currentSimTime.get());

            System.out.println("Current simulation time: " + currentSimTime.get());
            printWriter.println("Current simulation time: " + currentSimTime.get());
            vSim.getTime().setText("Time: " + currentSimTime.get());

            System.out.print("Waiting clients: ");
            printWriter.println("Waiting clients: ");

            String waitingClientsLabel = "";
            for ( Customer c : shoppingCustomers ){
                System.out.print("(" + c.getID() + "," + c.gettArrival() + "," + c.gettService() + ") ");
                printWriter.print("(" + c.getID() + "," + c.gettArrival() + "," + c.gettService() + ") ");
                waitingClientsLabel += "(" + c.getID() + "," + c.gettArrival() + "," + c.gettService() + ") ";
            }
            vSim.getWaitingClientsLabel().setText(waitingClientsLabel);

            System.out.println();
            printWriter.println();

            for ( int i = 0; i <  shopQueues.size(); i++){
                Server s = shopQueues.get(i);

                System.out.print("Queue " + s.getQueueID() + ": ");
                printWriter.print("Queue " + s.getQueueID() + ": ");

                String queueClients = "";
                if ( s.getShoppingCustomers().isEmpty() ){
                    System.out.print("empty");
                    printWriter.print("empty");
                    queueClients = "empty";
                }
                else{
                    for ( Customer c : s.getShoppingCustomers() ){
                        System.out.print("(" + c.getID() + "," + c.gettArrival() + "," + c.gettService() + ") ");
                        printWriter.print("(" + c.getID() + "," + c.gettArrival() + "," + c.gettService() + ") ");
                        queueClients += "(" + c.getID() + "," + c.gettArrival() + "," + c.gettService() + ") ";
                    }
                }
                System.out.println();
                printWriter.println();
                vSim.getQueuesLabels().get(i).setText(queueClients);
            }
            System.out.println();
            printWriter.println();
            //vSim.getFrame().setContentPane(vSim.getpGeneral());
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.stopQueues();
        float avgWaitTime = (float)totalWaitingTime.get() / (float)nbOfClients;
        System.out.println("Average waiting time is " + avgWaitTime + " seconds");
        System.out.println("Average service time is " + avgServTime + " seconds");
        System.out.println("Peak hour was at second " + peakHour);
        printWriter.println("Average waiting time is " + avgWaitTime + " seconds");
        printWriter.println("Average service time is " + avgServTime + " seconds");
        printWriter.println("Peak hour was at second " + peakHour);
        printWriter.close();

        for ( int i = 0; i < shopQueues.size(); i++){
            vSim.getQueuesLabels().get(i).setText("empty");
        }
        vSim.getAvgWaitingTime().setText("Average waiting time is " + avgWaitTime + " seconds");
        vSim.getAvgServiceTime().setText("Average service time is " + avgServTime + " seconds");
        vSim.getPeakHour().setText("Peak hour was at second " + peakHour);
        vSim.getpGeneral().add(vSim.getpStats());
        vSim.getFrame().pack();
    }
}
