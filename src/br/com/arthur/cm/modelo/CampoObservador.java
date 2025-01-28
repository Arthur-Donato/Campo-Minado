package br.com.arthur.cm.modelo;

@FunctionalInterface
public interface CampoObservador {

     void eventoOcorreu(Campo campo, AcoesDoCampo evento);
}
