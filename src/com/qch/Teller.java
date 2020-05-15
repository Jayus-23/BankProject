package com.qch;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * @fun 储蓄员类
 */
public class Teller extends JPanel implements Runnable {

    private final int tellerId;
    BankClient bankClient;
    private JLabel infoLabel;
    private JButton servingButton;
    private Customer customer;
    private volatile boolean isStart;

    public Teller(BankClient bankClient, int tellerId) {
        this.bankClient=bankClient;
        this.tellerId=tellerId;
        drawTeller();
    }
    private void drawTeller() {
        setLayout( null );
        this.drawInfoLabel();
        this.drawServeButton();
        this.setBorder( BorderFactory.createLineBorder( Color.PINK ) );
    }
    private void drawServeButton() {
        servingButton=new JButton( "开始办理" );
        servingButton.setBounds( 90, 70, 120, 30 );
        this.servingButton.addActionListener( (e) -> {
            if (customer != null) {
                isStart=true;
            }
        } );
        servingButton.setFont( new Font( Font.SERIF, 0, 10 ) );
        add( this.servingButton );
    }
    private void drawInfoLabel() {
        infoLabel=new JLabel();
        infoLabel.setHorizontalAlignment( 0 );
        infoLabel.setBounds( 90, 0, 140, 60 );
        setFont( new Font( Font.SERIF, Font.BOLD, 100 ) );
        infoLabel.setText( "待业中..." );
        add( infoLabel );
    }
    public void run() {
        while (true) {
            while (true) {
                if ((customer=bankClient.customers.poll()) != null) {
                    break;
                }
            }
            try {
                customer.toTeller();
                setInfoLabel( this.customer.getTicketId() + " 号顾客到 " + tellerId + " 号窗口" );
            } catch (InterruptedException e) {}

            while (!isStart) {
            }

            try {
                TimeUnit.MILLISECONDS.sleep( customer.getServiceTime() );
            } catch (InterruptedException e) {}
            repaint();
            customer=null;
            isStart=false;
        }
    }

    public void setInfoLabel(String info) {
        this.infoLabel.setText( info );
    }
}
