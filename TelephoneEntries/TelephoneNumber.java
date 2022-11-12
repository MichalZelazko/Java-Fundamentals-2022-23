public class TelephoneNumber implements Comparable<TelephoneNumber> {
   private String countryCode;
   private String localNumber;

    public TelephoneNumber(String countryCode, String localNumber) {
        this.countryCode = countryCode;
        this.localNumber = localNumber.replaceAll("\\s+", "");
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getLocalNumber() {
        return localNumber;
    }
    
    public void printTelephoneNumber() {
        System.out.println("+" + countryCode + " " + localNumber);
    }

    @Override
    public int compareTo(TelephoneNumber o) {
        int result = countryCode.compareTo(o.countryCode);
        if (result == 0) {
            result = localNumber.compareTo(o.localNumber);
        }
        return result;
    }
}
    