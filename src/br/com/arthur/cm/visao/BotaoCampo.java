package br.com.arthur.cm.visao;

import br.com.arthur.cm.modelo.AcoesDoCampo;
import br.com.arthur.cm.modelo.Campo;
import br.com.arthur.cm.modelo.CampoObservador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BotaoCampo extends JButton implements CampoObservador, MouseListener {

    private final Color BG_PADRAO = new Color(184,184,184);
    private final Color BG_MARCADO = new Color(8,179,247);
    private final Color BG_EXPLOSAO = new Color(189,66,68);
    private final Color TEXTO_VERDE = new Color(0,100,0);

    private Campo campo;

    public BotaoCampo(Campo campo){
        this.campo = campo;
        setBackground(BG_PADRAO);
        setOpaque(true);
        setBorder(BorderFactory.createBevelBorder(0));

        addMouseListener(this);
        campo.registrarObservers(this);
    }

    @Override
    public void eventoOcorreu(Campo campo, AcoesDoCampo evento){
        switch(evento){
            case ABRIR:
                aplicarEstiloAbrir();
                break;
            case MARCAR:
                aplicarEstiloMarcar();
                break;
            case EXPLODIR:
                aplicarEstiloExplodir();
                break;
            default:
                aplicarEstiloPadrao();
        }

    }

    private void aplicarEstiloPadrao() {
        setBackground(BG_PADRAO);
        setBorder(BorderFactory.createBevelBorder(0));
        setText("");
    }

    private void aplicarEstiloExplodir() {
        setBackground(BG_EXPLOSAO);
        setText("X");
        setForeground(Color.WHITE);
    }

    private void aplicarEstiloMarcar() {
        setBackground(BG_MARCADO);
        setText("M");
        setForeground(Color.BLACK);
    }

    private void aplicarEstiloAbrir() {

        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        if(campo.isMinado()){
            setBackground(BG_EXPLOSAO);
            return;
        }

        setBackground(BG_PADRAO);

        switch(campo.minasNaVizinhanca()){
            case 1:
                setForeground(TEXTO_VERDE);
                break;
            case 2:
                setForeground(Color.BLUE);
                break;
            case 3:
                setForeground(Color.YELLOW);
                break;
            case 4:
            case 5:
            case 6:
                setForeground(Color.RED);
                break;
            default:
                setForeground(Color.PINK);
        }
        String valor = !campo.vizinhancaSegura() ? campo.minasNaVizinhanca() + "" : "";

        setText(valor);
    }

    // Implementando os metodos dou mouse Event

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == 1){
            campo.abrirCampo();
        }
        else{
            campo.alternarMarcacao();
        }
    }

    public void mouseClicked(MouseEvent e) {}

    public void mouseEntered(MouseEvent e){}

    public void mouseExited(MouseEvent e){}

    public void mouseReleased(MouseEvent e){}

}
