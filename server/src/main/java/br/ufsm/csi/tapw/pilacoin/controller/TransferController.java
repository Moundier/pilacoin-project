package br.ufsm.csi.tapw.pilacoin.controller;

import br.ufsm.csi.tapw.pilacoin.dto.TransferirPilaDTO;
import br.ufsm.csi.tapw.pilacoin.model.Transferencia;
import br.ufsm.csi.tapw.pilacoin.service.QueueService;
import br.ufsm.csi.tapw.pilacoin.util.CryptoUtil;
import br.ufsm.csi.tapw.pilacoin.util.JacksonUtil;
import br.ufsm.csi.tapw.pilacoin.util.Singleton;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/transferir")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TransferController {
    
    private final Singleton singleton;
    private final QueueService queueService;

    @PostMapping
    public Transferencia transferir(@RequestBody TransferirPilaDTO dto) {
        Transferencia transferencia = Transferencia.builder()
            .chaveUsuarioOrigem(this.singleton.getPublicKey().getEncoded())
            .nomeUsuarioOrigem(this.singleton.getProperties().getUsername())
            .chaveUsuarioDestino(CryptoUtil.decodeBase64(dto.getChaveUsuarioDestino()))
            .nomeUsuarioDestino(dto.getNomeUsuarioDestino())
            .noncePila(dto.getNoncePila())
            .dataTransacao(new Date())
            .build();

        transferencia.setAssinatura(CryptoUtil.sign(JacksonUtil.toString(transferencia), this.singleton.getPrivateKey()));

        System.out.println("\n\n\n"+ transferencia + "\n\n\n");

        this.queueService.publishTransferencia(transferencia);

        return transferencia;
    }
}
