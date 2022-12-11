import java.util.*;
import java.io.*;

public class VectorApp {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        ArrayList<MyVector> vectors = new ArrayList<MyVector>();    
        int numberOfVectors = MyVector.enterNumberOfVectors(input);        
        vectors = MyVector.enterVectors(numberOfVectors, input);
        boolean correctVectors = false;
        while(!correctVectors){
            try {
                MyVector sum = MyVector.addVectors(vectors);
                MyVector.printVector(sum);
                writeToFile("sum.txt", sum.getVector().toString());
                correctVectors = true;
            } catch (DifferentVectorsLengthsException e) {
                System.out.println(e.getMessage());
                System.out.println("The " + e.getDataForExceptionMessage()[1] + " vector length is " + e.getDataForExceptionMessage()[0]);
                System.out.println("The " + e.getDataForExceptionMessage()[1] + " vector length is " + e.getDataForExceptionMessage()[3] + " than the " + e.getDataForExceptionMessage()[2] + " vector length");
                vectors = MyVector.enterVectors(numberOfVectors, input);
            } catch (IOException e) {
                System.out.println("File error occurred.");
            } finally {
                input.close();
            }
        }
    }

    private static void writeToFile(String fileName, String vector) throws IOException{
        FileWriter fileWriter = new FileWriter(fileName);
        fileWriter.write(vector);
        fileWriter.close();
    }
}
