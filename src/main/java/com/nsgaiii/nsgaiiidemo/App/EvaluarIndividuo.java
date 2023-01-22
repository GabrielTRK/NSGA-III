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
    	
    	Problema problema = new Vuelos(conexiones.keySet().size(), riesgos, conexiones, vuelos, 
    			AeropuertosEspanyoles, AeropuertosOrigen,
    			companyias, dineroMedio, pasajeros, pasajerosCompanyia,
    			vuelosEntrantesConexion, vuelosSalientesAEspanya, 
    			vuelosSalientes, conectividadesAeropuertosOrigen,
    			listaConexionesPorAeropuertoEspanyol, listaConexionesSalidas);
    	System.out.println(conexiones);
    	OperadorReemplazo reemplazo = new OperadorReemplazo(3, null);
    	        
    	
    	ArrayList<Individuo> lista = new ArrayList<>();
    	String nombre = "problemaVuelos20230122202827.csv";
    	
    	String nombreSolD = "problemaVuelos20230123004342.csv";
    	
    	List<Individuo> frenteDePareto = Utils.leerCSV(nombre);
    	
    	List<Individuo> solD = Utils.leerCSV(nombreSolD);
    	
    	System.out.println("Antes: " + solD.size());
    	System.out.println("Despues: " + frenteDePareto.size());
    	
    	lista = Utils.juntarListass(frenteDePareto, solD);
    	
    	Poblacion poblacion = new Poblacion(lista.size(), problema);
    	poblacion.setPoblacion(null);
    	System.out.println("Total: " + lista.size());
    	
    	System.out.println(reemplazo.obtenerFrentes(lista, problema).get(0));
    	
    	/*OperadorCruce cruce = new OperadorCruce(1, 0);
    	
    	Individuo padre1 = new Individuo(problema.getNumVariables(), problema.getNumObjetivos());
    	
    	ArrayList<Double> valores = new ArrayList<>();
    	for(int i = 0; i < problema.getNumVariables(); i++) {
    		valores.add(0.0);
    	}
    	
    	padre1.setVariables(valores);
    	
    	Individuo padre2 = new Individuo(problema.getNumVariables(), 3);
    	
    	ArrayList<Double> valores2 = new ArrayList<>();
    	for(int i = 0; i < problema.getNumVariables(); i++) {
    		valores2.add(1.0);
    	}
    	
    	padre2.setVariables(valores2);
    	
    	System.out.println(padre1);
    	System.out.println(padre2);
    	System.out.println(problema.evaluate(padre1));
    	System.out.println(problema.evaluate(padre2));
    	
    	System.out.println(cruce.cruceDosPuntos(padre1, padre2, problema));*/
    	
		

	}

}
