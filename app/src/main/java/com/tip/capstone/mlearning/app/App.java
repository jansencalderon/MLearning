package com.tip.capstone.mlearning.app;

import android.app.Application;

import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.helper.StringHelper;
import com.tip.capstone.mlearning.model.Assessment;
import com.tip.capstone.mlearning.model.AssessmentChoice;
import com.tip.capstone.mlearning.model.Choice;
import com.tip.capstone.mlearning.model.Glossary;
import com.tip.capstone.mlearning.model.Lesson;
import com.tip.capstone.mlearning.model.LessonDetail;
import com.tip.capstone.mlearning.model.Question;
import com.tip.capstone.mlearning.model.Term;
import com.tip.capstone.mlearning.model.Topic;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author pocholomia
 * @since 18/11/2016
 * Initialization and Configuration for the Application
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize realm configuration
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        // setting up data for realm
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                // delete all data excluding grades
                realm.delete(Term.class);
                realm.delete(Topic.class);
                realm.delete(Lesson.class);
                realm.delete(LessonDetail.class);
                realm.delete(Question.class);
                realm.delete(Choice.class);
                realm.delete(Assessment.class);
                realm.delete(AssessmentChoice.class);

                realm.delete(Glossary.class);

                // add data from raw files
                realm.createAllFromJson(Term.class, StringHelper.readRawTextFile(getApplicationContext(), R.raw.study));
                realm.createAllFromJson(Assessment.class, StringHelper.readRawTextFile(getApplicationContext(), R.raw.assessment));
                realm.createAllFromJson(Glossary.class, StringHelper.readRawTextFile(getApplicationContext(), R.raw.glossary));

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                realm.close();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                realm.close();
            }
        });

    }
}
