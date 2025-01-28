package br.com.arthur.cm.visao;

import br.com.arthur.cm.modelo.Tabuleiro;

public class Temp {
    public static void main(String[] args) {

        Tabuleiro tabuleiro = new Tabuleiro(3,3,9);
        tabuleiro.registrarObservador(e -> {
            if(e){
                System.out.println("Voce ganhou!");
            }
            else{
                System.out.println("Voce perdeu!");
            }
        });
        tabuleiro.abrir(2,2);
    }
}
