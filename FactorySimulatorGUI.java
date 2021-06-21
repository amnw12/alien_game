/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factorySimulation;

import java.awt.*;
import java.util.Stack;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author amnwaqar
 */
public class FactorySimulatorGUI extends JPanel implements ChangeListener{
    public final int PANEL_WIDTH = 1020; public final int PANEL_HEIGHT = 700;
    private DrawingPanel drawPanel;  
    private JLabel currentDetails, maxConsumptionLabel, maxProductionLabel;
    private ConveyorBelt[] belts;
    private JButton addDispatcher, removeDispatcher, addMachine, removeMachine;
    private JSlider maxConsumptionTime, maxProductionTime;
    private JPanel controls, maxConsumption, maxProduction;
    private Stack<Machine> machines;
    private Stack<Dispatcher> dispatchers;
    private int machineCounter, dispatcherCounter;
    private Timer timer;

    public FactorySimulatorGUI() {
        super(new BorderLayout());
        belts = new ConveyorBelt[5];
        drawPanel = new DrawingPanel();
        machines = new Stack<>();
        dispatchers = new Stack<>();
        machineCounter = 0;
        dispatcherCounter = 0;
        
        for (int k = 0; k < 5; k++)
        {
            belts[k] = new ConveyorBelt(8);
        }
        
        addDispatcher = new JButton("Add Dispatcher");
        addDispatcher.addActionListener(action -> addDispatcher());
        
        removeDispatcher = new JButton("Remove Dispatcher");
        removeDispatcher.addActionListener(action -> removeDispatcher());
        
        addMachine = new JButton("Add Machine");
        addMachine.addActionListener(action -> addMachine());
        
        removeMachine = new JButton("Remove Machine");
        removeMachine.addActionListener(action -> removeMachine());
        
        maxConsumptionTime = new JSlider(2000, 15000, 5000);
        maxConsumptionTime.addChangeListener(this);
        maxConsumptionLabel = new JLabel("Max Consumption Time", JLabel.CENTER);
        
        maxProductionTime = new JSlider(2000, 15000, 3000);
        maxProductionTime.addChangeListener(this);
        maxProductionLabel = new JLabel("Max Production Time", JLabel.CENTER);
        
        maxConsumption = new JPanel(new BorderLayout());
        maxConsumption.add(maxConsumptionLabel, BorderLayout.NORTH);
        maxConsumption.add(maxConsumptionTime, BorderLayout.CENTER);
        
        maxProduction = new JPanel(new BorderLayout());
        maxProduction.add(maxProductionLabel, BorderLayout.NORTH);
        maxProduction.add(maxProductionTime, BorderLayout.CENTER);
        
        controls = new JPanel();
        controls.add(addDispatcher);
        controls.add(removeDispatcher);
        controls.add(maxConsumption);
        controls.add(addMachine);
        controls.add(removeMachine);
        controls.add(maxProduction);
        
        currentDetails = new JLabel(">>>Number of Dispatchers = " + dispatcherCounter +" , Number of Machines = " + machineCounter);
        add(currentDetails, BorderLayout.NORTH);
        add(drawPanel, BorderLayout.CENTER);
        add(controls, BorderLayout.SOUTH);
        
        timer = new Timer(1, execute ->
            {
                drawPanel.repaint();
            });
        timer.start();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        
        if (source.equals(maxConsumptionTime))
        {
            Machine.MAX_CONSUMPTION_TIME = source.getValue();
        }
        else
        {
            Machine.MAX_PRODUCTION_TIME = source.getValue();
        }
    }
    
    private class DrawingPanel extends JPanel
    {   public DrawingPanel()
        {   setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
            setBackground(Color.WHITE);
        }
    
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            
            for (int k = 0; k < 5; k++)
            {
                belts[k].drawBelt(g, 110, (k*110) + 50, 800, 100);
            }
        }
    }
    
    public static void main(String[] args) {
        FactorySimulatorGUI myPanel = new FactorySimulatorGUI();
        JFrame frame = new JFrame("Factory Simulator"); //create frame to hold our JPanel subclass	
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().add(myPanel);  //add instance of MyGUI to the frame
        frame.pack(); //resize frame to fit our Jpanel
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(new Point((d.width / 2) - (frame.getWidth() / 2), (d.height / 2) - (frame.getHeight() / 2)));
	//show the frame	
        frame.setVisible(true);
    }
    
    public void addMachine() {
        machines.push(new Machine(belts));
        Thread thread = new Thread(machines.peek());
        thread.start();
        machineCounter++;
        drawPanel.repaint();
        currentDetails.setText(">>>Number of Dispatchers = " + dispatcherCounter +" , Number of Machines = " + machineCounter);
    }
    
    public void removeMachine()
    {
        if(!machines.isEmpty())
        {
            Machine removed = machines.pop();
            
            for (int k = 0; k < 5; k++)
            {
                belts[k].disconnectMachine(removed);
            }
            
            removed.stop();
            drawPanel.repaint();
            machineCounter--;
            currentDetails.setText(">>>Number of Dispatchers = " + dispatcherCounter +" , Number of Machines = " + machineCounter);
        }
    }
    
    public void addDispatcher()
    {
        dispatchers.push(new Dispatcher(belts));
        Thread thread = new Thread(dispatchers.peek());
        thread.start();
        dispatcherCounter++;
        drawPanel.repaint();
        currentDetails.setText(">>>Number of Dispatchers = " + dispatcherCounter +" , Number of Machines = " + machineCounter);
    }
    
    public void removeDispatcher()
    {
        if(!dispatchers.isEmpty())
        {
            Dispatcher removed = dispatchers.pop();
            
            for (int k = 0; k < 5; k++)
            {
                belts[k].disconnectDispatcher(removed);
            }
            
            removed.stop();
            drawPanel.repaint();
            dispatcherCounter--;
            currentDetails.setText(">>>Number of Dispatchers = " + dispatcherCounter +" , Number of Machines = " + machineCounter);
        }
    }
    
}
