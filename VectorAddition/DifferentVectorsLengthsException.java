import java.util.ArrayList;

public class DifferentVectorsLengthsException extends Exception {

    private ArrayList<Integer> lengths;

    public DifferentVectorsLengthsException(String message, ArrayList<Integer> vectorLengths){
        super(message);
        this.lengths = vectorLengths;
    }

    public ArrayList<Integer> getLengths(){
        return this.lengths;
    }
}
