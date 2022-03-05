import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class View {
    private JFrame frame = new JFrame("Simulation setup");

    private JPanel data = new JPanel();
    private JLabel nbOfClientsLabel = new JLabel("Number of clients",JLabel.CENTER);
    private JTextField nbOfClients = new JTextField(15);
    private JLabel nbOfQueuesLabel = new JLabel("Number of queues",JLabel.CENTER);
    private JTextField nbOfQueues = new JTextField(15);
    private JLabel simIntervalLabel = new JLabel("Simulation interval (seconds)",JLabel.CENTER);
    private JTextField simInterval = new JTextField(15);
    private JLabel tMinArrivalLabel = new JLabel("Minimum arrival time",JLabel.CENTER);
    private JTextField tMinArrival = new JTextField(15);
    private JLabel tMaxArrivalLabel = new JLabel("Maximum arrival time",JLabel.CENTER);
    private JTextField tMaxArrival = new JTextField(15);
    private JLabel tMinServiceLabel = new JLabel("Minimum service time",JLabel.CENTER);
    private JTextField tMinService = new JTextField(15);
    private JLabel tMaxServiceLabel = new JLabel("Maximum service time",JLabel.CENTER);
    private JTextField tMaxService = new JTextField(15);
    private JButton submitButton = new JButton("Start simulation");
    private JPanel pButton = new JPanel();
    private JPanel pGeneral = new JPanel();
    private GridLayout layout72 = new GridLayout(7,2);
    //private GridLayout layout21 = new GridLayout(2,1);
    private ArrayList<Integer> inputData = new ArrayList<>();
    private boolean isData = false;

    //private Store magazin;

    public View(){
        //magazin = s;

        frame.getContentPane().setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(400,350));
        frame.setLocationRelativeTo(null);

        data.setLayout(layout72);

        data.add(nbOfClientsLabel);
        data.add(nbOfClients);
        data.add(nbOfQueuesLabel);
        data.add(nbOfQueues);
        data.add(simIntervalLabel);
        data.add(simInterval);
        data.add(tMinArrivalLabel);
        data.add(tMinArrival);
        data.add(tMaxArrivalLabel);
        data.add(tMaxArrival);
        data.add(tMinServiceLabel);
        data.add(tMinService);
        data.add(tMaxServiceLabel);
        data.add(tMaxService);

        pButton.add(submitButton);
        pButton.setLayout(new FlowLayout(FlowLayout.CENTER));

        pGeneral.setLayout(new BoxLayout(pGeneral, BoxLayout.Y_AXIS));
        //pGeneral.setLayout(layout21);
        pGeneral.add(data);
        pGeneral.add(pButton);

        frame.setContentPane(pGeneral);
        frame.setVisible(true);
        frame.pack();

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputData.add(Integer.parseInt(nbOfClients.getText()));
                inputData.add(Integer.parseInt(nbOfQueues.getText()));
                inputData.add(Integer.parseInt(simInterval.getText()));
                inputData.add(Integer.parseInt(tMinArrival.getText()));
                inputData.add(Integer.parseInt(tMaxArrival.getText()));
                inputData.add(Integer.parseInt(tMinService.getText()));
                inputData.add(Integer.parseInt(tMaxService.getText()));
                startSim(inputData);
                frame.dispose();
            }
        });
    }

    private void startSim(ArrayList<Integer> inputData){
        ViewSim vSim = new ViewSim(inputData.get(1));
        vSim.getFrame().setVisible(true);
        Store magazin = null;
        try {
            magazin = new Store(vSim, inputData.get(0), inputData.get(1), inputData.get(2), inputData.get(3), inputData.get(4), inputData.get(5), inputData.get(6));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread t = new Thread(magazin);
        t.start();
    }

    public ArrayList<Integer> getInputData() {
        return inputData;
    }

    public boolean isData() {
        return isData;
    }
}
