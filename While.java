import java.util.Scanner;

public class While {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        System.out.print("Peso máximo da aeronave: ");
        int pesoMaximo = entrada.nextInt();

        System.out.println("Capacidade máxima de passageiros da aeronave: ");
        int capacidadeAeronave = entrada.nextInt();
        int passageiroAtual = 0;

        boolean adicionarPassageiro = true;

        while (pesoMaximo >= 0 && adicionarPassageiro && passageiroAtual < capacidadeAeronave) {
            System.out.println("--------------------------------");
            System.out.println("Deseja adicionar mais um passageiro? (s/n)");
            String resposta = entrada.next();
            System.out.println("Resposta: " + resposta);

            if (resposta.equals("n")) {
                adicionarPassageiro = false;
                System.out.println("Peso máximo não excedido");
                break;
            } else if (resposta.equals("s")) {
                adicionarPassageiro = true;
                System.out.println("Peso do passageiro " + (passageiroAtual + 1) + ": ");
                int pesoPassageiro = entrada.nextInt();
                pesoMaximo -= pesoPassageiro;
                passageiroAtual++;
                System.out.println("Peso restante: " + pesoMaximo);
                System.out.println("Quantidade de passageiros: " + (passageiroAtual + 1));
            } else {
                System.out.println("Resposta inválida, digite novamente");
                continue;
            }
            if (pesoMaximo < 0) {
                System.out.println("Peso máximo excedido");
                break;
            }
        }
        entrada.close();
    }
}
