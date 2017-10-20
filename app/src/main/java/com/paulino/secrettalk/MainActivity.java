package com.paulino.secrettalk;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paulino.secrettalk.logger.Log;
import com.paulino.secrettalk.logger.LogListActivity;
import com.paulino.secrettalk.logger.LogWrapper;
import com.paulino.secrettalk.logger.MessageOnlyLogFilter;

import java.util.ArrayList;

/**
 * Created by Guilherme Paulino on 10/1/2017.
 */

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "SecretTalk";

    public static ArrayList<String> LogList = new ArrayList<String>();

    public static LogWrapper logWrapper;
    public static MessageOnlyLogFilter msgFilter;

    // Variaveis de instancia
    private UserLoginTask mAuthTask = null;
    private EditText mKeywordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView countChar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        // Recebe o texto da chave inserida atraves de uma View EditText
        mKeywordView = (EditText) findViewById(R.id.keyword);


        // Ao apertar "Enter" no campo de texto da chave, pelo teclado, equivale a pressionar o botao "Enter Secret"
        mKeywordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        // Ao pressionar o botao "Enter Secret", eh feita uma tentativa de login
        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        // O formulario de Login eh uma "Scroll View"
        // Dentro dela, possui um EditText/TextInputLayout e um Button
        mLoginFormView = findViewById(R.id.login_form);
        // ProgressView permite aplicar uma animacao de barra de progresso
        // caso a tentativa de Login demore para processar
        mProgressView = findViewById(R.id.login_progress);


        countChar = (TextView) findViewById(R.id.count_char);

        mKeywordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 1) {
                    countChar.setText(String.valueOf(s.length())+" characters");
                } else if(s.length() == 1) {
                    countChar.setText(String.valueOf(s.length())+" character");
                } else {
                    countChar.setText("w/o cryptography");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);

        // Filter strips out everything except the message text.
        msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        Log.i(TAG, "Start Application");

    }


    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        mKeywordView.setError(null);
        String keyword = mKeywordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // se o usuario decidir por preencher uma senha, ela precisa ser valida.
        if (!TextUtils.isEmpty(keyword) && !isKeywordValid(keyword)) {
            mKeywordView.setError(getString(R.string.error_invalid_keyword));
            focusView = mKeywordView;
            cancel = true;
        }

        if (cancel) {
            // se cancelar (chave invalida), o campo de texto solicita o foco do cursor
            focusView.requestFocus();
        } else {
            // se a chave for aceita, mostra a barra de progresso, e executa o acesso
            showProgress(true);
            mAuthTask = new UserLoginTask(keyword);
            mAuthTask.execute((Void) null);
        }
    }

    // Uma chave eh valida apenas nas condicoes abaixo
    private boolean isKeywordValid(String keyword) {
        return ( keyword.isEmpty() || keyword.length() == 16 || keyword.length() == 24 || keyword.length() == 32 );
    }

    // Animacao da barra de progresso
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            // 200 milisegundos
            int shortAnimTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // Se a API da barra de progresso nao estiver disponivel,
            // simplesmente mostra e oculta componentes UI
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mKeyword;

        UserLoginTask(String keyword) {
            mKeyword = keyword;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                // Mensagem de boas vindas
                Toast toastText = Toast.makeText(MainActivity.this, "Welcome to BlueChat",Toast.LENGTH_SHORT);
                toastText.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
                toastText.show();

                // Inicia a nova Activity, de mensagens (BlueChat), como uma nova Task
                Intent toPretend = new Intent(MainActivity.this, MessageActivity.class);
                toPretend.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // envia a chave inserida pelo usuario, via intent
                toPretend.putExtra("Keyword", mKeyword);

                if (mKeyword.isEmpty()) {
                    Log.i(TAG, "Empty keyword");
                } else {
                    Log.i(TAG, "Keyword: "+mKeyword);
                }
                startActivity(toPretend);

            } else {
                //
                mKeywordView.setError(getString(R.string.error_incorrect_keyword));
                mKeywordView.requestFocus();
            }
        }

        // Caso a entrada seja cancelada
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

