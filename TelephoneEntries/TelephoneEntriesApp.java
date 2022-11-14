public class TelephoneEntriesApp {
    public static void main(String[] args) {
        Person person1 = new Person("John", "Smith", new Address(new TelephoneNumber( "48", "123 456 789" ), "123 Main Street"));
        Person person2 = new Person("Tyler", "Johnson", new Address(new TelephoneNumber( "48", "154 887 895" ), "12 South Street"));
        Person person3 = new Person("Kyle", "Brooks", new Address(new TelephoneNumber( "48", "124 856 477" ), "67 West Street"));
        TelephoneEntry[] telephoneEntries = {person1, person2, person3};
        for (TelephoneEntry telephoneEntry : telephoneEntries) {
            telephoneEntry.description();
        }
    }
}
