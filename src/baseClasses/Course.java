package baseClasses;

class Course {
    private int course_id;
    private String course_name;
    private int credits;
    private int lecturer_id;
    private int person_id;

    public Course(int course_id, String course_name, int credits, int lecturer_id, int person_id) {
        this.course_id = course_id;
        this.course_name = course_name;
        this.credits = credits;
        this.lecturer_id = lecturer_id;
        this.person_id = person_id;
    }

    public int getCourseID() { return course_id; }
    public String getCourseName() { return course_name; }
    public int getCredits() { return credits; }
    public int getLecturerID() { return lecturer_id; }
    public int getPersonID() { return person_id; }

    public void displayInfo() {
        System.out.println("Course ID: " + course_id + ", Name: " + course_name + ", Credits: " + credits);
    }
}
