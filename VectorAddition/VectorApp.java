import java.util.*;
import java.io.*;

public class VectorApp {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        ArrayList<MyVector> vectors = new ArrayList<MyVector>();    
        int numberOfVectors = enterNumberOfVectors(input);        
        vectors = enterVectors(numberOfVectors, input);
        boolean correctVectors = false;
        while(!correctVectors){
            try {
                MyVector sum = MyVector.addVectors(vectors);
                MyVector.printVector(sum);
                writeToFile("sum.txt", sum.getVector().toString());
                correctVectors = true;
            } catch (DifferentVectorsLengthsException e) {
                String[] data = prepareDataForExceptionMessage(e.getDataForExceptionMessage());
                System.out.println(e.getMessage());
                System.out.println("The " + data[1] + " vector length is " + data[0]);
                System.out.println("The " + data[1] + " vector length is " + data[3] + " than the " + data[2] + " vector length");
                vectors = enterVectors(numberOfVectors, input);
            } catch (IOException e) {
                System.out.println("File error occurred.");
            } finally {
                input.close();
            }
        }
    }

    private static int enterNumberOfVectors(Scanner input) {
        System.out.print("Enter the number of vectors you will enter: ");
        boolean correctInput = false;
        int numberOfVectors = 0;
        while(!correctInput){
            try{
                numberOfVectors = input.nextInt();
                correctInput = true;
                return numberOfVectors;
            } catch (InputMismatchException e) {
                System.out.print("Enter an integer: ");
            }
        }
        return numberOfVectors;
    }

    private static ArrayList<MyVector> enterVectors(int numberOfVectors, Scanner input) {
        ArrayList<MyVector> vectors = new ArrayList<MyVector>();
        for (int i = 0; i < numberOfVectors; i++) {
            System.out.print("Enter the values for vector " + (i + 1) + " (separated by comma, press Enter to finish): ");
            String vectorValues = input.next();
            String[] vectorValuesArray = vectorValues.split(",");
            ArrayList<Integer> vectorValuesIntArray = eliminateNotIntegers(vectorValuesArray);
            vectors.add(new MyVector(vectorValuesIntArray));
        }
        return vectors;
    }

    private static ArrayList<Integer> eliminateNotIntegers (String[] vectorValuesArray){
        ArrayList<Integer> vectorValuesIntArray = new ArrayList<Integer>();
        for (int i = 0; i < vectorValuesArray.length; i++) {
            try {
                vectorValuesIntArray.add(Integer.parseInt(vectorValuesArray[i]));
            } catch (NumberFormatException e) {
                continue;
            }
        }
        return vectorValuesIntArray;
    }

    private static void writeToFile(String fileName, String vector) throws IOException{
        FileWriter fileWriter = new FileWriter(fileName);
        fileWriter.write(vector);
        fileWriter.close();
    }

    private static String[] prepareDataForExceptionMessage(Integer[] lengthsAndIndexes){
        String[] data = new String[4];
        data[0] = Integer.toString(lengthsAndIndexes[0]);
        data[1] = Integer.toString(lengthsAndIndexes[2]);
        data[2] = Integer.toString(lengthsAndIndexes[3]);
        if (lengthsAndIndexes[0] > lengthsAndIndexes[1]){
            data[3] = "bigger";
        } else {
            data[3] = "smaller";
        }
        if(lengthsAndIndexes[2] == 1){
            data[1] += "st";
            data[2] += "nd";
        } else if (lengthsAndIndexes[2] == 2){
            data[1] += "nd";
            data[2] += "rd";
        } else if (lengthsAndIndexes[2] == 3){
            data[1] = "rd";
            data[2] += "th";
        } else {
            data[1] += "th";
            data[2] += "th";
        }
        return data;
    }
}
