package com.nsgaiii.nsgaiiidemo.App;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.nsgaiii.nsgaiiidemo.App.Lectura.LecturaDeDatos;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Individuo;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Poblacion;
import com.nsgaiii.nsgaiiidemo.App.Operadores.OperadorCruce;
import com.nsgaiii.nsgaiiidemo.App.Operadores.OperadorReemplazo;
import com.nsgaiii.nsgaiiidemo.App.Problemas.Problema;
import com.nsgaiii.nsgaiiidemo.App.Problemas.SubVuelos;
import com.nsgaiii.nsgaiiidemo.App.Problemas.Vuelos;
import com.nsgaiii.nsgaiiidemo.App.Utils.Utils;
import com.opencsv.exceptions.CsvException;

public class EvaluarIndividuo {

	public static void main(String[] args) throws Exception, IOException, CsvException {
		
		Map<List<String>, Integer> conexiones = new HashMap<>();
        Map<List<String>, Double> riesgos = new HashMap<>();
        Map<List<String>, Integer> vuelos = new HashMap<>();
        List<String> AeropuertosEspanyoles = new ArrayList<>();
        List<String> AeropuertosOrigen = new ArrayList<>();
        List<String> companyias = new ArrayList<>();
        Map<List<String>, Double> dineroMedio = new HashMap<>();
        Map<List<String>, Integer> pasajeros = new HashMap<>();
        Map<List<String>, Integer> pasajerosCompanyia = new HashMap<>();
        Map<List<String>, Integer> vuelosEntrantesConexion = new HashMap<>();
        Map<String, Integer> vuelosSalientesAEspanya = new HashMap<>();
        Map<String, Integer> vuelosSalientes = new HashMap<>();
        Map<String, Double> conectividadesAeropuertosOrigen = new HashMap<>();
        Map<String, Set<String>> listaConexionesPorAeropuertoEspanyol = new HashMap<>();
        Map<String, Set<String>> listaConexionesSalidas = new HashMap<>();
        List<Integer> indPorAeropuerto = new ArrayList<>();
    	
    	
    	LecturaDeDatos.leerDatos(conexiones, riesgos, vuelos);
    	LecturaDeDatos.leerDatosAeropuertosEspanyoles(AeropuertosEspanyoles);
    	LecturaDeDatos.leerDatosAeropuertosOrigen(AeropuertosOrigen);
    	LecturaDeDatos.leerDatosCompanyias(companyias);
    	LecturaDeDatos.leerDatosDineroMedio(dineroMedio);
    	LecturaDeDatos.leerDatosPasajeros(pasajeros);
    	LecturaDeDatos.leerDatosPasajerosCompanyia(pasajerosCompanyia);
    	LecturaDeDatos.leerDatosConectividad(vuelosEntrantesConexion, vuelosSalientesAEspanya,
    			vuelosSalientes, conectividadesAeropuertosOrigen, conexiones, AeropuertosOrigen);
    	LecturaDeDatos.leerDatosListaConexiones(listaConexionesPorAeropuertoEspanyol, AeropuertosEspanyoles, conexiones);
    	LecturaDeDatos.leerDatosListaConexionesSalidas(listaConexionesSalidas, AeropuertosOrigen, conexiones);
    	LecturaDeDatos.leerFicherosAeropuertos(AeropuertosEspanyoles, indPorAeropuerto);
    	
    	Problema problema = new SubVuelos(conexiones.keySet().size(), riesgos, conexiones, vuelos, 
    			AeropuertosEspanyoles, AeropuertosOrigen,
    			companyias, dineroMedio, pasajeros, pasajerosCompanyia,
    			vuelosEntrantesConexion, vuelosSalientesAEspanya, 
    			vuelosSalientes, conectividadesAeropuertosOrigen,
    			listaConexionesPorAeropuertoEspanyol, listaConexionesSalidas, indPorAeropuerto);
    	System.out.println(riesgos.size());
    	System.out.println(conexiones);
    	System.out.println(AeropuertosEspanyoles);
    	OperadorReemplazo reemplazo = new OperadorReemplazo(3, null);
    	
    	
    	System.out.println(indPorAeropuerto);
    	
    	for (int i = 0; i < 5; i++) {
    		Individuo indNuevo = new Individuo(problema.getNumVariables(), problema.getNumObjetivos());
        	System.out.println(problema.inicializarValores(indNuevo));
        	
    	}
    	Individuo indNuevo = new Individuo(problema.getNumVariables(), problema.getNumObjetivos());
    	/*        
    	
    	ArrayList<Individuo> lista = new ArrayList<>();
    	String antes = "problemaVuelos20230125010213.csv";
    	
    	String despues = "problemaVuelos20230125154316.csv";
    	
    	List<Individuo> frenteAntes = Utils.leerCSV(antes);
    	
    	List<Individuo> frenteDespues = Utils.leerCSV(despues);
    	
    	System.out.println("Antes: " + frenteAntes.size());
    	System.out.println("Despues: " + frenteDespues.size());
    	
    	lista = Utils.juntarListass(frenteAntes, frenteDespues);
    	
    	Poblacion poblacion = new Poblacion(lista.size(), problema);
    	poblacion.setPoblacion(null);
    	System.out.println("Total: " + lista.size());
    	
    	List<List<Individuo>> frentes = reemplazo.obtenerFrentes(lista, problema);
    	System.out.println("Frente 1: " + frentes.get(0).size());
    	System.out.println("Frente 2: " + frentes.get(1).size());
    	
    	System.out.println(frentes.get(0));
    	System.out.println(frentes.get(1));*/
    	/*
    	OperadorCruce cruce = new OperadorCruce(1, 0);
    	
    	Individuo padre1 = new Individuo(problema.getNumVariables(), problema.getNumObjetivos());
    	
    	ArrayList<Double> valores = new ArrayList<>();
    	for(int i = 0; i < problema.getNumVariables(); i++) {
    		valores.add(Utils.getRandNumber(0, 10) * 1.0);
    	}
    	
    	padre1.setVariables(valores);
    	
    	Individuo padre2 = new Individuo(problema.getNumVariables(), 3);
    	
    	ArrayList<Double> valores2 = new ArrayList<>();
    	for(int i = 0; i < problema.getNumVariables(); i++) {
    		valores2.add(Utils.getRandNumber(0, 10) * 1.0);
    	}
    	
    	padre2.setVariables(valores2);
    	
    	System.out.println(padre1);
    	System.out.println(padre2);
    	//System.out.println(problema.evaluate(padre1));
    	//System.out.println(problema.evaluate(padre2));
    	
    	System.out.println(cruce.cruceUniforme(padre1, padre2, problema));
    	*/
    	/*String s = "";
    	Poblacion poblacion = new Poblacion(1073741824 - 1, problema);
    	ArrayList<Individuo> indi = new ArrayList<>();
    	for (int i = 0; i < 1073741824; i++) {
    		String result = Integer.toBinaryString(i);
    		String resultWithPadding = String.format("%30s", result).replaceAll(" ", "0");
    		Individuo ind = new Individuo(30, 3);
    		ArrayList<Double> var  = new ArrayList<Double>(30);
    		for (int j = 0; j < 30; j++) {
    			var.add(j, Double.valueOf(resultWithPadding.charAt(j) + s));
    		}
    		ind.setVariables(var);
    		ind = problema.evaluate(ind);
    		indi.add(ind);
    	}
    	poblacion.setPoblacion(indi);
    	List<List<Individuo>> frentes = reemplazo.obtenerFrentes(poblacion, problema);
    	System.out.println(frentes.get(0).size());
    	Utils.crearCSVConObjetivos(frentes.get(0), problema.getNombre());
    	System.out.println("a");
    	*/
	}

}
