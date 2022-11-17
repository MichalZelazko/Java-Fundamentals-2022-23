public class Company extends TelephoneEntry{

    Company(String name, String countryCode, String localNumber, String street, String city) {
        super(name, street, city, countryCode, localNumber);
    }

    TelephoneNumber getTelephoneNumber() {
        return address.getTelephoneNumber();
    }

    String getAddress() {
        return address.getAddress();
    }

    String getName(){
        return name;
    }

    void description() {
        System.out.println(name);
        address.printAddress();
    }
}
