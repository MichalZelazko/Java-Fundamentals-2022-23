abstract class TelephoneEntry {
    protected String name;
    protected Address address;

    public TelephoneEntry(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    abstract void description();
}
