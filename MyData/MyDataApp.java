import java.io.*;
import java.util.*;

public class MyDataApp {
    public static void main(String[] args) {
        MyData.convertDate();
    }
}

class MyData {
    private int day;
    private int month;
    private int year;
    private String dayOfTheWeek;
    
    public MyData(int day, int month, int year, String dayOfTheWeek) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.dayOfTheWeek = dayOfTheWeek;
    }
    
    public String toString() {
        return "day=" + day +
               ", month=" + month +
               ", year=" + year +
               ", dayOfWeek='" + dayOfTheWeek + '\'';
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public static void convertDate() {
        String[] possibleFormats = new String[]{
            "\\d{2}/\\d{2}/\\d{4}+ \\w*",
            "\\d{2}/\\d/\\d{4}+ \\w*",
            "\\d{4}-\\d{2}-\\d{2}+ \\w*",
            "\\w*+ \\d{2}.\\d{2}.\\d{4}"
        };
    }
}