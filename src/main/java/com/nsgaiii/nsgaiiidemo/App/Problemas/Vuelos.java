package com.nsgaiii.nsgaiiidemo.App.Problemas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nsgaiii.nsgaiiidemo.App.Constantes.Constantes;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Individuo;
import com.nsgaiii.nsgaiiidemo.App.Utils.Utils;

public class Vuelos extends Problema{

	private Map<List<String>, Double> riesgos = new HashMap<>();
	private Map<List<String>, Integer> conexiones = new HashMap<>();
	private Map<List<String>, Integer> vuelos = new HashMap<>();
	
	private List<List<String>> listaConexiones;

	public Vuelos(int numVariables, Map<List<String>, Double> riesgos,
			Map<List<String>, Integer> conexiones, Map<List<String>, Integer> vuelos) {
		super(numVariables, 6);
		
		super.setNombre(Constantes.nombreProblemaVuelos);
		this.riesgos = riesgos;
		this.conexiones = conexiones;
		this.vuelos = vuelos;
		this.listaConexiones = new ArrayList<>(conexiones.keySet());
		
	}
	
	//Calcular valores de funcion objetivo
	@Override
	public Individuo evaluate(Individuo solution) {
		ArrayList<Double> objetivos = new ArrayList<>(super.getNumObjetivos());
		objetivos.add(0, calcularRiesgo(solution));
		solution.setObjetivos(objetivos);
		return solution;
	}
		 
	//Inicializar de forma aleatoria los valores de las variables según los límites
	@Override
	public Individuo inicializarValores(Individuo ind) {
		ArrayList<Double> valores = new ArrayList<>(super.getNumVariables());
		for(int i = 0; i < super.getNumVariables(); i++) {
			valores.add(i, Utils.getRandBinNumber());
		}
		ind.setVariables(valores);
		return ind;
	}
	
	private Double calcularRiesgo(Individuo solucion) {
		Double sumatorio = 0.0;
        Double sumatorioTotal = 0.0;
        List<List<String>> llaves = new ArrayList<>(riesgos.keySet());
        for (int i = 0; i < llaves.size(); i++) {
            sumatorio += riesgos.get(llaves.get(i)) * solucion.getVariables().get(i);
            sumatorioTotal += riesgos.get(llaves.get(i));
        }
        return sumatorio / sumatorioTotal;
	}

}
