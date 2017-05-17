/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.access.tool.script;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author MECC
 */
public class ConfigFileReader {

    private static String[] getUser;
    private static String[] getAdmin;
    private static String[] getAdminConfig;
    public static String[] getServerName;

    public static String[] getServerDatabase() throws FileNotFoundException, IOException {

        BufferedReader in = new BufferedReader(new FileReader("resources\\database_server.txt"));

        //BufferedReader in = new BufferedReader(new FileReader("database_server.txt"));
        String line;
        int resultIndex = 0;
        getAdminConfig = new String[6];

        ArrayList<String> theData = new ArrayList<String>();
        ArrayList<String> getServerNameArrayList = new ArrayList<String>();

        while ((line = in.readLine()) != null) {
            if (!(line.isEmpty())) {
                //System.out.println(line);

                boolean done = false;
                int i = 0;
                int indexAwal = 0;

                while (done != true) {

                    if (Character.toString(line.charAt(i)).matches("#")) {
                        done = true;
                    }
                    if (Character.toString(line.charAt(i)).matches("=")) {

                        String uji = line.substring(0, i);
                        if (uji.matches("LIST_USER") == true) {
                            //System.out.println(line);
                            //etGetUser(line, i);
                            setGetUserAdmin(line, i, "user");
                            done = true;

                        } else if (uji.matches("LIST_ADMIN") == true) {
                            setGetUserAdmin(line, i, "admin");
                            done = true;
                        } else if (uji.matches("DB_ADMIN_IP") == true) {
                            getAdminConfig[0] = getValue(line, i + 1);
                            done = true;
                        } else if (uji.matches("DB_ADMIN_PORT") == true) {
                            getAdminConfig[1] = getValue(line, i + 1);
                            done = true;
                        } else if (uji.matches("DB_ADMIN_USER") == true) {
                            getAdminConfig[2] = getValue(line, i + 1);
                            done = true;
                        } else if (uji.matches("DB_ADMIN_PASS") == true) {
                            getAdminConfig[3] = getValue(line, i + 1);
                            done = true;
                        } else if (uji.matches("DB_ADMIN_DATABASE") == true) {
                            getAdminConfig[4] = getValue(line, i + 1);
                            done = true;
                        } else if (uji.matches("DB_ADMIN_TABLE") == true) {
                            getAdminConfig[5] = getValue(line, i + 1);
                            done = true;
                        } else if (uji.matches("SERVER_NAME") == true) {
                            getServerNameArrayList.add(getValue(line, i + 1));
                        } else {
                            indexAwal = i + 1;
                        }
                        //indexAwal = i+1;
                    }
                    if (i == line.length() - 1) {

                        String a = line.substring(indexAwal, i + 1);
                        theData.add(a);
                        done = true;
                    }
                    i++;
                }
            }

        }
        in.close();

        String[] result = new String[theData.size()];
        result = theData.toArray(result);

        getServerName = new String[getServerNameArrayList.size()];
        getServerName = getServerNameArrayList.toArray(getServerName);

        System.out.println(theData);
        /*for(String okay : result){
            System.out.println(okay);
        }*/
        return result;
    }

    public static String getValue(String line, int indexAwal) {
        return line.substring(indexAwal, line.length());
    }

    public static String[] getDatabaseServerAccessAtribut(String conditional) throws FileNotFoundException, IOException {
        //public static void getFileFromConfig(String conditional) throws FileNotFoundException, IOException{
        
        System.out.println(" ado yang manggil ini getDatabaseServerAccessAtribut");
        BufferedReader in = new BufferedReader(new FileReader("resources\\database.txt"));

        String[] result = null;

        String line;
        boolean notMatch = false;

        int lineIndex = 0;
        while ((line = in.readLine()) != null) {
            System.out.println("Currently, we are in " + lineIndex);
            int i = 0;
            while (notMatch != true && i < line.length()) {

                if (Character.toString(line.charAt(i)).matches(";")) {
                    String a = line.substring(0, i);
                    if (a.matches(conditional)) {

                        System.out.println("Catch the nite");
                        System.out.println(a);

                        // result = getFileSubFunction(line, i);
                    }
                }
                i++;

            }
            lineIndex++;

        }
        in.close();

        return result;
    }

    public static void setGetUserAdmin(String line, int index, String mode) {

        ArrayList<String> resultArray = new ArrayList();

        int startFrom = index + 1;
        for (int k = index; k < line.length(); k++) {

            if (Character.toString(line.charAt(k)).matches(",")) {

                String a = line.substring(startFrom, k);
                // System.out.println(a);
                resultArray.add(a);
                startFrom = k + 1;
            }
            if (k == line.length() - 1) {
                String a = line.substring(startFrom, k + 1);
                resultArray.add(a);
            }
        }
        if ("user".equals(mode)) {

            getUser = new String[resultArray.size()];
            getUser = resultArray.toArray(getUser);

        } else {
            getAdmin = new String[resultArray.size()];
            getAdmin = resultArray.toArray(getAdmin);
        }

    }

    public static String[] getAdminConfig() {
        return getAdminConfig;
    }

    public static String[] getUserList() {
        return getUser;
    }

    public static String[] getAdminList() {
        return getAdmin;
    }
}
