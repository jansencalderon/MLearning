package com.tip.capstone.mlearning.ui.simulation;

import android.app.Dialog;
import android.content.ClipData;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.databinding.ActivityReviseSimulation2Binding;

public class ReviseSimulationActivity2 extends AppCompatActivity
        implements View.OnDragListener, View.OnTouchListener {

    private ActivityReviseSimulation2Binding binding;

    private Drawable enterShape;
    private Drawable normalShape;
    private Drawable aldeShape, aldeShapeDrop,
            hydroShape, hydroShapeDrop,
            phenolShape, phenolShapeDrop;

    public static final int[] SIMULATION_DRAWABLES = {R.drawable.ic_alde_benzaldehyde, R.drawable.ic_alde_cinammic, R.drawable.ic_alde_citronellal,
            R.drawable.ic_hydro_heptanes, R.drawable.ic_hydro_limonene, R.drawable.ic_hydro_pinene,
            R.drawable.ic_phenols_carvacrol, R.drawable.ic_phenols_eugenol, R.drawable.ic_phenols_thymol};

    public static final int[] PANEL_ALDE = {R.id.alde_benzaldehyde, R.id.alde_cinnamic, R.id.alde_citronellal};
    public static final int[] PANEL_HYDRO = {R.id.hydro_heptanes, R.id.hydro_limonene, R.id.hydro_pinene};
    public static final int[] PANEL_PHENOLS = {R.id.phenols_carvacrol, R.id.phenols_eugenol, R.id.phenols_thymol};

    private int score = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_revise_simulation_2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Volatile Oils Part 1");

        enterShape = ContextCompat.getDrawable(this, R.drawable.shape_droptarget);
        normalShape = ContextCompat.getDrawable(this, R.drawable.shape);
        aldeShape = ContextCompat.getDrawable(this, R.drawable.shape_alde);
        aldeShapeDrop = ContextCompat.getDrawable(this, R.drawable.shape_alde_drop);
        hydroShape = ContextCompat.getDrawable(this, R.drawable.shape_hydro);
        hydroShapeDrop = ContextCompat.getDrawable(this, R.drawable.shape_hydro_drop);
        phenolShape = ContextCompat.getDrawable(this, R.drawable.shape_phenols);
        phenolShapeDrop = ContextCompat.getDrawable(this, R.drawable.shape_phenols_drop);

        binding.layoutTopLeft.setOnDragListener(this);
        binding.layoutAldehydes.setOnDragListener(this);
        binding.layoutHydro.setOnDragListener(this);
        binding.layoutPhenols.setOnDragListener(this);

        ImageView[] imageViews = {binding.aldeBenzaldehyde, binding.aldeCinnamic, binding.aldeCitronellal
                , binding.hydroHeptanes, binding.hydroLimonene, binding.hydroPinene
                , binding.phenolsCarvacrol, binding.phenolsEugenol, binding.phenolsThymol};

        for (int i = 0; i < imageViews.length; i++) {
            Glide.with(this)
                    .load(SIMULATION_DRAWABLES[i])
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .priority(Priority.IMMEDIATE)
                    .dontAnimate()
                    .into(imageViews[i]);

            imageViews[i].setOnTouchListener(this);
        }

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_instruction_2);
        dialog.setCancelable(false);
        Button start = (Button) dialog.findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        ImageView imageView = (ImageView) dragEvent.getLocalState();
        switch (dragEvent.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // do nothing
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                enterShape(view);
                /*if (isEnterable(view, imageView)) {
                }*/
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                normalShape(view);
                break;
            case DragEvent.ACTION_DROP:
                if (isEnterable(view, imageView)) {
                    // Dropped, reassign View to ViewGroup
                    View view1 = (View) dragEvent.getLocalState();
                    ViewGroup owner = (ViewGroup) view1.getParent();
                    owner.removeView(view1);
                    FlexboxLayout container = (FlexboxLayout) view;
                    container.addView(view1);
                    view1.setVisibility(View.VISIBLE);
                    if(view.getId() != R.id.layout_top_left){
                        MediaPlayer mp = MediaPlayer.create(ReviseSimulationActivity2.this, R.raw.ting);
                        mp.start();
                    }
                } else {
                    imageView.setVisibility(View.VISIBLE);
                    MediaPlayer mp = MediaPlayer.create(ReviseSimulationActivity2.this, R.raw.engk);
                    mp.start();
                }
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                normalShape(view);
                if(isFinish()){
                    final Dialog dialog = new Dialog(this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_congrats_2);
                    dialog.setCancelable(false);
                    Button start = (Button) dialog.findViewById(R.id.start);
                    start.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    });
                    dialog.show();
                }
                break;
        }
        return true;
    }

    private void enterShape(View view) {
        switch (view.getId()) {
            case R.id.layout_aldehydes:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(aldeShapeDrop);
                } else {
                    //noinspection deprecation
                    view.setBackgroundDrawable(aldeShapeDrop);
                }
                break;
            case R.id.layout_hydro:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(hydroShapeDrop);
                } else {
                    //noinspection deprecation
                    view.setBackgroundDrawable(hydroShapeDrop);
                }
                break;
            case R.id.layout_phenols:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(phenolShapeDrop);
                } else {
                    //noinspection deprecation
                    view.setBackgroundDrawable(phenolShapeDrop);
                }
                break;
        }
    }

    private void normalShape(View view) {
        switch (view.getId()) {
            case R.id.layout_aldehydes:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(aldeShape);
                } else {
                    //noinspection deprecation
                    view.setBackgroundDrawable(aldeShape);
                }
                break;
            case R.id.layout_hydro:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(hydroShape);
                } else {
                    //noinspection deprecation
                    view.setBackgroundDrawable(hydroShape);
                }
                break;
            case R.id.layout_phenols:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(phenolShape);
                } else {
                    //noinspection deprecation
                    view.setBackgroundDrawable(phenolShape);
                }
                break;
        }
    }


    private boolean isEnterable(View view, ImageView imageView) {
        switch (view.getId()) {
            case R.id.layout_top_left:
                return true;
            case R.id.layout_aldehydes:
                return asd(PANEL_ALDE, (int) imageView.getId());
            case R.id.layout_phenols:
                return asd(PANEL_PHENOLS, (int) imageView.getId());
            case R.id.layout_hydro:
                return asd(PANEL_HYDRO, (int) imageView.getId());
            default:
                return false;
        }
    }

    private boolean asd(int[] panels, int id) {
        for (int i = 0; i < panels.length; i++) {
            if (id == panels[i]) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData clipData = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(clipData, shadowBuilder, view, 0);
            view.setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("ReviseSimulation Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private boolean isFinish() {
        for (int id : PANEL_ALDE) {
            if (binding.layoutAldehydes.findViewById(id) == null) {
                return false;
            }
        }
        for (int id : PANEL_PHENOLS) {
            if (binding.layoutPhenols.findViewById(id) == null) {
                return false;
            }
        }
        for (int id : PANEL_HYDRO) {
            if (binding.layoutHydro.findViewById(id) == null) {
                return false;
            }
        }
        return true;
    }

}
