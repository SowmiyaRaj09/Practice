package com.service;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import java.io.IOException;

import org.json.JSONObject;


// Class that reads from files and convert data inside file to Json object
public class FileUtility {

	// Function to read data from file and return the data as String
    public static String readFile(String dir_path) throws IOException {
        StringBuffer strbuf = new StringBuffer();
        
        try {
            File file = new File(dir_path);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line;
            while ((line = br.readLine()) != null) {
            	strbuf.append(line);
            }
            
            fr.close();
            
            // Display the content of the file
            System.out.println("File path: " + dir_path);
            System.out.println("Contents of File: ");
            System.out.println(strbuf.toString());
            System.out.println("");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strbuf.toString();
    }

    // Function to convert data from file to Json object and return the Json object
    public static JSONObject convertToJson(String file_data) throws IOException {
        return new JSONObject(file_data);
    }

}
