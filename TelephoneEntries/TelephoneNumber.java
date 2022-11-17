public class TelephoneNumber implements Comparable<TelephoneNumber> {
    private String countryCode;
    private String localNumber;

    TelephoneNumber(String countryCode, String localNumber) {
        this.countryCode = countryCode;
        this.localNumber = localNumber.replaceAll("\\s+", "");
    }
    
    public void printTelephoneNumber() {
        System.out.println("+" + countryCode + " " + localNumber + "\n");
    }

    @Override
    public int compareTo(TelephoneNumber otherNumber) {
        int result = countryCode.compareTo(otherNumber.countryCode);
        if (result == 0) {
            result = localNumber.compareTo(otherNumber.localNumber);
        }
        return result;
    }
}
    