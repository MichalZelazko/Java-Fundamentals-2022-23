import java.util.ArrayList;

public class MyData {
    private int day;
    private int month;
    private int year;
    private String dayOfTheWeek;
    private final static String[] possibleFormats = new String[]{
        "\\d{2}/\\d{2}/\\d{4}+ \\w+",
        "\\d{2}/\\d/\\d{4}+ \\w+",
        "\\d{4}-\\d{2}-\\d{2}+ \\w+",
        "\\w+ \\d{2}\\.\\d{2}\\.\\d{4}"
    };

    MyData(int day, int month, int year, String dayOfTheWeek) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.dayOfTheWeek = dayOfTheWeek;
    }
    
    public void printData() {
        System.out.println("day=" + day +
               ", month=" + month +
               ", year=" + year +
               ", dayOfWeek='" + dayOfTheWeek + '\'');
    }

    public static int convertDate(String inFileName, String outFileName) {
        ArrayList<String> dates = MyDataFileHandler.readFromFile(inFileName);
        ArrayList<String> convertedDates = new ArrayList<>();
        int i = 0;
        int pattern;
        String[] parsedDate = new String[4];
        for (String date : dates) {
            try{
                pattern = findUnrepeatedMatch(date, convertedDates);
                convertedDates.add(date);
                parsedDate = parsing(date, pattern);
                MyData myData = new MyData(Integer.parseInt(parsedDate[0]), Integer.parseInt(parsedDate[1]), Integer.parseInt(parsedDate[2]), parsedDate[3]);
                myData.printData();
                i+=1;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        MyDataFileHandler.writeToFile(outFileName, convertedDates);
        return i;
    }

    private static int findUnrepeatedMatch(String date, ArrayList<String> convertedDates) throws IllegalArgumentException{
        int i = 0;
        for (String format : possibleFormats) {
            if (date.matches(format)) {
                if(convertedDates.contains(date)){
                    throw new IllegalArgumentException("Date has already been converted");
                }
                return i;
            }
            i++;
        }
        throw new IllegalArgumentException("Date format is incorrect");
    }

    private static String[] parsing(String date, int pattern){
        String[] parsedDate = new String[4];
        String auxArray[] = date.split(" ");
        String auxDateArray[] = new String[3];
        parsedDate = handleWeekday(parsedDate, auxArray, date, pattern);
        parsedDate = handleNumericalDate(parsedDate, auxArray, auxDateArray, date, pattern);
        return parsedDate;
    }

    private static String[] handleWeekday(String[] parsedDate, String[] auxArray, String date, int pattern){
        if(pattern != 3){
            parsedDate[3] = auxArray[1];
        } else {
            parsedDate[3] = auxArray[0];
        }
        return parsedDate;
    }

    private static String[] handleNumericalDate(String[] parsedDate, String[] auxArray, String[] auxDateArray, String date, int pattern){
        if(pattern != 2){
            if(pattern == 3){
                auxDateArray = auxArray[1].split("\\.");
            } else {
                auxDateArray = auxArray[0].split("/");
            }
            parsedDate[0] = auxDateArray[0];
            parsedDate[1] = auxDateArray[1];
            parsedDate[2] = auxDateArray[2];
        } else if(pattern == 2){
            auxDateArray = auxArray[0].split("-");
            parsedDate[0] = auxDateArray[2];
            parsedDate[1] = auxDateArray[1];
            parsedDate[2] = auxDateArray[0];
        }
        return parsedDate;
    }
}
