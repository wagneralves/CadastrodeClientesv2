package br.com.wagner.cadastrodeclientes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ListarActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private ListView ltwDados;
    private InputStream in;
    private OutputStream out;
    private AlertDialog.Builder msg;
    private Intent it;
    private Toast toast;
    private long lastBackPressTime = 0;
    private TextView tvIdModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        tvIdModel = findViewById(R.id.tvIdModel);
        ltwDados = findViewById(R.id.ltwDados);

        // Request necessary permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
            }
        }

        BancoController crud = new BancoController(getBaseContext());
        final Cursor cursor = crud.carregaDados();

        String[] nomeCampos = new String[]{DBTableConfig.Columns.NOME, DBTableConfig.Columns.TEL, DBTableConfig.Columns.EMAIL, DBTableConfig.Columns.ID, DBTableConfig.Columns.CPF, DBTableConfig.Columns.DATA, DBTableConfig.Columns.DES, DBTableConfig.Columns.OBS};
        int[] idViews = new int[]{R.id.txvNome, R.id.txvTelefone, R.id.txvEmail, R.id.tvIdModel};

        SimpleCursorAdapter adaptador = new SimpleCursorAdapter(getBaseContext(),
                R.layout.listar_model, cursor, nomeCampos, idViews, 0);

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
    }

    public void mensagemExibir(String titulo, String texto) {
        AlertDialog.Builder mensagem = new AlertDialog.Builder(ListarActivity.this);
        mensagem.setTitle(titulo);
        mensagem.setMessage(texto);
        mensagem.setNeutralButton(getString(R.string.javaOK), null);
        mensagem.show();
    }

    public void CadastroClientes(View v) {
        it = new Intent(getBaseContext(), InsereDado.class);
        startActivity(it);
    }

    public void Lista() {
        setContentView(R.layout.content_listar);
        it = new Intent(getBaseContext(), ListarActivity.class);
        startActivity(it);
    }

    public void ApagarTudo(View view) {
        try {
            final SQLiteDatabase db = openOrCreateDatabase("clientes.db", Context.MODE_PRIVATE, null);
            msg = new AlertDialog.Builder(ListarActivity.this);
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

    public void Backup1(View view) {
        if (!isExternalStorageWritable()) {
            mensagemExibir(getString(R.string.javaErro), "Armazenamento externo não disponível.");
            return;
        }

        try {
            File inputFile = new File(getDatabasePath("clientes.db").getAbsolutePath());
            File outputFile = new File(getExternalFilesDir(null), "clientes_backup.db");

            InputStream in = new FileInputStream(inputFile);
            OutputStream out = new FileOutputStream(outputFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            in.close();
            out.close();
            mensagemExibir(getString(R.string.javaOK), getString(R.string.javaBackupRealizadoSucesso));
        } catch (FileNotFoundException e) {
            Log.e("BackupError", "Arquivo não encontrado: " + e.getMessage());
            mensagemExibir(getString(R.string.javaErro), "Arquivo não encontrado: " + e.getMessage());
        } catch (IOException e) {
            Log.e("BackupError", "Erro ao realizar backup: " + e.getMessage());
            mensagemExibir(getString(R.string.javaErro), "Erro ao realizar backup: " + e.getMessage());
        }
    }

    public void Restore1(View view) {
        if (!isExternalStorageReadable()) {
            mensagemExibir(getString(R.string.javaErro), "Armazenamento externo não disponível.");
            return;
        }

        try {
            File inputFile = new File(getExternalFilesDir(null), "clientes_backup.db");
            File outputFile = new File(getDatabasePath("clientes.db").getAbsolutePath());

            InputStream in = new FileInputStream(inputFile);
            OutputStream out = new FileOutputStream(outputFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            in.close();
            out.close();
            mensagemExibir(getString(R.string.javaOK), getString(R.string.javaRestoreSucesso));
            Lista();
        } catch (FileNotFoundException e) {
            Log.e("RestoreError", "Arquivo não encontrado: " + e.getMessage());
            mensagemExibir(getString(R.string.javaErro), "Arquivo não encontrado: " + e.getMessage());
        } catch (IOException e) {
            Log.e("RestoreError", "Erro ao restaurar backup: " + e.getMessage());
            mensagemExibir(getString(R.string.javaErro), "Erro ao restaurar backup: " + e.getMessage());
        }
    }

    public void Doe(View v) {
        it = new Intent(getBaseContext(), Doacoes.class);
        startActivity(it);
    }

    public void Sobre(View v) {
        it = new Intent(getBaseContext(), Sobre.class);
        startActivity(it);
    }

    public void Compartilhar(View view) {
        it = new Intent(android.content.Intent.ACTION_SEND);
        it.setType("text/plain");
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        it.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.javaTituloCompartilhar));
        it.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=br.com.wagner.cadastrodeclientes");
        startActivity(Intent.createChooser(it, getString(R.string.javaCompartilhar)));
    }

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

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
}
