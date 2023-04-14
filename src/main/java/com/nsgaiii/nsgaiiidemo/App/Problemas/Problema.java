package com.nsgaiii.nsgaiiidemo.App.Problemas;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.nsgaiii.nsgaiiidemo.App.Constantes.Constantes;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Individuo;
import com.nsgaiii.nsgaiiidemo.App.Utils.Utils;
import com.opencsv.exceptions.CsvException;

public class Problema {
	
	/* Un problema está compuesto de
	 	- Número de variables
	 	- Número de objetivos
	 	- Límites de cada variable 
	 	- Nombre del problema, para poder guardar los resultados en un fichero
	 	- Una lista de booleanos, 
	 	donde cada valor indica si la funcion objetivo se debe minimizar (true) o maximizar (false)
	 */
	
	private int numVariables;
	private int numObjetivos;
	private List<Double> limitesInferiores;
    private List<Double> limitesSuperiores;
    private String nombre;
    private List<Boolean> MinOMax;
	
	public Problema(int numVariables, int numObjetivos) {
		this.numObjetivos = numObjetivos;
		this.numVariables = numVariables;
		List<Double> linf = new ArrayList<>(this.numVariables);
		List<Double> lsup = new ArrayList<>(this.numVariables);
		for(int i = 0; i < this.numVariables; i++) {
			lsup.add(i, Utils.getRandNumber(1.0, 10.0));
			linf.add(i, Utils.getRandNumber(0.0, lsup.get(i)));
		}
		this.MinOMax = new ArrayList<>();
		for(int i = 0; i < this.numObjetivos; i++) {
			MinOMax.add(i, true);
		}
		this.setLimites(linf, lsup);
		this.nombre = Constantes.nombreProblemaDefecto;
		
	}

	public List<Boolean> getMinOMax() {
		return MinOMax;
	}

	public void setMinOMax(List<Boolean> minOMax) {
		MinOMax = minOMax;
	}

	public void setLimites(List<Double> inferiores, List<Double> superiores) {
		this.limitesInferiores = inferiores;
		this.limitesSuperiores = superiores;
	}

	public int getNumVariables() {
		return numVariables;
	}

	public void setNumVariables(int numVariables) {
		this.numVariables = numVariables;
	}

	public int getNumObjetivos() {
		return numObjetivos;
	}

	public void setNumObjetivos(int numObjetivos) {
		this.numObjetivos = numObjetivos;
	}
	
	public List<Double> getLimitesInferiores() {
		return limitesInferiores;
	}

	public void setLimitesInferiores(List<Double> limitesInferiores) {
		this.limitesInferiores = limitesInferiores;
	}

	public List<Double> getLimitesSuperiores() {
		return limitesSuperiores;
	}

	public void setLimitesSuperiores(List<Double> limitesSuperiores) {
		this.limitesSuperiores = limitesSuperiores;
	}
	

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	//Metodo por defecto de cálculo de objetivos
	public Individuo evaluate(Individuo solution) throws FileNotFoundException, IOException, CsvException {
		List<Double> objetivos = new ArrayList<>(this.numObjetivos);
		
		for (int i = 0; i < this.numObjetivos; i++) {
			objetivos.add(i, Utils.getRandNumber(0.0, 10.0));
		}
		solution.setObjetivos(objetivos);
		return solution;
	}
	
	//Metodo por defecto de inicialización de variables
	public Individuo inicializarValores(Individuo ind) {
		List<Double> variables = new ArrayList<>(this.numVariables);
		
		for (int i = 0; i < this.numVariables; i++) {
			variables.add(i, Utils.getRandNumber(this.limitesInferiores.get(i),
					Math.nextUp(this.limitesSuperiores.get(i))));
		}
		ind.setVariables(variables);
		return ind;
	}
	
	public Individuo comprobarRestricciones(Individuo ind) {
		return ind;
	}
	
}
