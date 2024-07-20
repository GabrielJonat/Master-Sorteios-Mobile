package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    EditText numAlunosInput, maxAlunosInput;
    Button sortearBtn, resortearBtn;
    TextView resultText;
    List<Integer> sorteados = new ArrayList<>();
    List<Integer> jafoi = new ArrayList<>();
    List<Integer> lista_negra = new ArrayList<>();
    int rejeitados = 0;
    int numAlunos;
    int maxAlunos;
    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numAlunosInput = findViewById(R.id.numAlunos);
        maxAlunosInput = findViewById(R.id.maxAlunos);
        sortearBtn = findViewById(R.id.sortearBtn);
        resortearBtn = findViewById(R.id.resortear);
        resultText = findViewById(R.id.resultText);

        sortearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    numAlunos = Integer.parseInt(numAlunosInput.getText().toString());
                    maxAlunos = Integer.parseInt(maxAlunosInput.getText().toString());

                    if (maxAlunos >= numAlunos) {
                        Toast.makeText(MainActivity.this, "Sorteio inválido!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    sorteados.clear();
                    jafoi.clear();
                    rejeitados = 0;
                    lista_negra.clear();

                    while (sorteados.size() < maxAlunos) {
                        int aluno = random.nextInt(numAlunos) + 1;
                        if (!jafoi.contains(aluno)) {
                            sorteados.add(aluno);
                            jafoi.add(aluno);
                        }
                    }

                    resultText.setText(sorteados.toString());
                    resortearBtn.setVisibility(View.VISIBLE);

                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Entrada inválida!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resortearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResortearDialog();
            }
        });
    }

    private void showResortearDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Desejas sortear novamente algum aluno? ('s', 'n')");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Continuar", (dialog, which) -> {
            String response = input.getText().toString().trim();
            if (response.equalsIgnoreCase("s") || response.equalsIgnoreCase("sim")) {
                showResortearAlunosDialog();
            } else {
                Toast.makeText(MainActivity.this, "Resorteio cancelado", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showResortearAlunosDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informe os alunos a serem sorteados novamente separando-os por espaço:");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Resortear", (dialog, which) -> {
            try {
                String[] inputArray = input.getText().toString().trim().split(" ");
                List<Integer> listaResort = new ArrayList<>();
                for (String s : inputArray) {
                    listaResort.add(Integer.parseInt(s));
                }

                int resort = listaResort.size();
                rejeitados += resort;

                if (rejeitados > numAlunos - maxAlunos) {
                    Toast.makeText(MainActivity.this, "Não há alunos o suficiente para o sorteio", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (resort > sorteados.size()) {
                    Toast.makeText(MainActivity.this, "Sorteio inválido!", Toast.LENGTH_SHORT).show();
                    return;
                }

                lista_negra.addAll(listaResort);
                sorteados.removeAll(listaResort);

                for (int i = 0; i < resort; i++) {
                    int aluno;
                    do {
                        aluno = random.nextInt(numAlunos) + 1;
                    } while (listaResort.contains(aluno) || sorteados.contains(aluno) || lista_negra.contains(aluno));

                    sorteados.add(aluno);
                }

                resultText.setText(sorteados.toString());

            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, "Entrada de dados inválida!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}