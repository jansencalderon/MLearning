package com.tip.capstone.mlearning.ui.simulation;

import android.content.ClipData;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.SimulationData;
import com.tip.capstone.mlearning.databinding.ActivitySimulationBinding;

public class SimulationActivity extends AppCompatActivity
        implements View.OnDragListener, View.OnTouchListener {

    private static final String TAG = SimulationActivity.class.getSimpleName();
    private ActivitySimulationBinding binding;
    final String[] items = {"Red", "Blue", "Yellow"};
    private Drawable enterShape;
    private Drawable normalShape;


    @SuppressWarnings("ConstantConditions") // for toolbar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_simulation);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Simulation"); // TODO: 01/12/2016 set title on manifest instead

        enterShape = ContextCompat.getDrawable(this, R.drawable.shape_droptarget);
        normalShape = ContextCompat.getDrawable(this, R.drawable.shape);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showItems();
            }
        });
        binding.clear.setOnDragListener(this);
        binding.contentSimulation.setOnDragListener(this);
    }

    private void showItems() {
        new AlertDialog.Builder(this)
                .setTitle("Items")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Snackbar.make(binding.getRoot(), items[i], Snackbar.LENGTH_SHORT).show();
                        ImageView imageView = new ImageView(SimulationActivity.this);
                        /*imageView.setMinimumWidth(48);
                        imageView.setMinimumWidth(48);
                        imageView.setMaxWidth(48);
                        imageView.setMaxHeight(48);*/
                        imageView.setBackground(normalShape);
                        imageView.setImageDrawable(ContextCompat.getDrawable(SimulationActivity.this,
                                SimulationData.SIMULATION_DRAWABLES[i]));
                        imageView.setOnTouchListener(SimulationActivity.this);
                        imageView.setOnDragListener(SimulationActivity.this);
                        binding.contentSimulation.addView(imageView);
                    }
                })
                .show();
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


        switch (dragEvent.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // do nothing
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                if (view != binding.contentSimulation) {
                    if (view == binding.clear) {
                        view.setBackground(enterShape);
                    } else {
                        ImageView view1 = (ImageView) dragEvent.getLocalState();
                        ImageView view2 = (ImageView) view;
                        if (SimulationData.getResultDrawable(SimulationActivity.this,
                                view1.getDrawable(), view2.getDrawable()) != null) {

                        }
                    }
                }
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                if (view != binding.contentSimulation) {
                    view.setBackground(normalShape);
                }
                break;
            case DragEvent.ACTION_DROP:
                // Dropped, reassign View to ViewGroup
                ImageView view1 = (ImageView) dragEvent.getLocalState();
                float X = dragEvent.getX();
                float Y = dragEvent.getY();
                ViewGroup owner = (ViewGroup) view1.getParent();
                owner.removeView(view1);
                if (view == binding.contentSimulation) {
                    RelativeLayout container = (RelativeLayout) view;
                    container.addView(view1);
                    view1.setX(X - (view1.getWidth() / 2));
                    view1.setY(Y - (view1.getHeight() / 2));
                    view1.setVisibility(View.VISIBLE);
                } else if (view == binding.clear) {
                    // do nothing
                } else {
                    ImageView view2 = (ImageView) view;
                    Drawable drawable = SimulationData.getResultDrawable(SimulationActivity.this,
                            view1.getDrawable(), view2.getDrawable());
                    if (drawable != null) {
                        Log.d(TAG, "onDrag: drawable " + drawable.toString());
                        view2.setImageDrawable(drawable);
                    } else {
                        Log.d(TAG, "onDrag: drawable no drawable");
                        binding.contentSimulation.addView(view1);
                        /*view1.setX(X - (view1.getWidth() / 2));
                        view1.setY(Y - (view1.getHeight() / 2));*/
                        view1.setVisibility(View.VISIBLE);
                    }
                }

                break;
            case DragEvent.ACTION_DRAG_ENDED:
                if (view != binding.contentSimulation) {
                    view.setBackground(normalShape);
                }
                if (!dragEvent.getResult()) view.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        return true;
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
}
