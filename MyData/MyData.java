import java.util.ArrayList;

public class MyData {
    private int day;
    private int month;
    private int year;
    private String dayOfTheWeek;
    private final static String[] possibleFormats = new String[]{
        "\\d{2}/\\d{2}/\\d{4}+ \\w*",
        "\\d{2}/\\d/\\d{4}+ \\w*",
        "\\d{4}-\\d{2}-\\d{2}+ \\w*",
        "\\w*+ \\d{2}.\\d{2}.\\d{4}"
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
        ArrayList<String> dates = MyDataFilesManagement.readFromFile(inFileName);
        ArrayList<String> convertedDates = new ArrayList<>();
        int i = 0;
        int pattern;
        ArrayList<String> parsedDate = new ArrayList<>();
        for (String date : dates) {
            try{
                pattern = findUnrepeatedMatch(date, convertedDates);
                convertedDates.add(date);
                parsedDate = chooseParsingPattern(date, pattern);
                MyData myData = new MyData(Integer.parseInt(parsedDate.get(0)), Integer.parseInt(parsedDate.get(1)), Integer.parseInt(parsedDate.get(2)), parsedDate.get(3));
                myData.printData();
                i+=1;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        MyDataFilesManagement.writeToFile(outFileName, convertedDates);
        return i;
    }

    private static int findUnrepeatedMatch(String date, ArrayList<String> convertedDates) throws IllegalArgumentException{
        int i = 0;
        for (String format : possibleFormats) {
            if (date.matches(format)) {
                if(convertedDates.contains(date)){
                    throw new IllegalArgumentException("Date already exists");
                }
                return i;
            }
            i++;
        }
        throw new IllegalArgumentException("Date format is incorrect");
    }

    private static ArrayList<String> parsePattern0(String date){
        ArrayList<String> parsedDate = new ArrayList<>();
        parsedDate.add(date.substring(0,2));
        parsedDate.add(date.substring(3,5));
        parsedDate.add(date.substring(6,10));
        parsedDate.add(date.substring(11));
        return parsedDate;
    }

    private static ArrayList<String> parsePattern1(String date){
        ArrayList<String> parsedDate = new ArrayList<>();
        parsedDate.add(date.substring(0,2));
        parsedDate.add(date.substring(3,4));
        parsedDate.add(date.substring(5,9));
        parsedDate.add(date.substring(10));
        return parsedDate;
    }

    private static ArrayList<String> parsePattern2(String date){
        ArrayList<String> parsedDate = new ArrayList<>();
        parsedDate.add(date.substring(8,10));
        parsedDate.add(date.substring(5,7));
        parsedDate.add(date.substring(0,4));
        parsedDate.add(date.substring(11));
        return parsedDate;
    }

    private static ArrayList<String> parsePattern3(String date){
        ArrayList<String> parsedDate = new ArrayList<>();
        String[] splittedDate = date.split(" ");
        parsedDate.add(splittedDate[1].substring(0,2));
        parsedDate.add(splittedDate[1].substring(3,5));
        parsedDate.add(splittedDate[1].substring(6,10));
        parsedDate.add(splittedDate[0]);
        return parsedDate;
    }

    private static ArrayList<String> chooseParsingPattern(String date, int pattern){
        ArrayList<String> parsedDate = new ArrayList<>();
        switch (pattern) {
            case 0:
                parsedDate = parsePattern0(date);
                break;
            case 1:
                parsedDate = parsePattern1(date);
                break;
            case 2:
                parsedDate = parsePattern2(date);
                break;
            case 3:
                parsedDate = parsePattern3(date);
                break;
        }
        return parsedDate;
    }
}
