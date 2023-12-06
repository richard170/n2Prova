package com.example.httpretrofit2completo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.httpretrofit2completo.dao.DatabaseHelper;
import com.example.httpretrofit2completo.model.CEP;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConsultaActivity extends AppCompatActivity {
    private EditText cepEditText, logradouroEditText, bairroEditText, cidadeEditText, ufEditText;
    private Button consultarButton, cadastrarButton;
    private String apiUrl = "https://viacep.com.br/ws/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        cepEditText = findViewById(R.id.editTextCEP);
        logradouroEditText = findViewById(R.id.editTextLogradouro);
        bairroEditText = findViewById(R.id.editTextBairro);
        cidadeEditText = findViewById(R.id.editTextCidade);
        ufEditText = findViewById(R.id.editTextUF);
        consultarButton = findViewById(R.id.buttonConsultar);
        cadastrarButton = findViewById(R.id.buttonCadastrar);

        consultarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cep = cepEditText.getText().toString();
                new ConsultaCEPTask().execute(apiUrl + cep + "/json");
            }
        });

        cadastrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenha os dados do CEP
                String cep = cepEditText.getText().toString();
                String logradouro = logradouroEditText.getText().toString();
                String bairro = bairroEditText.getText().toString();
                String cidade = cidadeEditText.getText().toString();
                String uf = ufEditText.getText().toString();

                // Crie um objeto CEP
                CEP novoCEP = new CEP(cep, logradouro, "", bairro, cidade, uf);

                // Armazene no banco de dados SQLite
                DatabaseHelper db = new DatabaseHelper(ConsultaActivity.this);
                db.addCEP(novoCEP);

                // Limpe os campos após cadastrar
                cepEditText.setText("");
                logradouroEditText.setText("");
                bairroEditText.setText("");
                cidadeEditText.setText("");
                ufEditText.setText("");

                Toast.makeText(ConsultaActivity.this, "CEP cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
            }
        });


        Button irParaListagemButton = findViewById(R.id.buttonIrParaListagem);
        irParaListagemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConsultaActivity.this, ListagemActivity.class);
                startActivity(intent);
            }
        });
    }

    private class ConsultaCEPTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                reader.close();
                connection.disconnect();

                return result.toString();
            } catch (Exception e) {
                return "Erro ao consultar o CEP.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                logradouroEditText.setText(jsonObject.getString("logradouro"));
                bairroEditText.setText(jsonObject.getString("bairro"));
                cidadeEditText.setText(jsonObject.getString("localidade"));
                ufEditText.setText(jsonObject.getString("uf"));
            } catch (Exception e) {
                Toast.makeText(ConsultaActivity.this, "Erro ao processar os dados do CEP.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

