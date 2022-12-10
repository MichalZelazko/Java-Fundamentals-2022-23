public class DifferentVectorsLengthsException extends Exception {

    private String[] dataForException;

    public DifferentVectorsLengthsException(String message, String[] dataForException){
        super(message);
        this.dataForException = dataForException;
    }

    public String[] getDataForException(){
        return this.dataForException;
    }
}
