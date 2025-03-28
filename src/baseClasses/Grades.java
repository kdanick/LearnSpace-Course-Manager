package baseClasses;

class Grades {
    private int course_id;
    private int student_id;
    private int score;
    private char grade;

    public Grades(int course_id, int student_id, int score, char grade) {
        this.course_id = course_id;
        this.student_id = student_id;
        this.score = score;
        this.grade = grade;
    }

    public int getCourseID() { return course_id; }
    public int getStudentID() { return student_id; }
    public int getScore() { return score; }
    public char getGrade() { return grade; }
}
