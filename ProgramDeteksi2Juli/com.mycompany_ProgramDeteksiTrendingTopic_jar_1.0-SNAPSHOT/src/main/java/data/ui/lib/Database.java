package data.ui.lib;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Dosen
 */
public class Database {
    public Properties mypanel;
    private String strNamaPanel;
    public Database(){
        
    }
    public String SettingPanel(String nmPanel){
        try{
            mypanel = new Properties();
            mypanel.load(new FileInputStream("lib/Database.ini"));
            strNamaPanel = mypanel.getProperty(nmPanel);
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
        return strNamaPanel;
    }
}
