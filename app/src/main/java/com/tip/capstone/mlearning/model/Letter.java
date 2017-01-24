package com.tip.capstone.mlearning.model;

/**
 * @author pocholomia
 * @since 08/12/2016
 */

public class Letter {

    private String letter;
    private boolean given;
    private boolean space;
    private boolean generated;

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public boolean isGiven() {
        return given;
    }

    public void setGiven(boolean given) {
        this.given = given;
    }

    public boolean isSpace() {
        return space;
    }

    public void setSpace(boolean space) {
        this.space = space;
    }

    public boolean isGenerated() {
        return generated;
    }

    public void setGenerated(boolean generated) {
        this.generated = generated;
    }
}
