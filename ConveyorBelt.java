/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factorySimulation;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author amnwaqar
 */
public class ConveyorBelt {
    private int maxCapacity;
    private Machine connectedMachine;
    private Dispatcher connectedDispatcher;
    private Queue<Parcel<?>> PriorityQueue;

    public ConveyorBelt(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.connectedDispatcher = null;
        this.connectedMachine = null;
        this.PriorityQueue = new LinkedList<>();
    }

    public ConveyorBelt() {
        this.maxCapacity = 10;
        this.connectedDispatcher = null;
        this.connectedMachine = null;
        this.PriorityQueue = new LinkedList<>();
    }
    
    public synchronized boolean connectMachine(Machine machine){
        if (connectedMachine == null)
        {
            connectedMachine = machine;
            return true;
        }
        return false;
    }
    
    public synchronized boolean connectDispatcher(Dispatcher dispatcher){
        if (connectedDispatcher == null)
        {
            connectedDispatcher = dispatcher;
            return true;
        }
        return false;
    }
    
    public synchronized boolean disconnectMachine(Machine machine){
        if (connectedMachine != null)
        {
            if (connectedMachine.equals(machine))
            {
                connectedMachine = null;
                return true;
            }
        }
        return false;
    }
    
    public synchronized boolean disconnectDispatcher(Dispatcher dispatcher){
        if (connectedDispatcher != null)
        {
            if (connectedDispatcher.equals(dispatcher))
            {
                connectedDispatcher = null;
                return true;
            }
        }
        return false;
    }
    
    public int size(){
        return PriorityQueue.size();
    }
    
    public boolean isEmpty() {
        return (size() == 0);
    }
    
    public boolean isFull() {
        return (size() == maxCapacity);
    }
    
    public synchronized boolean postParcel(Parcel<?> p, Machine machine){
        
        if (connectedMachine != null)
        {
            if (connectedMachine.equals(machine))
            {
                if (!isFull())
                {
                    return PriorityQueue.add(p);
                }
            }
        }
        return false;
    }
    
    public synchronized Parcel<?> getFirstParcel(Dispatcher dispatcher){
        if (connectedDispatcher != null)
        {
            if (connectedDispatcher.equals(dispatcher))
            {
                if (!isEmpty())
                {
                    return PriorityQueue.peek();
                }
            }
        }
        return null;
    }
    
    public synchronized Parcel<?> retrieveParcel(Dispatcher dispatcher){
        if (connectedDispatcher != null)
        {
            if (connectedDispatcher.equals(dispatcher))
            {
                if (!isEmpty())
                {
                    return PriorityQueue.poll();
                }
            }
        }
        return null;
    }
    
    public void drawBelt(Graphics g, int x, int y, int width, int height) {
        int parcelWidth = width / maxCapacity;
        
        for (int k = 0; k < maxCapacity; k++)
        {
            g.setColor(Color.blue);
            g.drawRect(x+(parcelWidth*k), y, parcelWidth, height);
        }
        
        Iterator iterator = PriorityQueue.iterator();
        int i =0;
        
        while (iterator.hasNext())
        {
            Parcel<?> parcel = (Parcel<?>)iterator.next();
            parcel.drawBox(g, x+(parcelWidth*i), y, parcelWidth, height);
            i++;
        }
        
        if (connectedDispatcher != null)
        {
            g.setColor(Color.blue);
            g.fillOval(x-(parcelWidth), y, parcelWidth, height);
        }
        
        if (connectedMachine != null)
        {
            g.setColor(Color.red);
            g.fillOval(x+width, y, parcelWidth, height);
        }
    }
}
