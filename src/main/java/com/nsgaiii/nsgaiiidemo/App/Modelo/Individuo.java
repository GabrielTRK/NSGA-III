package com.nsgaiii.nsgaiiidemo.App.Modelo;

import java.util.ArrayList;

public class Individuo implements Comparable<Individuo> {
	/*Cada individuo está compuesto por: 
		- Los valores de sus variables
		- Los valores de funcion objetivo
		- Los valores de funcion objetivo normalizados, se usan solo para el proceso de reemplazo
		- El número de individuos que dominan al individuo en cuestión
	*/
	
	private ArrayList<Double> variables;
	private int domina = 0;
	private ArrayList<Double> objetivos;
	private ArrayList<Double> objetivosNorm;
	
	public Individuo(int numVariables, int numObjetivos) {
		this.variables = new ArrayList<Double>(numVariables);
		this.objetivos = new ArrayList<Double>(numObjetivos);
	}

	public ArrayList<Double> getVariables() {
		return variables;
	}

	public void setVariables(ArrayList<Double> variables) {
		this.variables = variables;
	}
	
	public void setIVariable(int posicion, Double variable) {
		this.variables.add(posicion, variable);
	}

	public ArrayList<Double> getObjetivos() {
		return objetivos;
	}

	public void setObjetivos(ArrayList<Double> objetivos) {
		this.objetivos = objetivos;
	}
	
	public void addIObjetivo(int pos, Double obj) {
		this.objetivos.add(pos, obj);
	}
	
	public void setIObjetivo(int pos, Double obj) {
		this.objetivos.set(pos, obj);
	}

	@Override
	public String toString() {
		return "Individuo [variables=" + variables + ", objetivos=" + objetivos + "]";
	}

	public int getdomina() {
		return domina;
	}

	public void setdomina(int domina) {
		this.domina = domina;
	}

	public ArrayList<Double> getObjetivosNorm() {
		return objetivosNorm;
	}

	public void setObjetivosNorm(ArrayList<Double> objetivosNorm) {
		this.objetivosNorm = objetivosNorm;
	}

	//Se comparan el número de individuos que dominan a 2 individuos para asigarles un ranking
	@Override
	public int compareTo(Individuo o) {
		int compareDom = o.getdomina();
		    return this.getdomina() - compareDom;
	}

}
