package com.anarchy.deviceretriever;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.anarchy.deviceretriever.retriever.DeviceInfo;
import com.anarchy.deviceretriever.retriever.DeviceRetriever;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;
import rx.schedulers.Timestamped;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            final long start = SystemClock.elapsedRealtime();
                            @Override
                            public void onClick(View v) {
                                Observable.defer(new Func0<Observable<DeviceInfo>>() {
                                    @Override
                                    public Observable<DeviceInfo> call() {
                                        return Observable.just(DeviceRetriever.getInstance(MainActivity.this)
                                                .analyze(DeviceRetriever.LEVEL_MEDIUM));
                                    }
                                }).observeOn(Schedulers.io())
                                        .subscribeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<DeviceInfo>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                e.printStackTrace();
                                            }

                                            @Override
                                            public void onNext(DeviceInfo deviceInfo) {
                                                deviceInfo.initTime = SystemClock.elapsedRealtime() - start;
                                                Log.w("result",deviceInfo.toString());
                                            }
                                        });
                            }
                        }).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
