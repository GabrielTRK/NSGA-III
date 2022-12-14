package com.nsgaiii.nsgaiiidemo.App.Lectura;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.nsgaiii.nsgaiiidemo.App.Constantes.Constantes;

public class LecturaDeDatos {
	
	public static void leerDatos(Map<List<String>, Integer> conexiones, Map<List<String>, Double> riesgos, Map<List<String>, Integer> vuelos) {
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

}
