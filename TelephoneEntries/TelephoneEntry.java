abstract class TelephoneEntry {
    protected String name;
    protected Address address;

    TelephoneEntry(String name, String street, String city, String countryCode, String localNumber) {
        this.name = name;
        this.address = new Address(countryCode, localNumber, street, city);
    }

    abstract String getName();
    abstract TelephoneNumber getTelephoneNumber();
    abstract String getAddress();
    abstract void description();
}
