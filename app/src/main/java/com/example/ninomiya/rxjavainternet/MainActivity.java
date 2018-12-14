package com.example.ninomiya.rxjavainternet;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private ScrollView scrollView;
    String[] urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res = getResources();
        urls = res.getStringArray(R.array.kosen_url);

        textView = findViewById(R.id.myTextView);
        scrollView = findViewById(R.id.scroll);

        Button getButton = findViewById(R.id.htmlGetButton);
        getButton.setOnClickListener(v -> asyncProcess(this));

        Button clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(v -> textView.setText(null));
    }

    public void asyncProcess(Context context) {
        long start = System.currentTimeMillis();

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(String s) {
                textView.append(s + "\n\n");
                scrollView.scrollTo(0, textView.getBottom());
            }

            @Override
            public void onError(Throwable e) {
                textView.append("ERROR\n");
                textView.append(e.toString() + "\n");
            }

            @Override
            public void onComplete() {
                long end = System.currentTimeMillis();
                Toast.makeText(context, (end - start)  + "ms", Toast.LENGTH_LONG).show();
                scrollView.scrollTo(0, textView.getBottom());
            }
        };

        Observable.fromArray(urls)
                .take(1)
                .subscribeOn(Schedulers.io())
                .map(HtmlGet::htmlGet)
                .map(HtmlGet::titleGet)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}