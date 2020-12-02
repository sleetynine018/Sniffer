package com.packetgui;

import javax.swing.*;
import javax.swing.border.Border;

import com.packetsniffer.*;
import org.pcap4j.core.PcapNetworkInterface;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class MainApp {

    private static JFrame mainFrame = new JFrame("SniffSniff");
    private static JPanel mainPanel = new JPanel();
    private static JTextArea netInterfaces = new JTextArea(10,30);

    private static JFrame chooseInterfaceFrame = new JFrame("Choose interface");
    private static JPanel chooseInterfacePanel = new JPanel();



    private static void submitInterfaceToSniff() {
        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(100,100,140,40);

        JLabel infoSubmitLabel = new JLabel();
        infoSubmitLabel.setText("Choose network interface");
        infoSubmitLabel.setBounds(10,10,130,100);

        JTextField interfaceInput = new JTextField();
        interfaceInput.setBounds(145,50,130,30);

        chooseInterfaceFrame.add(submitButton);
        chooseInterfaceFrame.add(infoSubmitLabel);
        chooseInterfaceFrame.add(interfaceInput);

        chooseInterfaceFrame.setSize(300,200);
        chooseInterfaceFrame.setLocationRelativeTo(mainFrame);
        chooseInterfaceFrame.setResizable(false);
        chooseInterfaceFrame.setLayout(null);
        chooseInterfaceFrame.setVisible(true);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Submit button was pressed, going to listen on: " + interfaceInput.getText());
            }
        });

    }

    private static JButton addCheckButton(JTextArea netInterfaces, JFrame frame) {

        JButton checkInterfaces = new JButton("Check interfaces");

        checkInterfaces.addActionListener(new ActionListener() {
            int numOfClicks = 0;

            @Override
            public void actionPerformed(ActionEvent e) {

                //Resize all windows after button click
                netInterfaces.setBounds(50,10,500,250);
                //checkInterfaces.setBounds(250,300,200,40);
                List<PcapNetworkInterface> netsInterfaces;

                try {
                    netsInterfaces = Sniffer.checkInterfacesManual();

                    if (numOfClicks >0) netInterfaces.setText(""); //Clears text after every click

                    int interfaceID = 1;

                    //Prints out a new line for every interface available
                    for( PcapNetworkInterface netInterface :  netsInterfaces){
                        netInterfaces.append('\n' + "[" + Integer.toString(interfaceID) + "] " + netInterface.getDescription() + "\nAddress -> " + netInterface.getAddresses().get(0).getAddress() + "\n" );
                        interfaceID++;
                    }

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                numOfClicks++;

                checkInterfaces.setVisible(false);
                mainPanel.remove(checkInterfaces);

                submitInterfaceToSniff();
            }


        });
        return checkInterfaces;
    }

    private static void createAndShowMainGUI() {
        //Components initialize

        JScrollPane scroll = new JScrollPane (netInterfaces,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); //Adds a scroll to textArea

        //Text area edits
        netInterfaces.setEditable(false);
        netInterfaces.setLineWrap(true);

        //Add components to main panel
        mainPanel.add(scroll, BorderLayout.EAST);
        mainPanel.add(addCheckButton(netInterfaces, mainFrame));


        mainFrame.add(mainPanel); //Adds main panel to frame


        //MainFrame tweaks
        mainFrame.setResizable(false); //Doesnt let resize window
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setLayout(null);
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        createAndShowMainGUI();
    }
}
