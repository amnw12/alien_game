/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factorySimulation;

import java.awt.Color;
import java.util.Random;

/**
 *
 * @author amnwaqar
 */
public class Machine implements Runnable{
    public static int MIN_CONSUMPTION_TIME = 1000, MAX_CONSUMPTION_TIME = 5000, MIN_PRODUCTION_TIME = 500, MAX_PRODUCTION_TIME = 3000;
    private ConveyorBelt[] belts;
    private Random rand;
    private boolean stopTask;

    public Machine(ConveyorBelt[] belts) {
        this.belts = belts;
        stopTask = false;
        rand  = new Random();
    }
    
    @Override
    public void run() {
        
        int k = 0;
        
        while (!stopTask)
        {
            if (!belts[k].isFull())
            {
                boolean connected = belts[k].connectMachine(this);

                if (connected)
                {
                    while(!belts[k].isFull())
                    {
                        belts[k].postParcel(createParcel(), this);
                    }

                    belts[k].disconnectMachine(this);
                }
            }

            if (k < belts.length-1)
            {
                k++;
            }
            else
            {
                k = 0;
            }
        }
    }
    
    public Parcel<?> createParcel()
    {
        Color colour = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
        int priority = rand.nextInt(3) + 1;
        char letter = (char)(rand.nextInt(26)+ 65);
        int consumeTime = rand.nextInt(MAX_CONSUMPTION_TIME) + MIN_CONSUMPTION_TIME;
        int productionTime = rand.nextInt(MAX_PRODUCTION_TIME) + MIN_PRODUCTION_TIME;
        
        try {
            Thread.sleep(productionTime);
        }
        catch (InterruptedException e){};
        
        return new Parcel(letter,colour,consumeTime,priority);
    }
    
    public void stop()
    {
        stopTask = true;
    }
}
