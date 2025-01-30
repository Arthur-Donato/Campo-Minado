package br.com.arthur.cm.visao;

import br.com.arthur.cm.modelo.Tabuleiro;

import javax.swing.*;
import javax.swing.plaf.basic.BasicLookAndFeel;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal(){
    	
    	String[] dificuldades = {"Fácil", "Médio", "Difícil"};
    	
    	var selection = JOptionPane.showOptionDialog(null, "Selecione um modo de jogo", "Campo Minado", 0, 3, null, dificuldades, dificuldades[0]);
    
    	if(selection == 0) {
    		add(new PainelTabuleiro(new Tabuleiro(16,30,25)));
    	}
    	else if(selection == 1) {
    		add(new PainelTabuleiro(new Tabuleiro(16,30,35)));
    	}
    	else {
    		add(new PainelTabuleiro(new Tabuleiro(16,30,50)));
    	}

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
