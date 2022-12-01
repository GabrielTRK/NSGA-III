package com.nsgaiii.nsgaiiidemo.App.Problemas;

import java.util.ArrayList;

import com.nsgaiii.nsgaiiidemo.App.Constantes.Constantes;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Individuo;
import com.nsgaiii.nsgaiiidemo.App.Utils.Utils;

public class DTLZ5 extends Problema{
	
	//Inicializar parámetros utilizando la superclase
	
	public DTLZ5(Integer numberOfVariables, Integer numberOfObjectives) {
		super(numberOfVariables, numberOfObjectives);
		

		ArrayList<Double> lowerLimit = new ArrayList<Double>(super.getNumVariables());
		ArrayList<Double> upperLimit = new ArrayList<Double>(super.getNumVariables());

		for (int i = 0; i < super.getNumVariables(); i++) {
			lowerLimit.add(i, 0.0);
			upperLimit.add(i, Math.nextUp(1.0));
		}
		super.setLimites(lowerLimit, upperLimit);
		super.setNombre(Constantes.nombreDTLZ5);

	}
	
	//Calcular valores de funcion objetivo
	@Override
	public Individuo evaluate(Individuo solution) {
		int numberOfVariables = super.getNumVariables();
		int numberOfObjectives = super.getNumObjetivos();
		double[] theta = new double[numberOfObjectives - 1];
		double g = 0.0;

		double[] f = new double[numberOfObjectives];
		double[] x = new double[numberOfVariables];

		int k = numberOfVariables - numberOfObjectives + 1;

		for (int i = 0; i < numberOfVariables; i++) {
			x[i] = solution.getVariables().get(i);
		}

		for (int i = numberOfVariables - k; i < numberOfVariables; i++) {
			g += (x[i] - 0.5) * (x[i] - 0.5);
		}

		double t = java.lang.Math.PI / (4.0 * (1.0 + g));

		theta[0] = x[0] * java.lang.Math.PI / 2.0;
		for (int i = 1; i < (numberOfObjectives - 1); i++) {
			theta[i] = t * (1.0 + 2.0 * g * x[i]);
		}

		for (int i = 0; i < numberOfObjectives; i++) {
			f[i] = 1.0 + g;
		}

		for (int i = 0; i < numberOfObjectives; i++) {
			for (int j = 0; j < numberOfObjectives - (i + 1); j++) {
				f[i] *= java.lang.Math.cos(theta[j]);
			}
			if (i != 0) {
				int aux = numberOfObjectives - (i + 1);
				f[i] *= java.lang.Math.sin(theta[aux]);
			}
		}
		
		solution.setObjetivos(Utils.ArraytoArrayList(f));

		return solution;
	}
	
	//Inicializar de forma aleatoria los valores de las variables según los límites
	@Override
	public Individuo inicializarValores(Individuo ind) {
		ArrayList<Double> valores = new ArrayList<>(super.getNumVariables());
		for(int i = 0; i < super.getNumVariables(); i++) {
			valores.add(i, Utils.getRandNumber(super.getLimitesInferiores().get(i),
			super.getLimitesSuperiores().get(i)));
		}
		ind.setVariables(valores);
		return ind;
	}

}
