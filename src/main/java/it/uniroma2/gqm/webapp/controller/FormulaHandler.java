package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.CollectingTypeEnum;
import it.uniroma2.gqm.model.CombinedMetric;
import it.uniroma2.gqm.model.Measurement;
import it.uniroma2.gqm.model.MetricOutputValueTypeEnum;
import it.uniroma2.gqm.model.QuestionMetric;
import it.uniroma2.gqm.model.QuestionMetricStatus;
import it.uniroma2.gqm.model.RangeOfValues;
import it.uniroma2.gqm.model.SimpleMetric;
import it.uniroma2.gqm.service.ComplexMetricManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
		  operations.put("euler number power", "exp");
		  operations.put("nearest lower integer", "floor");
		  operations.put("natural logarithm", "log");
		  operations.put("base 10 logarithm", "log10");
		  operations.put("base 2 logarithm", "log2");
		  operations.put("sine", "sin");
		  operations.put("hyperbolic sine", "sinh");
		  operations.put("square root", "sqrt");
		  operations.put("tangent", "tan");
		  operations.put("hyperbolic tangent", "tanh");
		  operators.put("multiplication", "[^e]([x|*])[^p]");
		  operators.put("addition", "(\\+){1}");
		  operators.put("subtraction", "(-){1}");
		  operators.put("ratio", "([%|/]){1}");
		  operators.put("power", "(\\^){1}");
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
	  * @param exprBuilder
	  *            ExpressionBuilder which has to be enriched with custom
	  *            operator
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

	 /**
	  * Return the result of the simple metric passed as input
	  * 
	  * @param metric
	  * @return
	  * @throws IllegalArgumentException
	  */
	 public static Double evaluateFormula(SimpleMetric metric) throws IllegalArgumentException
	 {

		  Double input_value;
		  RangeOfValues rov = metric.getMeasurementScale().getRangeOfValues();
		  String metricFormula = metric.getFormula();

		  // retrieve measured value from SINGLEVALUE metric
		  if (metric.getCollectingType().equals(CollectingTypeEnum.SINGLE_VALUE))
		  {

				String measured_value = metric.getMeasurements().iterator().next().getValue();

				if (rov.isNumeric())
				{
					 input_value = Double.valueOf(measured_value);
				} else
				{
					 input_value = rov.getStringValueAsNumberByIndex(measured_value);
				}
		  }
		  // retrieve measured values from MULTIPLEVALUE metric
		  else
		  {
				Set<Measurement> collected_measurements = metric.getMeasurements();
				List<Double> collected_measurement_values = new ArrayList<Double>();

				for (Measurement m : collected_measurements)
				{
					 if (rov.isNumeric())
					 {
						  collected_measurement_values.add(Double.valueOf(m.getValue()));
					 } else
					 {
						  collected_measurement_values.add(rov.getStringValueAsNumberByIndex(m.getValue()));
					 }
				}
				input_value = AggregatorHandler.executeAggregator(metric.getAggregator(), collected_measurement_values);
				//TODO
		//		input value a stringa o numeric a seconda del tipo di metrica (undef/numeric)
		  }

		  // metric is a simple metric add only _this_ reference & membership
		  // classes
		  Set<String> metric_variables = new HashSet<String>();
		  metric_variables.add("_this_");


		  Map<String, Double> values = new HashMap<String, Double>();
		  
		  Set<String> entityClasses = MetricValidator.extractPattern(metricFormula, MetricValidator.ENTITY_CLASS_PATTERN, 1);
		  metricFormula = metricFormula.replaceAll("\"", "");
		  
		  ExpressionBuilder expressionBuilder = new ExpressionBuilder(metricFormula);

		  expressionBuilder = expressionBuilder.variables(metric_variables);

		  // ---------MEMBERSHIP CLASSES SUBSTITUTION------------
		  setClassesInExpressionBuilder(entityClasses, rov, expressionBuilder, values);
		  // ---------MEMBERSHIP CLASSES SUBSTITUTION END---------

		  values.put("_this_", input_value);

		  expressionBuilder = FormulaHandler.addCustomOperators(expressionBuilder);
		  Expression expr = expressionBuilder.build().setVariables(values);

		  // evaluate metric formula
		  Double result = new Double(expr.evaluate());

		  System.out.println("formula:  " + metricFormula);
		  System.out.println("formula test result=" + result + "\n\n");

		  return result;
	 }


	 
	 public static boolean evaluateFormula(CombinedMetric metric, ComplexMetricManager metricManager)
	 {
		  //if(!checkMetricEvaluability(metric))
			//  return true; //metric is not evaluable, but this is not an error in formula evaluation, so return true.
		 
		  Set<AbstractMetric> composers = metric.getComposedBy();
		  String metricFormula = metric.getFormula();
		  RangeOfValues rov = metric.getMeasurementScale().getRangeOfValues();
		  Double currentActualValue = metric.getConvertedActualValue(rov);

		  Set<String> metric_variables = new HashSet<String>();
		  Map<String, Double> values = new HashMap<String, Double>();
		  
		  Set<String> entityClasses = MetricValidator.extractPattern(metricFormula, MetricValidator.ENTITY_CLASS_PATTERN, 1);
		  
		  metricFormula = metricFormula.replaceAll("\"", "");
		  
		  ExpressionBuilder expressionBuilder = new ExpressionBuilder(metricFormula);

		  // ---------CLASSES SUBSTITUTION------------
		  setClassesInExpressionBuilder(entityClasses, rov, expressionBuilder, values);
		  // ---------CLASSES SUBSTITUTION END---------

		  boolean still_evaluable = true;
		  Iterator<AbstractMetric> it =  composers.iterator();
		  while(it.hasNext())//check every composer. Exit only if you find a null-evaluated one (null-evaluated composers lead the result response) or this metric and a composer one have an undefined result (already known undefined states do not need to be propagated). 
		  {
			  	AbstractMetric composer = it.next();
			  	
			  	//retrive the correct numeric value corresponding to the composer actual value
				Double composerActualValue = composer.getConvertedActualValue(rov);
				
				
				if (composerActualValue != null && !composerActualValue.equals(Double.MIN_VALUE))
				{
					 String metric_variable_name = "_" + composer.getName() + "_";
					 metric_variables.add(metric_variable_name);
					 values.put(metric_variable_name, composerActualValue);
					 //TODO
		//			 mettere stringa o valore numerico
				} 
				else if (composerActualValue == null)
				{
					 return true; //the input does not generate an error, but it is impossible to evaluate the formula because it lack some other inputs. This metric still has a null value.
				}
				else //a composerActualValue is undefined (MIN_VALUE)
				{
					 if(currentActualValue != null && currentActualValue.equals(Double.MIN_VALUE))
						  return true; //this metric has a value already set to MIN_VALUE (received by a composer), do not need to propagate neither to show an error
					 
					 //the value of one composer is undefined and this metric has a null or valid (but old) value
					 if(still_evaluable)
						 still_evaluable = false;
				}
		  }
		  
		  if(!still_evaluable)
		  {//set metric to the new value=MIN_VALUE, only after checking all composers and not founding a null-evaluated one.
			  metric.setActualValue(String.valueOf(Double.MIN_VALUE));
			  metric = (CombinedMetric) metricManager.save(metric);
			  currentActualValue = Double.MIN_VALUE;
		  }

		  if (currentActualValue == null || !currentActualValue.equals(Double.MIN_VALUE) || still_evaluable) //is null or an acceptable value or need to be evaluated
		  {
				expressionBuilder = expressionBuilder.variables(metric_variables);

				expressionBuilder = FormulaHandler.addCustomOperators(expressionBuilder);
				Expression expression = expressionBuilder.build().setVariables(values);

				Double result = expression.evaluate();

				// result validation

				if (metric.getOutputValueType() == MetricOutputValueTypeEnum.BOOLEAN)
				{
					 if (result != 0 && result != 1) // a boolean result must be 0
																// (false) or 1 (true)
						  result = Double.MIN_VALUE;
				}
				if (metric.getOutputValueType() != MetricOutputValueTypeEnum.BOOLEAN && !rov.isIncluded(result, false)) // output
					 result = Double.MIN_VALUE;

				// result validated, must be propagated above in the hierarchy

				//set the correct actual value according to the rov
				if(metric.getOutputValueType() == MetricOutputValueTypeEnum.BOOLEAN)
					 metric.setActualValue(String.valueOf(result));
				else	 
					 metric.setActualValue(String.valueOf(rov.getValueByIndex(result)));
				
				metric = (CombinedMetric) metricManager.save(metric);				
				
				//TODO
//				imposta result come stringa o numeric a seconda del tipo di metric che sono
//				metric.setActualValue(result);
//				metric = (CombinedMetric) metricManager.save(metric);
		  }

		  Set<CombinedMetric> composedByMetrics = metric.getComposerFor();

		  boolean evaluation_propagation_error = false;
		  //propagate evaluation and look for errors
		  for (CombinedMetric composedByMetric : composedByMetrics)
				if(!evaluateFormula(composedByMetric, metricManager))
					evaluation_propagation_error = true;

		  if(metric.getConvertedActualValue(rov).equals(Double.MIN_VALUE) || evaluation_propagation_error)
				return false; //the last (or any) metric returned an error
		  else
				return true; // composedByMetrics is empty, exit condition reached

	 }
	 
	 
	 public static void setClassesInExpressionBuilder(Set<String> entityClasses, RangeOfValues rov, ExpressionBuilder expressionBuilder, Map<String, Double> values){
		

		  if (entityClasses.size() != 0)
		  {
				expressionBuilder.variables(entityClasses);

				// add corresponding value of each class element
				for (String s : entityClasses)
				{
					 String s_value = s.replaceAll("_", "");
					 Double numeric_value = rov.getStringValueAsNumberByIndex(s_value);
					 values.put(s, numeric_value);
				}
		  }
	 }
	 
	 private static boolean checkMetricEvaluability(CombinedMetric metric)
	 {
		 //check if metric has been linked to at least a question and the relationship has been approved
		 Iterator<QuestionMetric> qmIt = metric.getQuestions().iterator();
		 if(!qmIt.hasNext())
			 return false; //the formula cannot be evaluated because it is not linked to any question.
		 else{
			 boolean evaluable = false;
			 while(qmIt.hasNext() && !evaluable){
				 if(qmIt.next().getStatus().equals(QuestionMetricStatus.APPROVED))
					 evaluable = true;
			 }
			 if(!evaluable)
				 return false;
			 else
				 return true;
		 }
	}

}
