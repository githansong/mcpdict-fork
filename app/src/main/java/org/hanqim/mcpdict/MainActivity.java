package org.hanqim.mcpdict;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.hanqim.mcpdict.database.MCPDatabase;
import org.hanqim.mcpdict.database.UserDatabase;
import org.hanqim.mcpdict.editor.CharsetDetector;
import org.hanqim.mcpdict.editor.Orthography;
import org.hanqim.mcpdict.views.Masks;
import org.hanqim.mcpdict.views.SearchEditView;
import org.hanqim.mcpdict.views.SearchResultCursorAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity
        extends AppCompatActivity
        implements
        AdapterView.OnItemSelectedListener,
        Masks {

    private static final CharsetDetector detector = CharsetDetector.getInstance();

    @BindView(R2.id.search_input)
    SearchEditView searchEdit;
    @BindView(R2.id.search_result)
    ListView listView;
    @BindView(R2.id.spinner_search_as)
    Spinner selectModeView;
    @BindView(R2.id.search_options)
    LinearLayout optionsView;

    @BindView(R2.id.check_box_kuangx_yonh_only)
    CheckBox checkBoxKuangxYonhOnly;
    @BindView(R2.id.check_box_allow_variants)
    CheckBox checkBoxAllowVariants;
    @BindView(R2.id.check_box_tone_insensitive)
    CheckBox checkBoxToneInsensitive;

    private SearchResultCursorAdapter searchAdapter;
    private boolean showFavoriteButton;
    private String query;
    private int searchMode = 0;

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                MCPDictApplication.hideKeyboard(getActivity());
                refresh();
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Initialize the some "static" classes on separate threads
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Orthography.initialize(getResources());
                return null;
            }
        }.execute();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                UserDatabase.initialize(MainActivity.this);
                MCPDatabase.initialize(MainActivity.this);
                return null;
            }
        }.execute();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                //FavoriteDialogs.initialize(MainActivity.this);
                return null;
            }
        }.execute();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Set up the searchAdapter
        if (searchAdapter == null) {
            searchAdapter = new SearchResultCursorAdapter(
                    this,
                    R.layout.search_result_item,
                    null,
                    showFavoriteButton
            );
        }
        listView.setAdapter(searchAdapter);

        searchEdit.setOnEditorActionListener(editorActionListener);

       // selectModeView.setAdapter();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.select_mode_data, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectModeView.setAdapter(adapter);
        selectModeView.setOnItemSelectedListener(this);

        loadCheckBoxes();
        updateCheckBoxesEnabled();
        CompoundButton.OnCheckedChangeListener checkBoxListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                saveCheckBoxes();
                refresh();
            }
        };
        checkBoxKuangxYonhOnly.setOnCheckedChangeListener(checkBoxListener);
        checkBoxAllowVariants.setOnCheckedChangeListener(checkBoxListener);
        checkBoxToneInsensitive.setOnCheckedChangeListener(checkBoxListener);
    }

    private void loadCheckBoxes() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Resources r = getResources();
        checkBoxKuangxYonhOnly.setChecked(sp.getBoolean(r.getString(R.string.pref_key_kuangx_yonh_only), false));
        checkBoxAllowVariants.setChecked(sp.getBoolean(r.getString(R.string.pref_key_allow_variants), true));
        checkBoxToneInsensitive.setChecked(sp.getBoolean(r.getString(R.string.pref_key_tone_insensitive), false));
    }

    private void saveCheckBoxes() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Resources r = getResources();
        sp.edit().putBoolean(r.getString(R.string.pref_key_kuangx_yonh_only), checkBoxKuangxYonhOnly.isChecked())
                .putBoolean(r.getString(R.string.pref_key_allow_variants), checkBoxAllowVariants.isChecked())
                .putBoolean(r.getString(R.string.pref_key_tone_insensitive), checkBoxToneInsensitive.isChecked())
                .apply();
    }

    private void updateCheckBoxesEnabled() {
        int mode = selectModeView.getSelectedItemPosition();
        checkBoxKuangxYonhOnly.setEnabled(mode != MCPDatabase.SEARCH_AS_MC);
        checkBoxAllowVariants.setEnabled(mode == MCPDatabase.SEARCH_AS_HZ);
        checkBoxToneInsensitive.setEnabled(mode == MCPDatabase.SEARCH_AS_MC ||
                mode == MCPDatabase.SEARCH_AS_PU ||
                mode == MCPDatabase.SEARCH_AS_CT ||
                mode == MCPDatabase.SEARCH_AS_SH ||
                mode == MCPDatabase.SEARCH_AS_MN ||
                mode == MCPDatabase.SEARCH_AS_VN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            startActivity(
                    new Intent()
                            .setClass(MainActivity.this, SettingsActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        } else if (id == R.id.action_toggle_options){
            toggleOptions();
        }
        return super.onOptionsItemSelected(item);

    }

    private Activity getActivity() {
        return this;
    }

    public void refresh() {
        query = searchEdit.getText().toString();
        Log.i("QueryRefresh", query);

        if (query.equals("")) return;
        //searchMode = spinnerSearchAs.getSelectedItemPosition();
        //if (searchMode == Spinner.INVALID_POSITION) return;
        //refresh();

        // Search on a separate thread
        // Because AsyncTasks are put in a queue,
        //   this will not run until the initialization of the orthography module finishes
        if (query == null) return;

        int _mode = searchMode;
        if(detector.isChinese(query)) {
            _mode = MCPDatabase.SEARCH_AS_HZ;
        }
        if(detector.isHangul(query)) {
            _mode = MCPDatabase.SEARCH_AS_KR;
        }
        if(detector.isKana(query)) {
            _mode = MCPDatabase.SEARCH_AS_JP_ANY;
        }

        final int mode = _mode;

        new AsyncTask<Void, Void, Cursor>() {
            @Override
            protected Cursor doInBackground(Void... params) {
                return MCPDatabase.search(query, mode); //searchMode
            }

            @Override
            protected void onPostExecute(Cursor data) {
                searchAdapter.changeCursor(data);
                //TextView textEmpty = (TextView) selfView.findViewById(android.R.id.empty);
                //textEmpty.setText(getString(R.string.no_matches));
            }
        }.execute();
    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.i("ModeChanged", Integer.toString(position));
        searchMode = position;
        updateCheckBoxesEnabled();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void getMode(String query) {
        // TODO:
    }

    public void toggleOptions() {
        if(optionsView.isShown()) {
            optionsView.setVisibility(View.GONE);
        } else {
            optionsView.setVisibility(View.VISIBLE);
        }
    }
}
