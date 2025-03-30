-- Create Users Table
CREATE TABLE Users (
                       user_id INT PRIMARY KEY,
                       name VARCHAR(50) NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password_hash TEXT NOT NULL,
                       phone_no VARCHAR(20) CHECK (phone_no ~ '^[0-9+()-]+$'),
                       gender VARCHAR(10) CHECK (LOWER(gender) IN ('male', 'female', 'other')) NOT NULL,
                       role VARCHAR(20) CHECK (LOWER(role) IN ('admin', 'lecturer')) NOT NULL
);

-- Create Students Table
CREATE TABLE Students (
                          student_id INT PRIMARY KEY,
                          name VARCHAR(50) NOT NULL,
                          email VARCHAR(100) UNIQUE NOT NULL,
                          gender VARCHAR(10) CHECK (LOWER(gender) IN ('male', 'female', 'other')) NOT NULL
);

-- Create Courses Table
CREATE TABLE Courses (
                         course_id VARCHAR(50) PRIMARY KEY,
                         course_name VARCHAR(255) NOT NULL,
                         credits INT CHECK (credits BETWEEN 0 AND 3) NOT NULL,
                         lecturer_id INT REFERENCES Users(user_id) ON DELETE SET NULL
);

-- Create Enrollments Table
CREATE TABLE Enrollments (
                             enrollment_id SERIAL PRIMARY KEY,
                             student_id INT REFERENCES Students(student_id) ON DELETE CASCADE,
                             course_id VARCHAR(50) REFERENCES Courses(course_id) ON DELETE CASCADE,
                             enrolled_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             UNIQUE(student_id, course_id)
);

-- Create GradeScale Table
CREATE TABLE GradeScale (
                            grade CHAR(1) PRIMARY KEY CHECK (grade IN ('A', 'B', 'C', 'D', 'F')),
                            min_score DECIMAL(5,2) CHECK (min_score >= 0 AND min_score <= 100),
                            max_score DECIMAL(5,2) CHECK (max_score >= 0 AND max_score <= 100),
                            classification VARCHAR(50) NOT NULL,
                            UNIQUE (min_score, max_score)
);

-- Create Grades Table
CREATE TABLE Grades (
                        student_id INT,
                        course_id VARCHAR(50),
                        score DECIMAL(5,2) CHECK (score BETWEEN 0 AND 100),
                        grade CHAR(1) REFERENCES GradeScale(grade) ON DELETE RESTRICT,
                        PRIMARY KEY (student_id, course_id),
                        FOREIGN KEY (student_id, course_id) REFERENCES Enrollments(student_id, course_id) ON DELETE CASCADE
);

-- Function to Assign Grades
CREATE OR REPLACE FUNCTION assign_grade()
    RETURNS TRIGGER AS $$
BEGIN
    SELECT grade INTO NEW.grade
    FROM GradeScale
    WHERE NEW.score BETWEEN min_score AND max_score
    LIMIT 1;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger for Auto Assigning Grades
CREATE TRIGGER update_grade
    BEFORE INSERT OR UPDATE ON Grades
    FOR EACH ROW
EXECUTE FUNCTION assign_grade();

-- Function to Enforce Lowercase Fields
CREATE OR REPLACE FUNCTION enforce_lowercase_fields()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.gender := LOWER(NEW.gender);
    IF TG_TABLE_NAME = 'users' THEN
        NEW.role := LOWER(NEW.role);
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger for Users Table
CREATE TRIGGER enforce_lowercase_users
    BEFORE INSERT OR UPDATE ON Users
    FOR EACH ROW
EXECUTE FUNCTION enforce_lowercase_fields();

-- Trigger for Students Table
CREATE TRIGGER enforce_lowercase_students
    BEFORE INSERT OR UPDATE ON Students
    FOR EACH ROW
EXECUTE FUNCTION enforce_lowercase_fields();
