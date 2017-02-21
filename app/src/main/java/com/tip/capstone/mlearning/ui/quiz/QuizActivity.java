package com.tip.capstone.mlearning.ui.quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.ActivityQuizBinding;
import com.tip.capstone.mlearning.databinding.DialogQuizSummaryBinding;
import com.tip.capstone.mlearning.model.Choice;
import com.tip.capstone.mlearning.model.Letter;
import com.tip.capstone.mlearning.model.PreQuizGrade;
import com.tip.capstone.mlearning.model.Question;
import com.tip.capstone.mlearning.model.QuizGrade;
import com.tip.capstone.mlearning.model.Topic;
import com.tip.capstone.mlearning.model.UserAnswer;
import com.tip.capstone.mlearning.ui.adapter.LetterListAdapter;
import com.tip.capstone.mlearning.ui.adapter.SummaryListAdapter;
import com.tip.capstone.mlearning.ui.lesson.LessonActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

/**
 * @author pocholomia
 * @since 21/11/2016
 */
public class QuizActivity extends MvpViewStateActivity<QuizView, QuizPresenter> implements QuizView {

    private static final String TAG = QuizActivity.class.getSimpleName();

    private Realm realm;
    private Topic topic;

    private ActivityQuizBinding binding;

    private List<Question> questionList;
    private List<UserAnswer> userAnswerList;

    private ChoiceListAdapter choiceAdapter;

    private LetterListAdapter adapterLetterAnswer;
    private LetterListAdapter adapterLetterChoice;
    private List<List<Letter>> lettersList;

    private boolean preQuiz;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        realm = Realm.getDefaultInstance(); // init realm db
        // check for intent
        int id = getIntent().getIntExtra(Constant.ID, -1);
        if (id == -1) {
            Toast.makeText(getApplicationContext(), "No Intent Extra Found!", Toast.LENGTH_SHORT).show();
            finish();
        }
        // check if has data
        topic = realm.where(Topic.class).equalTo(Constant.ID, id).findFirst();
        if (topic == null) {
            Toast.makeText(getApplicationContext(), "No Topic Data Found", Toast.LENGTH_SHORT).show();
            finish();
        }
        preQuiz = getIntent().getBooleanExtra(Constant.PRE_QUIZ, false);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz);
        // assumes theme has toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // TODO: 24/11/2016 setup activity title on manifest instead of here
        getSupportActionBar().setTitle("Short Quiz");
        // setup RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        // setup adapter
        choiceAdapter = new ChoiceListAdapter();
        binding.recyclerView.setAdapter(choiceAdapter);

        // recyclerview for identification answer
        adapterLetterAnswer = new LetterListAdapter(getMvpView(), false);
        binding.recyclerViewAnswer.setLayoutManager(new GridLayoutManager(this, 10));
        binding.recyclerViewAnswer.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewAnswer.setAdapter(adapterLetterAnswer);

        // recyclerview for identification letter choices
        adapterLetterChoice = new LetterListAdapter(getMvpView(), true);
        binding.recyclerViewLetterChoices.setLayoutManager(new GridLayoutManager(this, 10));
        binding.recyclerViewLetterChoices.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewLetterChoices.setAdapter(adapterLetterChoice);

        // setup additional data on layout using DataBinding
        binding.setView(getMvpView());
        String strNumItems = "Number of Items: " + topic.getQuestions().size();
        binding.txtNumItems.setText(strNumItems);
        Log.d(TAG, "onCreate: realm is instantiated");
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
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit?")
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        QuizActivity.this.finish();
                    }
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        Log.d(TAG, "onDestroy: realm is closed");
        super.onDestroy();
    }

    @NonNull
    @Override
    public QuizPresenter createPresenter() {
        return new QuizPresenter();
    }

    @NonNull
    @Override
    public ViewState<QuizView> createViewState() {
        setRetainInstance(true);
        return new QuizViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        // alert user that quiz already taken
        if (topic.getQuestions().size() <= 0) {
            new AlertDialog.Builder(this)
                    .setTitle("No Questions")
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            QuizActivity.this.finish();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
        /*QuizGrade quizGrade = realm.where(QuizGrade.class).equalTo(Constant.ID, topic.getId()).findFirst();
        if (quizGrade != null) {
            // already taken the quiz
            new AlertDialog.Builder(this)
                    .setTitle("Retake Quiz?")
                    .setMessage("If Submitted, it will overwrite previous grade!")
                    .setCancelable(false)
                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            QuizActivity.this.finish();
                        }
                    })
                    .show();
        }*/
        // setup data
        ((QuizViewState) getViewState()).setCounter(0);

        lettersList = new ArrayList<>();
        userAnswerList = new ArrayList<>();
        questionList = presenter.getShuffledQuestionList(realm.copyFromRealm(topic.getQuestions()));

        for (int i = 0; i < questionList.size(); i++) lettersList.add(new ArrayList<Letter>());

        ((QuizViewState) getViewState()).setLetterList(lettersList);
        ((QuizViewState) getViewState()).setUserAnswerList(userAnswerList);
        ((QuizViewState) getViewState()).setQuestionList(questionList);

        if (questionList.size() > 0)
            onSetQuestion(questionList.get(((QuizViewState) getViewState()).getCounter()));
    }

    /**
     * @param question question to display
     */
    private void onSetQuestion(Question question) {
        int counter = ((QuizViewState) getViewState()).getCounter();
        String header = "Short Quiz for " + topic.getTitle();
        binding.txtHeader.setText(header);
        binding.txtQuestion.setText((counter + 1) + ".) " + question.getQuestion());

        UserAnswer userAnswer = null;
        if (userAnswerList.size() > counter)
            userAnswer = userAnswerList.get(counter);

        if (question.getQuestion_type() == Constant.QUESTION_TYPE_MULTIPLE) {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.recyclerViewAnswer.setVisibility(View.GONE);
            binding.recyclerViewLetterChoices.setVisibility(View.GONE);
            choiceAdapter.setChoiceList(question.getChoices());
            if (userAnswer != null) choiceAdapter.setAnswer(userAnswer.getUserAnswer());
        } else if (question.getQuestion_type() == Constant.QUESTION_TYPE_IDENTIFICATION) {
            binding.recyclerView.setVisibility(View.GONE);

            binding.recyclerViewAnswer.setVisibility(View.VISIBLE);
            binding.recyclerViewLetterChoices.setVisibility(View.VISIBLE);
            adapterLetterAnswer.setLetters(presenter.getAssessmentLetter(question.getAnswer()));

            Log.d(TAG, "onSetQuestion: answer: " + question.getAnswer());

            if (lettersList.get(counter).size() <= 0) {
                lettersList.set(counter, presenter.getChoiceLetters(question.getAnswer()));
                ((QuizViewState) getViewState()).setLetterList(lettersList);
            }

            adapterLetterChoice.setLetters(lettersList.get(counter));
            Log.d(TAG, "onSetQuestion: choices: " + adapterLetterChoice.getAnswer());

            if (userAnswer != null)
                for (int i = 0; i < userAnswer.getUserAnswer().length(); i++) {
                    String s = userAnswer.getUserAnswer().charAt(i) + "";
                    int emptyIndex = adapterLetterAnswer.getEmptyIndex();
                    if (emptyIndex != -1) adapterLetterAnswer.addLetter(s, emptyIndex);
                }
            Log.d(TAG, "onSetQuestion: ident answer: " + adapterLetterAnswer.getAnswer());
        }
    }

    @Override
    public void onPrevious() {
        if (((QuizViewState) getViewState()).getCounter() <= 0) {
            // TODO: 24/11/2016 disable previous button instead of alert here if counter is 0
            new AlertDialog.Builder(this)
                    .setTitle("Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            QuizActivity.this.finish();
                        }
                    })
                    .setNegativeButton("CANCEL", null)
                    .show();
        } else {
            setUserAnswer(false);

            ((QuizViewState) getViewState()).decrementCounter();
            onSetQuestion(questionList.get(((QuizViewState) getViewState()).getCounter()));
        }
    }

    private String setUserAnswer(boolean hasReturn) {
        Question question = questionList.get(((QuizViewState) getViewState()).getCounter());
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setQuestionId(question.getId());
        userAnswer.setLessonDetailId(question.getLesson_detail());
        userAnswer.setCorrectAnswer(question.getAnswer());

        if (question.getQuestion_type() == Constant.QUESTION_TYPE_MULTIPLE) {
            Choice choice = choiceAdapter.getSelectedChoice();
            if (choice == null && hasReturn) {
                return "Select Answer";
            }
            userAnswer.setUserAnswer(choice != null ? choice.getBody() : "");
            userAnswer.setChoiceType(choice != null ? choice.getChoice_type() : 0);
        } else if (question.getQuestion_type() == Constant.QUESTION_TYPE_IDENTIFICATION) {
            if (hasReturn) {
                if (adapterLetterAnswer.getAnswer().isEmpty()) {
                    return "Enter Answer";
                }
                if (!adapterLetterAnswer.completeAnswer()) {
                    return "Answer incomplete";
                }
            }
            userAnswer.setUserAnswer(adapterLetterAnswer.getAnswer());
            userAnswer.setChoiceType(Constant.DETAIL_TYPE_IMAGE);
        }
        if (userAnswerList.size() > ((QuizViewState) getViewState()).getCounter())
            userAnswerList.set(((QuizViewState) getViewState()).getCounter(), userAnswer);
        else
            userAnswerList.add(userAnswer);
        ((QuizViewState) getViewState()).setUserAnswerList(userAnswerList);
        return null;
    }

    @Override
    public void onNext() {
        String message = setUserAnswer(true);
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            return;
        }

        if (((QuizViewState) getViewState()).getCounter() < questionList.size() - 1) {
            ((QuizViewState) getViewState()).incrementCounter();
            onSetQuestion(questionList.get(((QuizViewState) getViewState()).getCounter()));
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Submit?")
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            showSummary();
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            userAnswerList.remove(((QuizViewState) getViewState()).getCounter());
                            ((QuizViewState) getViewState()).setUserAnswerList(userAnswerList);
                            QuizActivity.this.onSetQuestion(questionList.get(((QuizViewState) getViewState()).getCounter()));
                        }
                    })
                    .show();

        }
    }

    @Override
    public void restoreData(int counter, List<Question> questionList, List<UserAnswer> userAnswerList, List<List<Letter>> lettersList) {
        this.questionList = questionList;
        this.userAnswerList = userAnswerList;
        this.lettersList = lettersList;
        if (questionList.size() > 0)
            onSetQuestion(questionList.get(counter));
    }

    @Override
    public void onLetterClicked(int position, boolean choice, Letter letter) {
        String strLetter = letter.getLetter();
        Log.d(TAG, "onLetterClicked: choice: " + choice + " ||letter: " + strLetter);
        if (choice) {
            int emptyIndex = adapterLetterAnswer.getEmptyIndex();
            if (emptyIndex != -1) {
                adapterLetterChoice.removeLetter(position);
                adapterLetterAnswer.addLetter(strLetter, emptyIndex);
            }
        } else {
            int emptyIndex = adapterLetterChoice.getEmptyIndex();
            if (emptyIndex != -1) {
                adapterLetterAnswer.removeLetter(position);
                adapterLetterChoice.addLetter(strLetter, emptyIndex);
            }
        }
    }

    /**
     * Show Dialog Susmmary
     */
    private void showSummary() {
        DialogQuizSummaryBinding dialogBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_quiz_summary,
                null,
                false);
        int score = 0;
        final int items = userAnswerList.size();
        for (UserAnswer userAnswer : userAnswerList) {
            if (userAnswer.isCorrect()) score++;
        }
        dialogBinding.txtRawScore.setText(score + "/" + items);
        String ave = Math.round(presenter.getAverage(score, items))+ "%";
        dialogBinding.txtAverage.setText(ave);

        dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dialogBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        dialogBinding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        SummaryListAdapter summaryListAdapter = new SummaryListAdapter(getMvpView());
        summaryListAdapter.setUserAnswerList(userAnswerList);
        dialogBinding.recyclerView.setAdapter(summaryListAdapter);

        if (preQuiz) {
            dialogBinding.recyclerView.setVisibility(View.GONE);
        }

        final int finalScore = score;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (preQuiz) {
                    PreQuizGrade preQuizGrade = new PreQuizGrade();
                    preQuizGrade.setId(topic.getId());
                    preQuizGrade.setRawScore(finalScore);
                    preQuizGrade.setItemCount(items);
                    preQuizGrade.setDateUpdated(new Date().getTime());
                    realm.copyToRealmOrUpdate(preQuizGrade);
                } else {

                    Number maxCount = realm.where(QuizGrade.class)
                            .equalTo("topic", topic.getId()).max("count");
                    Number maxId = realm.where(QuizGrade.class).max("id");

                    QuizGrade quizGrade = new QuizGrade();
                    quizGrade.setId(maxId == null ? 1 : maxId.intValue() + 1);
                    quizGrade.setTopic(topic.getId());
                    quizGrade.setRawScore(finalScore);
                    quizGrade.setItemCount(items);
                    quizGrade.setDateUpdated(new Date().getTime());
                    quizGrade.setCount(maxCount == null ? 1 : maxCount.intValue() + 1);
                    realm.copyToRealmOrUpdate(quizGrade);
                }
            }
        });

        new AlertDialog.Builder(this)
                .setTitle("Summary")
                .setView(dialogBinding.getRoot())
                .setCancelable(false)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        QuizActivity.this.finish();
                    }
                })
                .show();
    }

    @Override
    public void onViewReference(int lessonDetailId) {
        Intent intent = new Intent(this, LessonActivity.class);
        intent.putExtra("lesson_detail_ref_id", lessonDetailId);
        startActivity(intent);
    }
}
