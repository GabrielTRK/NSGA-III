package com.nsgaiii.nsgaiiidemo.App.Algoritmo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.nsgaiii.nsgaiiidemo.App.Modelo.Individuo;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Poblacion;
import com.nsgaiii.nsgaiiidemo.App.Modelo.ReferencePoint;
import com.nsgaiii.nsgaiiidemo.App.Operadores.OperadorCruce;
import com.nsgaiii.nsgaiiidemo.App.Operadores.OperadorMutacion;
import com.nsgaiii.nsgaiiidemo.App.Operadores.OperadorReemplazo;
import com.nsgaiii.nsgaiiidemo.App.Operadores.OperadorSeleccion;
import com.nsgaiii.nsgaiiidemo.App.Problemas.Problema;
import com.nsgaiii.nsgaiiidemo.App.Utils.Utils;
import com.opencsv.exceptions.CsvException;

public class Nsgaiii {
	
	private Poblacion poblacion;
	private List<Individuo> frenteDePareto;
	private List<Individuo> frentesAux = new ArrayList<>();
	private int numGeneraciones;
	private OperadorCruce cruce;
	private OperadorMutacion mutacion;
	private Double indiceDistrC;
	private Double indiceDistrM;
	private OperadorSeleccion seleccion;
	private OperadorReemplazo reemplazo;
	private List<ReferencePoint> referencePoints = new Vector<>();
	private Problema problema;
	private boolean elitismo;
	private int tamañoAux;
	private List<Individuo> structAux = new ArrayList<>();
	private List<Individuo> structAuxAnterior = new ArrayList<>();
	private int contIguales = 0;
	
	public Nsgaiii (int numIndividuos,
			int numGeneraciones, Double indiceDistrC, Double indiceDistrM, double probCruce,
			double probMut, int numberOfDivisions, Problema prob, boolean leerF, 
			String nombreFichero, boolean elitismo, int tamañoAux) throws FileNotFoundException, IOException, CsvException {
		
		//Inicializar problema, poblacion y operadores
		this.problema = prob;
		this.poblacion = new Poblacion(numIndividuos, this.problema);
		this.indiceDistrC = indiceDistrC;
		this.indiceDistrM = indiceDistrM;
		this.cruce = new OperadorCruce(probCruce, this.indiceDistrC);
		this.mutacion = new OperadorMutacion(probMut, this.indiceDistrM);
		this.seleccion = new OperadorSeleccion();
		(new ReferencePoint()).generateReferencePoints(referencePoints, this.problema.getNumObjetivos(), numberOfDivisions);
		this.reemplazo = new OperadorReemplazo(this.problema.getNumObjetivos(), referencePoints);
		this.numGeneraciones = numGeneraciones;
		this.elitismo = elitismo;
		this.structAux = new ArrayList<>(tamañoAux);
		this.tamañoAux = tamañoAux;
		this.poblacion.generarPoblacionInicial(this.problema, leerF, nombreFichero);

	}
	
	public List<Individuo> ejecutarNSGAIII() throws FileNotFoundException, IOException, CsvException{
		Poblacion hijos;
		
		int contadorGeneraciones = 0;
		long startTime = 0;
		long elapsedTime = 0;
		//Se mantiene en el bucle hasta que se lleguen al nº de generaciones especificado
		while (!condicionDeParadaConseguida(contadorGeneraciones, this.poblacion, elapsedTime)) {
			startTime = System.nanoTime();
			System.out.println(contadorGeneraciones);
			hijos = generarDescendientes(contadorGeneraciones); //Seleccion, cruce y mutacion
			obtenerNuevaGeneracion(hijos); //Reemplazo
			contadorGeneraciones++;
			elapsedTime = (System.nanoTime() - startTime) / 1000000000;
		}
		//this.frenteDePareto = this.reemplazo.obtenerFrentes(frentesAux, problema).get(0);
		this.frenteDePareto = this.reemplazo.obtenerFrentes(poblacion, problema).get(0);
		return this.structAux; //Devuelve el frente de pareto obtenido
	}
	
	private Poblacion generarDescendientes(int contadorGeneraciones) throws FileNotFoundException, IOException, CsvException {
		Poblacion poblacionHijos = new Poblacion(this.poblacion.getNumIndividuos(), this.problema);
		ArrayList<Individuo> totalHijos = new ArrayList<Individuo>(this.poblacion.getNumIndividuos());
		
		int contadorIndividuos = 0;
		
		while (totalHijos.size() < this.poblacion.getNumIndividuos()) {
			ArrayList<Individuo> nuevosHijos = new ArrayList<>(2);
			//Seleccion
			if(this.elitismo && contadorGeneraciones > 0) {
				nuevosHijos = this.seleccion.seleccionAleatoriaElitista(this.frentesAux);
			}else {
				nuevosHijos = this.seleccion.seleccionAleatoria(this.poblacion);
			}
			
			
			//Cruce
			nuevosHijos = this.cruce.cruceUniforme(
					nuevosHijos.get(0),
					nuevosHijos.get(1),
					this.problema);
			//Mutacion
			nuevosHijos.set(0, this.mutacion.numeroAleatorio(nuevosHijos.get(0), this.problema));
			nuevosHijos.set(1, this.mutacion.numeroAleatorio(nuevosHijos.get(1), this.problema));
			
			//Añadir hijos e incrementar el contador
			contadorIndividuos = contadorIndividuos + 2;
			if(this.poblacion.getNumIndividuos() - totalHijos.size() == 1) {
				totalHijos.add(contadorIndividuos - 2, nuevosHijos.get(0));
			}else {
				totalHijos.add(contadorIndividuos - 2, nuevosHijos.get(0));
				totalHijos.add(contadorIndividuos - 1, nuevosHijos.get(1));
			}
		}
		poblacionHijos.setPoblacion(totalHijos);
		poblacionHijos.calcularObjetivos(this.problema);
		
		
		return poblacionHijos;
	}
	
	private void obtenerNuevaGeneracion(Poblacion hijos) {
		//Reemplazo
		Poblacion total = Utils.juntarPoblaciones(this.poblacion, hijos, this.problema);
		Poblacion totalAux = total;
		this.frentesAux = this.reemplazo.obtenerFrentes(totalAux, this.problema).get(0);
		
		if(this.structAux.size() + this.frentesAux.size() <= this.tamañoAux) {
			this.structAux.addAll(this.frentesAux);
		}else {
			int restantes = this.structAux.size() + this.frentesAux.size() - this.tamañoAux;
			restantes = this.frentesAux.size() - restantes;
			List<Individuo> temp = frentesAux.subList(0, restantes);
			this.structAux.addAll(temp);
		}
		//quitar duplicados
		this.structAux = Utils.quitarDuplicados(this.structAux);
		//quitar dominados
		this.structAux = this.reemplazo.obtenerPrimerFrente(this.structAux, problema);
		if(Utils.listasIguales(structAux, structAuxAnterior)) {
			this.contIguales++;
		}
		this.structAuxAnterior = this.structAux;
		
		//Elegir grupos según el ranking y aplicar el método de Das y Dennis cuando corresponda
		this.poblacion = this.reemplazo.rellenarPoblacionConFrentes(this.poblacion,
				total, this.problema);
	}
	
	private boolean condicionDeParadaConseguida(int contadorGeneraciones, Poblacion p, long tiempo) {
		
		//Se comprueba la generación en la que se encuentra el algoritmo
		if(contIguales >= this.numGeneraciones /*|| tiempo >= 5*/) {
			return true;
		} else {
			return false;
		}
	}

}
