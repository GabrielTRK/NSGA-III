package com.nsgaiii.nsgaiiidemo.App.Operadores;

import java.util.ArrayList;

import com.nsgaiii.nsgaiiidemo.App.Modelo.Individuo;
import com.nsgaiii.nsgaiiidemo.App.Problemas.Problema;
import com.nsgaiii.nsgaiiidemo.App.Utils.Utils;

public class OperadorCruce {
	
	//Parámetros del Operador de Cruce
	
	private double probCruce;
	
	private double indiceDistr;
	
	private double EPS = 1.0e-14;
	
	
	public OperadorCruce (double probCruce, double indiceDistr) {
		this.indiceDistr = indiceDistr;
		this.probCruce = probCruce;
	}
	
	//Simulated Binary Crossover(SBX)
	public ArrayList<Individuo> SBX (Individuo padre1, Individuo padre2, Problema prob){
		Individuo hijo1 = new Individuo(prob.getNumVariables(), prob.getNumObjetivos());
		Individuo hijo2 = new Individuo(prob.getNumVariables(), prob.getNumObjetivos());
		ArrayList<Individuo> hijos = new ArrayList<Individuo>(2);
		hijos.add(padre1);
		hijos.add(padre2);
		double valorP1, valorP2, y1, y2, min, max, rand, beta, alpha, betaq, c1, c2;
		
		if (Utils.isProbValid(this.probCruce) && Utils.getRandNumber(0.0, Math.nextUp(1.0)) <= this.probCruce) {
			
			for (int i = 0; i < prob.getNumVariables(); i++) {
				valorP1 = padre1.getVariables().get(i);
				valorP2 = padre2.getVariables().get(i);
				if (Utils.getRandNumber(0.0, Math.nextUp(0.5)) <= 0.5) {
					if (Math.abs(valorP1 - valorP2) > EPS) {
						if (valorP1 < valorP2) {
							y1 = valorP1;
							y2 = valorP2;
						} else {
							y1 = valorP2;
							y2 = valorP1;
						}
						min = prob.getLimitesInferiores().get(i);
						max = prob.getLimitesSuperiores().get(i);
						
						rand = Utils.getRandNumber(0.0, Math.nextUp(1.0));
						beta = 1.0 + (2.0 * (y1 - min) / (y2 - y1));
						alpha = 2.0 - Math.pow(beta, -(this.indiceDistr + 1.0));
							
						if (rand <= (1.0 / alpha)) {
							betaq = Math.pow(rand * alpha, (1.0 / (this.indiceDistr + 1.0)));
						} else {
							betaq = Math.pow(1.0 / (2.0 - rand * alpha), 1.0 / (this.indiceDistr + 1.0));
						}
						c1 = 0.5 * (y1 + y2 - betaq * (y2 - y1));
							
						beta = 1.0 + (2.0 * (max - y2) / (y2 - y1));
						alpha = 2.0 - Math.pow(beta, -(this.indiceDistr + 1.0));
							
						if (rand <= (1.0 / alpha)) {
							betaq = Math.pow((rand * alpha), (1.0 / (this.indiceDistr + 1.0)));
					    } else {
					    	betaq = Math.pow(1.0 / (2.0 - rand * alpha), 1.0 / (this.indiceDistr + 1.0));
					    }
						c2 = 0.5 * (y1 + y2 + betaq * (y2 - y1));
							
						c1 = Utils.repararValorFueraDelRango(c1, min, max);
				        c2 = Utils.repararValorFueraDelRango(c2, min, max);
				        
				        if (Utils.getRandNumber(0.0, Math.nextUp(0.5)) <= 0.5) {
				        	hijo1.setIVariable(i, c2);
				            hijo2.setIVariable(i, c1);
				        } else {
				            hijo1.setIVariable(i, c1);
				            hijo2.setIVariable(i, c2);
				        }
					} else {
						hijo1.setIVariable(i, valorP1);
			            hijo2.setIVariable(i, valorP2);
					}
				} else {
					hijo1.setIVariable(i, valorP2);
		            hijo2.setIVariable(i, valorP1);
				}
			}
			hijos.set(0, hijo1);
			hijos.set(1, hijo2);
		}
		
		
		return hijos;
	}

}
