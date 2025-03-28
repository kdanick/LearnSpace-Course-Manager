package baseClasses;

class Person {
    private int person_ID;
    private String name;
    private String email;
    private String password_hash;
    private String phone_no;
    private String gender;
    private String role;

    public Person(int person_ID, String name, String email, String password_hash, String phone_no, String gender, String role) {
        this.person_ID = person_ID;
        this.name = name;
        this.email = email;
        this.password_hash = password_hash;
        this.phone_no = phone_no;
        this.gender = gender;
        this.role = role;
    }

    public int getPersonID() { return person_ID; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return password_hash; }
    public String getPhoneNo() { return phone_no; }
    public String getGender() { return gender; }
    public String getRole() { return role; }

    public void displayInfo() {
        System.out.println("Person ID: " + person_ID + ", Name: " + name + ", Email: " + email);
    }
}