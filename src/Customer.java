public class Customer {
    private int ID;
    private int tArrival;
    private int tService;

    public Customer(int ID, int tArrival, int tService){
        this.ID = ID;
        this.tArrival = tArrival;
        this.tService = tService;
    }

    public int getID() {
        return ID;
    }

    public int gettArrival() {
        return tArrival;
    }

    public int gettService() {
        return tService;
    }

    public void settService(int tService) {
        this.tService = tService;
    }
}
