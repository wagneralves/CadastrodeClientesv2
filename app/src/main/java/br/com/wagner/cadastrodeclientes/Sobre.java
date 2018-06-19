package br.com.wagner.cadastrodeclientes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Sobre extends Activity {

    Intent it;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
    }

    public void Clientes(View v) {

        it = new Intent(getBaseContext(), ListarActivity.class);
        startActivity(it);
    }
}
