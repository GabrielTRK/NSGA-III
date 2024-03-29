package com.nsgaiii.nsgaiiidemo.App.Modelo;

import java.util.ArrayList;

import com.nsgaiii.nsgaiiidemo.App.Problemas.Problema;


public class Poblacion {
	/*Una población está compuesta por: 
		- El número de individuos
		- Una lista de individuos
	 */
	
	private int numIndividuos;
	private ArrayList<Individuo> poblacion;
	
	public Poblacion(int numIndividuos, Problema p) {
		this.numIndividuos = numIndividuos;
		this.poblacion = new ArrayList<Individuo>(this.numIndividuos);
		
		poblacionPorDefecto(p.getNumVariables(), p.getNumObjetivos());
	}
	
	private void poblacionPorDefecto(int numValores, int numObjetivos) {
		for (int i = 0; i < this.numIndividuos; i++) {
			this.poblacion.add(i, new Individuo(numValores, numObjetivos));
		}
	}

	public int getNumIndividuos() {
		return numIndividuos;
	}
	
	
	public void setNumIndividuos(int numIndividuos) {
		this.numIndividuos = numIndividuos;
	}
	
	public void setIIndividuo(int posicion, ArrayList<Double> valores) {
		if (!poblacion.isEmpty()) {
			Individuo individuo = poblacion.get(posicion);
			individuo.setVariables(valores);
			poblacion.set(posicion, individuo);
		}
	}

	public void generarPoblacionInicial(Problema p) {
		this.obtenerValores(p);
		this.calcularObjetivos(p);
	}
	
	public void obtenerValores(Problema p) {
		for (int i = 0; i < this.numIndividuos; i++) {
			Individuo individuo = this.poblacion.get(i);
			individuo = p.inicializarValores(individuo);
			this.poblacion.set(i, individuo);
		}
	}
	
	public void calcularObjetivos(Problema p) {
		for (int i = 0; i < this.numIndividuos; i++) {
			Individuo individuo = this.poblacion.get(i);
			individuo = p.evaluate(individuo);
			this.poblacion.set(i, individuo);
		}
	}
	
	public int contarIndividuosDeUnRank(Poblacion p, int rank) {
		int contador = 0;
		for (int i = 0; i < p.getNumIndividuos(); i++) {
			if(p.getPoblacion().get(i).getdomina() == rank) {
				contador++;
			}
		}
		return contador;
	}
	
	public void añadirALaPoblacion(ArrayList<Individuo> lista) {
		for (int i = 0; i < lista.size(); i++) {
			this.poblacion.add(lista.get(i));
		}
	}
	
	
	public ArrayList<Individuo> getPoblacion() {
		return poblacion;
	}
	
	
	public void setPoblacion(ArrayList<Individuo> poblacion) {
		this.poblacion = poblacion;
	}


	@Override
	public String toString() {
		return "Poblacion [poblacion=" + poblacion + "]";
	}
	
	

}
