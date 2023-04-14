package com.nsgaiii.nsgaiiidemo.App;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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

import com.jogamp.common.util.Bitfield.Util;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.nsgaiii.nsgaiiidemo.App.Modelo.Individuo;
import com.nsgaiii.nsgaiiidemo.App.Modelo.ReferencePoint;
import com.nsgaiii.nsgaiiidemo.App.Operadores.OperadorReemplazo;
import com.nsgaiii.nsgaiiidemo.App.Utils.Utils;

public class VisualizarFichero extends AWTAbstractAnalysis{
	
	static List<Individuo> frenteDePareto;
	static List<Individuo> indSimplex;
	static List<Individuo> solD;
    public static void main( String[] args ) throws Exception
    {
    	
    	List<Individuo> lista = new ArrayList<>();
    	String nombre = "dtlz120230414142300.csv";
    	
    	String nombreSimplex = "dtlz120230323151118.csv";
    	
    	String nombreSolD = "simplex.csv";
        
    	frenteDePareto = Utils.leerCSV(nombre);
    	indSimplex = Utils.leerCSV(nombreSimplex);
    	//solD = Utils.leerCSV(nombreSolD);
    	
    	AnalysisLauncher.open(new VisualizarFichero());
    }
	

	@Override
	public void init() {
	    float x;
	    float y;
	    float z;
	    List<ReferencePoint> referencePoints = new Vector<>();
    	(new ReferencePoint()).generateReferencePoints(referencePoints, 2, 9);
	    
	    Coord3d[] pointsPareto = new Coord3d[frenteDePareto.size() + 1 + indSimplex.size() + referencePoints.size()];
	    Color[] colorsPareto = new Color[frenteDePareto.size() + 1 + indSimplex.size() + referencePoints.size()];
	    
	    for (int i = 0; i < frenteDePareto.size(); i++) {
	    	
	    	
		      x = frenteDePareto.get(i).getObjetivos().get(0).floatValue();
		      y = frenteDePareto.get(i).getObjetivos().get(1).floatValue();
		      z = frenteDePareto.get(i).getObjetivos().get(2).floatValue();
		      pointsPareto[i] = new Coord3d(x, y, z);
		      colorsPareto[i] = Color.RED;
		}
	    pointsPareto[frenteDePareto.size()] = new Coord3d(0, 0, 0);
	    colorsPareto[frenteDePareto.size()] = Color.BLACK;
	    
	    for (int i = 0; i < indSimplex.size(); i++) {
	    	
	    	
		      x = indSimplex.get(i).getObjetivos().get(0).floatValue();
		      y = indSimplex.get(i).getObjetivos().get(1).floatValue();
		      z = /*indSimplex.get(i).getObjetivos().get(2).floatValue()*/0;
		      pointsPareto[i + frenteDePareto.size() + 1] = new Coord3d(x, y, z);
		      colorsPareto[i + frenteDePareto.size() + 1] = Color.BLUE;
		}
	    
	    for (int i = 0; i < referencePoints.size(); i++) {
	    	
	    	
		      x = referencePoints.get(i).position.get(0).floatValue();
		      y = referencePoints.get(i).position.get(1).floatValue();
		      z = /*referencePoints.get(i).position.get(2).floatValue()*/0;
		      pointsPareto[i + frenteDePareto.size() + 1 + indSimplex.size()] = new Coord3d(x, y, z);
		      colorsPareto[i + frenteDePareto.size() + 1 + indSimplex.size()] = Color.GREEN;
		}
	    

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
