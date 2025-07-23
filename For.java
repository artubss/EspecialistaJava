import java.util.Scanner;

public class For {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        System.out.print("Peso máximo da aeronave: ");
        int pesoMaximo = entrada.nextInt();

        System.out.print("Quantidade de passageiros: ");
        int totalPassageiros = entrada.nextInt();
        for (int passageiroAtual = 0; passageiroAtual < totalPassageiros || pesoMaximo < 0; passageiroAtual++) {
            System.out.println("Peso do passageiro " + (passageiroAtual + 1) + ": ");
            int pesoPassageiro = entrada.nextInt();
            pesoMaximo -= pesoPassageiro;
            if (pesoMaximo < 0) {
                System.out.println("Peso máximo excedido");
                break;
            }
        }
        if (pesoMaximo >= 0) {
            System.out.println("Peso máximo não excedido");
        }
        entrada.close();
    }
}
