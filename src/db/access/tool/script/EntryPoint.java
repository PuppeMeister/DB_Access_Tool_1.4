/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.access.tool.script;

import db.access.tool.gui.MainGUI;
import java.awt.Component;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author rinnadia
 */
public class EntryPoint {
    
    private static String configFolder = "./Configuration";
    private static String configFile = "./Configuration/configuration.conf";
    
    public static void main(String[] args){
       
        System.err.println("Initializing Program");
        boolean checkConfigResult = false;
        checkConfigResult = checkConfigurationFile(checkConfigResult);
        
        if(checkConfigResult == true){
            try {
                
                MainGUI objMainGUI = new MainGUI();
                objMainGUI.setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(EntryPoint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            boolean createConfigResult = false;
            createConfigResult = createConfigFile(createConfigResult);
            
            Component frame = null;
            String messageShowDialog = null;
            if(createConfigResult == true){
                
                
                messageShowDialog = "Configuration file has been created in Configuration Folder."
                        + " Please input values of required parameters.";
                JOptionPane.showMessageDialog(frame, messageShowDialog, "Notification", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                
                messageShowDialog = "Configuration file cannot found can created.";
                JOptionPane.showMessageDialog(frame, messageShowDialog, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private static boolean checkConfigurationFile(boolean result){
     
        Path path = Paths.get(configFile);
        //if directory exists?
        if (!Files.exists(path)) {
            result = false;
                System.out.println("Configuration file is not found.");
        }
        else{
            result = true;
            System.out.println("Configuration file is found.");
            
        }
        return result;
    }
    
    private static boolean createConfigFile(boolean processResult){
        
        Path path = Paths.get(configFolder);
        //if directory exists?
        if (!Files.exists(path)) {
            System.out.println("Configuration folder is not found.");
            try {
                Files.createDirectories(path);
                System.out.println("Configuration folder is initialized.");
            } catch (IOException e) {
                //fail to create directory
                System.out.println("Configuration folder initialization is failed.");
                System.out.println(e);
            }
        }
        
        //Create Configuration File
        
        String[] multilineContens ={"#Informasi Server, Jika server lebih dari satu, duplicate sintak dari bagian awal sampai akhir \r\n",
                                    "LIST_SERVER=", 
                                    "#Awal Informasi Server",
                                    "IP_[isi dengan nama server]=",
                                    "PORT_[isi dengan nama server]=",
                                    "USER_[isi dengan nama server]=",
                                    "PASSWORD_[isi dengan nama server]=",
                                    "#Akhir Informasi Server \r\n",
                                    "#List User",
                                    "LIST_USER= \r\n",
                                    "#Konfigurasi Admin",
                                    "DB_ADMIN_IP=",
                                    "DB_ADMIN_PORT=",
                                    "DB_ADMIN_USER=",
                                    "DB_ADMIN_PASS=",
                                    "DB_ADMIN_DATABASE=",
                                    "DB_ADMIN_TABLE =",
                                    "LIST_ADMIN="};
        
        
        Set<OpenOption> options = new HashSet<OpenOption>();
        options.add(APPEND);
        options.add(CREATE);
        
        Path file = Paths.get(configFile);
        
        try (SeekableByteChannel sbc
                = Files.newByteChannel(file, options)) {
            
            for(String con : multilineContens){
                //Adding new line
                String content = con+"\r\n";
                byte data[] = content.getBytes();
                ByteBuffer bb = ByteBuffer.wrap(data);
                sbc.write(bb);
                processResult = true;
            }
            System.out.println("Configuration file has been created.");
            
        } catch (IOException x) {
            System.out.println("Configuration file cannot be created.");
            System.out.println(x);
            processResult = false;
        }
        return processResult;
    }
    
    public static String getConfigFilePath(){
        return configFile;
    }
    private static void shutDownApp(){
        System.exit(0);
    }
}
