public class Person extends TelephoneEntry{
    private String surname;

    Person(String name, String surname, String countryCode, String localNumber, String street, String city) {
        super(name, street, city, countryCode, localNumber);
        this.surname = surname; 
    }

    TelephoneNumber getTelephoneNumber() {
        return address.getTelephoneNumber();
    }

    String getAddress() {
        return address.getAddress();
    }

    String getName(){
        return name + " " + surname;
    }

    void description() {
        System.out.println(name + " " + surname);
        address.printAddress();
    }
}