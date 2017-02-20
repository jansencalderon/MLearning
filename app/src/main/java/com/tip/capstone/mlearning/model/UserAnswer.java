package com.tip.capstone.mlearning.model;

/**
 * @author pocholomia
 * @since 21/11/2016
 */

public class UserAnswer {

    private int questionId;
    private String userAnswer;
    private String correctAnswer;
    private int choiceType;
    private int lessonDetailId;

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public boolean isCorrect() {
        return correctAnswer.equalsIgnoreCase(userAnswer == null ? "" : userAnswer);
    }

    public int getChoiceType() {
        return choiceType;
    }

    public void setChoiceType(int choiceType) {
        this.choiceType = choiceType;
    }

    public int getLessonDetailId() {
        return lessonDetailId;
    }

    public void setLessonDetailId(int lessonDetailId) {
        this.lessonDetailId = lessonDetailId;
    }
}
