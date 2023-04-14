package com.nsgaiii.nsgaiiidemo.App.Operadores;

import java.util.List;

import com.nsgaiii.nsgaiiidemo.App.Modelo.Individuo;
import com.nsgaiii.nsgaiiidemo.App.Problemas.Problema;
import com.nsgaiii.nsgaiiidemo.App.Utils.Utils;

public class OperadorMutacion {
	
	//Parámetros del Operador de Mutación
	
	private double probMuta;
	
	private double indiceDistr;

	public OperadorMutacion(double probMuta, double indiceDistr) {
		super();
		this.probMuta = probMuta;
		this.indiceDistr = indiceDistr;
	}

	//Mutación polinómica
	public Individuo polyMut(Individuo solucion, Problema prob) {
		double rand, delta1, delta2, mutPow, deltaq, y, yl, yu, val, xy;
		List<Double> solucionVal = solucion.getVariables();
		
		for (int i = 0; i < prob.getNumVariables(); i++) {
			if (Utils.getRandNumber(0.0, Math.nextUp(1.0)) < this.probMuta) {
				y = solucionVal.get(i);
				yl = prob.getLimitesInferiores().get(i);
				yu = prob.getLimitesSuperiores().get(i);
				
				if (yl == yu) {
					y = yl;
			    } else {
			    	delta1 = (y - yl) / (yu - yl);
			    	delta2 = (yu - y) / (yu - yl);
			    	rand = Utils.getRandNumber(0.0,(Math.nextUp(1.0)));
			    	mutPow = 1.0 / (this.indiceDistr + 1.0);
			    	if (rand <= 0.5) {
			    		xy = 1.0 - delta1;
			    		val = 2.0 * rand + (1.0 - 2.0 * rand) * (Math.pow(xy, this.indiceDistr + 1.0));
			    		deltaq = Math.pow(val, mutPow) - 1.0;
			    	} else {
			            xy = 1.0 - delta2;
			            val = 2.0 * (1.0 - rand) + 2.0 * (rand - 0.5) * (Math.pow(xy, this.indiceDistr + 1.0));
			            deltaq = 1.0 - Math.pow(val, mutPow);
			        }
			    	y = y + deltaq * (yu - yl);
			        y = Utils.repararValorFueraDelRango(y, yl, yu);
			    }
				solucionVal.set(i, y);
			}
		}
		
		solucion.setVariables(solucionVal);
		return solucion;
	}
	
	//Mutación cambio de bit
	public Individuo cambioDeBit(Individuo solucion, Problema prob) {
		List<Double> solucionVal = solucion.getVariables();
		for (int j = 0; j < prob.getNumVariables(); j++) {
			if (Utils.getRandNumber(0.0, Math.nextUp(1.0)) < this.probMuta) {
				if (solucionVal.get(j).equals(0.0)) {
					solucionVal.set(j, 1.0);
				} else {
					solucionVal.set(j, 0.0);
				}
			}
		}
			
		solucion.setVariables(solucionVal);
		return solucion;
	}
	
	//Mutación cambio de bit
		public Individuo numeroAleatorio(Individuo solucion, Problema prob) {
			List<Double> solucionVal = solucion.getVariables();
			for (int j = 0; j < prob.getNumVariables(); j++) {
				if (Utils.getRandNumber(0.0, Math.nextUp(1.0)) < this.probMuta) {
					solucionVal.set(j, 1.0 * Utils.getRandNumber((int)Math.round(prob.getLimitesInferiores().get(j)), 
							(int)Math.round(prob.getLimitesSuperiores().get(j))));
				}
			}
				
			solucion.setVariables(solucionVal);
			
			
			return solucion;
		}
	

}
