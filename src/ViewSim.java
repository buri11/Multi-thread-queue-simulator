import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ViewSim {
    private int nbOfQueues = -1;

    private JFrame frame = new JFrame("Simulation");

    private JPanel shop = new JPanel();
    private final GridLayout layout21 = new GridLayout(1,2);
    private JLabel time = new JLabel("Time: 0", JLabel.CENTER);
    private JPanel pTime = new JPanel();
    private JLabel waitingClientsLabel = new JLabel("Empty store", JLabel.CENTER);
    private JLabel waitingName = new JLabel("Waiting clients: ", JLabel.CENTER);

    private GridLayout layoutQueues = new GridLayout(0,2);
    private JPanel pQueues = new JPanel();
    private JPanel pGeneral = new JPanel();

    private JLabel avgWaitingTime = new JLabel("Average waiting time is ~", JLabel.CENTER);
    private JLabel avgServiceTime = new JLabel("Average service time is ~", JLabel.CENTER);
    private JLabel peakHour = new JLabel("Peak hour was at ~", JLabel.CENTER);
    private JPanel pStats = new JPanel();

    private ArrayList<JLabel> queuesLabels = new ArrayList<>();

    public ViewSim(int nbOfQueues){
        this.nbOfQueues = nbOfQueues;

        frame.getContentPane().setLayout(new BorderLayout(2,2));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(400,450));
        frame.setLocationRelativeTo(null);

        pTime.add(time);

        shop.setLayout(layout21);
        shop.add(waitingName);
        shop.add(waitingClientsLabel);

        pQueues.setLayout(layoutQueues);
        for (int i = 0; i < this.nbOfQueues; i++){
            JLabel queue = new JLabel("Empty queue", JLabel.CENTER);
            JLabel name = new JLabel("Queue " + (i+1) + ":", JLabel.CENTER);
            pQueues.add(name);
            pQueues.add(queue);
            queuesLabels.add(queue);
        }

        pStats.setLayout(new GridLayout(3,1));
        pStats.add(avgWaitingTime);
        pStats.add(avgServiceTime);
        pStats.add(peakHour);

        pGeneral.setLayout(new BoxLayout(pGeneral, BoxLayout.Y_AXIS));
        pGeneral.add(pTime);
        pGeneral.add(shop);
        pGeneral.add(pQueues);

        frame.setContentPane(pGeneral);
        frame.pack();
    }

    public JLabel getAvgWaitingTime() {
        return avgWaitingTime;
    }

    public JLabel getAvgServiceTime() {
        return avgServiceTime;
    }

    public JLabel getPeakHour() {
        return peakHour;
    }

    public JPanel getpStats() {
        return pStats;
    }

    public JPanel getpGeneral() {
        return pGeneral;
    }

    public JFrame getFrame() {
        return frame;
    }

    public JLabel getTime() {
        return time;
    }

    public JLabel getWaitingClientsLabel() {
        return waitingClientsLabel;
    }

    public ArrayList<JLabel> getQueuesLabels() {
        return queuesLabels;
    }
}
