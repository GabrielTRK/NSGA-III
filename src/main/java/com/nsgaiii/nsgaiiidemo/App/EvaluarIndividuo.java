package com.nsgaiii.nsgaiiidemo.App;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import com.nsgaiii.nsgaiiidemo.App.Lectura.LecturaDeDatos;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Individuo;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Poblacion;
import com.nsgaiii.nsgaiiidemo.App.Modelo.ReferencePoint;
import com.nsgaiii.nsgaiiidemo.App.Operadores.OperadorCruce;
import com.nsgaiii.nsgaiiidemo.App.Operadores.OperadorReemplazo;
import com.nsgaiii.nsgaiiidemo.App.Operadores.OperadorSeleccion;
import com.nsgaiii.nsgaiiidemo.App.Problemas.Problema;
import com.nsgaiii.nsgaiiidemo.App.Problemas.SubVuelos;
import com.nsgaiii.nsgaiiidemo.App.Problemas.Vuelos;
import com.nsgaiii.nsgaiiidemo.App.Problemas.VuelosExt;
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
        List<List<String>> conexionesAMantener = new ArrayList<>();
        
        List<List<String>> conexiones2 = new ArrayList<>();
        List<Double> riesgos2 = new ArrayList<>();
        List<Integer> vuelos2 = new ArrayList<>();
        List<Double> dineroMedio2 = new ArrayList<>();
        List<Integer> pasajeros2 = new ArrayList<>();
    	
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
    	LecturaDeDatos.leerFicherosAeropuertos(AeropuertosEspanyoles, indPorAeropuerto, conexionesPorAeropuerto);
    	LecturaDeDatos.leerConexionesAMantener(conexionesAMantener);
    	
    	LecturaDeDatos.leerDatosNuevo(conexiones2, riesgos2, vuelos2);
    	LecturaDeDatos.leerDatosDineroMedioNuevo(conexiones2, dineroMedio2);
    	LecturaDeDatos.leerDatosPasajerosNuevo(conexiones2, pasajeros2);
    	
    	Problema problema = new Vuelos(conexiones.size(), riesgos, conexiones, vuelos, 
    			AeropuertosEspanyoles, AeropuertosOrigen,
    			companyias, dineroMedio, pasajeros, pasajerosCompanyia,
    			vuelosEntrantesConexion, vuelosSalientesAEspanya, 
    			vuelosSalientes, conectividadesAeropuertosOrigen,
    			listaConexionesPorAeropuertoEspanyol, listaConexionesSalidas);
    	
    	SubVuelos subproblema = new SubVuelos(AeropuertosEspanyoles.size(), riesgos, conexiones, vuelos, 
    			AeropuertosEspanyoles, AeropuertosOrigen,
    			companyias, dineroMedio, pasajeros, pasajerosCompanyia,
    			vuelosEntrantesConexion, vuelosSalientesAEspanya, 
    			vuelosSalientes, conectividadesAeropuertosOrigen,
    			listaConexionesPorAeropuertoEspanyol, listaConexionesSalidas, indPorAeropuerto, 
    			conexionesPorAeropuerto);
    	
    	Problema problemaext = new VuelosExt(conexiones.size(), riesgos, conexiones, vuelos, 
    			AeropuertosEspanyoles, AeropuertosOrigen,
    			companyias, dineroMedio, pasajeros, pasajerosCompanyia,
    			vuelosEntrantesConexion, vuelosSalientesAEspanya, 
    			vuelosSalientes, conectividadesAeropuertosOrigen,
    			listaConexionesPorAeropuertoEspanyol, listaConexionesSalidas, conexionesAMantener);
    	//System.out.println(riesgos.size());
    	//System.out.println(conexiones.keySet());
    	//System.out.println(AeropuertosEspanyoles);
    	OperadorReemplazo reemplazo = new OperadorReemplazo(3, null);
    	OperadorCruce cruce = new OperadorCruce(1, 0);
    	
    	/*List<ReferencePoint> referencePoints = new Vector<>();
    	(new ReferencePoint()).generateReferencePoints(referencePoints, 3, 8);
    	System.out.println("Num puntos: " + referencePoints.size());*/
    	
    	System.out.println(pasajeros2);
    	System.out.println(pasajeros2.size());
    	System.out.println();
    	
    	
    	//System.out.println(indPorAeropuerto);
    	Individuo sol = new Individuo(problemaext.getNumVariables(), problemaext.getNumObjetivos());
    	//System.out.println("Sol: " + sol);
    	sol = problemaext.inicializarValores(sol);
    	//sol = problemaext.inicializarValores(sol);
    	//ind = problema.inicializarValores(ind);
    	//problemaext.evaluate(sol);
    	//System.out.println(sol);
    	System.out.println(sol.getVariables().size());
    	System.out.println(problemaext.getNumVariables());
    	System.out.println(conexiones.size());
    	
    	//ArrayList<Double> variables = subproblema.traducirIndividuo(sol).getVariables();
    	//System.out.println(variables);
    	
    	
    	/*ArrayList<Double> numeros = new ArrayList<>(indPorAeropuerto.size());
    	
    	//ArrayList<Double> limInf = new ArrayList<>();
    	ArrayList<Double> limSup = new ArrayList<>();
    	
    	for (int i = 0; i < indPorAeropuerto.size(); i++) {
    		//limInf.add(0.0);
    		limSup.add(problema.getLimitesSuperiores().get(i) - 1.0);
    	}
    	
    	
    	for (int i = 0; i < indPorAeropuerto.size(); i++){
    		numeros.add(problema.getLimitesInferiores().get(i));
    	}*/
    	/*double productorio = 1.0;
    	for (int i = 0; i < indPorAeropuerto.size(); i++){
    		productorio = productorio * problema.getLimitesSuperiores().get(i);
    	}
    	System.out.println(productorio);*/
    	
    	
    	/*List<Individuo> solucionesOptimas = Utils.leerCSV("problemaSubVuelosMejoresPuntos.csv");
    	//System.out.println(solucionesOptimas);
    	if(solucionesOptimas.size() != 0) {
    		numeros = solucionesOptimas.get(solucionesOptimas.size() - 1).getVariables();
    		//System.out.println(numeros);
    		
    	}
    	
    	ArrayList<Individuo> solucionesTemporales = new ArrayList<>();
    	int cont = 0;
    	System.out.println(numeros);
    	while(!numeros.equals(limSup) && cont < 100000) {
    		Individuo sol = new Individuo(indPorAeropuerto.size(), 3);
    		sol.setVariables(numeros);
			problema.evaluate(sol);
			solucionesTemporales.add(sol);
    		int target = indPorAeropuerto.size() - 1;
    		numeros = Utils.copiarValoresDeLista(solucionesTemporales.get(cont).getVariables());
    		if(solucionesTemporales.get(cont).getVariables().get(target) < problema.getLimitesSuperiores().get(target) - 1) {
    			numeros.set(target, numeros.get(target) + 1.0);
    		}else {
    			while(solucionesTemporales.get(cont).getVariables().get(target) >= problema.getLimitesSuperiores().get(target) - 1) {
    				numeros.set(target, problema.getLimitesInferiores().get(target));
    				target--;
    			}
    			numeros.set(target, numeros.get(target) + 1.0);
    		}
    		
			
    		cont++;
    		System.out.println(cont);
    	}
    	System.out.println(cont);
    	if(cont == 100000) {
    		List<Individuo> solucionesOptimasAux = new ArrayList<>();
    		solucionesOptimasAux = Utils.juntarListass(solucionesOptimas, solucionesTemporales);
			ArrayList<Individuo> frenteDeParetoAL = Utils.pasarAArrayList(solucionesOptimasAux);
			solucionesOptimasAux = reemplazo.obtenerPrimerFrente(frenteDeParetoAL, problema);
			//solucionesOptimasAux = reemplazo.obtenerFrentes(frenteDeParetoAL, problema).get(0);
			for(Individuo i : solucionesOptimas) {
				solucionesOptimasAux.remove(i);
			}
			solucionesOptimasAux.remove(0);
			Utils.modificarCSV("problemaSubVuelosMejoresPuntos", solucionesOptimasAux);
		}*/
    	
    	
    	//String despues = "solucionDavid.csv";
    	
    	
    	
    	//List<Individuo> frenteDespues = Utils.leerCSV(despues);
    	
    	//System.out.println("Despues: " + frenteDespues.size());
    	/*ArrayList<Double> sumaP = new ArrayList<>();
    	for(int i = 0; i < frenteAntes.size(); i++) {
    		sumaP.add(Utils.sumaPonderada(frenteAntes.get(i), pesos));
    		System.out.println( i + " " + Utils.sumaPonderada(frenteAntes.get(i), pesos));
    	}
    	Collections.sort(sumaP);
    	System.out.println(sumaP.get(sumaP.size()-1));
    	System.out.println(sumaP.get(sumaP.size()-2));
    	System.out.println(sumaP.get(0));*/
    	
    	/*lista = Utils.juntarListass(frenteAntes, frenteDespues);
    	
    	
    	System.out.println("Total: " + lista.size());
    	
    	List<Individuo> frentes = reemplazo.obtenerPrimerFrente(lista, problema);
    	for (int i = 0; i < frentes.size(); i++) {
    		System.out.println("Frente " + i + ": " + frentes.get(i).size());
    		System.out.println(frentes.get(i));
    	}
    	
    	
    	//System.out.println(frentes.get(0).size());
    	Utils.crearCSVConObjetivos(frentes, problema.getNombre());*/
    	
    	/*String s = "";
    	ArrayList<Individuo> indi = new ArrayList<>();
    	for (int i = 0; i < 134217728; i++) {
    		System.out.println(i);
    		String result = Integer.toBinaryString(i);
    		String resultWithPadding = String.format("%27s", result).replaceAll(" ", "0");
    		Individuo ind = new Individuo(27, 3);
    		ArrayList<Double> var  = new ArrayList<Double>(19);
    		for (int j = 0; j < 27; j++) {
    			var.add(j, Double.valueOf(resultWithPadding.charAt(j) + s));
    		}
    		ind.setVariables(var);
    		ind = problema.evaluate(ind);
    		indi.add(ind);
    	}
    	List<Individuo> frentes = reemplazo.obtenerPrimerFrente(indi, problema);
    	System.out.println(frentes.size());
    	Utils.crearCSVConObjetivos(frentes, problema.getNombre());
    	System.out.println("a");*/
    	
	}

}
