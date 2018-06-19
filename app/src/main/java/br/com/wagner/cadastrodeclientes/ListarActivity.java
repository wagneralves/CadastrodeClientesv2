package br.com.wagner.cadastrodeclientes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ListarActivity extends AppCompatActivity {
    private ListView ltwDados;
   // ImageView ivIcoMail, ivIcoTel, ivPerfil;
    InputStream in;
    //ImageView ivFoto, ivMail, ivTel;

    SQLiteCursor c;
    TextView tvIdModel;
    OutputStream out;
    android.app.AlertDialog.Builder msg;
    Intent it;
    AlertDialog alerta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);
        tvIdModel = findViewById(R.id.tvIdModel);

        BancoController crud = new BancoController(getBaseContext());
        final Cursor cursor = crud.carregaDados();

      /*  String[] nomeCampos = new String[] {criaBanco.ID, criaBanco.NOME, criaBanco.TEL, criaBanco.CPF, criaBanco.DATA, criaBanco.DES, criaBanco.EMAIL, criaBanco.OBS};
        int[] idViews = new int[] {R.id.txvID, R.id.txvNome, R.id.txvTelefone, R.id.txvCPF, R.id.txvDATA, R.id.txvDescricao, R.id.txvEmail, R.id.txvObs};
*/
        String[] nomeCampos = new String[] {DBTableConfig.Columns.NOME, DBTableConfig.Columns.TEL,DBTableConfig.Columns.EMAIL, DBTableConfig.Columns.ID, DBTableConfig.Columns.CPF, DBTableConfig.Columns.DATA, DBTableConfig.Columns.DES, DBTableConfig.Columns.OBS};
        int[] idViews = new int[] {R.id.txvNome, R.id.txvTelefone, R.id.txvEmail, R.id.tvIdModel };

        final SimpleCursorAdapter adaptador = new SimpleCursorAdapter(getBaseContext(),
                R.layout.listar_model,cursor,nomeCampos,idViews, 0);

       // ivPerfil = findViewById(R.id.ivPerfil);
      //  ivIcoMail = findViewById(R.id.ivIcoMail);
       // ivIcoTel = findViewById(R.id.ivIcoTel);



        ltwDados = (ListView)findViewById(R.id.ltwDados);
        ltwDados.setAdapter(adaptador);


        ltwDados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String codigo;
                cursor.moveToPosition(position);
                codigo = cursor.getString(cursor.getColumnIndexOrThrow(DBTableConfig.Columns.ID));
                Intent intent = new Intent(ListarActivity.this, ExibeContato.class);
                intent.putExtra("codigo", codigo);
                startActivity(intent);
                finish();
            }
        });

        ltwDados.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                String codigo;
                cursor.moveToPosition(position);
                codigo = cursor.getString(cursor.getColumnIndexOrThrow(DBTableConfig.Columns.ID));
                Intent intent = new Intent(ListarActivity.this, Editar.class);
                intent.putExtra("codigo", codigo);
                startActivity(intent);
                finish();
                return true;
            }
        });
      //  ivPerfil.setImageResource(R.drawable.wagner);
        //ivIcoMail.setImageResource(R.drawable.mail);
      //  ivIcoTel.setImageResource(R.drawable.tel);




    }


    //Wagner

    public void mensagemExibir(String titulo, String texto) {
        AlertDialog.Builder mensagem = new AlertDialog.Builder(ListarActivity.this);
        mensagem.setTitle(titulo);
        mensagem.setMessage(texto);
        mensagem.setNeutralButton(getString(R.string.javaOK), null);
        mensagem.show();

    }


    public void CadastroClientes(View v){

        it = new Intent(getBaseContext(), InsereDado.class);
        startActivity(it);

    }

    public void Lista() {

        setContentView(R.layout.content_listar);
        it = new Intent(getBaseContext(), ListarActivity.class);
        startActivity(it);

    }
    public void ApagarTudo( View view) {

        try {

            final SQLiteDatabase db = openOrCreateDatabase("clientes.db",
                    Context.MODE_PRIVATE, null);

            msg = new android.app.AlertDialog.Builder(ListarActivity.this);
            msg.setMessage(getString(R.string.javaDesejaApagarTodos));
            msg.setNegativeButton(getString(R.string.javaNao), null);
            msg.setPositiveButton(getString(R.string.javaSim), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    db.delete("clientes", null, null);

                    mensagemExibir(getString(R.string.javaOK), getString(R.string.javaClientesDeletadosSucesso));
                    Lista();
                }
            });

            msg.show();

        } catch (Exception erro) {
            mensagemExibir(getString(R.string.javaErro), getString(R.string.javaFalhaApagarTodos) + erro.getMessage());
        }
    }

    public void Backup1 ( View view){

        msg = new android.app.AlertDialog.Builder(ListarActivity.this);
        msg.setMessage(getString(R.string.javaDesejaRealizarBackup));
        msg.setNegativeButton(getString(R.string.javaNao), null);
        msg.setPositiveButton(getString(R.string.javaSim), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                InputStream in = null;
                try {
                    in = new FileInputStream(
                            new File(Environment.getDataDirectory()
                                    + "/data/br.com.wagner.cadastrodeclientes/databases/clientes.db"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                OutputStream out = null;
                try {
                    out = new FileOutputStream(new File(
                            Environment.getExternalStorageDirectory()
                                    + "/clientes.db"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                byte[] buf = new byte[1024];
                int len;
                try {
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mensagemExibir(getString(R.string.javaOK), getString(R.string.javaBackupRealizadoSucesso));

            }
        });

        msg.show();


    }

    public void Restore1 (View view){
        // OutputStream out = null;

        try {

            final InputStream in = new FileInputStream(
                    new File(Environment.getExternalStorageDirectory()
                            + "/clientes.db"));

            // Caminho de Destino do Backup do Seu Banco de Dados


            msg = new android.app.AlertDialog.Builder(ListarActivity.this);
            msg.setMessage(getString(R.string.javaDesejaRestaurarBackup));
            msg.setNegativeButton(getString(R.string.javaNao), null);
            msg.setPositiveButton(getString(R.string.javaSim), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    OutputStream out = null;
                    try {
                        out = new FileOutputStream(new File(
                                Environment.getDataDirectory()
                                        + "/data/br.com.wagner.cadastrodeclientes/databases/clientes.db"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    byte[] buf = new byte[1024];
                    int len;

                    try {

                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {

                        in.close();

                    } catch (IOException e) {

                        e.printStackTrace();
                    }

                    try {

                        out.close();

                    } catch (IOException e) {

                        e.printStackTrace();
                    }

                    mensagemExibir(getString(R.string.javaOK), getString(R.string.javaRestoreSucesso));
                    Lista();

                }


            });


            msg.show();

        } catch (FileNotFoundException erro) {
            mensagemExibir(getString(R.string.javaErro), getString(R.string.javaFalhaRestore) + erro.getMessage());
        }


    }





    public void Doe(View v){

        it = new Intent(getBaseContext(), Doacoes.class);
        startActivity(it);

    }

    public void Sobre(View v){

        it = new Intent(getBaseContext(), Sobre.class);
        startActivity(it);

    }

    public void Compartilhar (View view){

        it = new Intent(android.content.Intent.ACTION_SEND);
        it.setType("text/plain");
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        it.putExtra(Intent.EXTRA_SUBJECT,
                getString(R.string.javaTituloCompartilhar));

        it.putExtra(Intent.EXTRA_TEXT,
                "https://play.google.com/store/apps/details?id=br.com.wagner.cadastrodeclientes");

        startActivity(Intent.createChooser(it, getString(R.string.javaCompartilhar)));

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
