package com.nsgaiii.nsgaiiidemo.App.Problemas;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nsgaiii.nsgaiiidemo.App.Constantes.Constantes;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Individuo;
import com.nsgaiii.nsgaiiidemo.App.Utils.Utils;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class SubVuelos extends Vuelos{
	
	private List<Integer> indPorAeropuerto;
	Map<String, List<List<String>>> conexionesPorAeropuerto;
	

	public SubVuelos(int numVariables, List<String> AeropuertosEspanyoles, List<String> AeropuertosOrigen,
			List<String> companyias, Map<List<String>, Integer> pasajerosCompanyia, Map<List<String>, Integer> vuelosEntrantesConexion,
			Map<String, Integer> vuelosSalientesAEspanya, Map<String, Integer> vuelosSalientes,
			Map<String, Double> conectividadesAeropuertosOrigen,
			Map<String, Set<String>> listaConexionesPorAeropuertoEspanyol,
			Map<String, Set<String>> listaConexionesSalidas, List<Integer> indPorAeropuerto, 
			Map<String, List<List<String>>> conexionesPorAeropuerto, 
			List<List<String>> conexiones, List<Double> riesgos, List<Double> dineroMedio, List<Integer> pasajeros) {
		super(AeropuertosEspanyoles.size(), AeropuertosEspanyoles, AeropuertosOrigen, companyias, pasajerosCompanyia, vuelosEntrantesConexion, vuelosSalientesAEspanya, vuelosSalientes,
				conectividadesAeropuertosOrigen, listaConexionesPorAeropuertoEspanyol, listaConexionesSalidas, conexiones, riesgos, dineroMedio, pasajeros);
		this.indPorAeropuerto = indPorAeropuerto;
		this.conexionesPorAeropuerto = conexionesPorAeropuerto;
		super.setNombre(Constantes.nombreProblemaSubVuelos);
		List<Double> limInf = new ArrayList<Double>(numVariables);
		List<Double> limSup = new ArrayList<Double>(numVariables);
		for (int i = 0; i < numVariables; i++) {
			limInf.add(1.0);
			limSup.add((indPorAeropuerto.get(i) + 1) * 1.0);
		}
		super.setLimitesInferiores(limInf);
		super.setLimitesSuperiores(limSup);
	}
	
	@Override
	public Individuo evaluate(Individuo solution) throws FileNotFoundException, IOException, CsvException {
		//1. Traducir individuo con numeros reales a bits
		Individuo indTraducido = this.traducirIndividuo(solution);
		//2. Calcular obj
		solution.setObjetivos(super.evaluate(indTraducido).getObjetivos());
		return solution;
	}
	
	//Inicializar de forma aleatoria los valores de las variables según los límites
	@Override
	public Individuo inicializarValores(Individuo ind) {
		List<Double> valores = new ArrayList<>(super.getNumVariables());
		for(int i = 0; i < super.getNumVariables(); i++) {
			if(super.getIndCont() == 0) {
				valores.add(i, 1.0);
			}
			else if(super.getIndCont() == 1) {
				valores.add(i, this.indPorAeropuerto.get(i) * 1.0);
			} else {
				valores.add(i, Utils.getRandNumber(1, this.indPorAeropuerto.get(i) + 1) * 1.0);
			}
		}
		super.setIndCont(getIndCont() + 1);;
		ind.setVariables(valores);
		return ind;
	}
	
	public Individuo traducirIndividuo(Individuo solucion) throws FileNotFoundException, IOException, CsvException {
		List<Double> valores = new ArrayList<>(super.getConexiones().size());
		/*for(int i = 0; i < super.getConexiones().size(); i++) {
			valores.add(0.0);
		}*/
		
		
		for (int i = 0; i < super.getNumVariables(); i++) {
			//Encontrar en el fichero correspondiente los bits
			List<Double> valoresAeropuerto = this.encontrarBitsEnFichero(solucion.getVariables().get(i), i);
			//Buscar la conexion para indicar el 0 o 1
			valores.addAll(valoresAeropuerto);
			/*for (int j = 0; j < valoresAeropuerto.size(); j++) {
				List<String> conexion = new ArrayList<>();
				conexion = this.conexionesPorAeropuerto.get(getAeropuertosEspanyoles().get(i)).get(j);
				int posicionBitUtils = Utils.encontrarIndiceEnLista(super.getListaConexiones(), conexion);
				valores.set(posicionBitUtils, valoresAeropuerto.get(j));
			}*/
		}
		Individuo indTraducido = new Individuo(super.getConexiones().size(), getNumObjetivos());
		indTraducido.setVariables(valores);
		
		return indTraducido;
	}
	
	public List<Double> encontrarBitsEnFichero(double filaFichero, int indiceArrayAeropuertos) throws FileNotFoundException, IOException, CsvException {
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaDatos_por_aeropuerto + 
				getAeropuertosEspanyoles().get(indiceArrayAeropuertos) + "\\" + "problemaVuelos"
				+ getAeropuertosEspanyoles().get(indiceArrayAeropuertos) +
				Constantes.extensionFichero))) {
			List<String[]> r = reader.readAll();
			
			int numVariables = Integer.valueOf((r.get(0)[0]));
			
		    List<Double> Var = new ArrayList<>();
		    for (int k = 0; k < numVariables; k++) {
		    	
		    	Var.add(Double.valueOf(r.get((int)filaFichero)[k]));
		    }
		    return Var;
		}
	}
		

}
