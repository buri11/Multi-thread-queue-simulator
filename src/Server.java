import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class Server implements Runnable{
    private List<Customer> shoppingCustomers = Collections.synchronizedList(new ArrayList<>());
    private AtomicInteger waitingTime = new AtomicInteger();
    private int queueID;

    public Server(int id){
        queueID = id;
        waitingTime.set(0);
    }

    public void addCustomer(Customer c){
        shoppingCustomers.add(c);
        waitingTime.getAndAdd(c.gettService());
    }

    public AtomicInteger getWaitingTime() {
        return waitingTime;
    }

    public int getQueueID() {
        return queueID;
    }

    public List<Customer> getShoppingCustomers() {
        return shoppingCustomers;
    }

    @Override
    public void run() {
        while(true){
            if ( !shoppingCustomers.isEmpty() ) {
                if (shoppingCustomers.get(0).gettService() == 1) {
                    //System.out.println("Client " + shoppingCustomers.get(0).getID() + " has left the store");
                    shoppingCustomers.remove(0);
                    waitingTime.decrementAndGet();
                } else {
                    shoppingCustomers.get(0).settService(shoppingCustomers.get(0).gettService() - 1);
                    waitingTime.decrementAndGet();
                }
            }

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
