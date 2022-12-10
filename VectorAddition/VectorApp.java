import java.util.*;

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
                correctVectors = true;
                input.close();
            } catch (DifferentVectorsLengthsException e) {
                System.out.println(e.getMessage());
                System.out.println("The " + e.getDataForException()[1] + " vector length is " + e.getDataForException()[0]);
                System.out.println("The " + e.getDataForException()[1] + " vector length is " + e.getDataForException()[3] + "than the " + e.getDataForException()[2] + " vector length");
                vectors = MyVector.enterVectors(numberOfVectors, input);
            }
        }
    }
}
