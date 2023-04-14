package com.nsgaiii.nsgaiiidemo.App.Problemas;

import java.util.ArrayList;
import java.util.List;

import com.nsgaiii.nsgaiiidemo.App.Constantes.Constantes;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Individuo;
import com.nsgaiii.nsgaiiidemo.App.Utils.Utils;

public class DTLZ7 extends Problema{

	//Inicializar parámetros utilizando la superclase
	public DTLZ7(int numVariables, int numObjetivos) {
		super(numVariables, numObjetivos);

		List<Double> lowerLimit = new ArrayList<Double>(super.getNumVariables());
		List<Double> upperLimit = new ArrayList<Double>(super.getNumVariables());

	    for (int i = 0; i < super.getNumVariables(); i++) {
	      lowerLimit.add(0.0);
	      upperLimit.add(1.0);
	    }

	    super.setLimites(lowerLimit, upperLimit);
		super.setNombre(Constantes.nombreDTLZ7);
	}
	
	//Calcular valores de funcion objetivo
	@Override
	public Individuo evaluate(Individuo solution) {
		int numberOfVariables = super.getNumVariables();
		int numberOfObjectives = super.getNumObjetivos();

		double[] f = new double[numberOfObjectives];
		double[] x = new double[numberOfVariables];

		int k = numberOfVariables - numberOfObjectives + 1;

		for (int i = 0; i < numberOfVariables; i++) {
			x[i] = solution.getVariables().get(i);
		}

		double g = 0.0;
		for (int i = numberOfVariables - k; i < numberOfVariables; i++) {
			g += x[i];
		}

		g = 1 + (9.0 * g) / k;

		System.arraycopy(x, 0, f, 0, numberOfObjectives - 1);

		double h = 0.0;
		for (int i = 0; i < numberOfObjectives - 1; i++) {
			h += (f[i] / (1.0 + g)) * (1 + Math.sin(3.0 * Math.PI * f[i]));
		}

		h = numberOfObjectives - h;

		f[numberOfObjectives - 1] = (1 + g) * h;

		solution.setObjetivos(Utils.ArraytoArrayList(f));

		return solution;
	  }
	
	//Inicializar de forma aleatoria los valores de las variables según los límites
	@Override
	public Individuo inicializarValores(Individuo ind) {
		List<Double> valores = new ArrayList<>(super.getNumVariables());
		for(int i = 0; i < super.getNumVariables(); i++) {
			valores.add(i, Utils.getRandNumber(super.getLimitesInferiores().get(i),
			super.getLimitesSuperiores().get(i)));
		}
		ind.setVariables(valores);
		return ind;
	}

}
