/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package livestreamer.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

/**
 *
 * @author Fanciu
 */
public class StreamCheckService implements Runnable {
 private volatile boolean running = true;
  public void terminate() {
        running = false;
    }
  
  
    @Override
    public void run(){
        running = true;
while (running) {
         
            for (int k = 0; k < Variables.listOfData.size(); k++) {
                String url = "";
                url = Variables.listOfData.get(k).url;

                String command = "livestreamer " + url;
                String commandOutput = "";
                System.out.println(command);

                try {
                    int g = 0;
                    String line;
                    Process p = Runtime.getRuntime().exec("cmd /c " + command);
                    BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                    while ((line = bri.readLine()) != null) {
                        System.out.println(line);
                        if (g == 1) {
                            commandOutput = line;
                        }
                        g++;

                    }
                    bri.close();
                    while ((line = bre.readLine()) != null) {
                        System.out.println(line);
                    }
                    bre.close();
                    p.waitFor();
                } catch (Exception err) {
                    err.printStackTrace();
                }

                String check = "error:";
                if (commandOutput.trim().equals("")) {
                    commandOutput = "error:";
                }
                check = commandOutput.substring(0, 5);
                System.out.println(check);
                if (check.equals(Variables.error)) {
                    Variables.listOfData.get(k).listOfQuality.clear();
                    Variables.listOfData.get(k).listOfQuality.add("null");

                } else {
                    if (Variables.listOfData.get(k).listOfQuality.get(0).equals("null")) {
                        String[] plc = commandOutput.split(":");
                        String[] plc2 = plc[1].trim().split(",");
                        Variables.listOfData.get(k).listOfQuality.clear();
                        for (int i = 0; i < plc2.length; i++) {
                            Variables.listOfData.get(k).listOfQuality.add(plc2[i]);
                        }
                        lunchNotification(k);
                    }

                }

            }
            try {
                Thread.sleep(Variables.refreshTime);
            } catch (InterruptedException ex) {
                Logger.getLogger(StreamCheckService.class.getName()).log(Level.SEVERE, null, ex);
            }
        
}
    }

    private void lunchNotification(int k) {
        String message = "";
        String header = "The Streamer:<br> " + Variables.listOfData.get(k).getAlias() + " is now online!";
        final JFrame frame = new JFrame();
        frame.setMaximumSize(new Dimension(900, 100));
        frame.setMinimumSize(new Dimension(300, 100));
        frame.setUndecorated(true);
        frame.setLayout(new GridBagLayout());
        frame.setResizable(false);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0f;
        constraints.weighty = 1.0f;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.BOTH;
        JLabel headingLabel = new JLabel("<HtMl>" + header);
        headingLabel.setSize(headingLabel.getPreferredSize());
        ImageIcon imgThisImg = new ImageIcon(getClass().getResource(Variables.warningPopupImage));
        headingLabel.setIcon(imgThisImg); // --- use image icon you want to be as heading image.
        headingLabel.setOpaque(false);
        frame.add(headingLabel, constraints);
        constraints.gridx++;
        constraints.weightx = 0f;
        constraints.weighty = 0f;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.NORTH;

        JButton closeButton = new JButton(new AbstractAction("x") {
            @Override
            public void actionPerformed(final ActionEvent e) {
                frame.dispose();
            }
        });
        closeButton.setMargin(new Insets(1, 4, 1, 4));
        closeButton.setFocusable(false);
        frame.add(closeButton, constraints);
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.weightx = 1.0f;
        constraints.weighty = 1.0f;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.BOTH;
        JLabel messageLabel = new JLabel("<HtMl>" + message);
        messageLabel.setSize(messageLabel.getPreferredSize());
        frame.add(messageLabel, constraints);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();// size of the screen
        Insets toolHeight = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());// height of the task bar
        frame.pack();
        frame.setLocation(scrSize.width - frame.getWidth(), scrSize.height - toolHeight.bottom - frame.getHeight());
        frame.setAlwaysOnTop(true);

        frame.setVisible(true);
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000); // time after which pop up will be disappeared.
                    frame.dispose();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        ;
    }

    .start();
    rewriteCfg();  
    }
    
    
    private void rewriteCfg() {
        BufferedWriter writer = null;
        String lineToWrite;
        String listOfQuality = "";
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(Variables.streamSaveOnCfg), "utf-8"));
            for (int i = 0; i < Variables.listOfData.size(); i++) {
                for (int j = 0; j < Variables.listOfData.get(i).listOfQuality.size(); j++) {
                    if (j == 0) {
                        listOfQuality = Variables.listOfData.get(i).listOfQuality.get(j).toString();
                    } else {
                        listOfQuality = listOfQuality + "," + Variables.listOfData.get(i).listOfQuality.get(j).toString();
                    }
                }
                if (listOfQuality.trim().equals("")) {
                    lineToWrite = Variables.listOfData.get(i).alias + ";" + Variables.listOfData.get(i).url + ";" + "null";
                } else {
                    lineToWrite = Variables.listOfData.get(i).alias + ";" + Variables.listOfData.get(i).url + ";" + listOfQuality;
                }
                writer.write(lineToWrite);
                writer.newLine();
                listOfQuality = "";
            }

        } catch (IOException ex) {
            // report
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
            }
        }

    }
    
    
}
