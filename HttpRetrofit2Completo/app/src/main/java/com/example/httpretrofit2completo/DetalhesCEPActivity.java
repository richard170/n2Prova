package com.example.httpretrofit2completo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetalhesCEPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_cep);

        // Obtenha os dados do Intent
        String cep = getIntent().getStringExtra("CEP");
        String logradouro = getIntent().getStringExtra("Logradouro");
        String bairro = getIntent().getStringExtra("Bairro");
        String cidade = getIntent().getStringExtra("Cidade");
        String uf = getIntent().getStringExtra("UF");

        // Atualize as TextViews com os dados do CEP
        TextView textViewCepDetalhes = findViewById(R.id.textViewCepDetalhes);
        TextView textViewLogradouroDetalhes = findViewById(R.id.textViewLogradouroDetalhes);
        TextView textViewBairroDetalhes = findViewById(R.id.textViewBairroDetalhes);
        TextView textViewCidadeDetalhes = findViewById(R.id.textViewCidadeDetalhes);
        TextView textViewUfDetalhes = findViewById(R.id.textViewUfDetalhes);

        textViewCepDetalhes.setText("CEP: " + cep);
        textViewLogradouroDetalhes.setText("Logradouro: " + logradouro);
        textViewBairroDetalhes.setText("Bairro: " + bairro);
        textViewCidadeDetalhes.setText("Cidade: " + cidade);
        textViewUfDetalhes.setText("UF: " + uf);

        // Configurar o botão para voltar para ListagemActivity
        Button voltarParaListagemButton = findViewById(R.id.buttonVoltarParaListagem);
        voltarParaListagemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetalhesCEPActivity.this, ListagemActivity.class));
                finish();
            }
        });
    }
}

