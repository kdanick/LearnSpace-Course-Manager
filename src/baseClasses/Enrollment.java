package baseClasses;

import java.util.Date;

class Enrollment {
    private int enrollment_id;
    private int student_id;
    private int course_id;
    private Date enrolled_on;

    public Enrollment(int enrollment_id, int student_id, int course_id, Date enrolled_on) {
        this.enrollment_id = enrollment_id;
        this.student_id = student_id;
        this.course_id = course_id;
        this.enrolled_on = enrolled_on;
    }

    public int getEnrollmentID() { return enrollment_id; }
    public int getStudentID() { return student_id; }
    public int getCourseID() { return course_id; }
    public Date getEnrolledOn() { return enrolled_on; }
}
