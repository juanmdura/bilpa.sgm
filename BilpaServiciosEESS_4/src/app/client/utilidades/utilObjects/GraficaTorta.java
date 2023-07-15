package app.client.utilidades.utilObjects;


import java.util.ArrayList;
import com.googlecode.gchart.client.GChart;

public class GraficaTorta extends GChart{

	private double[] pieMarketShare = {};
	private String[] pieTypes = {};
	private String[] pieColors = {"green", "red", "maroon", "yellow","orange","black","OrangeRed","blue","seagreen","lime"};
	private String mouseOverColor = "red";
	private ArrayList<Integer> porcentajes = new ArrayList<Integer>();
	private ArrayList<String> nombresPorcentajes = new ArrayList<String>();

	
	public GraficaTorta(String titulo, ArrayList<Integer> porcentajes, ArrayList<String> nombres){
		this.porcentajes = porcentajes;
		porcentajes.toArray();
		this.nombresPorcentajes = nombres;
		this.setChartSize(550, 450);
		setChartTitle("<h3>"+titulo+"</h3>");
		this.setLegendVisible(false);
		getXAxis().setAxisVisible(false);
		getYAxis().setAxisVisible(false);
		getXAxis().setAxisMin(0);
		getXAxis().setAxisMax(10);
		getXAxis().setTickCount(0);
		getYAxis().setAxisMin(0);
		getYAxis().setAxisMax(10);
		getYAxis().setTickCount(0);
		double[] pieMarketShare = {0.65};
		// this line orients the center of the first slice (apple) due east
		setInitialPieSliceOrientation(0.75 - pieMarketShare[0]/2);
		for (int i=0; i < porcentajes.size(); i++) {
			addCurve();
			
			getCurve().addPoint(5,5);
			getCurve().getSymbol().setSymbolType(SymbolType.PIE_SLICE_OPTIMAL_SHADING);
			getCurve().getSymbol().setBorderColor(pieColors[i]);			
			getCurve().getSymbol().setBorderStyle("dashed");
			getCurve().getSymbol().setBackgroundColor(pieColors[i]);
			// next two lines define pie diameter in x-axis model units
			getCurve().getSymbol().setModelWidth(5);
			getCurve().getSymbol().setHeight(0);		

			getCurve().getSymbol().setFillSpacing(1);
			getCurve().getSymbol().setFillThickness(3);
			
			getCurve().getSymbol().setHovertextTemplate(GChart.formatAsHovertext(nombresPorcentajes.get(i) + ", " + porcentajes.get(i)+"%"));
			
			double pedazo = this.porcentajes.get(i);
			pedazo = pedazo /100;
			getCurve().getSymbol().setPieSliceSize(pedazo);
			getCurve().getPoint().setAnnotationText("     " + nombresPorcentajes.get(i));
			getCurve().getPoint().setAnnotationLocation(AnnotationLocation.OUTSIDE_PIE_ARC);
		}
	}
	
	
	

}