package ru.mtucifiit.mtucifiit.view.home.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Stream;

import ru.mtucifiit.mtucifiit.MainActivity;

import ru.mtucifiit.mtucifiit.R;



public class WebViewActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener, OnErrorListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private PDFView pdfView;


    private String url = null;

    private ConstraintLayout are;
    private TextView header;


    private WebView view;

    private ProgressBar progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        init();
    }


    public void init() {
        url = getIntent().getStringExtra("url");
        String header_ = getIntent().getStringExtra("header");
        //String group = getIntent().getStringExtra("group");

        are = findViewById(R.id.back_area);
        header = findViewById(R.id.header_web_view);
        header.setText(header_);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.animate().start();
        Log.e("url",url);
/*
        view = findViewById(R.id.webView);

       Log.e("url",url);


        view.getSettings().setJavaScriptEnabled(true);

        // Optional: Enable zooming and built-in PDF support
        view.getSettings().setSupportZoom(true);
        view.getSettings().setBuiltInZoomControls(true);
        view.setWebViewClient(new CustomWebViewClient());

        view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);


        WebSettings webSettings = view.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        loadPDF(url);


 */


       // pdfView = findViewById(R.id.pdfView);
      //  load(url);
        are.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        pdfView = findViewById(R.id.pdfView);
        downloadAndSavePdf(url);
    }



    private void downloadAndSavePdf(String url_) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(url_);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {


                           InputStream inputStream = connection.getInputStream();



                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                displayPdf(inputStream);
                            }
                        });
                    } else {
                        Log.e(TAG, "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void displayPdf(InputStream stream) {
        pdfView.fromStream(stream)
                .defaultPage(0)
                .enableSwipe(true)
                .onPageChange(this)
                .onLoad(this)
                .onError(this)
                .load();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        // Handle page change events
    }

    @Override
    public void loadComplete(int nbPages) {
        // PDF loading is complete
        progressBar.setVisibility(View.GONE);
    }



    public void load(String url) {
        pdfView.fromUri(Uri.parse(url))
                .defaultPage(0)
                .enableSwipe(true)
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {

                    }
                })
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {

                    }
                })
                .onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }
                })
                .load();
    }

    @Override
    public void onError(Throwable t) {

    }


    private void loadPDF(String url) {
        long timeStart = System.currentTimeMillis();
        view.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);
        view.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("url", url);

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }


}