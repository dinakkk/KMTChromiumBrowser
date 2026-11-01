package com.chromium.kmt.browser;

import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private EditText urlInput;
    private static final int KMT_NAVY_BLUE = Color.parseColor("#000094");
    private static final String HOME_ASSET_URL = "file:///android_asset/home.html";
    private static final String KMT_SEARCH_PREFIX = "https://www.kmt.org.tw/search?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(KMT_NAVY_BLUE);
            window.setNavigationBarColor(KMT_NAVY_BLUE);
        }

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rootLayout.setBackgroundColor(KMT_NAVY_BLUE);

        LinearLayout toolbar = new LinearLayout(this);
        toolbar.setOrientation(LinearLayout.HORIZONTAL);
        toolbar.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(56)));
        toolbar.setBackgroundColor(KMT_NAVY_BLUE);
        toolbar.setPadding(dpToPx(10), dpToPx(8), dpToPx(10), dpToPx(8));
        toolbar.setGravity(Gravity.CENTER_VERTICAL);

        Button homeBtn = new Button(this);
        LinearLayout.LayoutParams homeBtnParams = new LinearLayout.LayoutParams(
                dpToPx(58), ViewGroup.LayoutParams.MATCH_PARENT);
        homeBtnParams.setMarginEnd(dpToPx(8));
        homeBtn.setLayoutParams(homeBtnParams);
        homeBtn.setText("首頁");
        homeBtn.setTextSize(13);
        homeBtn.setTextColor(Color.parseColor("#FFD700"));
        homeBtn.setBackgroundColor(Color.parseColor("#000068"));
        homeBtn.setPadding(0, 0, 0, 0);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHomePage();
            }
        });

        urlInput = new EditText(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        urlInput.setLayoutParams(params);
        urlInput.setHint("輸入網址或搜尋中國國民黨官網");
        urlInput.setHintTextColor(Color.parseColor("#B0C4DE"));
        urlInput.setTextColor(Color.WHITE);
        urlInput.setTextSize(14);
        urlInput.setSingleLine(true);
        urlInput.setImeOptions(EditorInfo.IME_ACTION_GO);
        urlInput.setBackgroundColor(Color.parseColor("#000068"));
        urlInput.setPadding(dpToPx(14), 0, dpToPx(14), 0);

        urlInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO ||
                        actionId == EditorInfo.IME_ACTION_SEARCH ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    String query = urlInput.getText().toString().trim();
                    navigateInput(query);
                    return true;
                }
                return false;
            }
        });

        toolbar.addView(homeBtn);
        toolbar.addView(urlInput);
        rootLayout.addView(toolbar);

        webView = new WebView(this);
        webView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f));
        webView.setBackgroundColor(KMT_NAVY_BLUE);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url != null && url.startsWith("file:///android_asset/")) {
                    urlInput.setText("");
                } else if (url != null) {
                    urlInput.setText(url);
                }
            }
        });

        webView.setWebChromeClient(new WebChromeClient());
        rootLayout.addView(webView);

        setContentView(rootLayout);
        loadHomePage();
    }

    private void navigateInput(String query) {
        if (query.isEmpty()) {
            loadHomePage();
            return;
        }
        if (query.startsWith("http://") || query.startsWith("https://")) {
            webView.loadUrl(query);
        } else if (query.contains(".") && !query.contains(" ")) {
            webView.loadUrl("https://" + query);
        } else {
            webView.loadUrl(KMT_SEARCH_PREFIX + Uri.encode(query));
        }
    }

    private void loadHomePage() {
        webView.loadUrl(HOME_ASSET_URL);
        urlInput.setText("");
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
