public class Address {
    private TelephoneNumber telephoneNumber;
    private String address;

    public Address(TelephoneNumber telephoneNumber, String address) {
        this.telephoneNumber = telephoneNumber;
        this.address = address;
    }

    public TelephoneNumber getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void printAddress() {
        System.out.println(address);
        telephoneNumber.printTelephoneNumber();
    }
}
