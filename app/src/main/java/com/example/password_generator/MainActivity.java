package com.example.password_generator;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.security.SecureRandom;

public class MainActivity extends AppCompatActivity {

    // Declaração dos elementos da interface
    private EditText lengthInput;
    private CheckBox uppercaseCheck, lowercaseCheck, numbersCheck, symbolsCheck;
    private Button generateButton, copyButton;
    private TextView passwordOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Configuração dos insets para barras do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Vinculando os elementos do layout às variáveis
        lengthInput = findViewById(R.id.lengthInput);
        uppercaseCheck = findViewById(R.id.uppercaseCheck);
        lowercaseCheck = findViewById(R.id.lowercaseCheck);
        numbersCheck = findViewById(R.id.numbersCheck);
        symbolsCheck = findViewById(R.id.symbolsCheck);
        generateButton = findViewById(R.id.button); // "Gerar Senha"
        copyButton = findViewById(R.id.copyButton);
        passwordOutput = findViewById(R.id.passwordOutput);

        // Listener para o botão "Gerar"
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePassword();
            }
        });

        // Listener para o botão "Copiar"
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard();
            }
        });
    }

    // Método para gerar a senha
    private void generatePassword() {
        // Pegando o comprimento digitado ou definindo um padrão
        String lengthStr = lengthInput.getText().toString();
        int length;
        try {
            length = Integer.parseInt(lengthStr);
            if (length < 8 || length > 20) {
                Toast.makeText(this, "Digite um número entre 8 e 20!", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            length = 12; // Valor padrão se nada for digitado ou for inválido
        }

        // Verificando quais tipos de caracteres foram selecionados
        boolean useUppercase = uppercaseCheck.isChecked();
        boolean useLowercase = lowercaseCheck.isChecked();
        boolean useNumbers = numbersCheck.isChecked();
        boolean useSymbols = symbolsCheck.isChecked();

        // Se nenhuma opção for selecionada, exibe um aviso
        if (!useUppercase && !useLowercase && !useNumbers && !useSymbols) {
            Toast.makeText(this, "Selecione pelo menos uma opção!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Definindo os conjuntos de caracteres
        String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercaseChars = "abcdefghijklmnopqrstuvwxyz";
        String numberChars = "0123456789";
        String symbolChars = "!@#$%^&*()_+-=[]{}|;:,.<>?";

        // Construindo a lista de caracteres permitidos
        StringBuilder allowedChars = new StringBuilder();
        if (useUppercase) allowedChars.append(uppercaseChars);
        if (useLowercase) allowedChars.append(lowercaseChars);
        if (useNumbers) allowedChars.append(numberChars);
        if (useSymbols) allowedChars.append(symbolChars);

        // Gerando a senha aleatoriamente
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(allowedChars.length());
            password.append(allowedChars.charAt(randomIndex));
        }

        // Exibindo a senha gerada
        passwordOutput.setText(password.toString());
    }

    // Método para copiar a senha para a área de transferência
    private void copyToClipboard() {
        String password = passwordOutput.getText().toString();
        if (password.equals("Sua senha aparecerá aqui") || password.isEmpty()) {
            Toast.makeText(this, "Gere uma senha primeiro!", Toast.LENGTH_SHORT).show();
            return;
        }

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Senha", password);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, "Senha copiada!", Toast.LENGTH_SHORT).show();
    }
}