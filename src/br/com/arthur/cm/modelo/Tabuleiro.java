package br.com.arthur.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class Tabuleiro implements CampoObservador{

	private int linhas;
	private int colunas;
	private int minas;
	
	private final List<Campo> campos = new ArrayList<>();
	private final List<Consumer<Boolean>> observadores = new ArrayList<>();
	
	public Tabuleiro(int linhas, int colunas, int minas) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
		
		gerarCampos();
		associarOsVizinhos();
		sortearMinas();
	}

	public void registrarObservador(Consumer<Boolean> observador){
		observadores.add(observador);
	}

	private void notificarObservadores(boolean resultado){
		observadores.stream().forEach(o -> o.accept(resultado));
	}
	
	public void abrir(int linha, int coluna) {
		System.out.println("Apenas um teste");
		campos.parallelStream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
				.findFirst()
				.ifPresent(c -> c.abrirCampo());
	}
	
	public void marcar(int linha, int coluna) {
		campos.parallelStream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
		.findFirst()
		.ifPresent(c -> c.alternarMarcacao());;
	}
	private void gerarCampos() {
		for(int i = 0; i < linhas; i++) {
			for(int j = 0; j < colunas; j++) {
				Campo campo = new Campo(i, j);
				campo.registrarObservers(this);
				campos.add(campo);
			}
		}
	}
	private void associarOsVizinhos() {
		for(Campo c1 : campos) {
			for(Campo c2: campos) {
				c1.adicionarVizinho(c2);
			}
		}
	}
	
	private void sortearMinas() {
		long minasArmadas = 0;
		
		do {
			
			int aleatorio = (int) (Math.random() * campos.size());
			
			campos.get(aleatorio).minarCampo();
			
			minasArmadas = campos.stream().filter(c -> c.isMinado()).count();
			
		}while(minasArmadas < minas);
		
	}
	
	public boolean objetivoFinalAlcancado() {
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}

	public void reiniciarJogo() {
		campos.stream().forEach(c -> c.reiniciarCampo());
		sortearMinas();
	}

	@Override
	public void eventoOcorreu(Campo campo, AcoesDoCampo evento){
		if(evento == AcoesDoCampo.EXPLODIR){
			mostrarMinas();
			notificarObservadores(false);
		}
		else if(objetivoFinalAlcancado()){
			System.out.println("Voce ganhou o jogo!!!");
			notificarObservadores(true);
		}
	}

	private void mostrarMinas(){
		campos.stream()
				.filter(c -> c.isMinado())
				.forEach(c -> c.setAberto(true));
	}
}
