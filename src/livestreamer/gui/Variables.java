/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package livestreamer.gui;

import com.sun.jndi.toolkit.url.Uri;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Fanciu
 */
public class Variables {
    static final String infoString1 ="<HtMl>This gui has been made by Raizer. You can email me @ <a href=\"mailto:raizer.software@gmail.com\">raizer.software@gmail.com</a><br><br>"
            + "<HtMl>The lifestreamer service that this application use and support is made by chrippa.<br> "
            + "<HtMl>You can find him @ http://livestreamer.tanuki.se or @ https://github.com/chrippa/livestreamer<br><br>"
            + "<HtMl>This software supports: Twitch.tv, Justin.tv, Ustream, Dailymotion, Youtube Live and Livestream";
       
    static final String streamSaveOnCfg = "streamlink.cfg";
    static StreamCheckService runnable = null;
    static final String optionsCfg = "options.cfg";
    static ArrayList<StreamData> listOfData = new  ArrayList();
    static final String error="error";
    static final String disconnectedIcon ="/livestreamer/gui/Image/network_disconnected.png";
    static final String warningPopupImage ="/livestreamer/gui/Image/Kappa.png";
    static final String appIcon ="/livestreamer/gui/Image/Kappa.png";
    static final String connectedIcon ="/livestreamer/gui/Image/network_connected.png";
    static int refreshTime = 20000;
    static final String javaLaunchVariables ="javaw -Xmx200m -jar";
    static Boolean startAtBoot = false;
    static Boolean autoStreamCheck = true;
 
}

class StreamData{
    public String alias;
    public String url;
    public ArrayList<String> listOfQuality = new  ArrayList();

    public ArrayList<String> getListOfQuality() {
        return listOfQuality;
    }

    public void setListOfQuality(ArrayList<String> listOfQuality) {
        this.listOfQuality = listOfQuality;
    }
            
            
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
}