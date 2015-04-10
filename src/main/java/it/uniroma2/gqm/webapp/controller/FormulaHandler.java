package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.CollectingTypeEnum;
import it.uniroma2.gqm.model.CombinedMetric;
import it.uniroma2.gqm.model.Measurement;
import it.uniroma2.gqm.model.MetricOutputValueTypeEnum;
import it.uniroma2.gqm.model.RangeOfValues;
import it.uniroma2.gqm.model.SimpleMetric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.operator.Operator;


public class FormulaHandler
{

	 private static Map<String, String> operations;
	 private static Map<String, String> operators;
	 static
	 {
		  operations = new HashMap<String, String>();
		  operators = new HashMap<String, String>();
		  
		  operations.put("absolute value", "abs");
		  operations.put("arccosine", "acos");
		  operations.put("arcsine", "asin");
		  operations.put("arctangent", "atan");
		  operations.put("cubic root", "cbrt");
		  operations.put("nearest upper integer", "ceil");
		  operations.put("cosine", "cos");
		  operations.put("hyperbolic cosine", "cosh");
		  operations.put("exponentiation", "exp");
		  operations.put("nearest lower integer", "floor");
		  operations.put("natural logarithm", "log");
		  operations.put("base 10 logarithm", "log10");
		  operations.put("base 2 logarithm", "log2");
		  operations.put("sine", "sin");
		  operations.put("hyperbolic sine", "sinh");
		  operations.put("square root", "sqrt");
		  operations.put("tangent", "tan");
		  operations.put("hyperbolic tangent", "tanh");
		  operators.put("multiplication", "([*|x]){1}");
		  operators.put("addition", "(\\+){1}");
		  operators.put("subtraction", "(-){1}");
		  operators.put("ratio", "([%|/]){1}");
		  operators.put("membership", "(#){1}");
		  operators.put("greater than", "(>)[^=]{1}");
		  operators.put("lower than", "(<)[^=]{1}");
		  operators.put("greater or equal than", "(>=){1}");
		  operators.put("lower or equal than", "(<=){1}");
		  operators.put("equality", "[^<>](=){1}");
		  operators.put("and", "(&&){1}");
		  operators.put("or", "(\\|\\|){1}");

	 }

	 public FormulaHandler()
	 {
	 }

	 public Map<String, String> getOperations()
	 {
		  return FormulaHandler.operations;
	 }
	 
	 public Map<String, String> getOperators()
	 {
		  return FormulaHandler.operators;
	 }

	 /**
	  * 
	  * @param exprBuilder ExpressionBuilder which has to be enriched with custom operator
	  * @return the enriched ExpressionBuilder object
	  */
	 public static ExpressionBuilder addCustomOperators(ExpressionBuilder exprBuilder)
	 {
		  List<Operator> operators = new ArrayList<Operator>();

		  Operator equality = new Operator("=", 2, true, Operator.PRECEDENCE_ADDITION - 1)
		  {

				@Override
				public double apply(double... args)
				{
					 return (args[0] == args[1]) ? 1 : 0;
				}
		  };

		  operators.add(equality);

		  Operator greater = new Operator(">", 2, true, Operator.PRECEDENCE_ADDITION - 1)
		  {
				@Override
				public double apply(double... args)
				{
					 return (args[0] > args[1]) ? 1 : 0;
				}
		  };

		  operators.add(greater);

		  Operator greater_equal = new Operator(">=", 2, true, Operator.PRECEDENCE_ADDITION - 1)
		  {
				@Override
				public double apply(double... args)
				{
					 return (args[0] >= args[1]) ? 1 : 0;
				}
		  };

		  operators.add(greater_equal);

		  Operator lower = new Operator("<", 2, true, Operator.PRECEDENCE_ADDITION - 1)
		  {
				@Override
				public double apply(double... args)
				{
					 return (args[0] < args[1]) ? 1 : 0;
				}
		  };

		  operators.add(lower);

		  Operator lower_equal = new Operator("<=", 2, true, Operator.PRECEDENCE_ADDITION - 1)
		  {
				@Override
				public double apply(double... args)
				{
					 return (args[0] <= args[1]) ? 1 : 0;
				}
		  };

		  operators.add(lower_equal);

		  Operator and = new Operator("&&", 2, true, Operator.PRECEDENCE_ADDITION - 2)
		  {
				@Override
				public double apply(double... args)
				{
					 if ((args[0] != 1 && args[0] != 0) || (args[1] != 1 && args[1] != 0))
						  throw new IllegalArgumentException("Expected boolean operands, got integer ones");
					 return ((args[0] == 1.0d) && (args[1] == 1.0d)) ? 1 : 0;
				}
		  };

		  operators.add(and);

		  Operator or = new Operator("||", 2, true, Operator.PRECEDENCE_ADDITION - 2)
		  {
				@Override
				public double apply(double... args)
				{
					 if ((args[0] != 1 && args[0] != 0) || (args[1] != 1 && args[1] != 0))
						  throw new IllegalArgumentException("Expected boolean operands, got integer ones");
					 return ((args[0] == 1) || (args[1] == 1)) ? 1 : 0;
				}
		  };

		  operators.add(or);

		  Operator membership = new Operator("#", 2, true, Operator.PRECEDENCE_ADDITION + 1)
		  {
				@Override
				public double apply(double... args)
				{
					 return (args[0] == args[1]) ? 1 : 0;
				}
		  };

		  operators.add(membership);

		  return exprBuilder.operator(operators);
	 }
	 
	 public static Double evaluateFormula(SimpleMetric metric){
		 
		 Double input_value;
		 RangeOfValues rov = metric.getMeasurementScale().getRangeOfValues();

		 //retrieve measured value from SINGLEVALUE metric
		 if(metric.getCollectingType().equals(CollectingTypeEnum.SINGLE_VALUE)){
			 
			 String measured_value = metric.getMeasurements().iterator().next().getValue();
			 
			 if(rov.isNumeric()){
				 input_value = Double.valueOf(measured_value);
			 }
			 else{
				 input_value = rov.getStringValueAsNumberByIndex(measured_value);
			 }
		 }
		 //retrieve measured value from MULTIPLEVALUE metric
		 else{
			 Set<Measurement> collected_measurements = metric.getMeasurements();
			 List<Double> collected_measurement_values = new ArrayList<Double>();
			 
			 for (Measurement m : collected_measurements){
				 if(rov.isNumeric()){
					 collected_measurement_values.add(Double.valueOf(m.getValue()));
				 }
				 else{
					 collected_measurement_values.add(rov.getStringValueAsNumberByIndex(m.getValue()));
				 }
			 }
			 input_value = AggregatorHandler.executeAggregator(metric.getAggregator(), collected_measurement_values);
		 }
		 
		 
			Set<String> metric_variables;
			Set<String> entityClasses = MetricValidator.extractPattern(metricFormula, MetricValidator.ENTITY_CLASS_PATTERN, 1);

			//metric is a simple metric add only _this_ reference
			metric_variables = new HashSet<String>();
			metric_variables.add("_this_");
			
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
				List<Measurement> saved_measurements = this.measurementManager.findMeasuremntsByMetric(metric);
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
				Expression expr = expressionBuilder.build().setVariables(
						values);
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
		


		 
		 //FIXME remove
		 return null;
	 }
	 
	 //TODO
	 public static Double evaluateFormula(CombinedMetric metric){
		 return null;

	 }

}
