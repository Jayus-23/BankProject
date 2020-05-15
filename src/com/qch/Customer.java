package com.qch;

import javax.swing.*;
import java.awt.*;

/**
 * @fun 顾客类    final限定只读对象,线程绝对安全类,无须同步
 */
public class Customer extends JPanel implements Runnable {

    private final int serviceTime;
    private final int ticketId;
    BankClient bankClient;
    private volatile boolean isStanding=false;
    private JLabel label;
    private Chair chair;
    private Chair[] chairs;

    public Customer(BankClient bankClient, int ticketId, int serviceTime) {
        this.bankClient=bankClient;
        this.ticketId=ticketId;
        this.serviceTime=serviceTime;
        drawCustomer();
    }

    private void drawCustomer() {
        label=new JLabel( String.valueOf( this.ticketId ) );
        label.setBounds( 0, 0, 35, 35 );
        label.setFont( new Font( Font.SERIF, 1, 20 ) );
        label.setHorizontalAlignment( 0 );
        add( this.label );
        setBackground( Color.PINK );
        setBounds( 0, 0, 40, 40 );
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void run() {
        bankClient.customers.add( this );
        if (!getChair()) {
            isStanding=true;
            bankClient.addStandingCustomer();
            while (true) {
                if (getChair()) {
                    bankClient.removeStandingCustomer();
                    break;
                }
            }
        }
    }
    private boolean getChair() {
        chairs=bankClient.waitingArea.getChairs();
        synchronized (chairs) {
            for (int i=0; i < chairs.length; ++i) {
                if (chairs[i].getChairSemaphore().tryAcquire()) {
                    chair=chairs[i];
                    chair.setChairCustomer( this );
                    isStanding=false;
                    return true;
                }
            }
            return false;
        }
    }

    public void toTeller() throws InterruptedException {
        if (isStanding) {
            bankClient.removeStandingCustomer();
        } else {
            chair.removeChairCustomer();
            chair.getChairSemaphore().release();
        }
    }
}
