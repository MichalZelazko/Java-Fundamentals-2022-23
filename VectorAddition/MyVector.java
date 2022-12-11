import java.util.*;

public class MyVector{
    private ArrayList<Integer> values;
    
    public MyVector(ArrayList<Integer> values) {
        this.values = new ArrayList<Integer>();
        for (int i = 0; i < values.size(); i++) {
            this.values.add(values.get(i));
        }
    }
    
    public int getLength(){
        return this.values.size();
    }

    public ArrayList<Integer> getVector(){
        return this.values;
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

    private static void checkVectorsLengths(ArrayList<MyVector> vectors) throws DifferentVectorsLengthsException{
        for (int i = 0; i < vectors.size() - 1; i++) {
            if (vectors.get(i).values.size() != vectors.get(i + 1).values.size()) {
                throw new DifferentVectorsLengthsException("The vectors are of a different size", vectors.get(i), vectors.get(i + 1), i);
            }
        }
    }
}