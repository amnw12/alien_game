/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factorySimulation;

import java.util.Random;

/**
 *
 * @author amnwaqar
 */
public class Dispatcher implements Runnable{
    private ConveyorBelt[] belts;
    private boolean stopTask;

    public Dispatcher(ConveyorBelt[] belts) {
        this.belts = belts;
        stopTask = false;
    }
    
    @Override
    public void run() {
        
        int k = 0;
        
        while (!stopTask)
        {
            if (!belts[k].isEmpty())
            {
                boolean connected = belts[k].connectDispatcher(this);

                if (connected)
                {
                    Parcel<?> parcel = belts[k].getFirstParcel(this);

                    if (parcel != null)
                    {
                        parcel.consume();
                        parcel = belts[k].retrieveParcel(this);
                    }

                    belts[k].disconnectDispatcher(this);
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
    
    public void stop()
    {
        stopTask = true;
    }
}
