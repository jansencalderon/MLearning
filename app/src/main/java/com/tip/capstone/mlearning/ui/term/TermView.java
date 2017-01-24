package com.tip.capstone.mlearning.ui.term;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tip.capstone.mlearning.model.Term;

/**
 * @author pocholomia
 * @since 18/11/2016
 */

@SuppressWarnings("WeakerAccess")
public interface TermView extends MvpView {

    void onTermClicked(Term term);

}
