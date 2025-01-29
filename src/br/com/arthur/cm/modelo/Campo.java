package br.com.arthur.cm.modelo;

import java.util.ArrayList;
import java.util.List;

public class Campo {
	private final int linha;
	private final int coluna;
	
	private boolean aberto = false;
	private boolean minado = false;
	private boolean marcado = false;
	
	private List<Campo> vizinhos = new ArrayList<>();
	private List<CampoObservador> observers = new ArrayList<>();
	
	
	Campo(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}

	public void registrarObservers(CampoObservador observador){
		observers.add(observador);
	}

	public void notificarObservadores(AcoesDoCampo evento){
		observers.stream().forEach(observador -> observador.eventoOcorreu(this, evento));
	}

	protected boolean adicionarVizinho(Campo vizinho) {
		boolean linhaDiferente = linha != vizinho.linha;
		boolean colunaDiferente = coluna != vizinho.coluna;
		boolean diagonal = linhaDiferente && colunaDiferente;
		
		int deltaLinha = Math.abs(linha - vizinho.linha);
		int deltaColuna = Math.abs(coluna - vizinho.coluna);
		int deltaGeral = deltaLinha + deltaColuna;
		
		if(deltaGeral == 1 && !diagonal) {
			vizinhos.add(vizinho);
			return true;
		}
		else if(deltaGeral == 2 && diagonal) {
			vizinhos.add(vizinho);
			return true;
		}
		else {
			return false;
		}
	}
	
	public void alternarMarcacao() {
		if(!aberto) {
			marcado = !marcado;

			if(marcado){
				notificarObservadores(AcoesDoCampo.MARCAR);
			}
			else{
				notificarObservadores(AcoesDoCampo.DESMARCAR);
			}
		}
	}
	
	
	public boolean abrirCampo() {
		if(!aberto && !marcado) {
			if(minado) {
				notificarObservadores(AcoesDoCampo.EXPLODIR);
				return true;
			}

			setAberto(true);

			if(vizinhancaSegura()) {
				vizinhos.forEach(v -> v.abrirCampo());
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean vizinhancaSegura() {
		return vizinhos.stream().noneMatch(v -> v.minado);
	}

	public boolean isMinado() {
		return this.minado;
	}
	public boolean isMarcado() {
		return this.marcado;
	}
	
	void minarCampo(boolean minado) {
		this.minado = true;
	}
	
	void setAberto(boolean aberto) {
		this.aberto = aberto;

		if(aberto){
			notificarObservadores(AcoesDoCampo.ABRIR);
		}
	}


	public boolean isAberto() {
		return this.aberto;
	}
	
	public boolean isFechado() {
		return !isAberto();
	}


	public int getLinha() {
		return linha;
	}


	public int getColuna() {
		return coluna;
	}
	
	boolean objetivoAlcancado() {
		boolean desvendado = !minado && aberto;
		boolean protegido = minado && marcado;
		
		return desvendado || protegido;
	}
	
	public int minasNaVizinhanca() {
		return (int) vizinhos.stream().filter(v -> v.minado).count();
	}
	
	void reiniciarCampo() {
		aberto = false;
		minado = false;
		marcado = false;
		notificarObservadores(AcoesDoCampo.REINICIAR);
	}
}
