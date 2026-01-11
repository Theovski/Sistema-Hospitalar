package hospital.service;

import hospital.util.FileManager;
import hospital.util.Logger;

public class NotificacaoService {

    public NotificacaoService() {
        Logger.info("NotificacaoService", "Serviço de notificações inicializado");
    }

    public void enviarNotificacaoPaciente(String cpf, String mensagem) {
        Logger.info("NotificacaoService", "Notificação para paciente " + cpf + ": " + mensagem);
    }

    public void enviarNotificacaoMedico(String crm, String mensagem) {
        Logger.info("NotificacaoService", "Notificação para médico " + crm + ": " + mensagem);
    }

    public void enviarNotificacaoRecepcionista(String matricula, String mensagem) {
        Logger.info("NotificacaoService", "Notificação para recepcionista " + matricula + ": " + mensagem);
    }
}
