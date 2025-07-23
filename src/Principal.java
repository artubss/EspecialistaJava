public class Principal {
    public static void main(String[] args) {
        Carro meuCarro;
        meuCarro = new Carro("Ford", "Mustang", "vermelho", 2024, 100000);
        meuCarro.exibirDetalhes();
        Carro seuCarro;
        seuCarro = new Carro("Chevrolet", "Camaro", "Azul", 2023, 150000);
        seuCarro.exibirDetalhes();
    }
}