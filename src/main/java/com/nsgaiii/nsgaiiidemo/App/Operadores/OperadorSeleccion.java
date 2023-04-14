package com.nsgaiii.nsgaiiidemo.App.Operadores;

import java.util.ArrayList;
import java.util.List;

import com.nsgaiii.nsgaiiidemo.App.Modelo.Individuo;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Poblacion;
import com.nsgaiii.nsgaiiidemo.App.Utils.Utils;

public class OperadorSeleccion {

	public OperadorSeleccion() {
		
	}
	
	//Se eligen de forna aleatoria 2 individuos para cruzarlos
	public List<Individuo> seleccionAleatoria(Poblacion p){
		int rand1 = Utils.getRandNumber(0, p.getNumIndividuos());
		int rand2 = Utils.getRandNumber(0, p.getNumIndividuos());
		while(rand1 == rand2) {
			rand2 = Utils.getRandNumber(0, p.getNumIndividuos());
		}
		
		List<Individuo> padres = new ArrayList<>(2);
		padres.add(p.getPoblacion().get(rand1));
		padres.add(p.getPoblacion().get(rand2));
		return padres;
	}
	
	public List<Individuo> seleccionAleatoriaElitista(List<Individuo> frente){
		
		int rand1 = Utils.getRandNumber(0, frente.size());
		int rand2 = Utils.getRandNumber(0, frente.size());
		while(rand1 == rand2) {
			rand2 = Utils.getRandNumber(0, frente.size());
		}
		
		List<Individuo> padres = new ArrayList<>(2);
		padres.add(frente.get(rand1));
		padres.add(frente.get(rand2));
		return padres;
	}
	
	
	public List<Individuo> seleccionPorTorneoNSGAIII(Poblacion p) {
		List<Individuo> padres = new ArrayList<>(2);
		for(int i = 0; i < 2; i++) {
			int rand1 = Utils.getRandNumber(0, p.getNumIndividuos());
			int rand2 = Utils.getRandNumber(0, p.getNumIndividuos());
			while(rand1 == rand2) {
				rand2 = Utils.getRandNumber(0, p.getNumIndividuos());
			}
			if(!p.getPoblacion().get(rand1).isFactible() && !p.getPoblacion().get(rand2).isFactible()) {
				if(p.getPoblacion().get(rand1).getConstraintViolation() < p.getPoblacion().get(rand2).getConstraintViolation()) {
					padres.add(p.getPoblacion().get(rand1));
				} else if(p.getPoblacion().get(rand1).getConstraintViolation() > p.getPoblacion().get(rand2).getConstraintViolation()) {
					padres.add(p.getPoblacion().get(rand2));
				}else {
					if(Utils.getRandBinNumber() == 0.0) {
						padres.add(p.getPoblacion().get(rand1));
					}else {
						padres.add(p.getPoblacion().get(rand2));
					}
				}
			}else if(p.getPoblacion().get(rand1).isFactible() && !p.getPoblacion().get(rand2).isFactible()) {
				padres.add(p.getPoblacion().get(rand1));
			}else if(!p.getPoblacion().get(rand1).isFactible() && p.getPoblacion().get(rand2).isFactible()){
				padres.add(p.getPoblacion().get(rand2));
			}else{
				if(Utils.getRandBinNumber() == 0.0) {
					padres.add(p.getPoblacion().get(rand1));
				}else {
					padres.add(p.getPoblacion().get(rand2));
				}
			}
		}
		
		return padres;
	}
	
}
