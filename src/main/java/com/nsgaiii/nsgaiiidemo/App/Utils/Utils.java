package com.nsgaiii.nsgaiiidemo.App.Utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.nsgaiii.nsgaiiidemo.App.Constantes.Constantes;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Individuo;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Poblacion;
import com.nsgaiii.nsgaiiidemo.App.Problemas.Problema;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

public class Utils {
	
	public static Double getRandNumber(Double min, Double max) {
		return (Double) ((Math.random() * (max - min)) + min);
	}
	
	public static int getRandNumber(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}
	
	public static boolean isProbValid (Double prob) {
		if ((prob < 0.0) || (prob > 1.0)) {
		      return false;
		}
		else
			return true;
	}
	
	public static Double repararValorFueraDelRango(Double valor, Double min, Double max) {
		if(valor < min) {
			return min;
		}
		else if (valor > max) {
			return max;
		} else {
			return valor;
		}
	}
	
	public static Poblacion juntarPoblaciones (Poblacion padres, Poblacion hijos, Problema problema) {
		ArrayList<Individuo> lista = new ArrayList<>(2 * padres.getNumIndividuos());
		for (int i = 0; i < padres.getNumIndividuos(); i++) {
			lista.add(padres.getPoblacion().get(i));
		}
		for (int i = 0; i < padres.getNumIndividuos(); i++) {
			lista.add(hijos.getPoblacion().get(i));
		}
		Poblacion total = new Poblacion(2 * padres.getNumIndividuos(), problema);
		total.setPoblacion(lista);
		return total;
	}
	
	public static List<Individuo> obtenerFrenteConIndice(Poblacion p, int pos){
		List<Individuo> frente = new ArrayList<>();
		frente.add(p.getPoblacion().get(pos));
		pos++;
		while( pos < p.getPoblacion().size()) {
			if(frente.get(0).getdomina() == p.getPoblacion().get(pos).getdomina()) {
				frente.add(p.getPoblacion().get(pos));
			}
			pos++;
		}
		return frente;
	}
	
	public static Poblacion borrarElementosDeLista(List<Individuo> lista, Poblacion p) {
		ArrayList<Individuo> poblacionABorrar = p.getPoblacion();
		for(int i = 0; i < lista.size(); i++) {
			Individuo ind = lista.get(i);
			poblacionABorrar.remove(ind);
		}
		p.setPoblacion(poblacionABorrar);
		return p;
	}
	
	public static ArrayList<Individuo> juntarListas (ArrayList<Individuo> Alista, List<Individuo> frente){
		for(Individuo i : frente) {
			Alista.add(i);
		}
		return Alista;
	}
	
	public static ArrayList<Double> inicializarLista (int tamaño){
		ArrayList<Double> lista = new ArrayList<>();
		for (int i = 0; i < tamaño; i++) {
			lista.add(0.0);
		}
		return lista;
	}
	
	public static int nextInt(int lower, int upper) {
		Random rand = new Random();
		return lower + rand.nextInt((upper - lower + 1)) ;
	}
	
	public static ArrayList<Double> ArraytoArrayList(double[] array){
		ArrayList<Double> list = new ArrayList<>(array.length);
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}
	
	public static void imprimirFrente(List<Individuo> lista) {
		for (int i = 0; i < lista.size(); i++) {
			System.out.println(lista.get(i));
		}
	}
	
	public static ArrayList<Double> mediaVariables (List<Individuo> frente){
		ArrayList<Double> medias = new ArrayList<>();
		for(int j = 0; j < frente.get(0).getVariables().size(); j++) {
			medias.add(0.0);
		}
		
		for (int i = 0; i < frente.size(); i++) {
			Individuo ind = frente.get(i);
			for(int j = 0; j < ind.getVariables().size(); j++) {
				medias.set(j, medias.get(j) + ind.getVariables().get(j));
			}
		}
		
		for(int j = 0; j < frente.get(0).getVariables().size(); j++) {
			medias.set(j, medias.get(j) / frente.size());
		}
		return medias;
	}
	
	public static ArrayList<Double> mediaObjetivos (List<Individuo> frente){
		ArrayList<Double> medias = new ArrayList<>();
		for(int j = 0; j < frente.get(0).getObjetivos().size(); j++) {
			medias.add(0.0);
		}
		
		for (int i = 0; i < frente.size(); i++) {
			Individuo ind = frente.get(i);
			for(int j = 0; j < ind.getObjetivos().size(); j++) {
				medias.set(j, medias.get(j) + ind.getObjetivos().get(j));
			}
		}
		
		for(int j = 0; j < frente.get(0).getObjetivos().size(); j++) {
			medias.set(j, medias.get(j) / frente.size());
		}
		return medias;
	}
	
	public static String crearCSVConObjetivos(List<Individuo> frente, String nombreProblema) throws IOException {
		List<String[]> lista = new ArrayList<>();
		
		String[] Cabecera = new String[2];
		Cabecera[0] = String.valueOf(frente.get(0).getVariables().size());
		Cabecera[1] = String.valueOf(frente.get(0).getObjetivos().size());
		
		lista.add(Cabecera);
		
		for(int i = 0; i < frente.size(); i++) {
			Individuo ind = frente.get(i);
			String[] VariablesYObjetivos = new String[ind.getVariables().size()
			                                          + ind.getObjetivos().size()];
			
			for(int j = 0; j < ind.getVariables().size(); j++) {
				VariablesYObjetivos[j] = String.valueOf(ind.getVariables().get(j));
			}
			for(int k = ind.getVariables().size(); k < ind.getVariables().size() + ind.getObjetivos().size(); k++) {
				VariablesYObjetivos[k] = String.valueOf(ind.getObjetivos().get(k - ind.getVariables().size()));
			}
			lista.add(VariablesYObjetivos);
		}
		
		Date date = new Date();
		String fileName = nombreProblema + Constantes.formatoFecha.format(date) + Constantes.extensionFichero;
		try (CSVWriter writer = new CSVWriter(new FileWriter(Constantes.rutaFicheros + fileName))) {
	            writer.writeAll(lista);
		}
		return fileName;
	}
	
	public static List<Individuo> leerCSV(String nombre) throws FileNotFoundException, IOException, CsvException {
		try (CSVReader reader = new CSVReader(new FileReader(Constantes.rutaFicheros + nombre))) {
			List<String[]> r = reader.readAll();
			List<Individuo> frente = new ArrayList<>();
			
			int numVariables = Integer.valueOf((r.get(0)[0]));
			int numObjetivos = Integer.valueOf((r.get(0)[1]));
			
			for(int i = 1; i < r.size(); i++) {
		    	Individuo ind = new Individuo(numVariables, numObjetivos);
		    	ArrayList<Double> Fobj = new ArrayList<>();
		    	for (int j = numVariables; j < r.get(i).length; j++) {
		    		Fobj.add(Double.valueOf(r.get(i)[j]));
		    	}
		    	ind.setObjetivos(Fobj);
		    	frente.add(ind);
		    }
		    return frente;
		}
	}

}
