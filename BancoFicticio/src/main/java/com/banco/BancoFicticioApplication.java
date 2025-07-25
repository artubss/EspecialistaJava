package com.banco;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.banco.service.BancoService;

@SpringBootApplication
@ComponentScan({"com.banco"})
@EntityScan("com.banco.domain")
public class BancoFicticioApplication {

    public static void main(String[] args) {
        SpringApplication.run(BancoFicticioApplication.class, args);
    }

    @Bean
    public CommandLineRunner inicializarDados(BancoService bancoService) {
        return args -> {
            // Inicializa dados de exemplo para demonstração
            bancoService.inicializarDadosExemplo();
            System.out.println("Dados de exemplo inicializados com sucesso!");
        };
    }
}
