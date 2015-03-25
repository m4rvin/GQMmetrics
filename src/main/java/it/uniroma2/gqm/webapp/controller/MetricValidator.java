package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.CombinedMetric;
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

@Component(value = "metricValidator")
public class MetricValidator implements Validator
{

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
		  if(formula != null && formula.length() > 0)
		  {
				Set<String> metrics = getUsedMetrics(formula);

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
