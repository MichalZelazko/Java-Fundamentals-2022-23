public class Company extends TelephoneEntry{

    public Company(String name, Address address) {
        super(name, address);
    }
    
    @Override
    void description() {
        System.out.println(name);
        address.printAddress();
    }
}
