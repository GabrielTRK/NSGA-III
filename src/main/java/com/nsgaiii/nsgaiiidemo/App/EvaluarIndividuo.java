package com.nsgaiii.nsgaiiidemo.App;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nsgaiii.nsgaiiidemo.App.Lectura.LecturaDeDatos;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Individuo;
import com.nsgaiii.nsgaiiidemo.App.Problemas.Problema;
import com.nsgaiii.nsgaiiidemo.App.Problemas.SubVuelos;
import com.nsgaiii.nsgaiiidemo.App.Problemas.Vuelos;
import com.nsgaiii.nsgaiiidemo.App.Problemas.VuelosExt;
import com.nsgaiii.nsgaiiidemo.App.Utils.Utils;
import com.opencsv.exceptions.CsvException;

public class EvaluarIndividuo {

	public static void main(String[] args) throws Exception, IOException, CsvException {
		
        List<String> AeropuertosEspanyoles = new ArrayList<>();
        List<String> AeropuertosOrigen = new ArrayList<>();
        List<String> companyias = new ArrayList<>();
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
        
        List<List<String>> conexiones = new ArrayList<>();
        List<Double> riesgos = new ArrayList<>();
        List<Integer> vuelos = new ArrayList<>();
        List<Double> dineroMedio = new ArrayList<>();
        List<Integer> pasajeros = new ArrayList<>();
    	
        LecturaDeDatos.leerDatosNuevo(conexiones, riesgos, vuelos);
    	LecturaDeDatos.leerDatosDineroMedioNuevo(conexiones, dineroMedio);
    	LecturaDeDatos.leerDatosPasajerosNuevo(conexiones, pasajeros);
    	LecturaDeDatos.leerDatosAeropuertosEspanyoles(AeropuertosEspanyoles);
    	LecturaDeDatos.leerDatosAeropuertosOrigen(AeropuertosOrigen);
    	LecturaDeDatos.leerDatosCompanyias(companyias);
    	LecturaDeDatos.leerDatosPasajerosCompanyia(pasajerosCompanyia);
    	LecturaDeDatos.leerDatosConectividad(vuelosEntrantesConexion, vuelosSalientesAEspanya,
    			vuelosSalientes, conectividadesAeropuertosOrigen, conexiones, AeropuertosOrigen);
    	LecturaDeDatos.leerDatosListaConexiones(listaConexionesPorAeropuertoEspanyol, AeropuertosEspanyoles, conexiones);
    	LecturaDeDatos.leerDatosListaConexionesSalidas(listaConexionesSalidas, AeropuertosOrigen, conexiones);
    	LecturaDeDatos.leerFicherosAeropuertos(AeropuertosEspanyoles, indPorAeropuerto, conexionesPorAeropuerto);
    	LecturaDeDatos.leerConexionesAMantener(conexionesAMantener);
    	
    	SubVuelos subproblema = new SubVuelos(AeropuertosEspanyoles.size(), AeropuertosEspanyoles, AeropuertosOrigen,
    			companyias, pasajerosCompanyia,
    			vuelosEntrantesConexion, vuelosSalientesAEspanya, 
    			vuelosSalientes, conectividadesAeropuertosOrigen,
    			listaConexionesPorAeropuertoEspanyol, listaConexionesSalidas, indPorAeropuerto, 
    			conexionesPorAeropuerto, conexiones, riesgos, dineroMedio, pasajeros);
    	
    	List<Individuo> solucionesOptimas = Utils.leerCSV("problemaSubVuelosFrente.csv");
    	
    	System.out.println(subproblema.evaluate2(solucionesOptimas.get(8)));
    	System.out.println(subproblema.evaluate(solucionesOptimas.get(8)).getObjetivos());
    	
    	
    	
	}

}
