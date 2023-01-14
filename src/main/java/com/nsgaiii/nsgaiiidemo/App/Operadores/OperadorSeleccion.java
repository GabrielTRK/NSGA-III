package com.nsgaiii.nsgaiiidemo.App.Operadores;

import java.util.ArrayList;

import com.nsgaiii.nsgaiiidemo.App.Modelo.Individuo;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Poblacion;
import com.nsgaiii.nsgaiiidemo.App.Utils.Utils;

public class OperadorSeleccion {

	public OperadorSeleccion() {
		
	}
	
	//Se eligen de forna aleatoria 2 individuos para cruzarlos
	public ArrayList<Individuo> seleccionAleatoria(Poblacion p){
		int rand1 = Utils.getRandNumber(0, p.getNumIndividuos());
		int rand2 = Utils.getRandNumber(0, p.getNumIndividuos());
		while(rand1 == rand2) {
			System.out.println("Padres iguales");
			rand2 = Utils.getRandNumber(0, p.getNumIndividuos());
		}
		
		ArrayList<Individuo> padres = new ArrayList<>(2);
		padres.add(p.getPoblacion().get(rand1));
		padres.add(p.getPoblacion().get(rand2));
		return padres;
	}
	
	
	public void seleccionPorTorneo() {
		
	}
	
}
