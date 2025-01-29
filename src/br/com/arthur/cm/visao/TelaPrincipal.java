package br.com.arthur.cm.visao;

import br.com.arthur.cm.modelo.Tabuleiro;

import javax.swing.*;
import javax.swing.plaf.basic.BasicLookAndFeel;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal(){
        Tabuleiro tabuleiro = new Tabuleiro(16,30,1);
        add(new PainelTabuleiro(tabuleiro));


        setTitle("Campo Minado");
        setSize(690,438);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setVisible(true);
    }

    public static void main(String[] args) {

        new TelaPrincipal();
    }

}
