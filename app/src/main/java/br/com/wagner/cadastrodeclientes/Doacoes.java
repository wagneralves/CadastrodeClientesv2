package br.com.wagner.cadastrodeclientes;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

public class Doacoes extends Activity {

    ImageView ivBit, ivDoge, ivEth;
    public ClipboardManager myClipboard;
    private ClipData myClip;
    WebView wvPaypal, wvPagSeguro;
    Intent it;
    String bitCoin, dogeCoin, ethCoin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doacoes);

        wvPaypal = (WebView) findViewById(R.id.wvPaypal);
        wvPaypal.getSettings().setJavaScriptEnabled(true);
        wvPaypal.loadUrl("file:///android_asset/index.html");

        wvPagSeguro = (WebView) findViewById(R.id.wvPagSeguro);
        wvPagSeguro.getSettings().setJavaScriptEnabled(true);
        wvPagSeguro.loadUrl("file:///android_asset/index2.html");

        ivBit = findViewById(R.id.ivBit);
        ivDoge = findViewById(R.id.ivDoge);
        ivEth = findViewById(R.id.ivEth);

        bitCoin = "13vnB91yHF1ofUYK37rxNkZZNWjBeUTwTz";
        dogeCoin = "DMkKUHaVkd8ZMFmHi4To36uHqWy2xrpCEn";
        ethCoin = "0x048531d1301884c56800ec7882441f13aedc0769";

        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    }

    public void copiar(View view) {
        String text = "";
        int id = view.getId();

        if (id == R.id.ivBit) {
            text = bitCoin;
        } else if (id == R.id.ivDoge) {
            text = dogeCoin;
        } else if (id == R.id.ivEth) {
            text = ethCoin;
        }

        if (!text.isEmpty()) {
            ClipData myClip = ClipData.newPlainText("text", text);
            myClipboard.setPrimaryClip(myClip);
            Toast.makeText(getApplicationContext(), "Text Copied", Toast.LENGTH_SHORT).show();
        }
    }

    public void Obrigado(View v) {
        it = new Intent(getBaseContext(), ListarActivity.class);
        startActivity(it);
    }
}
