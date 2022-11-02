public class MyDataApp {
    public static void main(String[] args) {
        int matched = MyData.convertDate("InputData.txt", "MyData.txt");
        System.out.println("\nNumber of valid dates: " + matched);
    }
}