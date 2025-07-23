
import java.util.Scanner;

public class Avaliacao {
    public static void main (String[] args) {
        Scanner entrada = new Scanner(System.in);
        System.out.println("Avalie o nosso atendimento (1 a 5): ");
        int nota = entrada.nextInt();

        String avaliacaoNota;

        avaliacaoNota = switch (nota) {
            case 1 -> "MUITO RUIM";
            case 2 -> "Ruim";
            case 3 -> "Regular";
            case 4 -> "Bom";
            case 5 -> "Muito Bom";
            default -> "Nota inválida";
        };

        System.out.printf("Avaliação: %s%n", avaliacaoNota);
        entrada.close();
    }
}