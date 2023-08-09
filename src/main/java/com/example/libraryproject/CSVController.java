package com.example.libraryproject;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.ArrayList;
public class CSVController {
    private String fileString;
    private ArrayList<LinkedHashMap<String, String>> csvData;
    public CSVController(String fileStr, String[] keyNames) {
        fileString = fileStr;
        csvData = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileString));
            String line = br.readLine();
            int which;
            while ((line = br.readLine()) != null) {
                which = 0;
                LinkedHashMap<String, String> entry = null;
                for (String sepLine : line.split(",")) {
                    entry = new LinkedHashMap<>();
                    entry.put(keyNames[which], sepLine);
                    which++;
                }
                csvData.add(entry);
            }
            br.close();
        } catch (IOException e) {
            csvData = new ArrayList<LinkedHashMap<String, String>>();
        }
    }
    public ArrayList<LinkedHashMap<String, String>> getCsvData() {
        return csvData;
    }
    public void setCsvData(ArrayList<LinkedHashMap<String, String>> csvData) {
        this.csvData = csvData;
    }

    public void UpdateCSV() {
        try {
            FileWriter fw = new FileWriter(fileString);
            for (int i = 0; i < this.csvData.size(); i++) {
                LinkedHashMap<String, String> curMap = this.csvData.get(i);
                for (String line : curMap.values()) {
                    fw.write(line + ",");
                }
                fw.write("\n");
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean search(String searchQuery, String keyName) {
        for (LinkedHashMap<String, String> lines : this.getCsvData()) {
            if (lines.get(keyName).equals(searchQuery)) {
                return true;
            }
        }
        return false;
    }


    public String[] returnArraySlice(String... keyNames) {
        ArrayList<String> toRet = new ArrayList<>();
        for (LinkedHashMap<String, String> lines : this.getCsvData())
            for (String keyName : keyNames)
                toRet.add(lines.get(keyName));
        return toRet.toArray(new String[0]);
    }


    public String searchAndReturnString(String searchQuery, String keyName) {
        int ind = 0;
        for (LinkedHashMap<String, String> lines : this.getCsvData()) {
            if (lines.get(keyName).equals(searchQuery)) {
                return String.join(",", lines.values());
            }
            ind++;
        }
        return null;
    }

    public ArrayList<LinkedHashMap<String, String>> searchAndReturnSetOfHashMaps(String searchQuery, String keyName) {
        ArrayList<LinkedHashMap<String, String>> validArrays = new ArrayList<>();
        int ind = 0;
        for (LinkedHashMap<String, String> lines : this.getCsvData()) {
            if (lines.get(keyName).equals(searchQuery)) {
                validArrays.add(this.getCsvData().get(ind));
            }
            ind++;
        }
        return validArrays;
    }

    public LinkedHashMap<String, String> searchAndReturnHashMap(String searchQuery, String keyName) {
        int ind = 0;
        for (LinkedHashMap<String, String> lines : this.getCsvData()) {
            if (lines.get(keyName).equals(searchQuery)) {
                return this.getCsvData().get(ind);
            }
            ind++;
        }
        return null;
    }

    public void deleteLine(int ind) {
        this.getCsvData().remove(ind);
    }

    public void addLine(LinkedHashMap<String, String> content) {
        this.getCsvData().add(content);
    }
}
