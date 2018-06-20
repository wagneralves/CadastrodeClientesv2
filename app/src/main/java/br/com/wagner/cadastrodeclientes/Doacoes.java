package br.com.wagner.cadastrodeclientes;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Doacoes extends Activity {

    ImageView ivBit, ivDoge, ivEth;
    public ClipboardManager myClipboard;
    private ClipData myClip;
    WebView wvPaypal, wvPagSeguro;
    Intent it;
    String bitCoin, dogeCoin, ethCoin;

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
        ivBit = findViewById(R.id.ivBit);
        ivDoge = findViewById(R.id.ivDoge);
        ivEth = findViewById(R.id.ivEth);


        bitCoin = "1DXWX9oA3PAgqTkuzfJEFX5mshSvCYsn6i";
        dogeCoin = "DHQ9uFbUCubn82zXcaxVC1TQ3zhpQ9u4U2";
        ethCoin = "0x4Ff98202D5fe16703486cDc456cC95A8309d73fe";

        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);


    }

    public void copiar(View view){

        switch (view.getId()){

            case (R.id.ivBit):
                String text;
                text = bitCoin;//.getText().toString()
                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(getApplicationContext(), "Text Copied",
                        Toast.LENGTH_SHORT).show();
                break;
            case (R.id.ivDoge):
                String doge;
                doge = dogeCoin;//.getText().toString()
                myClip = ClipData.newPlainText("text", doge);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(getApplicationContext(), "Text Copied",
                        Toast.LENGTH_SHORT).show();
                break;
            case (R.id.ivEth):
                String eth;
                eth = ethCoin;//.getText().toString()
                myClip = ClipData.newPlainText("text", eth);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(getApplicationContext(), "Text Copied",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //  ivBit.setOnClickListener(new View.OnClickListener() {

    //      @Override
    //      public void onClick(View v) {
    //
    //      }
    //  });






    public void Obrigado(View v) {
        it = new Intent(getBaseContext(), ListarActivity.class);
        startActivity(it);

    }




}