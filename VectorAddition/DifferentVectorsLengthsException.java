public class DifferentVectorsLengthsException extends Exception {

    private String[] dataForExceptionMessage;

    public DifferentVectorsLengthsException(String message, MyVector v1, MyVector v2, int index){
        super(message);
        this.dataForExceptionMessage = prepareDataForExceptionMessage(v1, v2, index);
    }

    public String[] getDataForExceptionMessage(){
        return this.dataForExceptionMessage;
    }

    private static String[] prepareDataForExceptionMessage(MyVector v1, MyVector v2, int index){
        String[] data = new String[4];
        data[0] = Integer.toString(v1.getLength());
        data[1] = Integer.toString(index + 1);
        data[2] = Integer.toString(index + 2);
        if (v1.getLength() > v2.getLength()){
            data[3] = "bigger";
        } else {
            data[3] = "smaller";
        }
        if(index == 0){
            data[1] += "st";
            data[2] += "nd";
        } else if (index == 1){
            data[1] += "nd";
            data[2] += "rd";
        } else if (index == 2){
            data[1] = "rd";
            data[2] += "th";
        } else {
            data[1] += "th";
            data[2] += "th";
        }
        return data;
    }
}
