package com.example.httpretrofit2completo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


import com.example.httpretrofit2completo.dao.DatabaseHelper;
import com.example.httpretrofit2completo.model.CEP;

import java.util.ArrayList;

public class ListagemActivity extends AppCompatActivity {

    private ListView listView;
    private TextView emptyTextView;
    private ArrayList<String> listaCEPs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem);

        listView = findViewById(R.id.listViewCEPs);
        emptyTextView = findViewById(R.id.textViewEmpty);
        listaCEPs = new ArrayList<>();

        // Carregue a lista de CEPs do banco de dados
        DatabaseHelper db = new DatabaseHelper(this);
        listaCEPs = db.getAllCEPs();

        atualizarLista();

        Button voltarParaConsultaButton = findViewById(R.id.buttonVoltarParaConsulta);
        voltarParaConsultaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Configurar o clique em um item da ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cepSelecionado = listaCEPs.get(position);
                mostrarDetalhesCEP(cepSelecionado);
            }
        });
    }

    private void mostrarDetalhesCEP(String cep) {
        DatabaseHelper db = new DatabaseHelper(this);
        CEP cepDetalhes = db.getCEPByCep(cep);

        Intent intent = new Intent(this, DetalhesCEPActivity.class);
        intent.putExtra("CEP", cepDetalhes.getCep());
        intent.putExtra("Logradouro", cepDetalhes.getLogradouro());
        intent.putExtra("Bairro", cepDetalhes.getBairro());
        intent.putExtra("Cidade", cepDetalhes.getCidade());
        intent.putExtra("UF", cepDetalhes.getUf());
        startActivity(intent);
    }

    private void atualizarLista() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaCEPs);
        listView.setAdapter(adapter);

        if (listaCEPs.isEmpty()) {
            listView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
        }
    }
}
