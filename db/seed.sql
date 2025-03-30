-- Insert Users
INSERT INTO Users (user_id, name, email, password_hash, phone_no, gender, role) VALUES
                                                                                    (1, 'Alice Johnson', 'alice@example.com', 'hashedpassword1', '+123456789', 'female', 'admin'),
                                                                                    (2, 'Bob Smith', 'bob@example.com', 'hashedpassword2', '+987654321', 'male', 'lecturer');

-- Insert Students
INSERT INTO Students (student_id, name, email, gender) VALUES
                                                           (1, 'Charlie Brown', 'charlie@example.com', 'male'),
                                                           (2, 'Dana White', 'dana@example.com', 'female');

-- Insert Courses
INSERT INTO Courses (course_id, course_name, credits, lecturer_id) VALUES
                                                                       ('CS101', 'Introduction to Computer Science', 3, 2),
                                                                       ('MATH101', 'Calculus I', 3, 2);

-- Insert Enrollments
INSERT INTO Enrollments (student_id, course_id) VALUES
                                                    (1, 'CS101'),
                                                    (2, 'MATH101');

-- Insert GradeScale Data
INSERT INTO GradeScale (grade, min_score, max_score, classification) VALUES
                                                                         ('A', 70, 100, 'First Class Honours'),
                                                                         ('B', 60, 69, 'Upper Second Class Honours'),
                                                                         ('C', 50, 59, 'Lower Second Class Honours'),
                                                                         ('D', 40, 49, 'Pass'),
                                                                         ('F', 0, 39, 'Fail')
ON CONFLICT (grade) DO NOTHING;

-- Insert Grades
INSERT INTO Grades (student_id, course_id, score) VALUES
                                                      (1, 'CS101', 85),
                                                      (2, 'MATH101', 55);
