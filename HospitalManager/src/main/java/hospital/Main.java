package hospital;

import hospital.view.LoginGUI;
import hospital.util.Logger;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    
    public static void main(String[] args) {
        Logger.info("Main", "=== INICIANDO SISTEMA HOSPITALAR ===");
        Logger.info("Main", "Versão: 1.0.0");
        Logger.info("Main", "Trabalho de Orientação a Objetos");
        
        // Define o Look and Feel do sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Logger.info("Main", "Look and Feel configurado");
        } catch (Exception e) {
            Logger.aviso("Main", "Não foi possível definir Look and Feel: " + e.getMessage());
        }
        
        // Inicia a interface gráfica na thread do Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Logger.info("Main", "Abrindo tela de login");
                LoginGUI login = new LoginGUI();
                login.setVisible(true);
            }
        });
    }
}
