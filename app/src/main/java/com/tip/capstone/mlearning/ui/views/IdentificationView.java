package com.tip.capstone.mlearning.ui.views;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tip.capstone.mlearning.model.Letter;

/**
 * Created by Cholo Mia on 12/21/2016.
 */

public interface IdentificationView extends MvpView {

    void onLetterClicked(int position, boolean choice, Letter letter);

}
