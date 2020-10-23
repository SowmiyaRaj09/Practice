package com.service;

import java.io.IOException;

import java.util.Timer;
import java.util.TimerTask;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONObject;

public class CurrencyConversionManager extends TimerTask {
	// Build ID to be used to check for every poll
    private int old_buildId = 0;

    private Map<String, Float> currencyDataMap;

    // Constructor - initialize HashMap
    public CurrencyConversionManager() {
    	currencyDataMap = new HashMap<String, Float>();
    }

    // Main function - defines timer task to poll every 1 minute
    public static void main(String[] args) {
    	CurrencyConversionManager ccMgr = new CurrencyConversionManager();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(ccMgr, 0, ApplnConstants.POLL_FREQUENCY);
    }

    // Overriding run() to perform the required tasks
    @Override
    public void run() {
        System.out.println("Accessing Build file");
        System.out.println("");
        try {
            String buildFile = FileUtility.readFile(ApplnConstants.DIRECTORY_PATH + ApplnConstants.BUILD_FILE_NAME);
            this.dataProcessor(buildFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    // Function to store data in memory (Map) using Json object utility functions
    private void updateDataInMemory(String currencyData) {
        try {
            JSONObject json = FileUtility.convertToJson(currencyData);
            
            Iterator<String> iter = json.keys();

            System.out.println("Storing data from file in memory");

            currencyDataMap.clear(); // Clear the old contents
            
            while (iter.hasNext()) {
                String countryCode = iter.next();
                float val = json.getFloat(countryCode);
                currencyDataMap.put(countryCode, val);
                System.out.println("Key: " + countryCode + " Value: " + val);
            }

            System.out.println("");
            
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Function to display data stored in memory (Map)
    private void displayDataInMemory() {

        System.out.println("Data in memory");
        
        for (String countryCode : currencyDataMap.keySet()) {
            System.out.println("Country Code: " + countryCode + " Value: " + currencyDataMap.get(countryCode));
        }
        
        System.out.println("");

    }
    
    /* Function to check current and previous Build ID and to obtain currency conversion file to be read 
     * Calls updateDataInMemory(String data) and displayDataInMemory() to store file contents in memory
     */
    private void dataProcessor(String buildFiledata) throws Exception {
    	String dataFile = "";
        JSONObject json = FileUtility.convertToJson(buildFiledata);

        int curr_buildId = Integer.parseInt(json.getString("buildID"));

        if (curr_buildId == old_buildId) {
            System.out.println("Build ID: " + curr_buildId + " already processed");
            return;
        }

        old_buildId = curr_buildId; // updating the build ID for next poll check

        System.out.println("New build id: " + curr_buildId);

        String dataFileName = json.getString("FileName");

        System.out.println("Data File to be read " + dataFileName);

        dataFile = FileUtility.readFile(ApplnConstants.DIRECTORY_PATH + dataFileName);

        updateDataInMemory(dataFile);
        displayDataInMemory();
    }
}
