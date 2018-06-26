package br.com.wagner.cadastrodeclientes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class InsereDado extends Activity {

    EditText txtNome, txtEmail, txtTelefone, txtDescricao, txtCPF, edtObs;
    TextView txvData;
    Intent it;
    Calendar c = Calendar.getInstance();
    TextView display;
    int cday, cmonth, cyear;
    ImageView chooseDateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inserir);



        ImageView changeDate = (ImageView) findViewById(R.id.chooseDateButton);
        display = (TextView) findViewById(R.id.txvDate);

        changeDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                new DatePickerDialog(InsereDado.this, d,
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                        .get(Calendar.DAY_OF_MONTH)).show();

            }
        });


    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            cday = dayOfMonth;
            cmonth = monthOfYear + 1;
            cyear = year;

            display.setText(cday + "/" + cmonth + "/"
                    + cyear);
        }
    };


    public void CadastrarClick(View v) {

        BancoController crud = new BancoController(getBaseContext());
        txtNome = (EditText) findViewById(R.id.txtNome);
        txtDescricao = (EditText) findViewById(R.id.txtDescricao);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtTelefone = (EditText) findViewById(R.id.txtTelefone);
        txtCPF = (EditText) findViewById(R.id.txtCpf);
        txvData = (TextView) findViewById(R.id.txvDate);
        edtObs = (EditText) findViewById(R.id.edtObs);

        String nomeString = txtNome.getText().toString();
        String telefoneString = txtTelefone.getText().toString();
        String cpfString = txtCPF.getText().toString();
        String dataString = txvData.getText().toString();
        String descricaoString = txtDescricao.getText().toString();
        String emailString = txtEmail.getText().toString();
        String obsString = edtObs.getText().toString();
        String resultado;

        try {
            resultado = crud.insereDado(nomeString, telefoneString, cpfString, dataString, descricaoString, emailString, obsString);

            Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();
            chamaListar();
        }catch(Exception erro){mensagemExibir("erro","Erro ao manipular banco de dados" + erro.getMessage() );}
    }

    //Mensagem para exibir exceções com parametros
    public void mensagemExibir (String titulo, String texto) {
        AlertDialog.Builder mensagem = new AlertDialog.Builder(InsereDado.this);
        mensagem.setTitle(titulo);
        mensagem.setMessage(texto);
        mensagem.setNeutralButton(getString(R.string.javaOK), null);
        mensagem.show();
    }

    public void ListaClientes(View v){

        it = new Intent(getBaseContext(), ListarActivity.class);
        startActivity(it);
    }

    public void chamaListar() {

        it = new Intent(getBaseContext(), ListarActivity.class);
        startActivity(it);
    }


}
