/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.access.tool.script;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 *
 * @author MECC
 */
public class DatabaseAffairs {
    
   /*public DatabaseAffairs(String[] atribut) throws SQLException{
        conn =  connect(atribut[0], atribut[2], atribut[3], atribut[1]);
    }*/
    public DatabaseAffairs(){
        
    }
   
    
   public String[] getDBList(String host, String username, String password, String port) throws SQLException{
    //public String[] getDBList() throws SQLException{
        Connection conn = connect(host, username, password, port);
        
        Statement stmt = (Statement) conn.createStatement();
        stmt.execute("SHOW DATABASES");
        ResultSet rs = stmt.getResultSet();
        
        ArrayList <String> resultBC = new ArrayList<String>();
        
        while(rs.next()){
            resultBC.add(rs.getString(1));
        }
        
        String[] result = new String[resultBC.size()]; 
        result = resultBC.toArray(result);
        return result;
    }
    
    public String[] getTableList(String host, String username, String password, String port, String database) throws SQLException{
      Connection conn = connect(host, username, password, port);
    //public String[] getTableList(String database) throws SQLException{
        
        Statement stmt = (Statement) conn.createStatement();
        stmt.execute("USE "+database);
        stmt.execute("SHOW TABLES");
        ResultSet rs = stmt.getResultSet();
        
        ArrayList <String> resultBC = new ArrayList<String>();
        
        while(rs.next()){
            resultBC.add(rs.getString("TABLES_in_"+database));
        }
        
        String[] result = new String[resultBC.size()]; 
        result = resultBC.toArray(result);
        return result;
    }
    
    public void getAccessExecute(HashMap connAttribut, String query) throws SQLException{
        String result = null;
        String host = String.valueOf(connAttribut.get("host"));
        String username = String.valueOf(connAttribut.get("username"));
        String password = String.valueOf(connAttribut.get("password"));
        String port = String.valueOf(connAttribut.get("port"));
        
        Connection conn = connect(host, username, password, port);
    //public String[] getTableList(String database) throws SQLException{
        
        Statement stmt = (Statement) conn.createStatement();
        stmt.execute(query);
        ResultSet rs = stmt.getResultSet();
       
    }
    
    public void getAdminActivityRecorded(String[] getAdminConfig, String query) throws SQLException{
       
       Connection conn = connect(getAdminConfig[0], getAdminConfig[2], getAdminConfig[3], getAdminConfig[1]);
       Statement stmt = (Statement) conn.createStatement();
       stmt.execute(query);
       
    }
    
    public Connection connect(String host, String username, String password, String port)throws SQLException{
        
    //Connection conn = null;
    String URL = "jdbc:mysql://"+host+":"+port;
    Properties info = new Properties( );
    info.put( "user", username );
    info.put( "password", password );

    Connection conn = (Connection) DriverManager.getConnection(URL, info);
    return conn;

    }
    
}
