package com.tip.capstone.mlearning.ui.tutorial;

import android.databinding.DataBindingUtil;
import android.databinding.tool.expr.ArgListExpr;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.databinding.FragmentTutorialDetailBinding;
import com.tip.capstone.mlearning.helper.ResourceHelper;
import com.tip.capstone.mlearning.model.Tutorial;

import java.util.Locale;

import io.realm.Realm;

public class TutorialFragment extends Fragment implements TextToSpeech.OnInitListener {
    private static final String ARG_PARAM1 = "param1";
    // TODO: Rename and change types of parameters
    private int mParam1;
    private FragmentTutorialDetailBinding binding;
    private TextToSpeech textToSpeech;


    public TutorialFragment() {
        // Required empty public constructor
    }


    public static TutorialFragment newInstance(int param1) {
        TutorialFragment fragment = new TutorialFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Realm realm = Realm.getDefaultInstance();
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_tutorial_detail, container, false);

        Tutorial tutorial = realm.where(Tutorial.class).equalTo("sequence",mParam1).findFirst();
        binding.text.setText(tutorial.getString());
        binding.imageView.setImageResource(ResourceHelper.getDrawableResourceId(getActivity(),tutorial.getDrawable()));
        //Glide.with(getActivity()).load(ResourceHelper.getDrawableResourceId(getActivity(),tutorial.getDrawable())).into(binding.imageView);

        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        textToSpeech = new TextToSpeech(getContext(), this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {

            int result = textToSpeech.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
                Snackbar.make(binding.getRoot(), "The TextToSpeech Language is not Supported",
                        Snackbar.LENGTH_SHORT).show();
            }

        } else {
            Snackbar.make(binding.getRoot(), "TextToSpeech Initialization Failed!",
                    Snackbar.LENGTH_SHORT).show();
            Log.e("TTS", "Initilization Failed!");
        }
    }
}
