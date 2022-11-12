public class Person extends TelephoneEntry{
    private String surname;

    public Person(String name, String surname, Address address) {
        super(name, address);
        this.surname = surname; 
    }
    
    @Override
    void description() {
        System.out.println(name + " " + surname);
        address.printAddress();
    }
}