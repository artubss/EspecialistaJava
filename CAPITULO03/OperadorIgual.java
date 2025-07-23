public class OperadorIgual {
    public static void main (String [] args) {
        int numero1 = 10;
        int numero2 = 11;

        //boolean numerosIguais = numero1 == 10;
        boolean numerosIguais = numero1 == numero2;

        //boolean numerosDiferentes = !(numero1 == numero2);
        boolean numerosDiferentes = numero1 != numero2;

        System.out.printf("Números iguais: %b%n", numerosIguais);
        System.out.printf("Números diferentes: %b%n", numerosDiferentes);

        String nome1 = "Thiago";
        String nome2 = "João";

        boolean nomeIguais = (nome1 == null ? nome2 == null : nome1.equals(nome2));
        boolean nomeDiferentes = nome1 != nome2;
        
        System.out.printf("Nomes iguais: %b%n", nomeIguais);
        System.out.printf("Nomes diferentes: %b%n", nomeDiferentes);
    }
}