package com.nsgaiii.nsgaiiidemo.App.Modelo;

import java.util.ArrayList;
import java.util.List;

public class Individuo implements Comparable<Individuo> {
	/*Cada individuo está compuesto por: 
		- Los valores de sus variables
		- Los valores de funcion objetivo
		- Los valores de funcion objetivo normalizados, se usan solo para el proceso de reemplazo
		- El número de individuos que dominan al individuo en cuestión
	*/
	
	private List<Double> variables;
	private int domina = 0;
	private List<Double> objetivos;
	private List<Double> objetivosNorm;
	private List<Double> restricciones;
	private boolean factible = true;
	private Double constraintViolation = 0.0;
	
	public Individuo(int numVariables, int numObjetivos) {
		this.variables = new ArrayList<Double>(numVariables);
		this.objetivos = new ArrayList<Double>(numObjetivos);
	}

	public List<Double> getVariables() {
		return variables;
	}

	public void setVariables(List<Double> variables) {
		this.variables = variables;
	}
	
	public void setIVariable(int posicion, Double variable) {
		this.variables.add(posicion, variable);
	}

	public List<Double> getObjetivos() {
		return objetivos;
	}

	public void setObjetivos(List<Double> objetivos) {
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
		return "Individuo [objetivos=" + objetivos + ", restricciones=" + restricciones + ", factible=" + factible
				+ ", constraintViolation=" + constraintViolation + "]";
	}

	public int getdomina() {
		return domina;
	}

	public void setdomina(int domina) {
		this.domina = domina;
	}

	public List<Double> getObjetivosNorm() {
		return objetivosNorm;
	}

	public void setObjetivosNorm(List<Double> objetivosNorm) {
		this.objetivosNorm = objetivosNorm;
	}

	public List<Double> getRestricciones() {
		return restricciones;
	}

	public void setRestricciones(List<Double> restricciones) {
		this.restricciones = restricciones;
	}

	public boolean isFactible() {
		return factible;
	}

	public void setFactible(boolean factible) {
		this.factible = factible;
	}

	public Double getConstraintViolation() {
		return constraintViolation;
	}

	public void setConstraintViolation(Double constraintViolation) {
		this.constraintViolation = constraintViolation;
	}

	//Se comparan el número de individuos que dominan a 2 individuos para asigarles un ranking
	@Override
	public int compareTo(Individuo o) {
		int compareDom = o.getdomina();
		    return this.getdomina() - compareDom;
	}

}
