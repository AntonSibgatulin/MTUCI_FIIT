package ru.mtucifiit.mtucifiit.view.sch.ege_activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ru.mtucifiit.mtucifiit.R;

public class RusEgeActivity extends AppCompatActivity {

    private WebView webView;
    private String url;
    private String domen;
    private ConstraintLayout are;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rus_ege);
        init();
    }
    private void init(){
        webView = findViewById(R.id.webView);
        are = findViewById(R.id.back_area);
        url = "http://rusege.scienceontheweb.net/";
        domen = "rusege.scienceontheweb.net";

        are.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                // Restrict access to a specific domain
                if (url.contains(domen)) {
                    return false; // Allow loading the URL
                } else {
                    return true; // Block loading the URL
                }
            }
        });
        webView.loadUrl(url);
    }
}