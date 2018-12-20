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
    //private ProgressBar progressBar;
    //private int val=0, urlCount =57;
    String[] urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res = getResources();
        urls = res.getStringArray(R.array.kosen_url);

        textView = findViewById(R.id.myTextView);
        scrollView = findViewById(R.id.scroll);
        //progressBar = findViewById(R.id.progressBar);
        //progressBar.setMax(urlCount);
        //progressBar.setVisibility(android.widget.ProgressBar.INVISIBLE);

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
                //progressBar.setProgress(0);
                //progressBar.setVisibility(android.widget.ProgressBar.VISIBLE);
            }

            @Override
            public void onNext(String s) {
                //val+=1;
                //progressBar.setProgress(val);
                //textView.setText(s);
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
                //progressBar.setVisibility(android.widget.ProgressBar.INVISIBLE);

                scrollView.scrollTo(0, textView.getBottom());

                long end = System.currentTimeMillis();
                Toast.makeText(context, (end - start)  + "ms", Toast.LENGTH_LONG).show();
            }
        };

        Observable.fromArray(urls)
                .subscribeOn(Schedulers.io())
                //.take(1)
                .map(HtmlGet::htmlGet)
                .map(HtmlGet::titleGet)
                //.scan((s1,s2)->s1+s2)
                //.doOnComplete(s->textView.append(s))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}