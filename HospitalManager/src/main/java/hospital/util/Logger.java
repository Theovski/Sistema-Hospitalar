package hospital.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String ARQUIVO_LOG = "src/main/resources/logs/sistema.log";
    private static final DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    static {
        FileManager.criarDiretorio("src/main/resources/logs");
    }
    
    private static void log(String nivel, String origem, String mensagem) {
        String timestamp = LocalDateTime.now().format(formatter);
        String linha = String.format("[%s] %s - %s: %s", 
            timestamp, nivel, origem, mensagem);
        
        FileManager.escreverArquivo(ARQUIVO_LOG, linha, true);
        System.out.println(linha);
    }
    
    public static void info(String origem, String mensagem) {
        log("INFO", origem, mensagem);
    }
    
    public static void aviso(String origem, String mensagem) {
        log("AVISO", origem, mensagem);
    }
    
    public static void erro(String origem, String mensagem) {
        log("ERRO", origem, mensagem);
    }
    
    public static void erro(String origem, String mensagem, Exception e) {
        log("ERRO", origem, mensagem + " | Exceção: " + e.getMessage());
        e.printStackTrace();
    }
}