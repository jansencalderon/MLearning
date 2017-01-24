package com.tip.capstone.mlearning.model;

/**
 * @author pocholomia
 * @since 22/11/2016
 */

public class Grades {

    private boolean header;
    private String title;
    private QuizGrade quizGrade;
    private AssessmentGrade assessmentGrade;
    private int sequence;

    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public QuizGrade getQuizGrade() {
        return quizGrade;
    }

    public void setQuizGrade(QuizGrade quizGrade) {
        this.quizGrade = quizGrade;
    }

    public AssessmentGrade getAssessmentGrade() {
        return assessmentGrade;
    }

    public void setAssessmentGrade(AssessmentGrade assessmentGrade) {
        this.assessmentGrade = assessmentGrade;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
