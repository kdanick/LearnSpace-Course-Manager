package baseClasses;

class Lecturer {
    private int lecturer_id;
    private String name;
    private String course;
    private String email;

    public Lecturer(int lecturer_id, String name, String course, String email) {
        this.lecturer_id = lecturer_id;
        this.name = name;
        this.course = course;
        this.email = email;
    }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
    public void displayInfo() {
        System.out.println("Lecturer ID: " + lecturer_id + ", Name: " + name + ", Course: " + course);
    }
}
