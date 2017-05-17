/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.access.tool.script;

import java.awt.Component;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author MECC
 */
public class MainController {
    
    private static String[] listDatabase;
    private static String[] listAdmin;
    
    public static HashMap dataPlace;
    //data from getServerDatabase
    public static String[] data;
    
    public static String[] getListDatabaseServerTypeOriginal() throws IOException{
        
        String[] data = ConfigFileReader.getServerDatabase();
        ArrayList<String> getDataReady = new ArrayList<String>();
        int indexData = 0;
        while(indexData<data.length){
            getDataReady.add(data[indexData]);
            indexData+=4;
        }
        String[] result = new String[getDataReady.size()];
        result =  getDataReady.toArray(result);
        
        /*System.out.println("This is the result!! Look at these!!");
        for(String a : result){
            System.out.println(a);
        }*/
        
        /*System.out.println("This is inside admin config");
        for(String b : ConfigFileReader.getAdminConfig()){
            System.out.println(b);
        }*/
        return result;
    }
    
    public static String[] getListDatabaseServer() throws IOException{
        
       data = ConfigFileReader.getServerDatabase();
        ArrayList<String> getDataReady = new ArrayList<String>();
        int indexData = 0;
        while(indexData<data.length){
            getDataReady.add(data[indexData]);
            indexData+=4;
        }
        String[] result = new String[getDataReady.size()];
        result =  getDataReady.toArray(result);
        
        System.out.println("Server names : dari tempat pertamo, men ado duo berarti ado yang manggil duo kali");
        //return result;
       
       return ConfigFileReader.getServerName;
    }
    
    
    
    public static String[] getListAdmin() throws IOException{
        String[] data = ConfigFileReader.getAdminList();
        return data;
    }
    
    //public static String[] getDSAA(String selectedServer) throws IOException{
    public static void getDSAA(String selectedServer) throws IOException{
        
        //data = ConfigFileReader.getServerDatabase();
        //String[] result = new String[4]; 
        
        dataPlace = new HashMap();
        
        boolean done = false;
        int dataIndex=0;
        while(done!=true){
            
            if(data[dataIndex].matches(selectedServer)){
                
                /* host*/ //result[0] = data[dataIndex];
                dataPlace.put("host", data[dataIndex]);
                /* port*/ //result[1] = data[dataIndex+1];
                dataPlace.put("port", data[dataIndex+1]);
                /* user*/ //result[2] = data[dataIndex+2];
                dataPlace.put("username", data[dataIndex+2]);
                /* password*/ //result[3] = data[dataIndex+3];
                dataPlace.put("password", data[dataIndex+3]);
                
                done = true;
            }
            dataIndex++;
        }
        
       // return result;
    }
   
    public static String[] getDatabaseList(String selectedServer) throws SQLException, IOException{
        getDSAA(selectedServer);
        
        DatabaseAffairs dbObject = new DatabaseAffairs();
        
        
        String[] dbList = dbObject.getDBList(String.valueOf(dataPlace.get("host")),
                                             String.valueOf(dataPlace.get("username")),
                                             String.valueOf(dataPlace.get("password")),
                                             String.valueOf(dataPlace.get("port")));
        return dbList;
    }
    
    //public static String[] getTableList() throws SQLException{
    public static String[] getTableList(String selectedDatabase) throws SQLException, IOException{
       
        DatabaseAffairs dbObject = new DatabaseAffairs();
        String[] tabelList = dbObject.getTableList(String.valueOf(dataPlace.get("host")),
                                             String.valueOf(dataPlace.get("username")),
                                             String.valueOf(dataPlace.get("password")),
                                             String.valueOf(dataPlace.get("port")),
                                             selectedDatabase);
        return tabelList;
    }
    
    public static String[] getUserList(){
        return ConfigFileReader.getUserList();
    }
    
    public static String excecuteOrder(){
        String status = null;
        
        return status;
    }
    
    public static String validateData(HashMap data) throws SQLException{
        
        //System.out.println("get validate data!");
        String status = "pass";
        //System.out.println(validateField(data).matches("notPassed"));
        //System.out.println(validateAction(data).matches("notPassed"));
        System.out.println("The Data ==> "+data);
        if(validateField(data).matches("notPassed")==true||validateAction(data).matches("notPassed")==true){
            Component frame = null;
            JOptionPane.showMessageDialog(frame, "Please complete the form","Stop",JOptionPane.ERROR_MESSAGE);
        }
        else{
            
            String query = "GRANT ";
            String accessType = "";
            if(data.containsKey("all")){
                accessType = "ALL";
                System.out.println(data.get("host"));
                query += "ALL ON "+data.get("database")+"."+data.get("table")+" TO "+data.get("user")+"@"+data.get("host");
                System.out.println(query);
                
            }
            else{
                
                String[] action = {"select", "insert", "delete", "update"};
                
                for(String actionValue : action){
                    
                    if(data.containsKey(actionValue)){
                        query += actionValue+", ";
                        accessType += actionValue+", ";
                    }
                    
                }
                int lastIndex = query.length();
                query = query.substring(0, lastIndex-2);
                //System.out.println(lastIndex);
                query += " ON "+data.get("database")+"."+data.get("table")+" TO "+data.get("user")+"@"+data.get("host");
                //System.out.println(query);
                
                
            }
            
            //Record admin activity
            String[] getAdminConfig = ConfigFileReader.getAdminConfig();
            String queryAdmin =" INSERT INTO "+getAdminConfig[4]+".`"+getAdminConfig[5]+"` "+
                               " (`reqid`, `reqdate`, `username`, `dbserver`, `dbname`, `tblname`, `access`,"
                               + " `duration`, `remark`, `grantedby`, `grantdate`) "+
                               "VALUES "
                                 + "(' ',"
                                 + " NOW(),"
                                 + "'"+data.get("user")+"',"
                                 + "'"+data.get("host")+"',"
                                 + "'"+data.get("database")+"',"
                                 + "'"+data.get("table")+"',"
                                 + "'"+accessType+"',"
                                 + "'',"
                                 + "'',"
                                 + "'"+data.get("admin")+"',"
                                 + " NOW())";
            
            System.out.println(queryAdmin);
            
            DatabaseAffairs objDA = new DatabaseAffairs();
            objDA.getAccessExecute(dataPlace, query);
            objDA.getAdminActivityRecorded(getAdminConfig, queryAdmin);
            Component frame = null;
            JOptionPane.showMessageDialog(frame, query,"Berhasil",JOptionPane.INFORMATION_MESSAGE);
        }
        
        return status;
        
    }
    
    public static String validateField(HashMap data){
    
        //System.out.println("The data -->"+data);
        String status = "passed";
        String[] fieldValidator =  {"table", "user", "database", "admin"};
        
        for(String field : fieldValidator){
            if(!(data.containsKey(field))){
              status = "notPassed";   
            }
        }
        
        return status;        
    }
    
    public static String validateAction (HashMap data){
        String status = "notPassed";
        String[] actionValidator = {"all", "select", "insert", "update", "delete"};
        
        for(String action : actionValidator){
           if((data.containsKey(action))== true){
                status = "passed";
            }
        }
        
        return status;
    }
    
    public static String[] getData(String paramGet) {
        String[] result = new String[2];

        return result;
    }

    private static String[] fetchDataType1(String[] result, String[] paramGet) {

        String configFile = EntryPoint.getConfigFilePath();
        InputStream input;
        try {
            
            input = new FileInputStream(configFile);
            Properties prop = new Properties();
            
            try {
                prop.load(input);
                
            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    private static String[] fetchDataType2(String[] result, String paramGet) {

        String configFile = EntryPoint.getConfigFilePath();

        return result;
    }


    
    
}
