package com.example.libraryproject;
import java.io.*;
import java.util.ArrayList;
public class CSVController {
    private String fileString;
    private ArrayList<String[]> csvData;
    public CSVController(String fileStr) {
        fileString = fileStr;
        csvData = new ArrayList<String[]>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileString));
            String line;
            while ((line = br.readLine()) != null)
                csvData.add(line.split(",", -1));
            br.close();
        } catch (IOException e) {
            csvData = new ArrayList<String[]>();
        }
    }
    public CSVController() {
        this("newcsv.csv");
    }
    public ArrayList<String[]> getCsvData() {
        return csvData;
    }
    public void setCsvData(ArrayList<String[]> csvData) {
        this.csvData = csvData;
    }

    public void UpdateCSV() {
        try {
            FileWriter fw = new FileWriter(fileString);
            for (String[] lines : csvData.toArray(new String[0][0])) {
                for (int i = 0; i < lines.length; i++) {
                    fw.write(lines[i]);
                    if (i < lines.length - 1) {
                        fw.write(",");
                    }
                }
                fw.write("\n");
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteLine(int ind) {
        this.getCsvData().remove(ind);
    }

    public void addLine(String[] content) {
        this.getCsvData().add(content);
    }
}
