package com.example.vehicledetection;

import android.util.Log;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataWindow {

    private StringBuilder data;
    private double window_time;
    private int records_sec;
    private String[] fixData;

    public String[] getFixData() { return fixData; }

    public void setFixData(String[] fixData) { this.fixData = fixData; }

    public DataWindow(int records_sec) {
        this.setRecords_sec(records_sec);
    }

    public StringBuilder getData() {
        return data;
    }

    public void setData(StringBuilder data) {
        this.data = data;
    }

    public double getWindow_time() {
        return window_time;
    }

    public void setWindow_time(double window_time) {
        this.window_time = window_time;
    }

    public int getRecords_sec() {
        return records_sec;
    }

    public void setRecords_sec(int records_sec) {
        this.records_sec = records_sec;
    }

    public String fixDataLength() {
        int lineCount;
        try {
            String[] dividedData = this.getData().toString().split(",");
            lineCount = dividedData.length / 3;
            if(this.getWindow_time() * this.getRecords_sec() < lineCount) {
                int[] randoms = generateRandomNumbers((int)(lineCount - (this.getRecords_sec() * this.getWindow_time())), lineCount);
                for(int i = 0; i < randoms.length; i++) {
                    dividedData[randoms[i]*3+1] = "";
                    dividedData[(randoms[i]*3)+2] = "";
                    dividedData[(randoms[i]*3)+3] = "";
                    fixData[randoms[i]*3] = "";
                    fixData[randoms[i]*3+1] = "";
                    fixData[randoms[i]*3+2] = "";
                }
                return  Stream.of(dividedData)
                        .filter(s -> s != null && !s.isEmpty())
                        .collect(Collectors.joining(","));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.toString();
    }

    private int[] generateRandomNumbers(int n, int max) {
        Random rnd = new Random();
        String[] randoms = new String[n];
        String num;
        for (int i = 0; i < n;) {
            num = Integer.toString(rnd.nextInt(max));
            if (Arrays.stream(randoms).noneMatch(num::equals)) {
                randoms[i] = num;
                i++;
            }
        }
        return  Arrays.asList(randoms).stream().mapToInt(Integer::parseInt).toArray();
    }
}
