package com.tip.capstone.mlearning.ui.term;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.ActivityTermBinding;
import com.tip.capstone.mlearning.model.Term;
import com.tip.capstone.mlearning.ui.topics.TopicsListActivity;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class TermActivity extends MvpActivity<TermView, TermPresenter> implements TermView {

    private Realm realm;
    private RealmResults<Term> termRealmResults;
    private TermListAdapter termListAdapter;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();

        ActivityTermBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_term);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        termListAdapter = new TermListAdapter(getMvpView());
        binding.recyclerView.setAdapter(termListAdapter);

        termRealmResults = realm.where(Term.class).findAllSortedAsync(Term.COL_SEQ);
        termRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Term>>() {
            @Override
            public void onChange(RealmResults<Term> element) {
                termListAdapter.setTermList(realm.copyFromRealm(termRealmResults));
            }
        });
    }

    @Override
    protected void onDestroy() {
        termRealmResults.removeChangeListeners();
        realm.close();
        super.onDestroy();
    }

    @NonNull
    @Override
    public TermPresenter createPresenter() {
        return new TermPresenter();
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
    public void onTermClicked(Term term) {
        Intent intent = new Intent(this, TopicsListActivity.class);
        intent.putExtra(Constant.ID, term.getId());
        startActivity(intent);
    }

}
