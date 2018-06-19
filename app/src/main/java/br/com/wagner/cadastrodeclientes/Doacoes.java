package br.com.wagner.cadastrodeclientes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class Doacoes extends Activity {

    WebView wvPaypal, wvPagSeguro;
    Intent it;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doacoes);

        wvPaypal = (WebView) findViewById(R.id.wvPaypal);
        wvPaypal.getSettings().setJavaScriptEnabled(true);

        wvPaypal.loadUrl("file:///android_asset/index.html");

        wvPagSeguro = (WebView) findViewById(R.id.wvPagSeguro);
        wvPagSeguro.getSettings().setJavaScriptEnabled(true);

        wvPaypal.loadUrl("file:///android_asset/index.html");
        wvPagSeguro.loadUrl("file:///android_asset/index2.html");
    }

    public void Obrigado(View v) {
        it = new Intent(getBaseContext(), ListarActivity.class);
        startActivity(it);

    }

}