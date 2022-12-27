package com.nsgaiii.nsgaiiidemo.App.Algoritmo;

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

public class Nsgaiii {
	
	private Poblacion poblacion;
	private List<Individuo> frenteDePareto;
	private int numGeneraciones;
	private OperadorCruce cruce;
	private OperadorMutacion mutacion;
	private Double indiceDistrC;
	private Double indiceDistrM;
	private OperadorSeleccion seleccion;
	private OperadorReemplazo reemplazo;
	private List<ReferencePoint> referencePoints = new Vector<>();
	private Problema problema;
	
	public Nsgaiii (int numIndividuos,
			int numGeneraciones, Double indiceDistrC, Double indiceDistrM, double probCruce,
			double probMut, int numberOfDivisions, Problema prob) {
		
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
		
		this.poblacion.generarPoblacionInicial(this.problema);
		System.out.println("a");
	}
	
	public List<Individuo> ejecutarNSGAIII(){
		Poblacion hijos;
		
		int contadorGeneraciones = 0;
		//Se mantiene en el bucle hasta que se lleguen al nº de generaciones especificado
		while (!condicionDeParadaConseguida(contadorGeneraciones, this.poblacion)) {
			System.out.println(contadorGeneraciones);
			hijos = generarDescendientes(); //Seleccion, cruce y mutacion
			obtenerNuevaGeneracion(hijos); //Reemplazo
			contadorGeneraciones++;
		}
		this.frenteDePareto = this.reemplazo.obtenerFrentes(poblacion, problema).get(0);
		return this.frenteDePareto; //Devuelve el frente de pareto obtenido
	}
	
	private Poblacion generarDescendientes() {
		Poblacion poblacionHijos = new Poblacion(this.poblacion.getNumIndividuos(), this.problema);
		ArrayList<Individuo> totalHijos = new ArrayList<Individuo>(this.poblacion.getNumIndividuos());
		
		int contadorIndividuos = 0;
		
		while (totalHijos.size() < this.poblacion.getNumIndividuos()) {
			ArrayList<Individuo> nuevosHijos = new ArrayList<>(2);
			//Seleccion
			nuevosHijos = this.seleccion.seleccionAleatoria(this.poblacion);
			
			//Cruce
			nuevosHijos = this.cruce.cruceUnPunto(
					nuevosHijos.get(0),
					nuevosHijos.get(1),
					this.problema);
			//Mutacion
			nuevosHijos.set(0, this.mutacion.cambioDeBit(nuevosHijos.get(0), this.problema));
			nuevosHijos.set(1, this.mutacion.cambioDeBit(nuevosHijos.get(1), this.problema));
			
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
		this.reemplazo.obtenerFrentes(totalAux, this.problema);
		//Elegir grupos según el ranking y aplicar el método de Das y Dennis cuando corresponda 
		this.poblacion = this.reemplazo.rellenarPoblacionConFrentes(this.poblacion,
				total, this.problema);
	}
	
	private boolean condicionDeParadaConseguida(int contadorGeneraciones, Poblacion p) {
		
		//Se comprueba la generación en la que se encuentra el algoritmo
		if(contadorGeneraciones >= this.numGeneraciones) {
			return true;
		} else {
			return false;
		}
	}

}
