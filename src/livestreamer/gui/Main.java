/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package livestreamer.gui;

import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Fanciu
 */
public class Main extends JFrame {

    public static void main(String[] args) throws IOException {

        readStreamCFG();
        setOptionCfg();

        MainInterface f = new MainInterface();
        f.setVisible(true);
        if(Variables.autoStreamCheck){
        Thread thread = null;
        StreamCheckService runnable = new StreamCheckService();
        thread = new Thread(runnable);
        thread.start();
        Variables.runnable = runnable;
        }

    }

    private static void setOptionCfg() {
        File file = new File(Variables.optionsCfg);
        if (file.exists()) {
            BufferedReader br = null;

            try {

                String sCurrentLine;

                br = new BufferedReader(new FileReader(Variables.optionsCfg));

                while ((sCurrentLine = br.readLine()) != null) {
                   ArrayList<String> items = new ArrayList<>(Arrays.asList(sCurrentLine.split(";"))); 
                   Variables.autoStreamCheck=Boolean.valueOf(items.get(0));
                   Variables.startAtBoot=Boolean.valueOf(items.get(1));
                   Variables.refreshTime =Integer.parseInt( items.get(2)) *60*1000;
                }

            } catch (IOException e) {
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException ex) {
                }
            }

        } else { 
            Variables.autoStreamCheck=true;
            Variables.startAtBoot=false;
            Variables.refreshTime =5 *60*1000;
        }
    }

    private static void readStreamCFG() {
        File file = new File(Variables.streamSaveOnCfg);
        if (file.exists()&&file.length()!=0) {
            BufferedReader br = null;

            try {

                String sCurrentLine;

                br = new BufferedReader(new FileReader(Variables.streamSaveOnCfg));

                while ((sCurrentLine = br.readLine()) != null) {
                    ArrayList<String> items = new ArrayList<>(Arrays.asList(sCurrentLine.split(";")));
                    ArrayList<String> itemsQuality = new ArrayList<>(Arrays.asList(items.get(2).trim().split(",")));

                    StreamData placeholder = new StreamData();
                    placeholder.setAlias(items.get(0));
                    placeholder.setUrl(items.get(1));
                    for (int i = 0; i < itemsQuality.size(); i++) {
                        placeholder.listOfQuality.add(itemsQuality.get(i).toString());
                    }
                    Variables.listOfData.add(placeholder);
                }

            } catch (IOException e) {
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException ex) {
                }
            }

        } else {
            StreamData placeholder = new StreamData();
            placeholder.setAlias("Add Stream");
            placeholder.setUrl("http://www.google.it");
            placeholder.listOfQuality.add("null");
            Variables.listOfData.add(placeholder);
        }
    }

}
