package com.qch;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @fun 银行系统操作客户端
 */
public class BankClient extends Frame {


    public static BankClient bankClient=new BankClient();

    Teller[] tellers=new Teller[3];
    static ArrayBlockingQueue<Customer> customers=new ArrayBlockingQueue<>( 100, true );
    private int customerTotalCounter=1;
    int standingCustomerCounter=0;

    private Random random=new Random( 47 );
    JFrame frame=new JFrame( "超有钱银行叫号系统" );
    JLabel welcomeLabel=new JLabel( "欢迎来到超有钱银行" );
    JButton enterButton=new JButton( "进入银行并取号" );
    JPanel tellerAreaPanel=new JPanel();
    JLabel standingCounterLabel=new JLabel("0人");
    JLabel standingImageLabel=new JLabel();

    static WaitingArea waitingArea=new WaitingArea();

    public static void main(String[] args) {
        bankClient.initDrawing();
        bankClient.startTellersThreads();
    }

    private void startTellersThreads() {
        for (int i=0; i < tellers.length; i++) {
            new Thread( tellers[i] ).start();
        }
    }

    public void initDrawing() {
        frame.setLayout( null );
        frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        Toolkit toolkit=Toolkit.getDefaultToolkit();
        Dimension screenSize=toolkit.getScreenSize();
        int screenWidth=screenSize.width;
        int screenHeight=screenSize.height;
        System.out.println(screenHeight);
        System.out.println(screenWidth);
        frame.setLocation( 0, 0 );
        frame.setSize( screenWidth, screenHeight );
        Toolkit tool=frame.getToolkit();
        Image image=tool.getImage( "images//bank.jpg" );
        frame.setIconImage( image );
        welcomeLabel.setBounds( 0, 0, screenWidth, screenWidth*23/500 );
        welcomeLabel.setFont( new Font( Font.SERIF, Font.BOLD, screenWidth*23/500 ) );
        welcomeLabel.setHorizontalAlignment( 0 );
        enterButton.setBounds( 500, 540, 190, 100 );
        enterButton.addActionListener( (e -> newCustomer()) );
        enterButton.setText( "" );
      //设置储蓄员面板
        tellerAreaPanel.setLayout( null );
        tellerAreaPanel.setBounds( 30, 130, 1330, 140 );
        for (int i=0; i < 3; i++) {
            tellers[i]=new Teller( this, i + 1 );
            tellers[i].setBounds( 500 * i, 10, 320, 120 );
            tellerAreaPanel.add( this.tellers[i] );
        }
        waitingArea.setBounds( 30, 300, 1330, 120 );
        standingCounterLabel.setBorder( BorderFactory.createLineBorder( Color.yellow ) );
        standingCounterLabel.setBounds( 700, 430, 100, 100 );
        standingCounterLabel.setFont( new Font( Font.SERIF, 1, 50 ) );
        standingCounterLabel.setHorizontalTextPosition( 0 );
        standingImageLabel.setBounds( 400, 430, 300, 100 );
        standingImageLabel.setIcon( new ImageIcon( "images//standing.jpg" ) );
        //将基本控件都加入框架
        frame.add( tellerAreaPanel );
        frame.add( enterButton );
        frame.add( welcomeLabel );
        frame.add( waitingArea );
        frame.add( standingCounterLabel );
        frame.add( standingImageLabel );
        frame.setVisible( true );
    }
    private void newCustomer() {
        Customer customer=new Customer( this, customerTotalCounter++, random.nextInt( 1000 ) );
        new Thread( customer  ).start();
        enterButton.setText( "Entered 顾客ID: " + customer.getTicketId() );
    }
    private void printInfo() {
        standingCounterLabel.setText( "" + standingCustomerCounter+"人" );
    }


    public synchronized void addStandingCustomer() {
        bankClient.standingCustomerCounter++;
        printInfo();
        repaint();
    }

    public synchronized void removeStandingCustomer() {
        bankClient.standingCustomerCounter--;
        printInfo();
        repaint();
    }

    public static class WaitingArea extends JPanel {
        private Chair[] chairs=new Chair[10];

        public WaitingArea() {
            setLayout(null);
            for(int i = 0; i < 10; ++i) {
                this.chairs[i] = new Chair(i);
                this.add(this.chairs[i]);
            }
        }

        public Chair[] getChairs() {
            return this.chairs;
        }
    }
}
