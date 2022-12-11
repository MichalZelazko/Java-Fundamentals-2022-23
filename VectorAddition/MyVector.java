import java.util.*;

public class MyVector{
    private ArrayList<Integer> values;

    public MyVector(ArrayList<Integer> values) {
        this.values = new ArrayList<Integer>();
        for (int i = 0; i < values.size(); i++) {
            this.values.add(values.get(i));
        }
    }
    
    public static int enterNumberOfVectors(Scanner input) {
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

    public static ArrayList<MyVector> enterVectors(int numberOfVectors, Scanner input) {
        ArrayList<MyVector> vectors = new ArrayList<MyVector>();
        for (int i = 0; i < numberOfVectors; i++) {
            System.out.print("Enter the values for vector " + (i + 1) + " (separated by comma, press Enter to finish): ");
            String vectorValues = input.next();
            String[] vectorValuesArray = vectorValues.split(",");
            ArrayList<Integer> vectorValuesIntArray = new ArrayList<Integer>();
            for (int j = 0; j < vectorValuesArray.length; j++) {
                try {
                    vectorValuesIntArray.add(Integer.parseInt(vectorValuesArray[j]));
                } catch (NumberFormatException e) {
                    continue;
                }
            }
            vectors.add(new MyVector(vectorValuesIntArray));
        }
        return vectors;
    }

    public static void printVector(MyVector vector){
        String vectorString = "[";
        int i = 0;
        for (i = 0; i < vector.getLength() - 1; i++) {
            vectorString += (vector.values.get(i) + ", ");
        }
        vectorString += (vector.values.get(i) + "]\n");
        System.out.println(vectorString);
    }

    public static MyVector addVectors(ArrayList<MyVector> vectors) throws DifferentVectorsLengthsException{
        ArrayList<Integer> sum = new ArrayList<Integer>();
        checkVectorsLengths(vectors);
        for (int i = 0; i < vectors.get(0).values.size(); i++) {
            int sumOfValues = 0;
            for (int j = 0; j < vectors.size(); j++) {
                sumOfValues += vectors.get(j).values.get(i);
            }
            sum.add(sumOfValues);
        }
        return new MyVector(sum);
    }

    public int getLength(){
        return this.values.size();
    }

    public ArrayList<Integer> getVector(){
        return this.values;
    }

    private static void checkVectorsLengths(ArrayList<MyVector> vectors) throws DifferentVectorsLengthsException{
        for (int i = 0; i < vectors.size() - 1; i++) {
            if (vectors.get(i).values.size() != vectors.get(i + 1).values.size()) {
                throw new DifferentVectorsLengthsException("The vectors are of a different size", vectors.get(i), vectors.get(i + 1), i);
            }
        }
    }
}