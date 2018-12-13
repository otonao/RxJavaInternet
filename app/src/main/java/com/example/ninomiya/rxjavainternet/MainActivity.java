package com.example.ninomiya.rxjavainternet;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

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
        getButton.setOnClickListener(v -> asyncProcess());

        Button clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(v -> textView.setText(null));
    }

    public void asyncProcess() {

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("START");
            }

            @Override
            public void onNext(String s) {
                System.out.println("SUCCESS" + s);
                textView.append(s + "\n\n");
                scrollView.scrollTo(0, textView.getBottom());
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("ERROR");
                System.out.println(e.toString());
                textView.append("ERROR\n");
                textView.append(e.toString() + "\n");
            }

            @Override
            public void onComplete() {
                System.out.println("FINISH");
                textView.append("FINISH");
                scrollView.scrollTo(0, textView.getBottom());
            }
        };

        Observable.fromArray(urls)
                .subscribeOn(Schedulers.io())
                .map(HtmlGet::htmlGet)
                .map(HtmlGet::titleGet)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
}