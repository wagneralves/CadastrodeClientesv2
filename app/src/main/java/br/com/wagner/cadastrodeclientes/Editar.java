package br.com.wagner.cadastrodeclientes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Editar extends AppCompatActivity {
    //Variáveis
    EditText txtNome, txtEmail, txtTelefone, txtDescricao, txtCpf, edtObs;
    SQLiteDatabase db;
    TextView txvDate;
    ContentValues ctv;
    BancoController crud;
    String codigo;
    Cursor cursor;
    Builder msg;
    Intent it;
    ImageView alterar;
    int id;
    ImageView IB_Cancelar;
    Calendar c = Calendar.getInstance();
    TextView display;
    int cday, cmonth, cyear;
    ImageView chooseDateButton;
    //Fim

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar);


        ImageView changeDate = (ImageView) findViewById(R.id.chooseDateButton);
        display = (TextView) findViewById(R.id.txvDate);

        changeDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Editar.this, d,
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                        .get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        codigo = this.getIntent().getStringExtra("codigo");
        crud = new BancoController(getBaseContext());

        txtNome = (EditText) findViewById(R.id.txtNome);
        edtObs = (EditText) findViewById(R.id.edtObs);
        txtDescricao = (EditText) findViewById(R.id.txtDescricao);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtTelefone = (EditText) findViewById(R.id.txtTelefone);
        txtCpf = (EditText) findViewById(R.id.txtCpf);
        txvDate = (TextView) findViewById(R.id.txvDate);

        cursor = crud.carregaDadoById(Integer.parseInt(codigo));
        txtNome.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBTableConfig.Columns.NOME)));
        txtTelefone.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBTableConfig.Columns.TEL)));
        txtCpf.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBTableConfig.Columns.CPF)));
        txvDate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBTableConfig.Columns.DATA)));
        txtDescricao.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBTableConfig.Columns.DES)));
        txtEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBTableConfig.Columns.EMAIL)));
        edtObs.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBTableConfig.Columns.OBS)));
    }

    //DatePicker
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            cday = dayOfMonth;
            cmonth = monthOfYear + 1;
            cyear = year;

            display.setText(cday + "/" + cmonth + "/" + cyear);
        }
    };

    //fim

    //Atualiza Contato
    public void AtualizarClick(View v) {

        crud.alteraRegistro(Integer.parseInt(codigo),
                txtNome.getText().toString(),txtTelefone.getText().toString(),
                txtCpf.getText().toString(), txvDate.getText().toString(), txtDescricao.getText().toString(), txtEmail.getText().toString(), edtObs.getText().toString());
        Intent intent = new Intent(Editar.this,ListarActivity.class);
        startActivity(intent);
        finish();
    }
    //Fim

    public void ApagarClick(View view) {

        crud.deletaRegistro(Integer.parseInt(codigo));
        Intent intent = new Intent(Editar.this,ListarActivity.class);
        startActivity(intent);
        finish();

    }

    //Mensagem para exibir exceções com parametros
    public void mensagemExibir(String titulo, String texto) {
        AlertDialog.Builder mensagem = new AlertDialog.Builder(Editar.this);
        mensagem.setTitle(titulo);
        mensagem.setMessage(texto);
        mensagem.setNeutralButton(getString(R.string.javaOK), null);
        mensagem.show();
    }
    //Fim

    public void ListarClientes() {

        it = new Intent(getBaseContext(), ListarActivity.class);
        startActivity(it);
    }

    public void ListarClientes2(View view) {

        it = new Intent(getBaseContext(), ListarActivity.class);
        startActivity(it);
    }

    private Toast toast;
    private long lastBackPressTime = 0;

    @SuppressLint("WrongConstant")
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