package br.ufsm.csi.tapw.pilacoin.model.json;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.ufsm.csi.tapw.pilacoin.util.JournalUtil;

@Data
public class ReportJson {
    public Long geradoEm;
    public String nomeUsuario;
    public Boolean minerouPila;
    public Boolean validouPila;
    public Boolean minerouBloco;
    public Boolean validouBloco;
    public Boolean transferiuPila;

    public boolean compareTo(ReportJson otherReport) {
        return this.nomeUsuario.equals(otherReport.nomeUsuario)
            && this.minerouPila.equals(otherReport.minerouPila)
            && this.validouPila.equals(otherReport.validouPila)
            && this.minerouBloco.equals(otherReport.minerouBloco)
            && this.validouBloco.equals(otherReport.validouBloco)
            && this.transferiuPila.equals(otherReport.transferiuPila);
    }

    public void printReport() {

        JournalUtil.logDoubleLineBox(
            "REPORT: " +  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(geradoEm)) +
            "\n---\n" +
            "Nome de usuário: " + nomeUsuario +
            "\nMinerou pila: " + (minerouPila ? "Sim" : "Não") +
            "\nValidou pila: " + (validouPila ? "Sim" : "Não") +
            "\nMinerou bloco: " + (minerouBloco ? "Sim" : "Não") +
            "\nValidou bloco: " + (validouBloco ? "Sim" : "Não") +
            "\nTransferiu pila: " + (transferiuPila ? "Sim" : "Não")
        );
    }
}
