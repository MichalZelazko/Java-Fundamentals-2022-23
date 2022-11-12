public class TelephoneEntriesApp {
    public static void main(String[] args) {
        TelephoneNumber telephoneNumber = new TelephoneNumber( "48", "123 456 789" );
        Address address = new Address(telephoneNumber, "123 Main Street");
        Person person = new Person("John", "Smith", address);
        Company company = new Company("ABC", address);
        TelephoneEntry[] telephoneEntries = {person, company};
        for (TelephoneEntry telephoneEntry : telephoneEntries) {
            telephoneEntry.description();
        }
    }
}
