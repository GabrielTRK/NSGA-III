package com.nsgaiii.nsgaiiidemo.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.jzy3d.analysis.AWTAbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.chart.factories.AWTChartFactory;
import org.jzy3d.chart.factories.AWTPainterFactory;
import org.jzy3d.chart.factories.IChartFactory;
import org.jzy3d.chart.factories.IPainterFactory;
import org.jzy3d.chart2d.Chart2d;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot2d.primitives.LineSerie2d;
import org.jzy3d.plot3d.primitives.Drawable;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.legends.overlay.Legend;
import org.jzy3d.plot3d.rendering.legends.overlay.LegendLayout;
import org.jzy3d.plot3d.rendering.legends.overlay.OverlayLegendRenderer;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.nsgaiii.nsgaiiidemo.App.Algoritmo.Nsgaiii;
import com.nsgaiii.nsgaiiidemo.App.Lectura.LecturaDeDatos;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Individuo;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Poblacion;
import com.nsgaiii.nsgaiiidemo.App.Problemas.DTLZ1;
import com.nsgaiii.nsgaiiidemo.App.Problemas.DTLZ2;
import com.nsgaiii.nsgaiiidemo.App.Problemas.DTLZ5;
import com.nsgaiii.nsgaiiidemo.App.Problemas.DTLZ7;
import com.nsgaiii.nsgaiiidemo.App.Problemas.Problema;
import com.nsgaiii.nsgaiiidemo.App.Problemas.Vuelos;
import com.nsgaiii.nsgaiiidemo.App.Utils.Utils;



public class App extends AWTAbstractAnalysis
{
	
	static List<Individuo> frenteDePareto;
	static List<Individuo> indSimplex;
    public static void main( String[] args ) throws Exception
    {
    	
    	Map<List<String>, Integer> conexiones = new HashMap<>();
        Map<List<String>, Double> riesgos = new HashMap<>();
        Map<List<String>, Integer> vuelos = new HashMap<>();
        List<String> AeropuertosEspanyoles = new ArrayList<>();
        List<String> AeropuertosOrigen = new ArrayList<>();
        List<String> companyias = new ArrayList<>();
        Map<List<String>, Double> dineroMedio = new HashMap<>();
        Map<List<String>, Integer> pasajeros = new HashMap<>();
        Map<List<String>, Integer> pasajerosCompanyia = new HashMap<>();
        Map<List<String>, Integer> vuelosEntrantesConexion = new HashMap<>();
        Map<String, Integer> vuelosSalientesAEspanya = new HashMap<>();
        Map<String, Integer> vuelosSalientes = new HashMap<>();
        Map<String, Double> conectividadesAeropuertosOrigen = new HashMap<>();
        Map<String, Set<String>> listaConexionesPorAeropuertoEspanyol = new HashMap<>();
        Map<String, Set<String>> listaConexionesSalidas = new HashMap<>();
        
        
    	LecturaDeDatos.leerDatos(conexiones, riesgos, vuelos);
    	LecturaDeDatos.leerDatosAeropuertosEspanyoles(AeropuertosEspanyoles);
    	LecturaDeDatos.leerDatosAeropuertosOrigen(AeropuertosOrigen);
    	LecturaDeDatos.leerDatosCompanyias(companyias);
    	LecturaDeDatos.leerDatosDineroMedio(dineroMedio);
    	LecturaDeDatos.leerDatosPasajeros(pasajeros);
    	LecturaDeDatos.leerDatosPasajerosCompanyia(pasajerosCompanyia);
    	LecturaDeDatos.leerDatosConectividad(vuelosEntrantesConexion, vuelosSalientesAEspanya,
    			vuelosSalientes, conectividadesAeropuertosOrigen, conexiones, AeropuertosOrigen);
    	LecturaDeDatos.leerDatosListaConexiones(listaConexionesPorAeropuertoEspanyol, AeropuertosEspanyoles, conexiones);
    	LecturaDeDatos.leerDatosListaConexionesSalidas(listaConexionesSalidas, AeropuertosOrigen, conexiones);
    	
    	//Declarar problema y pasarselo al algoritmo
    	
    	Problema problema = new Vuelos(conexiones.keySet().size(), riesgos, conexiones, vuelos, 
    			AeropuertosEspanyoles, AeropuertosOrigen,
    			companyias, dineroMedio, pasajeros, pasajerosCompanyia,
    			vuelosEntrantesConexion, vuelosSalientesAEspanya, 
    			vuelosSalientes, conectividadesAeropuertosOrigen,
    			listaConexionesPorAeropuertoEspanyol, listaConexionesSalidas);
    	
    	
    	//Indicar parámetros del problema y algoritmo
    	int numeroDeIndividuos = 12;
    	//int numeroDeVariables = 7;
    	int numeroDeGeneraciones = 200000;
    	double indiceDeDistribucionM = 20.0;
    	double indiceDeDistribucionC = 30.0;
    	double probabilidadDeCruce = 1.0;
    	double probabilidadDeMutacion = 1.0 / problema.getNumVariables();
    	//double probabilidadDeMutacion = 1.0 / numeroDeVariables;
    	int divisiones = 3;
    	//int numeroDeObjetivos = 3;
    	
    	//Problema problema = new DTLZ1(numeroDeVariables, numeroDeObjetivos);
    	long startTime = System.nanoTime();
    	
        Nsgaiii nsgaiii = new Nsgaiii(numeroDeIndividuos, 
        		numeroDeGeneraciones, indiceDeDistribucionC,
        		indiceDeDistribucionM, probabilidadDeCruce, probabilidadDeMutacion,
        		 divisiones, problema);
        
        //Ejecutar algoritmo
        frenteDePareto = nsgaiii.ejecutarNSGAIII();
        
        
        long elapsedTime = System.nanoTime() - startTime;
        if (elapsedTime / 1000000000 >= 60) {
        	elapsedTime = (elapsedTime / 1000000000) / 60;
        	System.out.println("Tiempo de ejecucion en minutos: "
                    + elapsedTime);
        } else {
        	elapsedTime = (elapsedTime / 1000000000);
        	System.out.println("Tiempo de ejecucion en segundos: "
                    + elapsedTime);
        }
        
        //Guarda resultados en un csv y crea un diagrama de dispersión
        String nombre = Utils.crearCSVConObjetivos(frenteDePareto, problema.getNombre());
    	frenteDePareto = Utils.leerCSV(nombre);
    	
    	//String nombreSimplex = "simplex.csv";
    	
    	//indSimplex = Utils.leerCSV(nombreSimplex);
        
        AnalysisLauncher.open(new App());
        
    }

	@Override
	public void init() {
	    float x;
	    float y;
	    float z;
	    
	    //Coord3d[] pointsPareto = new Coord3d[frenteDePareto.size() + 1 + indSimplex.size()];
	    //Color[] colorsPareto = new Color[frenteDePareto.size() + 1 + indSimplex.size()];
	    Coord3d[] pointsPareto = new Coord3d[frenteDePareto.size() + 1];
	    Color[] colorsPareto = new Color[frenteDePareto.size() + 1];
	    
	    
	    //Obtener coordenadas a partir de los valores objetivo
	    
	    for (int i = 0; i < frenteDePareto.size(); i++) {
	    	
	    	
		      x = frenteDePareto.get(i).getObjetivos().get(0).floatValue();
		      //y = Utils.mediaDeValoresObjetivo(frenteDePareto.get(i).getObjetivos().subList(1, 5)).floatValue();
		      y = frenteDePareto.get(i).getObjetivos().get(1).floatValue();
		      z = frenteDePareto.get(i).getObjetivos().get(2).floatValue();
		      pointsPareto[i] = new Coord3d(x, y, z);
		      colorsPareto[i] = Color.RED;
		}
	    pointsPareto[frenteDePareto.size()] = new Coord3d(0, 0, 0);
	    colorsPareto[frenteDePareto.size()] = Color.BLACK;
	    
	    /*for (int i = 0; i < indSimplex.size(); i++) {
	    	
	    	
		      x = indSimplex.get(i).getObjetivos().get(0).floatValue();
		      y = indSimplex.get(i).getObjetivos().get(1).floatValue();
		      z = indSimplex.get(i).getObjetivos().get(2).floatValue();
		      pointsPareto[i + frenteDePareto.size() + 1] = new Coord3d(x, y, z);
		      colorsPareto[i + frenteDePareto.size() + 1] = Color.BLUE;
		}*/

	    Scatter scatter = new Scatter(pointsPareto, colorsPareto);
	    scatter.setWidth(5);

	    Quality q = Quality.Advanced();
	    // q.setPreserveViewportSize(true);
	    
	    GLCapabilities c = new GLCapabilities(GLProfile.get(GLProfile.GL2));
	    IPainterFactory p = new AWTPainterFactory(c);
	    IChartFactory f = new AWTChartFactory(p);

	    chart = f.newChart(q);
	    chart.getScene().add(scatter);
	  }
}
