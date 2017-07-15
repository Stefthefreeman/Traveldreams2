package sc.creation.agence.traveldreams2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ShowOffer extends AppCompatActivity {

    WebView ww2;
    String mypartnerurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_offer);
        Intent gopart = getIntent();
         mypartnerurl = gopart.getStringExtra("url");
        ww2 = (WebView) findViewById(R.id.partner);
        showpartner();
    }
    public void showpartner(){
        final ProgressDialog dialog = ProgressDialog.show(this, "", "Chargement en cours,patientez SVP", true);
        dialog.setCanceledOnTouchOutside(true);
        ww2 .getSettings().setJavaScriptEnabled(true);
        ww2 .getSettings().setLoadWithOverviewMode(true);
        ww2 .getSettings().setUseWideViewPort(true);
        ww2 .getSettings().setBuiltInZoomControls(true);

        ww2 .setWebViewClient(new WebViewClient() {

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                dialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                dialog.dismiss();
                ww2.getUrl();
            }


        });

        ww2 .loadUrl(mypartnerurl);
    }
}
