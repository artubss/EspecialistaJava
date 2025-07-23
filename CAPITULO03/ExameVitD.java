//desejável acima de 20 ng/ml
//grupos de risco: 30-60 ng/ml

import java.util.Scanner;

public class ExameVitD {
    public static void main (String[] args) {
        Scanner entrada = new Scanner(System.in);

        System.out.println("Você é grupo de risco? (sim/não)");

        System.out.println("Digite o valor do exame de vitamina D: ");
        double vitaminaD = entrada.nextDouble(); //ng/ml

        System.out.println("Você é grupo de risco? (sim/não)");
        String grupoRisco = entrada.next();
        boolean grupoRiscoSim = grupoRisco.equals("sim");

        boolean vitaminaDNormal = grupoRiscoSim ? vitaminaD >=30 && vitaminaD <= 60: vitaminaD >= 20;

        if (vitaminaDNormal) {
            System.out.println("Vitamina D normal");
        } else {
            System.out.println("Vitamina D alterada");
        }
        entrada.close();
}}