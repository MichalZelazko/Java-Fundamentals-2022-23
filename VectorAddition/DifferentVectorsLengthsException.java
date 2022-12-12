public class DifferentVectorsLengthsException extends Exception {

    private Integer[] lengthsAndIndexes;

    public DifferentVectorsLengthsException(String message, MyVector v1, MyVector v2, int index){
        super(message);
        this.lengthsAndIndexes = new Integer[4];
        this.lengthsAndIndexes[0] = v1.getLength();
        this.lengthsAndIndexes[1] = v2.getLength();
        this.lengthsAndIndexes[2] = index + 1;
        this.lengthsAndIndexes[3] = index + 2;
    }

    public Integer[] getDataForExceptionMessage(){
        return this.lengthsAndIndexes;
    }
}
