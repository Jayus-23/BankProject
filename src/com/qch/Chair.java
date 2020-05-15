package com.qch;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Semaphore;

/**
 * @fun 椅子面板类
 */
public class Chair extends JPanel {


    private final int chairId;
    private JLabel idLabel;
    private Semaphore chairSemaphore;
    private Customer customer;

    public Chair(int chairId) {
        this.chairId=chairId;
        chairSemaphore=new Semaphore(1);
        draw();
    }

    private void draw() {
        setLayout( null );
        setBounds(50 * chairId + 35*((chairId+1)*2-1), 25, 40, 90);
        idLabel = new JLabel(String.valueOf(this.chairId+1));
        idLabel.setBounds(20, 72, 40, 15);
        idLabel.setFont(new Font(Font.SERIF, 1, 15));
        add(idLabel);
    }

    public Semaphore getChairSemaphore() {
        return this.chairSemaphore;
    }

    public void removeChairCustomer() throws InterruptedException {
        remove(customer);
        customer=null;
        repaint();
    }

    public void setChairCustomer(Customer customer) {
        this.customer=customer;
        //将顾客组件添加到该椅子组件
        add(customer);
        repaint();
    }

    public int getChairId() {
        return chairId;
    }
}
