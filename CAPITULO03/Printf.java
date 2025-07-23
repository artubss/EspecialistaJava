public class Printf {
    public static void main (String[] args) {

        String nome1 = "Arthur";
        System.out.printf("Olá, %s!%n", nome1);

        Double pesoCarga1 = 1033.49;
        System.out.printf("Peso da carga 1: %.2f kg%n", pesoCarga1);

        int quantidade = 48;
        System.out.printf("Quantidade de itens: %d%n", quantidade);

        double altura = 1.75;
        System.out.printf("Altura: %f m%n", altura);

        double pesoCarga2 = 849.88;
        System.out.printf("Peso da carga 2: %10.2f%n", pesoCarga2);
    }

}