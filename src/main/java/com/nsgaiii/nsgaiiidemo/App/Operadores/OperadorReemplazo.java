package com.nsgaiii.nsgaiiidemo.App.Operadores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import com.nsgaiii.nsgaiiidemo.App.Modelo.Individuo;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Poblacion;
import com.nsgaiii.nsgaiiidemo.App.Modelo.ReferencePoint;
import com.nsgaiii.nsgaiiidemo.App.Problemas.Problema;
import com.nsgaiii.nsgaiiidemo.App.Utils.Utils;

public class OperadorReemplazo {
	
	//Parámetros del Operador de Reemplazo

	private List<List<Individuo>> frentesDePareto;
	private int solutionsToSelect;
	private List<ReferencePoint> referencePoints;
	
	public OperadorReemplazo(int numberOfObjectives, List<ReferencePoint> referencePoints) {
		this.referencePoints = referencePoints;
	}
	
	//Obtiene el ranking de No Dominancia a partir de los frentes de pareto encontrados
	public List<List<Individuo>> obtenerFrentes(Poblacion total, Problema prob){
		
		List<List<Individuo>> frentesDePareto = new ArrayList<>();
		
		this.rankingNoDominancia(total, prob);
		while(total.getPoblacion().size() != 0) {
			List<Individuo> frenteTemp = Utils.obtenerFrenteConIndice(total, 0);
			total = Utils.borrarElementosDeLista(frenteTemp, total);
			frentesDePareto.add(frenteTemp);
			this.rankingNoDominancia(total, prob);
		}
		this.frentesDePareto = frentesDePareto;
		return this.frentesDePareto;
	}
	
	//Para cada individuo calcula cuántos individuos lo dominan
	public Poblacion rankingNoDominancia(Poblacion p, Problema prob) {
		for (int i = 0; i < p.getPoblacion().size(); i++) {
			int domina = 0;
			Individuo a = p.getPoblacion().get(i);
			for (int j = 0; j < p.getPoblacion().size(); j++) {
				if (i != j) {
					Individuo b = p.getPoblacion().get(j);
					if(esDominante(b, a, prob)) {
						domina++;
					}
				}
			}
			a.setdomina(domina);
		}
		ArrayList<Individuo> listaOrden = p.getPoblacion();
		Collections.sort(listaOrden);
		p.setPoblacion(listaOrden);
		return p;
	}
	
	//Definición de dominancia. En la función, "a" domina a "b"
	private boolean esDominante (Individuo a, Individuo b, Problema prob) {
		int mEstricto = 0;
		int mOIgual = 0;
		for (int i = 0; i < prob.getNumObjetivos(); i++) {
			if(prob.getMinOMax().get(i)) {
				if(a.getObjetivos().get(i) < b.getObjetivos().get(i) && mEstricto == 0) {
					mEstricto = 1;
				}
				if (a.getObjetivos().get(i) <= b.getObjetivos().get(i)) {
					mOIgual++;
				}
			}else {
				if(a.getObjetivos().get(i) > b.getObjetivos().get(i) && mEstricto == 0) {
					mEstricto = 1;
				}
				if (a.getObjetivos().get(i) >= b.getObjetivos().get(i)) {
					mOIgual++;
				}
			}
		}
		if(mEstricto == 1 && mOIgual == prob.getNumObjetivos()) {
			return true;
		} else {
			return false;
		}
	}
	
	//Inserta los individuos de cada frente según su ranking.
	//En caso de que a un frente le sobren individuos, se aplica el método del hiperplano de Das y Dennis para elegir los individuos que fomenten mayor diversidad
	public Poblacion rellenarPoblacionConFrentes (Poblacion p, 
			Poblacion total, Problema prob) {
		
		ArrayList<Individuo> nuevaLista = new ArrayList<>(p.getNumIndividuos());
		for (int i = 0; i < this.frentesDePareto.size(); i++) {
			if(nuevaLista.size() + this.frentesDePareto.get(i).size() <= p.getNumIndividuos()) {
				Utils.juntarListas(nuevaLista, this.frentesDePareto.get(i));
			}else {
				if(nuevaLista.size() != p.getNumIndividuos()) {
					this.frentesDePareto = this.frentesDePareto.subList(0, i + 1);
					this.solutionsToSelect = p.getNumIndividuos() - nuevaLista.size();
					//Metodo del hiperplano
					ArrayList<Individuo> ultimosMiembros = dasDennis(prob);
					Utils.juntarListas(nuevaLista, ultimosMiembros);
				}
				p.setPoblacion(nuevaLista);
				return p;
			}
		}
		p.setPoblacion(nuevaLista);
		return p;
	}
	
	private ArrayList<Individuo> dasDennis(Problema prob){
		//Restarle a cada individuo los valores del punto ideal
		List<Double> punto_ideal = traducirObjetivos(prob);
		//Calcular los puntos extremos con el Achivement Scalarization Function de cada individuo del primer frente
		List<Individuo> extreme_points = encontrarPuntosExtremos(prob);
		//Crear hiperplano con lospuntos extremos
	    List<Double> intercepts = construirHiperplano(extreme_points, prob);
	    //Normalizar los valores de funcion objetivo de cada individuo
	    normalizeObjectives(intercepts, punto_ideal, prob);

	    //Asociar cada individuo a un punto de referencia
	    associate();
	    
	    for (ReferencePoint rp : this.referencePoints) {
	    	rp.sort();
	    	this.addToTree(rp);
	    }
	    
	    
	    ArrayList<Individuo> result = new ArrayList<>();

	    while (result.size() < this.solutionsToSelect) {
	    	final ArrayList<ReferencePoint> first = this.referencePointsTree.firstEntry().getValue();
	    	final int min_rp_index = 1 == first.size() ? 0 : Utils.nextInt(0, first.size() - 1);
	    	final ReferencePoint min_rp = first.remove(min_rp_index);
	    	if (first.isEmpty()) this.referencePointsTree.pollFirstEntry();
	    	Individuo chosen = SelectClusterMember(min_rp);
	    	if (chosen != null) {
	    		min_rp.AddMember();
	    		this.addToTree(min_rp);
	    		result.add(chosen);
	    	}
	    }
	    
		return result;
	}
	
	private List<Double> traducirObjetivos(Problema prob){
		ArrayList<Double> punto_ideal;
		punto_ideal = new ArrayList<>(prob.getNumObjetivos());
		
		//Obtener punto ideal
		for (int f = 0; f < prob.getNumObjetivos(); f += 1) {
			double minf = Double.MAX_VALUE;
		    for (int i = 0; i < this.frentesDePareto.get(0).size(); i += 1) // min values must appear in the first front
		    {
		    	minf = Math.min(minf, this.frentesDePareto.get(0).get(i).getObjetivos().get(f));
		    }
		    punto_ideal.add(minf);
		}
		//Inicializar ObjetivosNorm
		for (List<Individuo> list : this.frentesDePareto) {
	    	for (Individuo i : list) {
	    		i.setObjetivosNorm(Utils.inicializarLista(prob.getNumObjetivos()));
	        }
	    }
		//Restarle el punto ideal a cada valor de funcion objetivo
		for (List<Individuo> list : this.frentesDePareto) {
		    	for (Individuo i : list) {
		    		ArrayList<Double> o = i.getObjetivosNorm();
		    		for (int f = 0; f < prob.getNumObjetivos(); f += 1) {
		    			o.set(f, i.getObjetivos().get(f) - punto_ideal.get(f));
		        }
		    }
		}
		
		return punto_ideal;
	}
	
	//Encontrar puntos extremos con el Achivement Scalarization Function de cada individuo
	private List<Individuo> encontrarPuntosExtremos (Problema prob){
		List<Individuo> extremePoints = new ArrayList<>();
		Individuo min_indv = null;
		for (int f = 0; f < prob.getNumObjetivos(); f += 1) {
			double min_ASF = Double.MAX_VALUE;
			for (Individuo s : frentesDePareto.get(0)) {
				double asf = ASF(s, f, prob);
				if (asf < min_ASF) {
					min_ASF = asf;
					min_indv = s;
				}
			}

			extremePoints.add(min_indv);
	    }
		return extremePoints;
	}
	
	//Achivement Scalarization Function
	private double ASF(Individuo s, int index, Problema prob) {
		double max_ratio = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < prob.getNumObjetivos(); i++) {
			double weight = (index == i) ? 1.0 : 0.000001;
			max_ratio = Math.max(max_ratio, s.getObjetivosNorm().get(i) / weight);
		}
		return max_ratio;
	}
	
	//Construcción del hiperplano con los puntos extremos
	private List<Double> construirHiperplano(List<Individuo> extreme_points, Problema prob){
		// Check whether there are duplicate extreme points.
		// This might happen but the original paper does not mention how to deal with it.
		boolean duplicate = false;
		for (int i = 0; !duplicate && i < extreme_points.size(); i += 1) {
			for (int j = i + 1; !duplicate && j < extreme_points.size(); j += 1) {
				duplicate = extreme_points.get(i).equals(extreme_points.get(j));
			}
		}

		List<Double> intercepts = new ArrayList<>();

	    if (duplicate)
	                   
	    {
	    	System.out.println("Duplicado");
	    	for (int f = 0; f < prob.getNumObjetivos(); f += 1) {
	    		intercepts.add(extreme_points.get(f).getObjetivos().get(f));
	    	}
	    } else {
	    	// Encontrar la ecuación del hiperplano
	    	List<Double> b = new ArrayList<>();
	    	for (int i = 0; i < prob.getNumObjetivos(); i++) b.add(1.0);

	    	List<List<Double>> A = new ArrayList<>();
	    	for (Individuo s : extreme_points) {
	    		List<Double> aux = new ArrayList<>();
	    		for (int i = 0; i < prob.getNumObjetivos(); i++) aux.add(s.getObjetivos().get(i));
	    		A.add(aux);
	    	}
	    	List<Double> x = guassianElimination(A, b);

	    	// Encontrar intersecciones
	    	for (int f = 0; f < prob.getNumObjetivos(); f += 1) {
	    		intercepts.add(1.0 / x.get(f));
	    	}
	    }
	    return intercepts;
	}
	
	public List<Double> guassianElimination(List<List<Double>> A, List<Double> b) {
		List<Double> x = new ArrayList<>();

		int N = A.size();
		for (int i = 0; i < N; i += 1) {
			A.get(i).add(b.get(i));
		}

		for (int base = 0; base < N - 1; base += 1) {
			for (int target = base + 1; target < N; target += 1) {
				double ratio = A.get(target).get(base) / A.get(base).get(base);
				for (int term = 0; term < A.get(base).size(); term += 1) {
					A.get(target).set(term, A.get(target).get(term) - A.get(base).get(term) * ratio);
				}
			}
		}

		for (int i = 0; i < N; i++) x.add(0.0);

		for (int i = N - 1; i >= 0; i -= 1) {
			for (int known = i + 1; known < N; known += 1) {
				A.get(i).set(N, A.get(i).get(N) - A.get(i).get(known) * x.get(known));
			}
			x.set(i, A.get(i).get(N) / A.get(i).get(i));
		}
		return x;
	 }
	
	//Normalizacion de objetivos mediante la division por los puntos de interseccion traducidos
	public void normalizeObjectives(List<Double> intercepts, List<Double> ideal_point, Problema prob) {
		for (int t = 0; t < frentesDePareto.size(); t += 1) {
			for (Individuo s : frentesDePareto.get(t)) {

				for (int f = 0; f < prob.getNumObjetivos(); f++) {
					List<Double> conv_obj = s.getObjetivosNorm();
					if (Math.abs(intercepts.get(f) - ideal_point.get(f)) > 10e-10) {
						conv_obj.set(f, conv_obj.get(f) / (intercepts.get(f) - ideal_point.get(f)));
					} else {
						conv_obj.set(f, conv_obj.get(f) / (10e-10));
					}
		        }
		    }
		}
	}
	
	public void associate() {

		for (int t = 0; t < frentesDePareto.size(); t++) {
			for (Individuo s : frentesDePareto.get(t)) {
				int min_rp = -1;
				double min_dist = Double.MAX_VALUE;
				for (int r = 0; r < this.referencePoints.size(); r++) {
					double d = perpendicularDistance(this.referencePoints.get(r).position, s.getObjetivosNorm());
					if (d < min_dist) {
						min_dist = d;
						min_rp = r;
					}
				}
				if (t + 1 != frentesDePareto.size()) {
					this.referencePoints.get(min_rp).AddMember();
				} else {
					this.referencePoints.get(min_rp).AddPotentialMember(s, min_dist);
				}
			}
	    }
	}
	
	public double perpendicularDistance(List<Double> direction, List<Double> point) {
		double numerator = 0, denominator = 0;
		for (int i = 0; i < direction.size(); i += 1) {
			numerator += direction.get(i) * point.get(i);
			denominator += Math.pow(direction.get(i), 2.0);
	    }
		double k = numerator / denominator;

		double d = 0;
		for (int i = 0; i < direction.size(); i += 1) {
			d += Math.pow(k * direction.get(i) - point.get(i), 2.0);
		}
		return Math.sqrt(d);
	}
	
	private TreeMap<Integer, ArrayList<ReferencePoint>> referencePointsTree = new TreeMap<>();
	
	private void addToTree(ReferencePoint rp) {
		int key = rp.MemberSize();
		if (!this.referencePointsTree.containsKey(key))
			this.referencePointsTree.put(key, new ArrayList<ReferencePoint>());
		this.referencePointsTree.get(key).add(rp);
	}
	
	Individuo SelectClusterMember(ReferencePoint rp) {
		Individuo chosen = null;
		if (rp.HasPotentialMember()) {
			if (rp.MemberSize() == 0) // currently has no member
			{
				chosen = rp.FindClosestMember();
			} else {
				chosen = rp.RandomMember();
			}
		}
		return chosen;
	}
	
}
