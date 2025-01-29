package br.com.arthur.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class Tabuleiro implements CampoObservador{

	private final int linhas;
	private final int colunas;
	private final int minas;
	
	private final List<Campo> campos = new ArrayList<>();
	private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();
	
	public Tabuleiro(int linhas, int colunas, int minas) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
		
		gerarCampos();
		associarOsVizinhos();
		sortearMinas();
	}

	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}

	public void paraCada(Consumer<Campo> funcao){
		campos.forEach(funcao);
	}

	public void registrarObservador(Consumer<ResultadoEvento> observador){
		observadores.add(observador);
	}

	private void notificarObservadores(boolean resultado){
		observadores.stream().forEach(o -> o.accept(new ResultadoEvento(resultado)));
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
			
			campos.get(aleatorio).minarCampo(true);
			
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
			notificarObservadores(true);
		}
	}

	private void mostrarMinas(){
		campos.stream()
				.filter(c -> c.isMinado())
				.filter(c -> !c.isMarcado())
				.forEach(c -> c.setAberto(true));
	}
}
