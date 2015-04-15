package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.CollectingTypeEnum;
import it.uniroma2.gqm.model.CombinedMetric;
import it.uniroma2.gqm.model.Measurement;
import it.uniroma2.gqm.model.MetricOutputValueTypeEnum;
import it.uniroma2.gqm.model.RangeOfValues;
import it.uniroma2.gqm.model.SimpleMetric;
import it.uniroma2.gqm.service.MeasurementManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component(value = "measurementValidator")
public class MeasurementValidator implements Validator {
	
    private final transient Log log = LogFactory.getLog(getClass());
	
    @Autowired
    private MeasurementManager measurementManager;
    
    
	public static final String INCOMPATIBLE_SYMBOL_IN_INPUT_VALUE = "The symbol in input value is not mappable with any symbol in (or is out from the set of) the associated Measurement Scale's Range of Values";
	public static final String INCOMPATIBLE_SYMBOL_IN_OUTPUT_VALUE = "The symbol in output value is not mappable with any symbol in (or is out from the set of) the associated Measurement Scale's Range of Values";
	public static final String INCOMPATIBLE_VALUE_TYPE = "The symbol in input is not compatible with this range type. Check your input is correct.";


	@Override
	public boolean supports(Class<?> clazz) {
		return Measurement.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		Measurement measurement = (Measurement) target;

		try{
			checkFormulaEvaluation(measurement, errors);
		}
		catch(NumberFormatException e){
			 errors.rejectValue("value", "incompatible value type", INCOMPATIBLE_VALUE_TYPE);
			 return;
		}
	}
	
	

	public boolean checkFormulaEvaluation(Measurement measurement, Errors errors) throws NumberFormatException{

		SimpleMetric metric = (SimpleMetric) measurement.getMetric();
		String input_value = measurement.getValue();
		RangeOfValues rov = metric.getMeasurementScale().getRangeOfValues();
		
		//Check input type compatibility for numeric range of values
		if(rov.isNumeric()){
			try{
				Double.valueOf(input_value);
			}
			catch(NumberFormatException e){
				//rov is numeric but the input is not
				 errors.rejectValue("value", "incompatible input value", INCOMPATIBLE_SYMBOL_IN_INPUT_VALUE);
				 return false;
			}
		}
		/*DO NOT CHECK INPUT INSIDE rov. CHECK ONLY OUTPUT INSIDE rov
		 * if(!rov.isIncluded(input_value, true)){ //input not valid
			 errors.rejectValue("value", "incompatible input value", INCOMPATIBLE_SYMBOL_IN_INPUT_VALUE);
			 return false;
		}*/
		
		//retrieve the correct input value (depending on the rangeofvalues-type)
		Double value;
		if(!rov.isRange() && !rov.isNumeric()){//range of values is not numeric and not range
			value = rov.getStringValueAsNumberByIndex(input_value);
			if(value==-1){
				//rov is not numeric but the input is (or it has not been found)
				errors.rejectValue("value", "incompatible input value", INCOMPATIBLE_SYMBOL_IN_INPUT_VALUE);
				return false;
			}	
		}
		else
			value = new Double(input_value);
		
		System.out.println("\n\nformula input value=" + value);

		
		String metricFormula = metric.getFormula();
		//formula compaction
		metricFormula = metricFormula.replaceAll(" ", "");
		if (metricFormula != null && metricFormula.length() > 0) {

			Set<String> metric_variables;
			Set<String> entityClasses = MetricValidator.extractPattern(metricFormula, MetricValidator.ENTITY_CLASS_PATTERN, 1);

			//metric is a simple metric add only _this_ reference
			metric_variables = new HashSet<String>();
			metric_variables.add("_this_");
			
			metricFormula = metricFormula.replaceAll("\"", "");
			
			ExpressionBuilder expressionBuilder = new ExpressionBuilder(metricFormula);
			
			Map<String, Double> values = new HashMap<String, Double>();
			
			expressionBuilder.variables(metric_variables);
			
			if(entityClasses.size() != 0){
				expressionBuilder.variables(entityClasses);
				
				//add corresponding value of each class element
				for(String s : entityClasses){
					String s_value = s.replaceAll("_", "");
					Double numeric_value = rov.getStringValueAsNumberByIndex(s_value);
					values.put(s, numeric_value);
				}
			}
			
			
			Double aggregated_values;
			//multiple value metric: retrieve other measurement and aggregate them with the actual one, before evaluate the formula
			if(metric.getCollectingType().equals(CollectingTypeEnum.MULTIPLE_VALUE)){
				Set<Measurement> saved_measurements = metric.getMeasurements();
				 
				//List<Measurement> saved_measurements = this.measurementManager.findMeasuremntsByMetric(metric);
				List<Double> saved_measurements_values = new ArrayList<Double>();
				for(Measurement m : saved_measurements){
					if(rov.isNumeric())
						saved_measurements_values.add(Double.valueOf(m.getValue()));
					else//rov is not numeric
						saved_measurements_values.add(rov.getStringValueAsNumberByIndex(m.getValue()));
				}
				//add last measurement (not yet saved in the system)
				saved_measurements_values.add(value);
				aggregated_values = AggregatorHandler.executeAggregator(metric.getAggregator(), saved_measurements_values);
				values.put("_this_", aggregated_values);
			}
			else//single value metric
				values.put("_this_", value);
			
			try {
				expressionBuilder = FormulaHandler.addCustomOperators(expressionBuilder);
				//FormulaHandler.setMembershipClassesInExpressionBuilder(metricFormula, rov, expressionBuilder, values);
				Expression expr = expressionBuilder.build().setVariables(values);
				/*ValidationResult validator = expr.validate();
				if (!validator.isValid()) {
					for (String error : validator.getErrors()) {
						errors.rejectValue("metric", "formula", error);
					}
					return false;
				}
				*/
				//evaluate metric formula to retrieve result and check if it is compatible with the assigned range of values
				Double result = new Double(expr.evaluate());
				
				System.out.println("formula:  " + metricFormula);
				System.out.println("formula test result=" + result + "\n\n");
				
				//TODO FIXME membership o altre operazioni strane vanno valutate in altro modo!!
				
				//Check output compatibility
				if(metric.getOutputValueType() == MetricOutputValueTypeEnum.BOOLEAN){
					if(result == 0 || result == 1) //a boolean result must be 0 (false) or 1 (true)
						return true;
					else{
						 errors.rejectValue("value", "incompatible output value", INCOMPATIBLE_SYMBOL_IN_OUTPUT_VALUE);
						 return false;
					}
				}
				if(metric.getOutputValueType() != MetricOutputValueTypeEnum.BOOLEAN && !rov.isIncluded(result, false)){ //output not valid
					 errors.rejectValue("value", "incompatible output value", INCOMPATIBLE_SYMBOL_IN_OUTPUT_VALUE);
					 return false;
				}
				else
					return true;
				
			} catch (IllegalArgumentException e) {
				errors.rejectValue("metric", "formula",
						"Syntax errors in formula declaration");
				return false;
			}
		}
		errors.rejectValue("metric", "inexisting formula", "inexisting formula");
		return false;
	}

}
