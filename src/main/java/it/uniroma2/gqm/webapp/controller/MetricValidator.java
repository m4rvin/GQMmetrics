package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.CombinedMetric;
import it.uniroma2.gqm.model.DefaultOperation;
import it.uniroma2.gqm.model.SimpleMetric;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.google.common.collect.ImmutableMap;

@Component(value = "metricValidator")
public class MetricValidator implements Validator
{
	 
	 private static Map<String, String> operations;
	 static
	 {
		  operations = new HashMap<String, String>();
		  operations.put("modulo", "(abs)\\(.+\\)");
		  operations.put("arccosine", "(acos)\\(.+\\)");
		  operations.put("arcsine", "(asin)\\(.+\\)");
		  operations.put("arctangent", "(atan)\\(.+\\)");
		  operations.put("cubic root", "(cbrt)\\(.+\\)");
		  operations.put("nearest upper integer", "(ceil)\\(.+\\)");
		  operations.put("cosine", "(cosh)\\(.+\\)");
		  operations.put("hyperbolic cosine", "(cosh)\\(.+\\)");
		  operations.put("exponentiation", "(exp)\\(.+\\)");
		  operations.put("nearest lower integer", "(floor)\\(.+\\)");
		  operations.put("natural logarithm", "(log)\\(.+\\)");
		  operations.put("base 10 logarithm", "(log10)\\(.+\\)");
		  operations.put("base 2 logarithm", "(log2)\\(.+\\)");
		  operations.put("sine", "(sin)\\(.+\\)");
		  operations.put("hyperbolic sine", "(sinh)\\(.+\\)");
		  operations.put("square root", "(sqrt)\\(.+\\)");
		  operations.put("tangent", "(tan)\\(.+\\)");
		  operations.put("hyperbolic tangent", "(tanh)\\(.+\\)");
		  operations.put("multiplication", "[*|x]{1}");
		  operations.put("addition", "(\\+){1}");
		  operations.put("subtraction", "(-){1}");
		  operations.put("ratio", "[%|/]{1}");
		  operations.put("membership", "(@){1}");
		  operations.put("greater than", "(>){1}");
		  operations.put("lower than", "(<){1}");
		  operations.put("equal", "(=){1}");
	 }

	 @Override
	 public boolean supports(Class<?> clazz)
	 {
		  return SimpleMetric.class.equals(clazz) || CombinedMetric.class.equals(clazz);
	 }

	 @Override
	 public void validate(Object target, Errors errors)
	 {
		  AbstractMetric metric = (AbstractMetric) target;

		  String formula = metric.getFormula();
		  formula = formula.replaceAll(" ", "");
		  if (formula != null && formula.length() > 0) //valida prima la sintassi e poi le operazioni all'interno
		  {
				Set<String> metrics = new HashSet<String>();
				
				if(metric instanceof CombinedMetric)
					 metrics = getUsedMetrics(formula);

				ExpressionBuilder expressionBuilder = new ExpressionBuilder(formula);
				Map<String, Double> fakeValues = new HashMap<String, Double>();
				for (String m : metrics)
				{
					 expressionBuilder.variable(m);
					 fakeValues.put(m, 1.0);
				}
				try
				{
					 Expression expr = expressionBuilder.build().setVariables(fakeValues);
					 ValidationResult validator = expr.validate();
					 if (!validator.isValid())
					 {
						  for (String error : validator.getErrors())
						  {
								errors.rejectValue("formula", "formula", error);
						  }

						  return;
					 }
				} catch (IllegalArgumentException e)
				{
					 errors.rejectValue("formula", "formula", "Syntax errors in formula declaration");
					 return;
				}
				//Valida le operazioni accettate
				if(metric.getMeasurementScale() != null)
				{
					 for(DefaultOperation operation : metric.getMeasurementScale().getOperations())
						{
						    String regex = operations.get(operation.getOperation());
							 formula = formula.replaceAll(regex, "");
						}
						if(formula.length() > 0 && !formula.matches("[\\d|\\.]*"))
							 errors.rejectValue("formula", "formula", "Usage of not allowed operations");
						return;
				}	
		  }
	 }

	 public static Set<String> getUsedMetrics(String formula)
	 {
		  Set<String> metrics = new HashSet<String>();
		  Pattern pattern = Pattern.compile("(_){1}[a-zA-Z_0-9]+(_){1}");
		  Matcher matcher = pattern.matcher(formula);

		  while (matcher.find())
		  {
				metrics.add(matcher.group());
		  }

		  return metrics;
	 }

}
