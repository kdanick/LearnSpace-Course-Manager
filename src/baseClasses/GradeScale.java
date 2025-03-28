package baseClasses;

class GradeScale {
    private char grade;
    private double min_score;
    private double max_score;
    private String classification;

    public GradeScale(char grade, double min_score, double max_score, String classification) {
        this.grade = grade;
        this.min_score = min_score;
        this.max_score = max_score;
        this.classification = classification;
    }

    public char getGrade() { return grade; }
    public double getMinScore() { return min_score; }
    public double getMaxScore() { return max_score; }
    public String getClassification() { return classification; }
}
