package main.logs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RegistroOperacoes {
    private static final String CAMINHO_ARQUIVO = "registros.txt";
    static File arquivo = new File(CAMINHO_ARQUIVO);

    public RegistroOperacoes() {
        try {
            // Verificar se o arquivo de registro (log) já existe
            if(!arquivo.exists()){
                arquivo.createNewFile();
            }
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    public static void registrarOperacoes(String mensagem) {
        FileWriter fw;
        BufferedWriter bw;
        try {
            fw = new FileWriter(arquivo, true);
            bw = new BufferedWriter(fw);

            // Escrever a mensagem no arquivo
            bw.write(mensagem);
            bw.newLine(); // Adicionar uma quebra de linha para separar operações

            // Fechar o arquivo
            bw.close();
        } catch (IOException exp) {
            exp.printStackTrace();
            // Lide com possíveis erros ao abrir/gravar no arquivo, por exemplo, lançando exceções personalizadas ou registrando erros
        }
    }
}
