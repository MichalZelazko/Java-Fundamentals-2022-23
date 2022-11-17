public class Address {
    private TelephoneNumber telephoneNumber;
    private String street;
    private String city;

    Address(String countryCode, String localNumber, String street, String city) {
        this.telephoneNumber = new TelephoneNumber(countryCode, localNumber);
        this.street = street;
        this.city = city;
    }

    public TelephoneNumber getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getAddress() {
        return street + ", " + city;
    }

    public void printAddress() {
        System.out.println(street + ", " + city);
        telephoneNumber.printTelephoneNumber();
    }
}
