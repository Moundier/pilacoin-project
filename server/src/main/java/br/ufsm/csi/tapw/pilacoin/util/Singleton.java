package br.ufsm.csi.tapw.pilacoin.util;

import br.ufsm.csi.tapw.pilacoin.constants.Constants;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.SneakyThrows;

import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Data
@Component
public class Singleton {

    private PrivateKey privateKey;
    private PublicKey publicKey;
    private Constants properties;

    private Singleton(Constants properties) { 
        this.properties = properties;
    }

    // BILL PUGS'S SINGLETON IMPLEMENTATION
    public static Singleton getInstance() {
        return GetSingleton.INSTANCE;
    }

    private static class GetSingleton {
        private static Constants properties;
        private static final Singleton INSTANCE = new Singleton(properties);
    }

    @SneakyThrows
    @PostConstruct
    private void loadKeypair() {
        System.out.println("\u001B[33mKey pair loaded!\u001B[0m");
        Path privateKeyPath = this.properties.getHomePath().resolve("private.key");
        Path publicKeyPath = this.properties.getHomePath().resolve("public.key");

        if (Files.exists(privateKeyPath) && Files.exists(publicKeyPath)) {
            this.loadFoundKeypair(privateKeyPath, publicKeyPath);
        } else {
            this.generateKeypair(privateKeyPath, publicKeyPath);
        }
    }

    @SneakyThrows
    private void loadFoundKeypair(Path privateKeyPath, Path publicKeyPath) {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        byte[] privateKeyBytes = Files.readAllBytes(privateKeyPath);
        byte[] publicKeyBytes = Files.readAllBytes(publicKeyPath);

        PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(publicKeyBytes);

        this.privateKey = kf.generatePrivate(privateSpec);
        this.publicKey = kf.generatePublic(publicSpec);
    }

    @SneakyThrows
    private void generateKeypair(Path privateKeyPath, Path publicKeyPath) {
        Files.createDirectories(this.properties.getHomePath());

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();

        Files.write(privateKeyPath, this.privateKey.getEncoded());
        Files.write(publicKeyPath, this.publicKey.getEncoded());
    }
}
