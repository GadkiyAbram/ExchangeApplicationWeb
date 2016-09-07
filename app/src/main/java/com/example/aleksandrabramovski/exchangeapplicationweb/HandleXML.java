package com.example.aleksandrabramovski.exchangeapplicationweb;

import android.support.v7.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Aleksandr.Abramovski on 21/08/2016.
 */
public class HandleXML extends AppCompatActivity{
    public int flag = 0;
    private String index = "index";
    private String currency = "currency";
    private String urlString = null;
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;
    public static String nameCurrency;
    public static String nominalString;

//    initializing data from "MainActivity class":
//    we can use MainActivity.data directly in expressions


    public HandleXML(String url) {
        this.urlString = url;
    }
    public String getIndex(){
        return index;
    }

    public String getCountry() {
        return currency;
    }

    public String parseXMLAndStoreIt(XmlPullParser xmlResourceParser) {
        int eventType;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            eventType = xmlResourceParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.TEXT:
                        if (xmlResourceParser.getText().equals(MainActivity.selectedCurrency)) {
                            index = xmlResourceParser.getText();
                            flag = 1;
                        } else if (flag > 0) {
                            flag++;
                            if (flag == 3) {
                                nominalString = xmlResourceParser.getText();
                            } else if (flag == 5){
                                nameCurrency = xmlResourceParser.getText();
                            } else if (flag == 7){
                                currency = xmlResourceParser.getText();
                                currency = currency.replace(",", ".");
                            }
                        }
                }
                eventType = xmlResourceParser.next();
            }
            parsingComplete = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public void fetchXML(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream stream = conn.getInputStream();
                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser xmlResourceParser = xmlFactoryObject.newPullParser();

                    xmlResourceParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    xmlResourceParser.setInput(stream, null);

                    parseXMLAndStoreIt(xmlResourceParser);
                    stream.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}

