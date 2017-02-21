package com.tip.capstone.mlearning.ui.grades.detail;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.ActivityGradesDetailBinding;
import com.tip.capstone.mlearning.databinding.DialogGradesBinding;
import com.tip.capstone.mlearning.databinding.DialogGradesHistoryBinding;
import com.tip.capstone.mlearning.model.AssessmentGrade;
import com.tip.capstone.mlearning.model.Grades;
import com.tip.capstone.mlearning.model.PreQuizGrade;
import com.tip.capstone.mlearning.model.QuizGrade;
import com.tip.capstone.mlearning.model.Term;
import com.tip.capstone.mlearning.model.Topic;
import com.tip.capstone.mlearning.ui.adapter.AssessmentHistoryListAdapter;
import com.tip.capstone.mlearning.ui.adapter.GradesHistoryListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * @author pocholomia
 * @since 22/11/2016
 */
public class GradesDetailActivity extends MvpActivity<GradesDetailView, GradesDetailPresenter> implements GradesDetailView {

    private Realm realm;
    private GradesDetailListAdapter adapter;
    private ActivityGradesDetailBinding binding;
    private List<QuizGrade> quizGrades = new ArrayList<>();
    private List<Topic> topics = new ArrayList<>();

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_grades_detail);

        // assumes that theme has toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // setup RecyclerView and adapter
        adapter = new GradesDetailListAdapter(getMvpView());

        int is = getIntent().getIntExtra(Constant.ID, -1);
        Term terms = realm.where(Term.class).equalTo("id", is).findFirst();
        if (terms != null) {
            binding.perTerm.setVisibility(View.VISIBLE);
            binding.activityGrades.setVisibility(View.GONE);
            getSupportActionBar().setTitle(terms.getTitle());


            List<Grades> gradesList = new ArrayList<>();
            RealmResults<Term> termRealmResults = realm.where(Term.class).equalTo("id", is).findAllSorted(Term.COL_SEQ);
            for (Term term : termRealmResults) {
                //Grades headerTerm = new Grades();
                //headerTerm.setHeader(true);
                //headerTerm.setTitle(term.getTitle());
                //headerTerm.setSequence(gradesList.size() + 1);
                // gradesList.add(headerTerm);

                for (Topic topic : term.getTopics().sort(Topic.COL_SEQ)) {
                    // topic header
                    Grades topicHeader = new Grades();
                    topicHeader.setHeader(true);
                    topicHeader.setTitle(topic.getTitle());
                    topicHeader.setSequence(gradesList.size() + 1);

                    RealmResults<QuizGrade> quizGradeRealmResults = realm.where(QuizGrade.class)
                            .equalTo("topic", topic.getId()).findAllSorted("count", Sort.DESCENDING);

                    QuizGrade quizGrade = null;
                    if (quizGradeRealmResults.size() > 0) {
                        quizGrade = quizGradeRealmResults.first();
                    }

                    if (quizGrade != null) {
                        // nonHeader.setQuizGrade(realm.copyFromRealm(quizGrade));
                        topicHeader.setQuizGrade(quizGrade);
                        quizGrades.add(quizGrade);
                        topics.add(topic);
                        Log.d("Quiz Grade", topic.getTitle() + " : " + quizGrade);
                    }


                    gradesList.add(topicHeader);
                }

                RealmResults<AssessmentGrade> assessmentGradeRealmResults = realm
                        .where(AssessmentGrade.class)
                        .equalTo("term", term.getId())
                        .findAllSorted("count", Sort.DESCENDING);

                AssessmentGrade assessmentGrade = null;
                if (assessmentGradeRealmResults.size() > 0) {
                    assessmentGrade = assessmentGradeRealmResults.first();
                }

                Grades gradeAssessmentHeader = new Grades();
                gradeAssessmentHeader.setTitle(term.getTitle() + " Assessment");
                gradeAssessmentHeader.setSequence(gradesList.size() + 1);
                gradeAssessmentHeader.setAssessmentGrade(assessmentGrade != null ? realm.copyFromRealm(assessmentGrade) : new AssessmentGrade());
                gradesList.add(gradeAssessmentHeader);

            }

            adapter.setGradesList(gradesList);
            binding.topicRecyclerView.setAdapter(adapter);
            binding.topicRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            binding.topicRecyclerView.setItemAnimator(new DefaultItemAnimator());
            binding.topicRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

            if (quizGrades.size() > 0) {

                //set Line Graph

                LineChart lineChart;
                lineChart = binding.topicsPerTerm;
                //lineChart.setTouchEnabled(false);
                // lineChart.setPinchZoom(false);
                lineChart.getXAxis().setDrawLabels(false);
                lineChart.setDescription(null);
                lineChart.setDoubleTapToZoomEnabled(false);
                lineChart.getAxisLeft().setAxisMaximum(100f);
                lineChart.getAxisRight().setEnabled(false);
                lineChart.animateY(1000);

                XAxis xl2 = lineChart.getXAxis();
                xl2.setGranularity(1f);

                ArrayList<Entry> entries = new ArrayList<>();

                for (int i = 0; i < quizGrades.size(); i++) {
                    entries.add(new Entry(i + 1, (float) quizGrades.get(i).getRawScore() * 10));
                }


                ArrayList<String> labels = new ArrayList<>();
                for (int k = 0; k < topics.size(); k++) {
                    StringBuilder initials = new StringBuilder();
                    for (String s : topics.get(k).getTitle().split(" ")) {
                        initials.append(s.charAt(0));
                    }
                  labels.add(initials.toString());
                    Log.d("INITIALS: ", initials.toString());
                }
                /*List<LegendEntry> legendEntries = new ArrayList<>();
                for (int i = 0; i < topics.size(); i++) {
                    labels.add(i, topics.get(i).getDescription());
                }*/


                LineDataSet lineDataSet = new LineDataSet(entries, "Line");
                lineDataSet.setColors(ContextCompat.getColor(this, R.color.colorPrimaryDark));
                lineDataSet.setDrawFilled(true);
                lineDataSet.setCircleColors(ColorTemplate.COLORFUL_COLORS);
                lineDataSet.setDrawCircleHole(false);

                LineData lineData = new LineData(lineDataSet);

                lineChart.setData(lineData);


                Legend l = lineChart.getLegend();
                l.setFormSize(10f); // set the size of the legend forms/shapes
                l.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
                l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
                l.setWordWrapEnabled(true);
                l.setTextSize(12f);
                l.setTextColor(Color.BLACK);
                l.setXEntrySpace(5f); // set the space between the legend entries on the x-axisx

                // set custom labels and colors
                String[] stockArr = new String[labels.size()];
                stockArr =labels.toArray(stockArr);
                l.setExtra(ColorTemplate.COLORFUL_COLORS, stockArr);
            }

        } else {
            binding.activityGrades.setVisibility(View.VISIBLE);
            binding.perTerm.setVisibility(View.GONE);
            getData();
            setDataPre();
            setDataQuiz();
            setDataComparison();
        }


        binding.showDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    private void setDataPre() {


        RealmResults<PreQuizGrade> realmResults = realm.where(PreQuizGrade.class).findAll();
        if (realmResults.size() > 0) {
            BarChart chart;
            ArrayList<BarEntry> BARENTRY;
            ArrayList<String> BarEntryLabels;
            BarDataSet Bardataset;
            BarData BARDATA;
            chart = binding.preQuiz;
            BARENTRY = new ArrayList<>();

            XAxis xl = chart.getXAxis();
            xl.setGranularity(1f);

            for (int i = 0; i < realmResults.size(); i++) {
                BARENTRY.add(new BarEntry((float) i + 1, (float) realmResults.get(i).getRawScore() * 10f, "Pre Quiz " + i));
            }

            Bardataset = new BarDataSet(BARENTRY, "Pre Assessments");


            BARDATA = new BarData(Bardataset);

            Bardataset.setColors(ContextCompat.getColor(this, R.color.colorPrimary));
            chart.setData(BARDATA);
            chart.getAxisRight().setAxisMaximum(100f);
            chart.getAxisLeft().setAxisMaximum(100f);
            chart.setDescription(null);
            //chart.setPinchZoom(false);
            chart.animateY(2000);
            chart.setDoubleTapToZoomEnabled(false);
            //chart.setTouchEnabled(false);
            chart.getXAxis().setAxisMinimum(1f);
            chart.setHighlightFullBarEnabled(false);
            chart.setDoubleTapToZoomEnabled(false);
            chart.getAxisRight().setEnabled(false);
            chart.invalidate();

            //set Line Graph

            LineChart lineChart;
            lineChart = binding.preQuizLineGraph;
            //lineChart.setTouchEnabled(false);
            // lineChart.setPinchZoom(false);
            lineChart.setDescription(null);
            lineChart.setDoubleTapToZoomEnabled(false);
            lineChart.getAxisLeft().setAxisMaximum(100f);
            lineChart.getAxisRight().setEnabled(false);
            lineChart.animateY(1000);

            XAxis xl2 = lineChart.getXAxis();
            xl2.setGranularity(1f);

            ArrayList<Entry> entries = new ArrayList<>();
            for (int i = 0; i < realmResults.size(); i++) {
                entries.add(new Entry(i + 1, (float) realmResults.get(i).getRawScore() * 10));
            }

            ArrayList<String> labels = new ArrayList<>();
            for (int i = 0; i < realmResults.size(); i++) {
                labels.add(i, "Quiz " + i);
            }

            LineDataSet lineDataSet = new LineDataSet(entries, "Pre Assessments");
            lineDataSet.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
            lineDataSet.setDrawFilled(true);
            lineDataSet.setCircleColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

            LineData lineData = new LineData(lineDataSet);

            lineChart.setData(lineData);
        }

    }


    public void setDataQuiz() {


        RealmResults<QuizGrade> realmResults2 = realm.where(QuizGrade.class).findAll();

        if (realmResults2.size() > 0) {
            BarChart chart;
            ArrayList<BarEntry> BARENTRY;
            ArrayList<String> BarEntryLabels;
            BarDataSet Bardataset;
            BarData BARDATA;
            chart = binding.quiz;
            BARENTRY = new ArrayList<>();

            XAxis xl = chart.getXAxis();
            xl.setGranularity(1f);

            for (int i = 0; i < realmResults2.size(); i++) {
                BARENTRY.add(new BarEntry((float) i + 1, (float) realmResults2.get(i).getRawScore() * 10f, "Quiz " + i));
            }
            Bardataset = new BarDataSet(BARENTRY, "Post Assessments");


            BARDATA = new BarData(Bardataset);

            Bardataset.setColors(ContextCompat.getColor(this, R.color.colorPrimary));
            chart.setData(BARDATA);
            chart.setDescription(null);
            chart.animateY(1000);
            // chart.setPinchZoom(false);
            // chart.setTouchEnabled(false);
            chart.getXAxis().setAxisMinimum(1f);
            chart.setHighlightFullBarEnabled(false);
            chart.setDoubleTapToZoomEnabled(false);
            chart.getAxisLeft().setAxisMaximum(100f);
            chart.getAxisRight().setAxisMaximum(100f);
            chart.getAxisRight().setEnabled(false);
            chart.invalidate();

            LineChart lineChart;
            lineChart = binding.postQuizLineGraph;
            //lineChart.setTouchEnabled(false);
            // lineChart.setPinchZoom(false);
            lineChart.setDoubleTapToZoomEnabled(false);
            lineChart.setDescription(null);
            lineChart.getAxisLeft().setAxisMaximum(100f);
            lineChart.getAxisRight().setEnabled(false);
            lineChart.animateY(1000);

            XAxis xl2 = lineChart.getXAxis();
            xl2.setGranularity(1f);

            ArrayList<Entry> entries = new ArrayList<>();
            for (int i = 0; i < realmResults2.size(); i++) {
                entries.add(new Entry(i + 1, (float) realmResults2.get(i).getRawScore() * 10));
            }

            ArrayList<String> labels = new ArrayList<>();
            for (int i = 0; i < realmResults2.size(); i++) {
                labels.add(i, "Post Assessments " + i);
            }

            LineDataSet lineDataSet = new LineDataSet(entries, "Post Assessments");
            lineDataSet.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
            lineDataSet.setDrawFilled(true);
            lineDataSet.setCircleColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

            LineData lineData = new LineData(lineDataSet);

            lineChart.setData(lineData);
        }


    }

    public void setDataComparison() {

        RealmResults<PreQuizGrade> realmResults = realm.where(PreQuizGrade.class).findAll();
        RealmResults<QuizGrade> realmResults2 = realm.where(QuizGrade.class).findAll();
        if (realmResults.size() > 0 && realmResults2.size() > 0) {
            BarChart chart;
            float groupSpace = 0.04f;
            float barSpace = 0.02f; // x2 dataset
            float barWidth = 0.46f;
            BarDataSet set1, set2;

            List<BarEntry> yVals1 = new ArrayList<BarEntry>();
            List<BarEntry> yVals2 = new ArrayList<BarEntry>();

            XAxis xl = binding.comparison.getXAxis();
            xl.setGranularity(1f);


            for (int i = 0; i < realmResults.size(); i++) {
                try {
                    yVals1.add(new BarEntry(i, (float) realmResults.get(i).getRawScore() * 10f));
                    yVals2.add(new BarEntry(i, (float) realmResults2.get(i).getRawScore() * 10f));
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }

            set1 = new BarDataSet(yVals1, "Pre Assessments");
            set1.setColors(ColorTemplate.COLORFUL_COLORS);
            set2 = new BarDataSet(yVals2, "Post Assessments");
            set2.setColors(ColorTemplate.COLORFUL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            dataSets.add(set2);

            BarData data = new BarData(dataSets);
            binding.comparison.setData(data);
            binding.comparison.setDescription(null);
            binding.comparison.setFitBars(true);
            binding.comparison.setTouchEnabled(false);
            binding.comparison.setPinchZoom(false);
            binding.comparison.animateY(1000);
            binding.comparison.setMaxVisibleValueCount(50);
            binding.comparison.setDoubleTapToZoomEnabled(false);
            binding.comparison.setHighlightFullBarEnabled(false);
            binding.comparison.getAxisLeft().setAxisMaximum(100f);
            binding.comparison.getXAxis().setAxisMinimum(0f);
            binding.comparison.getAxisLeft().setDrawGridLines(false);
            binding.comparison.getAxisRight().setEnabled(false);
            binding.comparison.getBarData().setBarWidth(barWidth);
            binding.comparison.groupBars(0, groupSpace, barSpace);
            binding.comparison.invalidate();

        }

    }

    /**
     * Setup the Grades Data for both Short Quiz and Final Assessment
     */
    private void getData() {
        List<Grades> gradesList = new ArrayList<>();
        RealmResults<Term> termRealmResults = realm.where(Term.class).findAllSorted(Term.COL_SEQ);
        for (Term term : termRealmResults) {
            Grades headerTerm = new Grades();
            headerTerm.setHeader(true);
            headerTerm.setTitle(term.getTitle());
            headerTerm.setSequence(gradesList.size() + 1);
            gradesList.add(headerTerm);

            for (Topic topic : term.getTopics().sort(Topic.COL_SEQ)) {
                // topic header
                Grades topicHeader = new Grades();
                topicHeader.setHeader(true);
                topicHeader.setTitle(topic.getTitle());
                topicHeader.setSequence(gradesList.size() + 1);
                gradesList.add(topicHeader);

                Grades nonHeader = new Grades();

                nonHeader.setHeader(false);
                nonHeader.setTitle("Pre Assessments");
                PreQuizGrade preQuizGrade = realm.where(PreQuizGrade.class)
                        .equalTo(Constant.ID, topic.getId()).findFirst();
                if (preQuizGrade != null) {
                    QuizGrade preGrade = new QuizGrade();
                    preGrade.setRawScore(preQuizGrade.getRawScore());
                    preGrade.setItemCount(preQuizGrade.getItemCount());
                    nonHeader.setQuizGrade(preGrade);
                } else {
                    nonHeader.setQuizGrade(null);
                }
                nonHeader.setSequence(gradesList.size() + 1);
                gradesList.add(nonHeader);

                nonHeader = new Grades();
                nonHeader.setHeader(false);
                nonHeader.setTitle("Post Assessments");
                RealmResults<QuizGrade> quizGradeRealmResults = realm.where(QuizGrade.class)
                        .equalTo("topic", topic.getId()).findAllSorted("count", Sort.DESCENDING);

                QuizGrade quizGrade = null;
                if (quizGradeRealmResults.size() > 0) {
                    quizGrade = quizGradeRealmResults.first();
                }

                if (quizGrade != null) {
                    nonHeader.setQuizGrade(realm.copyFromRealm(quizGrade));
                } else {
                    nonHeader.setQuizGrade(null);
                }

                nonHeader.setSequence(gradesList.size() + 1);
                gradesList.add(nonHeader);
            }

            RealmResults<AssessmentGrade> assessmentGradeRealmResults = realm
                    .where(AssessmentGrade.class)
                    .equalTo("term", term.getId())
                    .findAllSorted("count", Sort.DESCENDING);

            AssessmentGrade assessmentGrade = null;
            if (assessmentGradeRealmResults.size() > 0) {
                assessmentGrade = assessmentGradeRealmResults.first();
            }

            Grades gradeAssessmentHeader = new Grades();
            gradeAssessmentHeader.setTitle(term.getTitle() + " Assessment");
            gradeAssessmentHeader.setSequence(gradesList.size() + 1);
            gradeAssessmentHeader.setAssessmentGrade(assessmentGrade != null ? realm.copyFromRealm(assessmentGrade) : new AssessmentGrade());
            gradesList.add(gradeAssessmentHeader);

        }


        adapter.setGradesList(gradesList);

    }

    public void showDialog() {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        DialogGradesBinding dialogGradesBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_grades,
                null,
                false);
        dialogGradesBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dialogGradesBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        dialogGradesBinding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        dialogGradesBinding.recyclerView.setAdapter(adapter);

        dialog.setContentView(dialogGradesBinding.getRoot());
        dialog.show();
    }

    @Override
    public void showGradeHistory(Grades grade) {
        DialogGradesHistoryBinding historyBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_grades_history,
                null,
                false);

        Topic topic = realm.where(Topic.class).equalTo("title", grade.getTitle()).findFirst();

        RealmResults<QuizGrade> quizGradeRealmResults = realm.where(QuizGrade.class)
                .equalTo("topic", topic.getId()).findAllSorted("count", Sort.DESCENDING);

        GradesHistoryListAdapter gradesHistoryListAdapter = new GradesHistoryListAdapter();
        gradesHistoryListAdapter.setGradesList(quizGradeRealmResults);
        historyBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        historyBinding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        historyBinding.recyclerView.setAdapter(gradesHistoryListAdapter);
        historyBinding.topic.setText(grade.getTitle());
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(historyBinding.getRoot());
        if (!quizGradeRealmResults.isEmpty()) {
            dialog.show();
        } else {
            Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
    @Override
    public void showAssessmentHistory(Grades grades) {
        DialogGradesHistoryBinding historyBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_grades_history,
                null,
                false);

        RealmResults<AssessmentGrade> quizGradeRealmResults = realm.where(AssessmentGrade.class).equalTo("term", grades.getAssessmentGrade().getTerm())
                .findAllSorted("count", Sort.ASCENDING);
        AssessmentHistoryListAdapter assessmentHistoryListAdapter = new AssessmentHistoryListAdapter();
        assessmentHistoryListAdapter.setGradesList(quizGradeRealmResults);
        historyBinding.topic.setText(grades.getTitle());

        historyBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        historyBinding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        historyBinding.recyclerView.setAdapter(assessmentHistoryListAdapter);

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(historyBinding.getRoot());
        dialog.show();

        if (!quizGradeRealmResults.isEmpty()) {
            dialog.show();
        } else {
            Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_grades, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_reset_grades:
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(PreQuizGrade.class);
                        realm.delete(QuizGrade.class);
                        realm.delete(AssessmentGrade.class);
                        binding.preQuiz.invalidate();
                        binding.quiz.invalidate();
                        binding.postQuizLineGraph.invalidate();
                        binding.preQuizLineGraph.invalidate();
                    }
                });
                getData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @NonNull
    @Override
    public GradesDetailPresenter createPresenter() {
        return new GradesDetailPresenter();
    }

}
