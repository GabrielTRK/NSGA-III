package com.nsgaiii.nsgaiiidemo.App.Problemas;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nsgaiii.nsgaiiidemo.App.Constantes.Constantes;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Individuo;
import com.nsgaiii.nsgaiiidemo.App.Utils.Utils;
import com.opencsv.exceptions.CsvException;

public class Vuelos extends Problema{

	private Map<List<String>, Double> riesgos = new HashMap<>();
	private Map<List<String>, Integer> conexiones = new HashMap<>();
	private Map<List<String>, Integer> vuelos = new HashMap<>();
	

	private List<String> AeropuertosEspanyoles = new ArrayList<>();
	private List<String> AeropuertosOrigen = new ArrayList<>();
	private List<String> companyias = new ArrayList<>();
	private Map<List<String>, Double> dineroMedio = new HashMap<>();
	private Map<List<String>, Integer> pasajeros = new HashMap<>();
	private Map<List<String>, Integer> pasajerosCompanyia = new HashMap<>();
	private Map<List<String>, Integer> vuelosEntrantesConexion = new HashMap<>();
	private Map<String, Integer> vuelosSalientesAEspanya = new HashMap<>();
	private Map<String, Integer> vuelosSalientes = new HashMap<>();
	private Map<String, Double> conectividadesAeropuertosOrigen = new HashMap<>();
	private Map<String, Set<String>> listaConexionesPorAeropuertoEspanyol = new HashMap<>();
	private Map<String, Set<String>> listaConexionesSalidas = new HashMap<>();
	private int indCont = 0;
	
	private List<List<String>> listaConexiones;

	public Vuelos(int numVariables, Map<List<String>, Double> riesgos,
			Map<List<String>, Integer> conexiones, Map<List<String>, Integer> vuelos, 
			List<String> AeropuertosEspanyoles, List<String> AeropuertosOrigen, 
			List<String> companyias, Map<List<String>, Double> dineroMedio, 
			Map<List<String>, Integer> pasajeros, Map<List<String>, Integer> pasajerosCompanyia, 
			Map<List<String>, Integer> vuelosEntrantesConexion, Map<String, Integer> vuelosSalientesAEspanya, 
			Map<String, Integer> vuelosSalientes, Map<String, Double> conectividadesAeropuertosOrigen, 
			Map<String, Set<String>> listaConexionesPorAeropuertoEspanyol, Map<String, Set<String>> listaConexionesSalidas) {
		super(numVariables, 3);
		
		super.setNombre(Constantes.nombreProblemaVuelos);
		this.riesgos = riesgos;
		this.conexiones = conexiones;
		this.vuelos = vuelos;
		this.AeropuertosEspanyoles = AeropuertosEspanyoles;
		this.AeropuertosOrigen = AeropuertosOrigen;
		this.companyias = companyias;
		this.dineroMedio = dineroMedio;
		this.pasajeros = pasajeros;
		this.pasajerosCompanyia = pasajerosCompanyia;
		this.vuelosEntrantesConexion = vuelosEntrantesConexion;
		this.vuelosSalientesAEspanya = vuelosSalientesAEspanya;
		this.vuelosSalientes = vuelosSalientes;
		this.conectividadesAeropuertosOrigen = conectividadesAeropuertosOrigen;
		this.listaConexionesPorAeropuertoEspanyol = listaConexionesPorAeropuertoEspanyol;
		this.listaConexionesSalidas = listaConexionesSalidas;
		
		this.listaConexiones = new ArrayList<>(conexiones.keySet());
		
	}
	
	/*Calcular valores de funcion objetivo. 6 objetivos en total
	 * Lista de tamaño 6
	 * Posición 0: Riesgo
	 * Posición 1: Pasajeros perdidos
	 * Posición 2: Pérdida de ingresos
	 * Posición 3: Homogeneidad de pérdida de pasajeros por las compañías
	 * Posición 4: Homogeneidad de pérdida de ingresos en los destinos
	 * Posición 5: Conectividad de la red de transporte aéreo
	 */
	@Override
	public Individuo evaluate(Individuo solution) throws FileNotFoundException, IOException, CsvException {
		
		
		List<Double> objetivos = new ArrayList<>(super.getNumObjetivos());
		
		List<Double> riesgoPasajerosIngresos = calcularRiesgoPasajerosIngresosHPasajerosHIngresos(solution);
		
		objetivos.add(0, riesgoPasajerosIngresos.get(0));
		objetivos.add(1, Utils.mediaDeValoresObjetivo(riesgoPasajerosIngresos.subList(1, 5)));
		/*objetivos.add(1, riesgoPasajerosIngresos.get(1));
		objetivos.add(2, riesgoPasajerosIngresos.get(2));
		objetivos.add(3, riesgoPasajerosIngresos.get(3));
		objetivos.add(4, riesgoPasajerosIngresos.get(4));*/
		
		
		/*objetivos.add(0, calcularRiesgo(solution));
		objetivos.add(1, calcularPasajerosPerdidos(solution));
		objetivos.add(2, calcularPerdidaDeIngresos(solution));
		objetivos.add(3, calculoHomogeneidadPasajerosAerolineas(solution));
		objetivos.add(4, calculoHomogeneidadIngresosTurismoAeropuertos(solution));*/
		objetivos.add(2, calculoConectividad(solution));
		
		solution.setObjetivos(objetivos);
		return solution;
	}
	
	//Inicializar de forma aleatoria los valores de las variables según los límites
	@Override
	public Individuo inicializarValores(Individuo ind) {
		List<Double> valores = new ArrayList<>(super.getNumVariables());
		for(int i = 0; i < super.getNumVariables(); i++) {
			if(this.indCont == 0) {
				valores.add(i, 0.0);
			}
			else if(this.indCont == 1) {
				valores.add(i, 1.0);
			} else {
				valores.add(i, Utils.getRandBinNumber());
			}
		}
		this.indCont++;
		ind.setVariables(valores);
		return ind;
	}
	
	private List<Double> calcularRiesgoPasajerosIngresosHPasajerosHIngresos(Individuo solucion) {
		List<Double> objetivos = new ArrayList<Double>(5);
		
		Double Riesgosumatorio = 0.0;
        Double RiesgosumatorioTotal = 0.0;
        
        Double Pasajerossumatorio = 0.0;
        Double Pasajerostotal = 0.0;
        
        Double Ingresossuma = 0.0;
        Double IngresostotalSuma = 0.0;
        
        boolean calculado = false;
        
        java.util.Map<String, Integer> numPasajerosAeropuerto = new java.util.HashMap<>();
        java.util.Map<String, Integer> numPasajerosAeropuertoConexiones = new java.util.HashMap<>();
        List<Double> IporcentajePerdido = new ArrayList<>();
        double ImediaPorcentajeVuelosPerdidos = 0.0;
        double IporcentajePerdidoDesviacionMedia = 0.0;
        
        int[] totalPasajerosCompanyias = new int[this.companyias.size()];
        int[] totalPasajerosConexiones = new int[this.companyias.size()];    
        List<Double> porcentajePerdido = new ArrayList<>();             
        double porcentajePerdidoMedia = 0.0;                            
        double porcentajePerdidoDesviacionMedia = 0.0;
        
        
        List<List<String>> llaves = new ArrayList<>(this.riesgos.keySet());
        for (int j = 0; j < this.companyias.size(); j++) {
        	for (int i = 0; i < this.listaConexiones.size(); i++) {
        		if(!calculado) {
        			//Riesgo, Pasajeros e Ingresos -----------------------
        			Riesgosumatorio += this.riesgos.get(listaConexiones.get(i)) * 
            			solucion.getVariables().get(i);
        			RiesgosumatorioTotal += this.riesgos.get(llaves.get(i));
            	
        			Pasajerossumatorio += this.pasajeros.get(llaves.get(i)) * 
                		solucion.getVariables().get(i);
        			Pasajerostotal += this.pasajeros.get(llaves.get(i));
            	
        			Ingresossuma += this.dineroMedio.get(llaves.get(i)) * 
            			solucion.getVariables().get(i);
        			IngresostotalSuma += this.dineroMedio.get(llaves.get(i));
        			//----------------------------------------------------
            	
        			//calculoHomogeneidadIngresosTurismoAeropuertos
        			if (numPasajerosAeropuerto.get(this.listaConexiones.get(i).get(1)) != null) {
        				numPasajerosAeropuerto.put(this.listaConexiones.get(i).get(1), numPasajerosAeropuerto.get(
                    		this.listaConexiones.get(i).get(1)) + this.pasajeros.get(this.listaConexiones.get(i)));
        			} else {
        				numPasajerosAeropuerto.put(this.listaConexiones.get(i).get(1), this.pasajeros.get(this.listaConexiones.get(i)));
        			}
        			if (/*solucion.getVariables().get(i) == 1.0*/ solucion.getVariables().get(Utils.encontrarIndiceEnLista(this.listaConexiones, this.listaConexiones.get(i))) == 1.0) {
        				if (numPasajerosAeropuertoConexiones.get(this.listaConexiones.get(i).get(1)) != null) {
        					numPasajerosAeropuertoConexiones.put(this.listaConexiones.get(i).get(1), numPasajerosAeropuertoConexiones.get(
                        		this.listaConexiones.get(i).get(1)) + this.pasajeros.get(this.listaConexiones.get(i)));
        				} else {
                        numPasajerosAeropuertoConexiones.put(this.listaConexiones.get(i).get(1), this.pasajeros.get(this.listaConexiones.get(i)));
        				}
        			}
        			//----------------------------------------------
        		}
        		//calculoHomogeneidadPasajerosAerolineas------------
        		if (this.pasajerosCompanyia.get(List.of(this.listaConexiones.get(i).get(0),this.listaConexiones.get(i).get(1),
                		this.companyias.get(j))) != null) {
                    totalPasajerosCompanyias[j] = totalPasajerosCompanyias[j] + this.pasajerosCompanyia.get(
                            List.of(this.listaConexiones.get(i).get(0), this.listaConexiones.get(i).get(1), this.companyias.get(j)));
                    if (/*solucion.getVariables().get(i) == 1.0*/ solucion.getVariables().get(Utils.encontrarIndiceEnLista(this.listaConexiones, this.listaConexiones.get(i))) == 1.0) {
                        totalPasajerosConexiones[j] = totalPasajerosConexiones[j] + this.pasajerosCompanyia.
                                get(List.of(this.listaConexiones.get(i).get(0), this.listaConexiones.get(i).get(1),this.companyias.get(j)));
                    }
                }
        		
        		
        	}
        	calculado = true;
        	//calculoHomogeneidadPasajerosAerolineas----------------
        	if (totalPasajerosCompanyias[j] != 0) {
                porcentajePerdido.add(1 - (double) totalPasajerosConexiones[j] / totalPasajerosCompanyias[j]);
                porcentajePerdidoMedia = porcentajePerdidoMedia +
                        1 - (double) totalPasajerosConexiones[j] / totalPasajerosCompanyias[j];
            }
        }
        	
        	
        int i = 0;
        
        Double aux = 0.0;
        if (IngresostotalSuma != 0.0) {
            aux = Ingresossuma / IngresostotalSuma;
        }
        
        for (String key : numPasajerosAeropuerto.keySet()) {
            if (numPasajerosAeropuertoConexiones.get(key) != null) {
                IporcentajePerdido.add((double) numPasajerosAeropuertoConexiones.get(key) / numPasajerosAeropuerto.get(key));
                ImediaPorcentajeVuelosPerdidos = ImediaPorcentajeVuelosPerdidos + IporcentajePerdido.get(i);
            } else {
                IporcentajePerdido.add(0.0);
            }
            i++;
        }
        ImediaPorcentajeVuelosPerdidos = ImediaPorcentajeVuelosPerdidos / IporcentajePerdido.size();
        for (i = 0; i < IporcentajePerdido.size(); i++) {
            IporcentajePerdidoDesviacionMedia = IporcentajePerdidoDesviacionMedia +
                    Math.abs(IporcentajePerdido.get(i) - ImediaPorcentajeVuelosPerdidos);
        }
        IporcentajePerdidoDesviacionMedia = IporcentajePerdidoDesviacionMedia / IporcentajePerdido.size();
        
        porcentajePerdidoMedia = porcentajePerdidoMedia / porcentajePerdido.size();
        for (i = 0; i < porcentajePerdido.size(); i++) {
            porcentajePerdidoDesviacionMedia = porcentajePerdidoDesviacionMedia +
                    Math.abs(porcentajePerdido.get(i) - porcentajePerdidoMedia);
        }
        porcentajePerdidoDesviacionMedia = porcentajePerdidoDesviacionMedia / porcentajePerdido.size();
        
        
        
        objetivos.add(0, Riesgosumatorio / RiesgosumatorioTotal);
        objetivos.add(1, 1 - Pasajerossumatorio / Pasajerostotal);
        objetivos.add(2, 1 - aux);
        objetivos.add(3, porcentajePerdidoDesviacionMedia);
        objetivos.add(4, IporcentajePerdidoDesviacionMedia);
        
        return objetivos;
	}
	
	
	//Función objetivo Riesgo
	private Double calcularRiesgo(Individuo solucion) {
		Double sumatorio = 0.0;
        Double sumatorioTotal = 0.0;
        List<List<String>> llaves = new ArrayList<>(this.riesgos.keySet());
        for (int i = 0; i < llaves.size(); i++) {
            //sumatorio += riesgos.get(llaves.get(i)) * solucion.getVariables().get(i);
        	sumatorio += this.riesgos.get(llaves.get(i)) * 
        			solucion.getVariables().get(Utils.encontrarIndiceEnLista(this.listaConexiones, llaves.get(i)));
            sumatorioTotal += this.riesgos.get(llaves.get(i));
        }
        return sumatorio / sumatorioTotal;
	}
	
	//Función objetivo Pasajeros perdidos
	private Double calcularPasajerosPerdidos(Individuo solucion) {
		Double sumatorio = 0.0;
        Double totalPasajeros = 0.0;
        List<List<String>> llaves = new ArrayList<>(this.pasajeros.keySet());
        for (int i = 0; i < llaves.size(); i++) {
            //sumatorio += pasajeros.get(llaves.get(i)) * solucion.getVariables().get(i);
            sumatorio += this.pasajeros.get(llaves.get(i)) * 
            		solucion.getVariables().get(Utils.encontrarIndiceEnLista(this.listaConexiones, llaves.get(i)));
            totalPasajeros += this.pasajeros.get(llaves.get(i));
        }
        Double porcentaje = 1 - sumatorio / totalPasajeros;
        return porcentaje;
	}
	
	//Función objetivo Perdida de ingresos
	private Double calcularPerdidaDeIngresos(Individuo solucion) {
		Double suma = 0.0;
        Double totalSuma = 0.0;
        List<List<String>> llaves = new ArrayList<>(this.conexiones.keySet());
        for (int i = 0; i < llaves.size(); i++) {
        	//suma += dineroMedio.get(llaves.get(i)) * solucion.getVariables().get(i);
        	suma += this.dineroMedio.get(llaves.get(i)) * 
        			solucion.getVariables().get(Utils.encontrarIndiceEnLista(this.listaConexiones, llaves.get(i)));
        	totalSuma += this.dineroMedio.get(llaves.get(i));
        }
        Double aux = 0.0;
        if (totalSuma != 0.0) {
            aux = suma / totalSuma;
        }
        return 1 - aux;
	}
	
	private Double calculoHomogeneidadPasajerosAerolineas(Individuo solucion) {
        int i;
        int[] totalPasajerosCompanyias = new int[this.companyias.size()];
        int[] totalPasajerosConexiones = new int[this.companyias.size()];    
        List<Double> porcentajePerdido = new ArrayList<>();             
        double porcentajePerdidoMedia = 0.0;                            
        double porcentajePerdidoDesviacionMedia = 0.0;                  
        for (int j = 0; j < this.companyias.size(); j++) {
            for (i = 0; i < this.listaConexiones.size(); i++) {
                if (this.pasajerosCompanyia.get(List.of(this.listaConexiones.get(i).get(0),this.listaConexiones.get(i).get(1),
                		this.companyias.get(j))) != null) {
                    totalPasajerosCompanyias[j] = totalPasajerosCompanyias[j] + this.pasajerosCompanyia.get(
                            List.of(this.listaConexiones.get(i).get(0), this.listaConexiones.get(i).get(1), this.companyias.get(j)));
                    if (/*solucion.getVariables().get(i) == 1.0*/ solucion.getVariables().get(Utils.encontrarIndiceEnLista(this.listaConexiones, this.listaConexiones.get(i))) == 1.0) {
                        totalPasajerosConexiones[j] = totalPasajerosConexiones[j] + this.pasajerosCompanyia.
                                get(List.of(this.listaConexiones.get(i).get(0), this.listaConexiones.get(i).get(1),this.companyias.get(j)));
                    }
                }
            }
            if (totalPasajerosCompanyias[j] != 0) {
                porcentajePerdido.add(1 - (double) totalPasajerosConexiones[j] / totalPasajerosCompanyias[j]);
                porcentajePerdidoMedia = porcentajePerdidoMedia +
                        1 - (double) totalPasajerosConexiones[j] / totalPasajerosCompanyias[j];
            }
        }
        porcentajePerdidoMedia = porcentajePerdidoMedia / porcentajePerdido.size();
        
        for (i = 0; i < porcentajePerdido.size(); i++) {
            porcentajePerdidoDesviacionMedia = porcentajePerdidoDesviacionMedia +
                    //Math.pow(Math.abs(porcentajePerdido.get(i) - porcentajePerdidoMedia),2);
            		Math.abs(porcentajePerdido.get(i) - porcentajePerdidoMedia);
        }
        porcentajePerdidoDesviacionMedia = porcentajePerdidoDesviacionMedia / porcentajePerdido.size();
        //porcentajePerdidoDesviacionMedia = Math.sqrt(porcentajePerdidoDesviacionMedia);
        //System.out.println(porcentajePerdidoDesviacionMedia);
        return porcentajePerdidoDesviacionMedia;

    }
	
	private Double calculoHomogeneidadIngresosTurismoAeropuertos(Individuo solucion) {
        java.util.Map<String, Integer> numPasajerosAeropuerto = new java.util.HashMap<>();
        java.util.Map<String, Integer> numPasajerosAeropuertoConexiones = new java.util.HashMap<>();
        List<Double> porcentajePerdido = new ArrayList<>();
        double mediaPorcentajeVuelosPerdidos = 0.0;
        double porcentajePerdidoDesviacionMedia = 0.0;
        int i;
        for (i = 0; i < this.listaConexiones.size(); i++) {
            if (numPasajerosAeropuerto.get(this.listaConexiones.get(i).get(1)) != null) {
                numPasajerosAeropuerto.put(this.listaConexiones.get(i).get(1), numPasajerosAeropuerto.get(
                		this.listaConexiones.get(i).get(1)) + this.pasajeros.get(this.listaConexiones.get(i)));
            } else {
                numPasajerosAeropuerto.put(this.listaConexiones.get(i).get(1), this.pasajeros.get(this.listaConexiones.get(i)));
            }
            if (/*solucion.getVariables().get(i) == 1.0*/ solucion.getVariables().get(Utils.encontrarIndiceEnLista(this.listaConexiones, this.listaConexiones.get(i))) == 1.0) {
                if (numPasajerosAeropuertoConexiones.get(this.listaConexiones.get(i).get(1)) != null) {
                    numPasajerosAeropuertoConexiones.put(this.listaConexiones.get(i).get(1), numPasajerosAeropuertoConexiones.get(
                    		this.listaConexiones.get(i).get(1)) + this.pasajeros.get(this.listaConexiones.get(i)));
                } else {
                    numPasajerosAeropuertoConexiones.put(this.listaConexiones.get(i).get(1), this.pasajeros.get(this.listaConexiones.get(i)));
                }
            }
        }
        i = 0;
        for (String key : numPasajerosAeropuerto.keySet()) {
            if (numPasajerosAeropuertoConexiones.get(key) != null) {
                porcentajePerdido.add((double) numPasajerosAeropuertoConexiones.get(key) / numPasajerosAeropuerto.get(key));
                mediaPorcentajeVuelosPerdidos = mediaPorcentajeVuelosPerdidos + porcentajePerdido.get(i);
            } else {
                porcentajePerdido.add(0.0);
            }
            i++;
        }
        mediaPorcentajeVuelosPerdidos = mediaPorcentajeVuelosPerdidos / porcentajePerdido.size();
        
        for (i = 0; i < porcentajePerdido.size(); i++) {
            porcentajePerdidoDesviacionMedia = porcentajePerdidoDesviacionMedia +
                    //Math.pow(Math.abs(porcentajePerdido.get(i) - mediaPorcentajeVuelosPerdidos),2);
            		Math.abs(porcentajePerdido.get(i) - mediaPorcentajeVuelosPerdidos);
        }
        porcentajePerdidoDesviacionMedia = porcentajePerdidoDesviacionMedia / porcentajePerdido.size();
        //porcentajePerdidoDesviacionMedia = Math.sqrt(porcentajePerdidoDesviacionMedia);
        //System.out.println(porcentajePerdidoDesviacionMedia);
        return porcentajePerdidoDesviacionMedia;
    }
	
	private Double calculoConectividad(Individuo solution) { // Perfecto, se compara con objetivo conectividad
        Double suma = 0.0;
        Double totalSuma = 0.0;
        Double solucion = 0.0;
        
        for (String origen : this.AeropuertosOrigen) {
            Double auxSuma = 0.0;
            Double auxTotalSuma = 0.0;
            
            for (String destino : this.listaConexionesSalidas.get(origen)) {
                auxSuma += solution.getVariables().get(
                		Utils.encontrarIndiceEnLista(this.listaConexiones, List.of(origen, destino)))
                		* this.vuelosEntrantesConexion.get(List.of(origen, destino));
                auxTotalSuma += this.vuelosEntrantesConexion.get(List.of(origen, destino));
            }
            Double aux = 0.0;
            if (auxTotalSuma != 0) {
                aux = auxSuma / auxTotalSuma;
            }
            suma += this.conectividadesAeropuertosOrigen.get(origen) * (1 - aux);
            totalSuma += this.conectividadesAeropuertosOrigen.get(origen);
        }
        if (totalSuma != 0) {
            solucion = suma / totalSuma;
        }
        return solucion;
    }

	public int getIndCont() {
		return indCont;
	}

	public void setIndCont(int indCont) {
		this.indCont = indCont;
	}

	public List<String> getAeropuertosEspanyoles() {
		return AeropuertosEspanyoles;
	}

	public void setAeropuertosEspanyoles(List<String> aeropuertosEspanyoles) {
		AeropuertosEspanyoles = aeropuertosEspanyoles;
	}

	public Map<List<String>, Integer> getConexiones() {
		return conexiones;
	}

	public void setConexiones(Map<List<String>, Integer> conexiones) {
		this.conexiones = conexiones;
	}

	public List<List<String>> getListaConexiones() {
		return listaConexiones;
	}

	public void setListaConexiones(List<List<String>> listaConexiones) {
		this.listaConexiones = listaConexiones;
	}
	
	

}
