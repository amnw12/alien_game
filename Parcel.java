/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factorySimulation;

import java.awt.*;

/**
 *
 * @author amnwaqar
 */
public class Parcel<E> implements Comparable<Parcel<?>> {
    private E element;
    private Color colour;
    private int consumeTime;
    private int priority;
    private long timeStamp;

    public Parcel(E element, Color colour, int consumeTime, int priority) {
        this.element = element;
        this.colour = colour;
        this.consumeTime = consumeTime;
        this.priority = priority;
        this.timeStamp = System.nanoTime();
    }
    
    public void consume() {
        try {
            Thread.sleep(consumeTime);
        }
        catch (InterruptedException e){};
    }

    @Override
    public String toString() {
        return element + "(" + priority + ")";
    }
    
    public void drawBox(Graphics g, int x, int y, int width, int height)
    {
        g.setColor(colour);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawString(toString(), x + (width/2), y + (height/2));
    }
    
    @Override
    public int compareTo(Parcel<?> p) {
        
        if (priority > p.priority)
        {
            return 1;
        }
        else if (priority < p.priority)
        {       
            return -1;
        }
        else
        {
            if (timeStamp > p.timeStamp)
            {
                return 1;
            }
            else
            {
                return -1;
            }
        }
    }
}
