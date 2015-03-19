package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.CollectingTypeEnum;
import it.uniroma2.gqm.model.Measurement;
import it.uniroma2.gqm.model.SimpleMetric;
import it.uniroma2.gqm.service.MeasurementManager;
import it.uniroma2.gqm.service.MetricManager;

import java.awt.Font;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.TextAnchor;
import org.jfree.util.Rotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/charts")
public class PieChartController {

    
    @Autowired
    private MetricManager metricManager;
    
    @Autowired
    private MeasurementManager measurementManager;
    
	@RequestMapping(value="/draw", method=RequestMethod.GET)
	public void drawChart(HttpServletResponse response,
			@RequestParam(required = false, value = "id") String id){
		SimpleMetric m = metricManager.get(new Long(id));

		if(m.getCollectingType()== CollectingTypeEnum.MULTIPLE_VALUE){
			drawLineChart(response,m);
		}else {
			drawBarChart(response, m);
		}
	}
	

	public void drawLineChart(HttpServletResponse response,SimpleMetric m){
		response.setContentType("image/png");

		List<Measurement> measurements = measurementManager.findMeasuremntsByMetric(m);
		
		DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
		for(Measurement mm:measurements){
			line_chart_dataset.addValue(mm.getValue(), mm.getMetricCode() ,(mm.getCollectingDate() + " " + mm.getCollectingTime()));
			if((mm.getMetric().getSatisfyingConditionValue()!=null && mm.getMetric().getSatisfyingConditionValue() > 0))
					line_chart_dataset.addValue(mm.getMetric().getSatisfyingConditionValue(),"Satisfying Value" ,(mm.getCollectingDate() + " " + mm.getCollectingTime()));
			if((mm.getMetric().getActualValue()!=null && mm.getMetric().getActualValue() > 0))
				line_chart_dataset.addValue(mm.getMetric().getActualValue(),"Actual Value" ,(mm.getCollectingDate() + " " + mm.getCollectingTime()));
			
		}
        JFreeChart lineChartObject=ChartFactory.createLineChart("Measured Metric (multiple value)","Metric","Value", 
        			line_chart_dataset,PlotOrientation.VERTICAL,true,true,false);  
		try {
			ChartUtilities.writeChartAsPNG(response.getOutputStream(), lineChartObject,750,400);
			response.getOutputStream().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	



	public void drawBarChart(HttpServletResponse response, SimpleMetric m){
		
		JFreeChart chart = ChartFactory.createBarChart(
                "Measured Metrics", // chart title
                "Metric", // domain axis label
                "Value", // range axis label
                createDataset(m), // data
                PlotOrientation.VERTICAL, // orientation
                false, // include legend
                false, // tooltips
                false // URLs?
                );



        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        CategoryItemRenderer renderer = plot.getRenderer();
        CategoryItemLabelGenerator generator
            = new StandardCategoryItemLabelGenerator("{0}",
                    NumberFormat.getInstance());
        renderer.setBaseItemLabelGenerator(generator);
        renderer.setBaseItemLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(
                ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));


        response.setContentType("image/png");
        //response.addHeader("Refresh", "5");

        //Write numbers on range axis just as integrals, not decimals
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); 

        try {
			ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 800, 500);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private  CategoryDataset createDataset(SimpleMetric m) {

		List<Measurement> measurements = measurementManager.findMeasuremntsByMetric(m);
		DefaultPieDataset dpd = new DefaultPieDataset();
		for(Measurement mm:measurements)
			dpd.setValue(mm.getCollectingDate() + " " + mm.getCollectingTime(), mm.getValue());
		
	    // row keys...
	    String series1 = "Measured value";
	    String series2 = "Satisfying Value"	;

	
	    // column keys...
	    String category1 = m.getCode();

	    // create the dataset...
	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

	    dataset.addValue(m.getMeasuredValue(), series1, category1);

	    dataset.addValue(m.getSatisfyingConditionValue(), series2, category1);

	    return dataset;

	}
}
