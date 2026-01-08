<<<<<<< HEAD
package hospital.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    
    public static void criarDiretorio(String caminho) {
        File diretorio = new File(caminho);
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }
    }
    
    public static void escreverArquivo(String caminho, String conteudo, boolean append) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho, append))) {
            writer.write(conteudo);
            writer.newLine();
        } catch (IOException e) {
            Logger.erro("FileManager", "Erro ao escrever em " + caminho + ": " + e.getMessage());
        }
    }
    
    public static List<String> lerArquivo(String caminho) {
        List<String> linhas = new ArrayList<>();
        File arquivo = new File(caminho);
        
        if (!arquivo.exists()) {
            return linhas;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    linhas.add(linha);
                }
            }
        } catch (IOException e) {
            Logger.erro("FileManager", "Erro ao ler " + caminho + ": " + e.getMessage());
        }
        return linhas;
    }
    
    public static void limparArquivo(String caminho) {
        escreverArquivo(caminho, "", false);
    }
    
    public static void criarDiretorioSeNaoExistir(String caminho) {
        criarDiretorio(caminho);
    }
}
=======

>>>>>>> 586504875a4bde42a23314da35225f64cbeac28e
