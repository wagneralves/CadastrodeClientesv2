package br.com.wagner.cadastrodeclientes; // Certifique-se de que o pacote esteja correto

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Sobre extends AppCompatActivity { // Nome da classe alterado para corresponder ao nome do arquivo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre); // Certifique-se de que o layout esteja correto

        TextView privacyPolicyTextView = findViewById(R.id.privacyPolicy);

        // Configura o link da política de privacidade
        String privacyPolicyUrl = "https://github.com/wagneralves/CadastrodeClientesv2/blob/master/Privacy";
        privacyPolicyTextView.setText(Html.fromHtml("<a href='" + privacyPolicyUrl + "'>Política de Privacidade</a>"));
        privacyPolicyTextView.setMovementMethod(LinkMovementMethod.getInstance());

        // Se você deseja adicionar comportamento para o clique, você pode configurar um listener, mas isso não é necessário aqui
        privacyPolicyTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(privacyPolicyUrl));
            startActivity(intent);
        });
    }
}
