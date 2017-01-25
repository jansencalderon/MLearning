package com.tip.capstone.mlearning.ui.simulation;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.SimulationData;
import com.tip.capstone.mlearning.databinding.ActivityReviseSimulationBinding;

import java.util.List;

import static android.R.attr.width;

public class ReviseSimulationActivity extends AppCompatActivity
        implements View.OnDragListener, View.OnTouchListener {

    private ActivityReviseSimulationBinding binding;

    private Drawable enterShape;
    private Drawable normalShape;
    private Drawable acidShape, acidShapeDrop,
            alcoholShape, alcoholShapeDrop,
            ketoneShape, ketoneShapeDrop;

    public static final int[] SIMULATION_DRAWABLES = {R.drawable.ic_acetic, R.drawable.ic_butyric, R.drawable.ic_cinnamic,
            R.drawable.ic_borneol, R.drawable.ic_geraniol, R.drawable.ic_menthols,
            R.drawable.ic_camphor, R.drawable.ic_carvone, R.drawable.ic_menthone};

    public static final int[] PANEL_ACIDS = {R.id.acids_acetic, R.id.acids_butyric, R.id.acids_cinnamic};
    public static final int[] PANEL_KETONES = {R.id.ketones_camphor, R.id.ketones_carvone, R.id.ketones_menthone};
    public static final int[] PANEL_ALCOHOL = {R.id.alcohol_borneol, R.id.alcohol_geraniol, R.id.alcohol_menthol};

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_revise_simulation);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Simulation"); // TODO: 01/12/2016 set title on manifest instead


        enterShape = ContextCompat.getDrawable(this, R.drawable.shape_droptarget);
        normalShape = ContextCompat.getDrawable(this, R.drawable.shape);
        acidShape = ContextCompat.getDrawable(this, R.drawable.shape_acids);
        acidShapeDrop = ContextCompat.getDrawable(this, R.drawable.shape_acids_drop);
        alcoholShape = ContextCompat.getDrawable(this, R.drawable.shape_alcohols);
        alcoholShapeDrop = ContextCompat.getDrawable(this, R.drawable.shape_alcohols_drop);
        ketoneShape = ContextCompat.getDrawable(this, R.drawable.shape_ketones);
        ketoneShapeDrop = ContextCompat.getDrawable(this, R.drawable.shape_ketones_drop);

        binding.layoutTopLeft.setOnDragListener(this);
        binding.layoutAlcohol.setOnDragListener(this);
        binding.layoutKetones.setOnDragListener(this);
        binding.layoutAcids.setOnDragListener(this);

        ImageView[] imageViews = {binding.acidsAcetic, binding.acidsButyric, binding.acidsCinnamic
                , binding.alcoholBorneol, binding.alcoholGeraniol, binding.alcoholMenthol
                , binding.ketonesCamphor, binding.ketonesCarvone, binding.ketonesMenthone};

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
        dialog.setContentView(R.layout.dialog_instruction);
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
                        MediaPlayer mp = MediaPlayer.create(ReviseSimulationActivity.this, R.raw.ting);
                        mp.start();
                    }
                } else {
                    imageView.setVisibility(View.VISIBLE);
                    MediaPlayer mp = MediaPlayer.create(ReviseSimulationActivity.this, R.raw.engk);
                    mp.start();
                }
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                normalShape(view);
                if(isFinish()){
                    final Dialog dialog = new Dialog(this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_congrats_1);
                    dialog.setCancelable(false);
                    Button start = (Button) dialog.findViewById(R.id.start);
                    start.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(ReviseSimulationActivity.this,ReviseSimulationActivity2.class));
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
            case R.id.layout_alcohol:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(alcoholShapeDrop);
                } else {
                    //noinspection deprecation
                    view.setBackgroundDrawable(alcoholShapeDrop);
                }
                break;
            case R.id.layout_acids:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(acidShapeDrop);
                } else {
                    //noinspection deprecation
                    view.setBackgroundDrawable(acidShapeDrop);
                }
                break;
            case R.id.layout_ketones:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(ketoneShapeDrop);
                } else {
                    //noinspection deprecation
                    view.setBackgroundDrawable(ketoneShapeDrop);
                }
                break;
        }
    }

    private void normalShape(View view) {
        switch (view.getId()) {
            case R.id.layout_alcohol:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(alcoholShape);
                } else {
                    //noinspection deprecation
                    view.setBackgroundDrawable(alcoholShape);
                }
                break;
            case R.id.layout_acids:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(acidShape);
                } else {
                    //noinspection deprecation
                    view.setBackgroundDrawable(acidShape);
                }
                break;
            case R.id.layout_ketones:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(ketoneShape);
                } else {
                    //noinspection deprecation
                    view.setBackgroundDrawable(ketoneShape);
                }
                break;
        }
    }


    private boolean isEnterable(View view, ImageView imageView) {
        switch (view.getId()) {
            case R.id.layout_top_left:
                return true;
            case R.id.layout_acids:
                return asd(PANEL_ACIDS, (int) imageView.getId());
            case R.id.layout_alcohol:
                return asd(PANEL_ALCOHOL, (int) imageView.getId());
            case R.id.layout_ketones:
                return asd(PANEL_KETONES, (int) imageView.getId());
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
        for (int id : PANEL_ACIDS) {
            if (binding.layoutAcids.findViewById(id) == null) {
                return false;
            }
        }
        for (int id : PANEL_ALCOHOL) {
            if (binding.layoutAlcohol.findViewById(id) == null) {
                return false;
            }
        }
        for (int id : PANEL_KETONES) {
            if (binding.layoutKetones.findViewById(id) == null) {
                return false;
            }
        }
        return true;
    }

}
