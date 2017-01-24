package com.tip.capstone.mlearning.ui.videos;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.model.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 29/11/2016
 */

public class VideoListPresenter extends MvpNullObjectBasePresenter<VideoListView> {

    private static final int[] VIDEO_RES_ID = {
            R.raw.acidimetric,
            R.raw.alkalimetric_titration,
            R.raw.aqueous_solution_1_m_nacl,
            R.raw.gravimetric_analysis_of_an_unknown_chloride_salt,
            R.raw.non_aqueous,
            R.raw.titrimetric_volumetric,
            R.raw.complexometric_titration,
            R.raw.how_to_prepare_standard_edta_solution,
            R.raw.iodemetric_titration,
            R.raw.iodometric_titration_of_copper,
            R.raw.precipitation_titration_mohr_method,
            R.raw.redox_titration_lab
    };
    private static final String[] VIDEO_NAME_WITHOUT_EXTENSION = {
            "acidimetric",
            "alkalimetric_titration",
            "aqueous_solution_1_m_nacl",
            "gravimetric_analysis_of_an_unknown_chloride_salt",
            "non_aqueous",
            "titrimetric_volumetric",
            "complexometric_titration",
            "how_to_prepare_standard_edta_solution",
            "iodemetric_titration",
            "iodometric_titration_of_copper",
            "precipitation_titration_mohr_method",
            "redox_titration_lab"
    };
    private static final String[] VIDEO_TITLE = {
            "Acidimetric",
            "Alkalimetric Titration",
            "Aqueous solution 1 M NaCl",
            "Gravimetric Analysis of an Unknown Chloride Salt",
            "Non Aqueous",
            "Titrimetric Volumetric",
            "Complexometric Titration",
            "How to prepare standard EDTA solution",
            "Iodemetric Titration",
            "Iodometric Titration of Copper",
            "Precipitation Titration - Mohr Method",
            "Redox titration lab - permanganate and iron (II) under acidic conditions"
    };

    public void loadVideoList(String query) {
        List<Video> videoList = new ArrayList<>();
        for (int i = 0; i < VIDEO_RES_ID.length; i++) {
            int resId = VIDEO_RES_ID[i];
            String nameWithoutExtension = i < VIDEO_NAME_WITHOUT_EXTENSION.length ?
                    VIDEO_NAME_WITHOUT_EXTENSION[i] : "";
            String videoTitle = i < VIDEO_TITLE.length ?
                    VIDEO_TITLE[i] : "";
            Video video = new Video(resId, nameWithoutExtension, videoTitle);
            if (query != null && !query.isEmpty() && videoTitle.toUpperCase().contains(query.toUpperCase())) {
                videoList.add(video);
            } else if (query == null || query.isEmpty()) {
                videoList.add(video);
            }

        }
        getView().setVideoList(videoList);
    }
}