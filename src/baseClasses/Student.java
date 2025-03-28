package baseClasses;

class Student {
    private int student_id;
    private String name;
    private String email;
    private String gender;

    public Student(int student_id, String name, String email, String gender) {
        this.student_id = student_id;
        this.name = name;
        this.email = email;
        this.gender = gender;
    }

    public int getStudentID() { return student_id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getGender() { return gender; }

    public void displayInfo() {
        System.out.println("Student ID: " + student_id + ", Name: " + name + ", Email: " + email);
    }
}