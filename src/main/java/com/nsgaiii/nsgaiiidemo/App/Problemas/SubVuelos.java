package com.nsgaiii.nsgaiiidemo.App.Problemas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nsgaiii.nsgaiiidemo.App.Modelo.Individuo;
import com.nsgaiii.nsgaiiidemo.App.Utils.Utils;

public class SubVuelos extends Vuelos{
	
	private List<Integer> indPorAeropuerto;

	public SubVuelos(int numVariables, Map<List<String>, Double> riesgos, Map<List<String>, Integer> conexiones,
			Map<List<String>, Integer> vuelos, List<String> AeropuertosEspanyoles, List<String> AeropuertosOrigen,
			List<String> companyias, Map<List<String>, Double> dineroMedio, Map<List<String>, Integer> pasajeros,
			Map<List<String>, Integer> pasajerosCompanyia, Map<List<String>, Integer> vuelosEntrantesConexion,
			Map<String, Integer> vuelosSalientesAEspanya, Map<String, Integer> vuelosSalientes,
			Map<String, Double> conectividadesAeropuertosOrigen,
			Map<String, Set<String>> listaConexionesPorAeropuertoEspanyol,
			Map<String, Set<String>> listaConexionesSalidas, List<Integer> indPorAeropuerto) {
		super(AeropuertosEspanyoles.size(), riesgos, conexiones, vuelos, AeropuertosEspanyoles, AeropuertosOrigen, companyias, dineroMedio,
				pasajeros, pasajerosCompanyia, vuelosEntrantesConexion, vuelosSalientesAEspanya, vuelosSalientes,
				conectividadesAeropuertosOrigen, listaConexionesPorAeropuertoEspanyol, listaConexionesSalidas);
		this.indPorAeropuerto = indPorAeropuerto;
	}
	
	@Override
	public Individuo evaluate(Individuo solution) {
		ArrayList<Double> objetivos = new ArrayList<>(super.getNumObjetivos());
		
		//ArrayList<Double> riesgoPasajerosIngresos = calcularRiesgoPasajerosIngresosHPasajerosHIngresos(solution);
		
		//objetivos.add(0, riesgoPasajerosIngresos.get(0));
		//objetivos.add(1, Utils.mediaDeValoresObjetivo(riesgoPasajerosIngresos.subList(1, 5)));
		/*objetivos.add(1, riesgoPasajerosIngresos.get(1));
		objetivos.add(2, riesgoPasajerosIngresos.get(2));
		objetivos.add(3, riesgoPasajerosIngresos.get(3));
		objetivos.add(4, riesgoPasajerosIngresos.get(4));*/
		
		
		/*objetivos.add(0, calcularRiesgo(solution));
		objetivos.add(1, calcularPasajerosPerdidos(solution));
		objetivos.add(2, calcularPerdidaDeIngresos(solution));*/
		//objetivos.add(3, calculoHomogeneidadPasajerosAerolineas(solution));
		//objetivos.add(4, calculoHomogeneidadIngresosTurismoAeropuertos(solution));
		//objetivos.add(2, calculoConectividad(solution));
		
		solution.setObjetivos(objetivos);
		return solution;
	}
	
	//Inicializar de forma aleatoria los valores de las variables según los límites
		@Override
		public Individuo inicializarValores(Individuo ind) {
			ArrayList<Double> valores = new ArrayList<>(super.getNumVariables());
			for(int i = 0; i < super.getNumVariables(); i++) {
				if(super.getIndCont() == 0) {
					valores.add(i, 0.0);
				}
				else if(super.getIndCont() == 1) {
					valores.add(i, this.indPorAeropuerto.get(i) - 1.0);
				} else {
					valores.add(i, Utils.getRandNumber(0, this.indPorAeropuerto.get(i)) * 1.0);
				}
			}
			super.setIndCont(getIndCont() + 1);;
			ind.setVariables(valores);
			return ind;
		}
		
		private void leerFicherosAeropuertos() {
			for(int i = 0; i < super.getAeropuertosEspanyoles().size(); i++) {
				
			}
		}

}
