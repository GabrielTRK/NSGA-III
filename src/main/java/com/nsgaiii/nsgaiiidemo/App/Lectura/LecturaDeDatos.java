package com.nsgaiii.nsgaiiidemo.App.Lectura;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.nsgaiii.nsgaiiidemo.App.Constantes.Constantes;

public class LecturaDeDatos {
	
	
	
	public static void leerDatos(Map<List<String>, Integer> conexiones, Map<List<String>, 
			Double> riesgos, Map<List<String>, Integer> vuelos) {
		try {
            Scanner scanner = new Scanner(new File(Constantes.rutaDatos + Constantes.nombreFicheroSIR + Constantes.extensionFichero));
            //Comma as a delimiter
            scanner.useDelimiter("\n");
            scanner.next();
            while (scanner.hasNext()) {
                String str = scanner.next();
                String split[] = str.split(",");
                conexiones.put(List.of(split[2], split[1]), 0);
                if (riesgos.containsKey(List.of(split[2], split[1]))) {
                	riesgos.put(List.of(split[2], split[1]), Double.parseDouble(split[0]) + riesgos.get(List.of(split[2], split[1])));
                } else {
                	riesgos.put(List.of(split[2], split[1]), Double.parseDouble(split[0]));
                }
                if (vuelos.containsKey(List.of(split[2], split[1]))) {
                	vuelos.put(List.of(split[2], split[1]), vuelos.get(List.of(split[2], split[1])) + 1);
                } else {
                	vuelos.put(List.of(split[2], split[1]), 1);
                }

            }
            // Closing the scanner
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("El path del documento conexiones no está bien especificado");
            //do something with e, or handle this case
        }
	}
	
	public static void leerDatosAeropuertosEspanyoles(List<String> AeropuertosEspanyoles) {
		try {
            Scanner scanner = new Scanner(new File(Constantes.rutaDatos + Constantes.nombreFicheroAeropuertos_Entradas + Constantes.extensionFichero));
            //Comma as a delimiter
            scanner.useDelimiter("\n");
            scanner.next();
            while (scanner.hasNext()) {
                String str = scanner.next();
                String split[] = str.split(",");
                AeropuertosEspanyoles.add(split[0]);
            }
            // Closing the scanner
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("El path del documento AeropuertosEspanyoles no está bien especificado");
            //do something with e, or handle this case
        }
	}
	
	public static void leerDatosAeropuertosOrigen(List<String> AeropuertosOrigen) {
		try {
            Scanner scanner = new Scanner(new File(Constantes.rutaDatos + Constantes.nombreFicheroAeropuertos_Origen + Constantes.extensionFichero));
            //Comma as a delimiter
            scanner.useDelimiter("\n");
            scanner.next();
            while (scanner.hasNext()) {
                String str = scanner.next();
                String split[] = str.split(",");
                AeropuertosOrigen.add(split[0]);
            }
            // Closing the scanner
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("El path del documento aeropuertos_salida no está bien especificado");
            //do something with e, or handle this case
        }
	}
	
	public static void leerDatosCompanyias(List<String> companyias) {
		try {
            Scanner scanner = new Scanner(new File(Constantes.rutaDatos + Constantes.nombreFicheroCompanyias + Constantes.extensionFichero));
            //Comma as a delimiter
            scanner.useDelimiter("\n");
            scanner.next();
            while (scanner.hasNext()) {
                String str = scanner.next();
                companyias.add(str);
            }
            // Closing the scanner
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("El path del documento companyias no está bien especificado");
            //do something with e, or handle this case
        }
        companyias.remove("UNKNOWN");
	}
	
	public static void leerDatosDineroMedio(Map<List<String>, Double> dineroMedio) {
		try {
            Scanner scanner = new Scanner(new File(Constantes.rutaDatos + Constantes.nombreFicheroDineroPorVuelo + Constantes.extensionFichero));
            //Comma as a delimiter
            scanner.useDelimiter("\n");
            scanner.next();
            while (scanner.hasNext()) {
                String str = scanner.next();
                String split[] = str.split(",");
                if (dineroMedio.containsKey(List.of(split[2], split[1]))) {
                    dineroMedio.put(List.of(split[2], split[1]), dineroMedio.get(List.of(split[2], split[1])) +
                            Double.parseDouble(split[0]));
                } else {
                    dineroMedio.put(List.of(split[2], split[1]), Double.parseDouble(split[0]));
                }

            }
            // Closing the scanner
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("El path del documento dineroMedio no está bien especificado");
            //do something with e, or handle this case
        }
	}
	
	public static void leerDatosPasajeros(Map<List<String>, Integer> pasajeros) {
		try {
            Scanner scanner = new Scanner(new File(Constantes.rutaDatos + Constantes.nombreFicheroPasajerosPorVuelo + Constantes.extensionFichero));
            //Comma as a delimiter
            scanner.useDelimiter("\n");
            scanner.next();
            while (scanner.hasNext()) {
                String str = scanner.next();
                String split[] = str.split(",");
                if (pasajeros.keySet().contains(List.of(split[2], split[1]))) {
                    pasajeros.put(List.of(split[2], split[1]), pasajeros.get(List.of(split[2], split[1])) + (int) Double.parseDouble(split[0]));
                } else {
                    pasajeros.put(List.of(split[2], split[1]), (int) Double.parseDouble(split[0]));
                }
            }
            // Closing the scanner
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("El path del documento pasajeros no está bien especificado");
            //do something with e, or handle this case
        }
	}
	
	public static void leerDatosPasajerosCompanyia(Map<List<String>, Integer> pasajerosCompanyia) {
		try {
            Scanner scanner = new Scanner(new File(Constantes.rutaDatos + Constantes.nombreFicheroPasajerosCompanyia + Constantes.extensionFichero));
            //Comma as a delimiter
            scanner.useDelimiter("\n");
            scanner.next();
            while (scanner.hasNext()) {
                String str = scanner.next();
                String split[] = str.split(",");
                if (pasajerosCompanyia.keySet().contains(List.of(split[3], split[2], split[1]))) {
                    pasajerosCompanyia.put(List.of(split[3], split[2], split[1]),
                            pasajerosCompanyia.get(List.of(split[3], split[2], split[1])) + (int) Double.parseDouble(split[0]));

                } else {
                    pasajerosCompanyia.put(List.of(split[3], split[2], split[1]), (int) Double.parseDouble(split[0]));
                }
            }
            // Closing the scanner
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("El path del documento pasajeros por vuelo y companyias no está bien especificado");
            //do something with e, or handle this case
        }
	}
	
	public static void leerDatosConectividad(Map<List<String>, Integer> vuelosEntrantesConexion, 
			Map<String, Integer> vuelosSalientesAEspanya, Map<String, Integer> vuelosSalientes,
			Map<String, Double> conectividadesAeropuertosOrigen, Map<List<String>, Integer> conexiones, 
			List<String> AeropuertosOrigen) {
		
		for (List<String> conexion : conexiones.keySet()) {
            vuelosEntrantesConexion.put(conexion, 0);
        }
        for (String aeropuerto : AeropuertosOrigen) {
            conectividadesAeropuertosOrigen.put(aeropuerto, 0.0);
            vuelosSalientesAEspanya.put(aeropuerto, 0);
            vuelosSalientes.put(aeropuerto, 0);
        }
        
        try {
            Scanner scanner = new Scanner(new File(Constantes.rutaDatos + Constantes.nombreFicheroPasajerosConectividad + Constantes.extensionFichero));
            //Comma as a delimiter
            scanner.useDelimiter("\n");
            scanner.next();
            while (scanner.hasNext()) {
                String str = scanner.next();
                String split[] = str.split(",");
                vuelosEntrantesConexion.put(List.of(split[1], split[0]), Integer.parseInt(split[2]));
                vuelosSalientesAEspanya.put(split[1], vuelosSalientesAEspanya.get(split[1]) + Integer.parseInt(split[2]));
                vuelosSalientes.put(split[1], Integer.parseInt(split[3]));
                conectividadesAeropuertosOrigen.put(split[1], Double.parseDouble(split[4]));
            }
            // Closing the scanner
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("El path del documento conectividad_por_aeropuerto no está bien especificado");
            //do something with e, or handle this case
        }
        
        for (String aeropuerto : AeropuertosOrigen) {
            if (vuelosSalientes.get(aeropuerto) != 0) {
                conectividadesAeropuertosOrigen.put(aeropuerto,
                        conectividadesAeropuertosOrigen.get(aeropuerto) *
                                vuelosSalientesAEspanya.get(aeropuerto)
                                / vuelosSalientes.get(aeropuerto));
            }
        }
	}
	
	public static void leerDatosListaConexiones(Map<String, Set<String>> listaConexionesPorAeropuertoEspanyol, 
			List<String> AeropuertosEspanyoles, Map<List<String>, Integer> conexiones) {
		for (String aeropuerto : AeropuertosEspanyoles) {
            Set<String> aux = Collections.emptySet();
            listaConexionesPorAeropuertoEspanyol.put(aeropuerto, aux);
        }
        for (String aeropuerto : AeropuertosEspanyoles) {
            for (List<String> conexion : conexiones.keySet()) {
                if (conexion.get(1).equals(aeropuerto)) {
                    Set<String> aux = new HashSet<>();
                    if (listaConexionesPorAeropuertoEspanyol.get(aeropuerto).size() != 0) {
                        aux = new HashSet<>(listaConexionesPorAeropuertoEspanyol.get(aeropuerto));
                    }
                    aux.add(conexion.get(0));
                    listaConexionesPorAeropuertoEspanyol.put(aeropuerto, aux);
                }
            }
        }
	}

}
