package br.ufsm.csi.tapw.pilacoin.constants;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Data
@Component
@PropertySource("classpath:application.yml")
public class Constants {
    
    @Value("${pilacoin.username}")
    private String username;
    
    @Value("${pilacoin.home}")
    private String home;

    private Path homePath;
    
    @Value("${pilacoin.mining-threads:#{null}}")
    private Integer numberOfThreads;

    @PostConstruct
    public void init() {
        this.homePath = Paths.get(this.home).toAbsolutePath();

        if (this.numberOfThreads == null) {
            this.numberOfThreads = Runtime.getRuntime().availableProcessors();
        }
    }
}
