package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.Measurement;
import it.uniroma2.gqm.model.RangeOfValues;

import java.util.HashMap;
import java.util.Map;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component(value = "measurementValidator")
public class MeasurementValidator implements Validator {
	
    private final transient Log log = LogFactory.getLog(getClass());
	
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
			 errors.rejectValue("incompatible value type", "incompatible value type", INCOMPATIBLE_VALUE_TYPE);
			 return;
		}
	}
	
	

	public boolean checkFormulaEvaluation(Measurement measurement, Errors errors) throws NumberFormatException{

		AbstractMetric metric = measurement.getMetric();
		String input_value = measurement.getValue();
		RangeOfValues rov = metric.getMeasurementScale().getRangeOfValues();
		
		//Check input compatibility
		if(!rov.isIncluded(input_value, true)){ //input not valid
			 errors.rejectValue("incompatible input value", "incompatible input value", INCOMPATIBLE_SYMBOL_IN_INPUT_VALUE);
			 return false;
		}
		
		Double value;
		if(!rov.isRange() && !rov.isNumeric())//range of values is not numeric and not range
			value = rov.getStringValueAsNumberByIndex(input_value);
		else
			value = new Double(input_value);
		
		System.out.println("\n\nformula input value=" + value);

		
		String metricFormula = metric.getFormula();
		//formula compaction
		metricFormula = metricFormula.replaceAll(" ", "");
		if (metricFormula != null && metricFormula.length() > 0) {

			ExpressionBuilder expressionBuilder = new ExpressionBuilder(metricFormula);
			
			Map<String, Double> values = new HashMap<String, Double>();
			
			expressionBuilder.variable("_this_");
			values.put("_this_", value);
			
			try {
				Expression expr = expressionBuilder.build().setVariables(
						values);
				ValidationResult validator = expr.validate();
				if (!validator.isValid()) {
					for (String error : validator.getErrors()) {
						errors.rejectValue("formula", "formula", error);
					}
					return false;
				}
				//evaluate metric formula to retrieve result and check if it is compatible with the assigned range of values
				Double result = new Double(expr.evaluate());
				
				System.out.println("formula:  " + metricFormula);
				System.out.println("formula test result=" + result + "\n\n");
				
				//TODO FIXME membership o altre operazioni strane vanno valutate in altro modo!!
				
				//Check output compatibility
				if(!rov.isIncluded(result, false)){ //output not valid
					 errors.rejectValue("incompatible output value", "incompatible output value", INCOMPATIBLE_SYMBOL_IN_OUTPUT_VALUE);
					 return false;
				}
				else
					return true;
				
			} catch (IllegalArgumentException e) {
				errors.rejectValue("formula", "formula",
						"Syntax errors in formula declaration");
				return false;
			}
		}
		errors.rejectValue("inexisting formula", "inexisting formula", "inexisting formula");
		return false;
	}

}