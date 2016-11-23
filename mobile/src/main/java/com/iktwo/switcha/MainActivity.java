package com.iktwo.switcha;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.iktwo.switcha.databinding.MainActivityBinding;
import com.iktwo.switcha.delegates.SwitchDelegate;
import com.iktwo.switcha.delegates.SwitchDelegateHandler;
import com.iktwo.switcha.model.Switch;
import com.iktwo.switcha.model.SwitchDatabaseContract;
import com.iktwo.switcha.utils.CursorDelegateAdapter;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements
        Loader.OnLoadCompleteListener<Cursor> {
    private static final int CURSOR_LOADER = 8;
    private static final String TAG = MainActivity.class.getSimpleName();
    private CursorDelegateAdapter cursorDelegateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        setContentView(R.layout.main_activity);

        cursorDelegateAdapter = new CursorDelegateAdapter(
                c -> new Switch(c.getString(c.getColumnIndex(SwitchDatabaseContract.SwitchColumns.NAME)),
                        c.getString(c.getColumnIndex(SwitchDatabaseContract.SwitchColumns.ON)),
                        c.getString(c.getColumnIndex(SwitchDatabaseContract.SwitchColumns.OFF))));

        SwitchDelegateHandler switchDelegateHandler = this::makeRequest;

        cursorDelegateAdapter.registerDelegate(new SwitchDelegate(switchDelegateHandler));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cursorDelegateAdapter);

        setSupportActionBar(binding.toolbar);

        CursorLoader cursorLoader = new CursorLoader(this,
                SwitchDatabaseContract.CONTENT_URI, null, null, null, null);

        cursorLoader.registerListener(CURSOR_LOADER, this);
        cursorLoader.startLoading();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
        cursorDelegateAdapter.swapCursor(data);
    }

    private void makeRequest(Switch sw, boolean on, int index) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl url = HttpUrl.parse(BuildConfig.SWITCHA_SRV_URL);

        url = url.newBuilder()
                .addEncodedPathSegment("")
                .addQueryParameter("action", on ? "TurnOnRequest" : "TurnOffRequest")
                .addQueryParameter("access_token", BuildConfig.SWITCHA_ACCESS_TOKEN)
                .addQueryParameter(BuildConfig.SWITCHA_ID_NAME, "rf_x_1_" + (index + 1))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }
}
