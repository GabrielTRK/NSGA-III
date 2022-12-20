package com.nsgaiii.nsgaiiidemo.App.Problemas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nsgaiii.nsgaiiidemo.App.Constantes.Constantes;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Individuo;
import com.nsgaiii.nsgaiiidemo.App.Utils.Utils;

public class Vuelos extends Problema{

	private Map<List<String>, Double> riesgos = new HashMap<>();
	private Map<List<String>, Integer> conexiones = new HashMap<>();
	private Map<List<String>, Integer> vuelos = new HashMap<>();
	

	private List<String> AeropuertosEspanyoles = new ArrayList<>();
	private List<String> AeropuertosOrigen = new ArrayList<>();
	private List<String> companyias = new ArrayList<>();
	private Map<List<String>, Double> dineroMedio = new HashMap<>();
	private Map<List<String>, Integer> pasajeros = new HashMap<>();
	private Map<List<String>, Integer> pasajerosCompanyia = new HashMap<>();
	private Map<List<String>, Integer> vuelosEntrantesConexion = new HashMap<>();
	private Map<String, Integer> vuelosSalientesAEspanya = new HashMap<>();
	private Map<String, Integer> vuelosSalientes = new HashMap<>();
	private Map<String, Double> conectividadesAeropuertosOrigen = new HashMap<>();
	private Map<String, Set<String>> listaConexionesPorAeropuertoEspanyol = new HashMap<>();
	
	private List<List<String>> listaConexiones;

	public Vuelos(int numVariables, Map<List<String>, Double> riesgos,
			Map<List<String>, Integer> conexiones, Map<List<String>, Integer> vuelos, 
			List<String> AeropuertosEspanyoles, List<String> AeropuertosOrigen, 
			List<String> companyias, Map<List<String>, Double> dineroMedio, 
			Map<List<String>, Integer> pasajeros, Map<List<String>, Integer> pasajerosCompanyia, 
			Map<List<String>, Integer> vuelosEntrantesConexion, Map<String, Integer> vuelosSalientesAEspanya, 
			Map<String, Integer> vuelosSalientes, Map<String, Double> conectividadesAeropuertosOrigen, 
			Map<String, Set<String>> listaConexionesPorAeropuertoEspanyol) {
		super(numVariables, 6);
		
		super.setNombre(Constantes.nombreProblemaVuelos);
		this.riesgos = riesgos;
		this.conexiones = conexiones;
		this.vuelos = vuelos;
		this.AeropuertosEspanyoles = AeropuertosEspanyoles;
		this.AeropuertosOrigen = AeropuertosOrigen;
		this.companyias = companyias;
		this.dineroMedio = dineroMedio;
		this.pasajeros = pasajeros;
		this.pasajerosCompanyia = pasajerosCompanyia;
		this.vuelosEntrantesConexion = vuelosEntrantesConexion;
		this.vuelosSalientesAEspanya = vuelosSalientesAEspanya;
		this.vuelosSalientes = vuelosSalientes;
		this.conectividadesAeropuertosOrigen = conectividadesAeropuertosOrigen;
		this.listaConexionesPorAeropuertoEspanyol = listaConexionesPorAeropuertoEspanyol;
		
		
		this.listaConexiones = new ArrayList<>(conexiones.keySet());
		
	}
	
	/*Calcular valores de funcion objetivo. 6 objetivos en total
	 * Lista de tamaño 6
	 * Posición 0: Riesgo
	 * Posición 1: Pasajeros perdidos
	 * Posición 2: 
	 * Posición 3: 
	 * Posición 4: 
	 * Posición 5: 
	 */
	@Override
	public Individuo evaluate(Individuo solution) {
		ArrayList<Double> objetivos = new ArrayList<>(super.getNumObjetivos());
		objetivos.add(0, calcularRiesgo(solution));
		objetivos.add(1, calcularPasajerosPerdidos(solution));
		objetivos.add(2, calcularPerdidaDeIngresos(solution));
		//TODO: Obtener las otras funciones
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
	
	//Función objetivo Riesgo
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
	
	//Función objetivo Pasajeros perdidos
	private Double calcularPasajerosPerdidos(Individuo solucion) {
		Double sumatorio = 0.0;
        Double totalPasajeros = 0.0;
        List<List<String>> llaves = new ArrayList<>(pasajeros.keySet());
        for (int i = 0; i < llaves.size(); i++) {
            sumatorio += pasajeros.get(llaves.get(i)) * solucion.getVariables().get(i);
            totalPasajeros += pasajeros.get(llaves.get(i));
        }
        Double porcentaje = 1 - sumatorio / totalPasajeros;
        return porcentaje;
	}
	
	//Función objetivo Perdida de ingresos
	private Double calcularPerdidaDeIngresos(Individuo solucion) {
		Double suma = 0.0;
        Double totalSuma = 0.0;
        List<List<String>> llaves = new ArrayList<>(conexiones.keySet());
        for (int i = 0; i < llaves.size(); i++) {
        	suma += dineroMedio.get(llaves.get(i)) * solucion.getVariables().get(i);
        	totalSuma += dineroMedio.get(llaves.get(i));
        }
        Double aux = 0.0;
        if (totalSuma != 0.0) {
            aux = suma / totalSuma;
        }
        return 1 - aux;
	}
	
	

}
