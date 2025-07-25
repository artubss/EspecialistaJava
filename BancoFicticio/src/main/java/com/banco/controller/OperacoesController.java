package com.banco.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OperacoesController {

    @GetMapping("/operacoes")
    public String operacoes() {
        return "operacoes";
    }
}
