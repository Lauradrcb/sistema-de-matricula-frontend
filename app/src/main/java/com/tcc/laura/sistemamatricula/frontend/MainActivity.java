package com.tcc.laura.sistemamatricula.frontend;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button button_login_login;
    private EditText username_field;
    private EditText password_field;
    private String username;
    private String password;
    private String baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Replace this with your own IP address or URL.
        baseUrl = "http://192.168.178.78:8080/rest/users/login/";

        username_field = (EditText) findViewById(R.id.editText_login_username);
        password_field = (EditText) findViewById(R.id.editText_login_password);

        button_login_login = (Button) findViewById(R.id.button_login_login);

        button_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            try {

                username = username_field.getText().toString();
                password = password_field.getText().toString();

                ApiAuthenticationClient apiAuthenticationClient = new ApiAuthenticationClient(baseUrl, username, password);
                apiAuthenticationClient.setHttpMethod("POST");
                AsyncTask<Void, Void, String> execute = new ExecuteNetworkOperation(apiAuthenticationClient);
                execute.execute();
            } catch (Exception ex) {}
            }
        });
    }

    /**
     * This subclass handles the network operations in a new thread.
     * It starts the progress bar, makes the API call, and ends the progress bar.
     */
    public class ExecuteNetworkOperation extends AsyncTask<Void, Void, String> {

        private ApiAuthenticationClient apiAuthenticationClient;
        private String isValidCredentials;

        /**
         * Overload the constructor to pass objects to this class.
         */
        public ExecuteNetworkOperation(ApiAuthenticationClient apiAuthenticationClient) {
            this.apiAuthenticationClient = apiAuthenticationClient;
        }

        @Override
        //Torna o simbolo de carregamento visivel
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        }

        @Override
        //Verifica se usuario e senha estao consistentes com o DB
        protected String doInBackground(Void... params) {
            try {
                isValidCredentials = apiAuthenticationClient.execute();
            } catch (Exception e) {e.printStackTrace();}

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Esconde o icone de carregamento
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);

            // Redireciona para a tela de usuario caso o Login seja bem sucedido
            if (isValidCredentials.equals("granted")) {
                goToSecondActivity();
            }
            else if(isValidCredentials.equals("notregistered")){
                Toast.makeText(getApplicationContext(), "User not registered", Toast.LENGTH_LONG).show();
            }
            // Exibe mensagem de erro caso o login tenha falhado
            else if(isValidCredentials.equals("wrong")){
                Toast.makeText(getApplicationContext(), "Wrong credentials", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Generic Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    //Funcao que redireciona para a proxima tela
    private void goToSecondActivity() {
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("password", password);
        bundle.putString("baseUrl", baseUrl);

        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}