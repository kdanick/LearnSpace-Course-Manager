package baseClasses;

public class User {
    private int user_id;
    private String name;
    private String email;
    private String password_hash;
    private String role;
    private String phone_no;
    private String gender;

    public User(int user_id, String name, String email, String password_hash, String role, String phone_no, String gender) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.password_hash = password_hash;
        this.role = role;
        this.phone_no = phone_no;
        this.gender = gender;
    }

    // Getters
    public int getUser_id() { return user_id; }
    public String getUsername() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password_hash; }
    public String getRole() { return role; }
    public String getPhoneNumber() { return phone_no; }
    public String getGender() { return gender; }
}
