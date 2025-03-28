public abstract class Person {
    protected int personID;
    protected String name;
    protected String email;
    protected String passwordHash;
    protected String phoneNo;
    protected String gender;
    protected String role;

    public Person(int personID, String name, String email, String passwordHash, String phoneNo, String gender, String role) {
        this.personID = personID;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phoneNo = phoneNo;
        this.gender = gender;
        this.role = role;
    }

    public int getPersonID() {
        return personID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getGender() {
        return gender;
    }

    public String getRole() {
        return role;
    }

    public abstract void displayInfo();
}
