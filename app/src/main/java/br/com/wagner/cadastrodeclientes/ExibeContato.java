package br.com.wagner.cadastrodeclientes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ExibeContato extends Activity {

    TextView exNome, exTelefone, exEmail, exCpf, exData, exDesc, exObs, tvIdModel2;
    Cursor cursor;
    BancoController crud;
    String codigo;
    ImageView ibVoltar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibe_contato);

        ibVoltar = (ImageView) findViewById(R.id.ibVoltar);

        codigo = this.getIntent().getStringExtra("codigo");
        crud = new BancoController(getBaseContext());

        exNome = (TextView) findViewById(R.id.txvExNome);
        exTelefone = (TextView) findViewById(R.id.txvExTelefone);
        exEmail = (TextView) findViewById(R.id.txvExEmail);
        exCpf = (TextView) findViewById(R.id.txvExCPF);
        exData = (TextView) findViewById(R.id.txvExDATA);
        exDesc = (TextView) findViewById(R.id.txvExDescricao);
        exObs = (TextView) findViewById(R.id.txvExObs);
        tvIdModel2 = findViewById(R.id.tvIdModel2);

        cursor = crud.carregaDadoById(Integer.parseInt(codigo));
        exNome.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBTableConfig.Columns.NOME)));
        exTelefone.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBTableConfig.Columns.TEL)));
        exCpf.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBTableConfig.Columns.CPF)));
        exData.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBTableConfig.Columns.DATA)));
        exDesc.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBTableConfig.Columns.DES)));
        exEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBTableConfig.Columns.EMAIL)));
        exObs.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBTableConfig.Columns.OBS)));
        tvIdModel2.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBTableConfig.Columns.ID)));


    }

    public void voltar (View view){

        Intent it = new Intent(getBaseContext(), ListarActivity.class);
        startActivity(it);


    }

    private Toast toast;
    private long lastBackPressTime = 0;

    @SuppressLint({"WrongConstant", "ShowToast"})
    @Override
    public void onBackPressed() {
        if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
            toast = Toast.makeText(this, "Pressione o botão voltar novamente para sair!!!.", 4000);
            toast.show();
            this.lastBackPressTime = System.currentTimeMillis();
        } else {
            if (toast != null) {
                toast.cancel();
            }
            super.onBackPressed();
        }
    }




}
