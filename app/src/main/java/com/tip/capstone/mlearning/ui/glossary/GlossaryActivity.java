package com.tip.capstone.mlearning.ui.glossary;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.databinding.ActivityGlossaryBinding;
import com.tip.capstone.mlearning.model.Glossary;

import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class GlossaryActivity extends MvpActivity<GlossaryView, GlossaryPresenter> implements GlossaryView {

    private ActivityGlossaryBinding binding;
    private Realm realm;
    private RealmResults<Glossary> glossaryRealmResults;
    private String searchText;
    private GlossaryListAdapter adapter;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchText = "";
        realm = Realm.getDefaultInstance();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_glossary);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // assumes uses toolbar on theme
        getSupportActionBar().setTitle("Glossary"); // TODO: 02/12/2016 set title on manifest instead

        adapter = new GlossaryListAdapter();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.recyclerView.setAdapter(adapter);

        glossaryRealmResults = realm.where(Glossary.class).findAllSortedAsync("word");
        glossaryRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Glossary>>() {
            @Override
            public void onChange(RealmResults<Glossary> element) {
                prepareList();
            }
        });
    }

    @Override
    protected void onDestroy() {
        glossaryRealmResults.removeChangeListeners();
        realm.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_glossary, menu);
        SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText = newText;
                prepareList();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void prepareList() {
        if (glossaryRealmResults.isLoaded() && glossaryRealmResults.isValid()) {
            List<Glossary> glossaryList;
            if (searchText.isEmpty()) {
                glossaryList = realm.copyFromRealm(glossaryRealmResults);
            } else {
                glossaryList = realm.copyFromRealm(glossaryRealmResults.where()
                        .contains("word", searchText, Case.INSENSITIVE).findAll());
            }
            adapter.setGlossaryList(glossaryList);
        }
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

    @NonNull
    @Override
    public GlossaryPresenter createPresenter() {
        return new GlossaryPresenter();
    }
}
