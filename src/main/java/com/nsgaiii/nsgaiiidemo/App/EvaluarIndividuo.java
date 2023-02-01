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
        Map<String, List<List<String>>> conexionesPorAeropuerto = new HashMap<>();
    	
    	
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
    	//LecturaDeDatos.leerFicherosAeropuertos(AeropuertosEspanyoles, indPorAeropuerto, conexionesPorAeropuerto);
    	
    	Problema problema = new Vuelos(AeropuertosEspanyoles.size(), riesgos, conexiones, vuelos, 
    			AeropuertosEspanyoles, AeropuertosOrigen,
    			companyias, dineroMedio, pasajeros, pasajerosCompanyia,
    			vuelosEntrantesConexion, vuelosSalientesAEspanya, 
    			vuelosSalientes, conectividadesAeropuertosOrigen,
    			listaConexionesPorAeropuertoEspanyol, listaConexionesSalidas);
    	
    	/*Problema problema = new SubVuelos(AeropuertosEspanyoles.size(), riesgos, conexiones, vuelos, 
    			AeropuertosEspanyoles, AeropuertosOrigen,
    			companyias, dineroMedio, pasajeros, pasajerosCompanyia,
    			vuelosEntrantesConexion, vuelosSalientesAEspanya, 
    			vuelosSalientes, conectividadesAeropuertosOrigen,
    			listaConexionesPorAeropuertoEspanyol, listaConexionesSalidas, indPorAeropuerto, 
    			conexionesPorAeropuerto);*/
    	//System.out.println(riesgos.size());
    	System.out.println(conexiones.keySet());
    	System.out.println(AeropuertosEspanyoles);
    	OperadorReemplazo reemplazo = new OperadorReemplazo(3, null);
    	
    	
    	System.out.println(indPorAeropuerto);
    	Individuo sol = new Individuo(indPorAeropuerto.size(), 3);
    	sol = problema.inicializarValores(sol);
    	sol = problema.inicializarValores(sol);
    	//ind = problema.inicializarValores(ind);
    	System.out.println(problema.evaluate(sol));
    	
    	/*ArrayList<Individuo> lista = new ArrayList<>();
    	String antes = "problemaVuelos20230125154316.csv";
    	
    	String despues = "problemaSubVuelos20230130182709.csv";
    	
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
    	
    	String s = "";
    	Poblacion poblacion = new Poblacion(8388608 - 1, problema);
    	ArrayList<Individuo> indi = new ArrayList<>();
    	for (int i = 0; i < 8388608; i++) {
    		System.out.println(i);
    		String result = Integer.toBinaryString(i);
    		String resultWithPadding = String.format("%23s", result).replaceAll(" ", "0");
    		Individuo ind = new Individuo(23, 3);
    		ArrayList<Double> var  = new ArrayList<Double>(23);
    		for (int j = 0; j < 23; j++) {
    			var.add(j, Double.valueOf(resultWithPadding.charAt(j) + s));
    		}
    		ind.setVariables(var);
    		ind = problema.evaluate(ind);
    		indi.add(ind);
    	}
    	poblacion.setPoblacion(indi);
    	List<Individuo> frentes = reemplazo.obtenerPrimerFrente(poblacion, problema);
    	System.out.println(frentes.size());
    	Utils.crearCSVConObjetivos(frentes, problema.getNombre());
    	System.out.println("a");
    	
	}

}
