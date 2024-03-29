package com.nsgaiii.nsgaiiidemo.App;

import java.util.List;
import org.jzy3d.analysis.AWTAbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.AWTChartFactory;
import org.jzy3d.chart.factories.AWTPainterFactory;
import org.jzy3d.chart.factories.IChartFactory;
import org.jzy3d.chart.factories.IPainterFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.nsgaiii.nsgaiiidemo.App.Algoritmo.Nsgaiii;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Individuo;
import com.nsgaiii.nsgaiiidemo.App.Problemas.DTLZ1;
import com.nsgaiii.nsgaiiidemo.App.Problemas.DTLZ2;
import com.nsgaiii.nsgaiiidemo.App.Problemas.DTLZ5;
import com.nsgaiii.nsgaiiidemo.App.Problemas.DTLZ7;
import com.nsgaiii.nsgaiiidemo.App.Problemas.Problema;
import com.nsgaiii.nsgaiiidemo.App.Utils.Utils;



public class App extends AWTAbstractAnalysis
{
	
	static List<Individuo> frenteDePareto;
    public static void main( String[] args ) throws Exception
    {
    	//Indicar parámetros del problema y algoritmo
    	int numeroDeIndividuos = 500;
    	int numeroDeVariables = 7;
    	int numeroDeGeneraciones = 5000;
    	double indiceDeDistribucionM = 20.0;
    	double indiceDeDistribucionC = 30.0;
    	double probabilidadDeCruce = 1;
    	double probabilidadDeMutacion = 1 / numeroDeVariables;
    	boolean minimizacion = true;
    	int divisiones = 30;
    	int numeroDeObjetivos = 3;
    	
    	Problema problema = new DTLZ7(numeroDeVariables, numeroDeObjetivos);
    	long startTime = System.nanoTime();
    	
        Nsgaiii nsgaiii = new Nsgaiii(numeroDeIndividuos, 
        		numeroDeGeneraciones, indiceDeDistribucionC,
        		indiceDeDistribucionM, probabilidadDeCruce, probabilidadDeMutacion,
        		minimizacion, divisiones, problema);
        
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
        
        AnalysisLauncher.open(new App());
        
        
    }

	@Override
	public void init() {
	    float x;
	    float y;
	    float z;
	    
	    Coord3d[] pointsPareto = new Coord3d[frenteDePareto.size() + 1];
	    Color[] colorsPareto = new Color[frenteDePareto.size() + 1];
	    
	    //Obtener coordenadas a partir de los valores objetivo
	    
	    for (int i = 0; i < frenteDePareto.size(); i++) {
		      x = frenteDePareto.get(i).getObjetivos().get(0).floatValue();
		      y = frenteDePareto.get(i).getObjetivos().get(1).floatValue();
		      z = frenteDePareto.get(i).getObjetivos().get(2).floatValue();
		      pointsPareto[i] = new Coord3d(x, y, z);
		      colorsPareto[i] = Color.RED;
		}
	    pointsPareto[frenteDePareto.size()] = new Coord3d(0, 0, 0);
	    colorsPareto[frenteDePareto.size()] = Color.BLACK;

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
