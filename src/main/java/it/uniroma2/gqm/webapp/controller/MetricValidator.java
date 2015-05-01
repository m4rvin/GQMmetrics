package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.CombinedMetric;
import it.uniroma2.gqm.model.DefaultOperation;
import it.uniroma2.gqm.model.MetricOutputValueTypeEnum;
import it.uniroma2.gqm.model.QuestionMetric;
import it.uniroma2.gqm.model.RangeOfValues;
import it.uniroma2.gqm.model.SimpleMetric;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component(value = "metricValidator")
public class MetricValidator implements Validator
{
	 
	 private static final String FORMULA_FIELD = "formula";

	 public static final String METRIC_PATTERN_MIDDLE = "[^\"](_[a-zA-Z0-9]+_)[^\"]";
	 public static final String METRIC_PATTERN_BEGIN = "^(_[a-zA-Z0-9]+_)[^\"]";
	 public static final String METRIC_PATTERN_END = "[^\"](_[a-zA-Z0-9]+_)$";
	 public static final String METRIC_PATTERN_ALONE = "^(_[a-zA-Z0-9]+_)$";

	 public static final String ENTITY_CLASS_PATTERN = "\"(.*?)\"";
	 
	 private static final String THIS_PATTERN = "(_){1}(this){1}(_){1}";
	 private static final String MULTIPLICATION_PATTERN = "(\\$){2}|\\d+(\\$)|(\\$)\\d+|(\\$)(£)|\\d+(£)|(£)\\d+|\\)\\d+|\\)(\\$)|\\)(£)";
	 private static final String MEMBERSHIP_PATTERN = "#\"(_[^\\s|^\"]+_)\"";
	 private static final String VALID_RESULT_PATTERN = "[\\d|\\.|\\)|\\(|\\,|£|\\?|\\$]*";
	 
	 private static final String METRIC_REPLACEMENT = "$";
	 private static final String OPERATION_REPLACEMENT = "£";
	 private static final String MEMBERSHIP_REPLACEMENT = "?$";
	 private static final String OPERATOR_REPLACEMENT = "?";
	 
	 private static final String _THIS_ = "_this_";
	 
	 private static final String UNSUPPORTED_THIS_ERROR = "The use of _this_ value is not supported in CombinedMetrics. Please create a SimpleMetric that accomplish your needs and retry.";
	 private static final String MISSING_THIS_ERROR = "Missing reference to _this_ value";
	 private static final String SYNTAX_ERROR = "Syntax errors in formula declaration";
	 private static final String INVALID_MEMBERSHIP_ERROR = "Invalid use of membership operator";
	 private static final String MISSING_MEASUREMENT_SCALE_ERROR = "Missing measurement scale, validation of formula is not possible";
	 private static final String INVALID_OPERATIONS_ERROR = "Usage of invalid operations";
	 private static final String CLASS_OUT_OF_RANGE_ERROR = "Entity class out of range of values";
	 
	 
	 @Autowired
	 private FormulaHandler handler;
	 
	 @Autowired
	 HttpSession session;

	 @Override
	 public boolean supports(Class<?> clazz)
	 {
		  return SimpleMetric.class.equals(clazz) || CombinedMetric.class.equals(clazz);
	 }
	 
	 @Override
	 public void validate(Object target, Errors errors)
	 {
		  AbstractMetric metric = (AbstractMetric) target;
		  
		  String metric_name = metric.getName();
		  if(!metric_name.matches("^[\\d\\p{L}]+$"))
				errors.rejectValue("name", "name", "name must be composed only by letters or numbers");
		  

		  validateQuestions(metric, errors);
		  
		  String formula = metric.getFormula();

		  formula = formula.replaceAll(" ", "");

		  if (metric instanceof SimpleMetric && extractPattern(formula, THIS_PATTERN, 0).size() == 0)
		  {
				errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, MISSING_THIS_ERROR);
				return;
		  }
		  if (metric instanceof CombinedMetric && extractPattern(formula, THIS_PATTERN, 0).size() != 0)
		  {
				errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, UNSUPPORTED_THIS_ERROR);
				return;
		  }
		  if (!validateFormulaSyntax(formula, metric.getClass(), errors))
				return;

		  //formula = formula.replaceAll(METRIC_PATTERN_MIDDLE, METRIC_REPLACEMENT);
		  //formula = formula.replaceAll(METRIC_PATTERN_BEGIN, METRIC_REPLACEMENT);
		  //formula = formula.replaceAll(METRIC_PATTERN_END, METRIC_REPLACEMENT);
		  formula = substituteMetrics(formula);
		  
		  checkAllowedOperationsAndOperators(formula, metric, errors);
	 }
	 
	 @SuppressWarnings("unchecked")
	 public void validateQuestions(AbstractMetric metric, Errors errors)
	 {
		  Set<QuestionMetric> sessionQM = (Set<QuestionMetric>) session.getAttribute("currentQuestions");
		  Set<QuestionMetric> currentQM = metric.getQuestions();
		  
		  if(sessionQM == null) //creation case
				return;
		  
		  if(currentQM == null)
				errors.rejectValue("questions", "questions", "cannot deselect a question from a metric");
		  
		  for(QuestionMetric qm : sessionQM)
		  {
				if(!currentQM.contains(qm))
				{
					 errors.rejectValue("questions", "questions", "cannot deselect a question from a metric");
					 metric.setQuestions(sessionQM);
					 break;
				}
		  }
		//  session.removeAttribute("currentQuestions");
		  
	 }

	 
	 /**
	  * Check if the formula is well written
	  * @param formula formula whose syntax has to be checked
	  * @param metricClass class of the input metric
	  * @param errors Map where to save possible errors
	  * @return true no errors occurred, false otherwise
	  */
	 public static boolean validateFormulaSyntax(String formula, Class<?> metricClass, Errors errors)
	 {

		  if (formula != null && formula.length() > 0)
		  {
				Set<String> metrics;
				Set<String> entityClasses = extractPattern(formula, ENTITY_CLASS_PATTERN, 1);

				if (metricClass.equals(CombinedMetric.class))
				{
					 metrics = extractPattern(formula, METRIC_PATTERN_BEGIN, 1);
					 metrics.addAll(extractPattern(formula, METRIC_PATTERN_END, 1));
					 metrics.addAll(extractPattern(formula, METRIC_PATTERN_MIDDLE, 1));
					 metrics.addAll(extractPattern(formula, METRIC_PATTERN_ALONE, 1));
				}
					 
				else //if metric is a simple metric remove only _this_ reference
				{
					 metrics = new HashSet<String>();
					 metrics.add(_THIS_);
				}
				
				Map<String, Double> fakeValues = new HashMap<String, Double>();
				for(String e : entityClasses)
				{
					 fakeValues.put(e, 1.0);
				}
				for (String m : metrics)
				{
					 fakeValues.put(m, 1.0);
				}

				formula = formula.replaceAll("\"", "");
				ExpressionBuilder expressionBuilder = new ExpressionBuilder(formula);
				expressionBuilder = FormulaHandler.addCustomOperators(expressionBuilder);
				
				try
				{
					 expressionBuilder.variables(entityClasses).variables(metrics);
					 Expression expr = expressionBuilder.build().setVariables(fakeValues);
					 ValidationResult validator = expr.validate();
					 if (!validator.isValid())
					 {
						  for (String error : validator.getErrors())
						  {
								errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, error);
						  }

						  return false;
					 }
				} catch (IllegalArgumentException e)
				{
					 errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, SYNTAX_ERROR);
					 return false;
				}
				return true;
		  }
		  return false;
	 }
	 
	 /**
	  * Check if the formula contains only allows operations and operators
	  * @param formula formula whose operators and operations have to be checked
	  * @param metric AbstractMetric the formula belongs to
	  * @param errors Map where to save possible errors
	  * @return
	  */
	 public boolean checkAllowedOperationsAndOperators(String formula, AbstractMetric metric, Errors errors )
	 {
		  boolean multiplication = false;
		  boolean booleanFormula = false;
		  
		  metric.setOutputValueType(null);

		  if (metric.getMeasurementScale() != null)
		  {
				for (DefaultOperation operation : metric.getMeasurementScale().getOperations()) //iterate over all the allowed operators and operations
				{
					 if (operation.getOperation().equals("multiplication"))
						  multiplication = true;
					 
					 String regex;
					 try //retrive the regex from the operations map
					 {
						  regex = this.handler.getOperations().get(operation.getOperation());
						  formula = formula.replaceAll(regex, OPERATION_REPLACEMENT); //replace the operation with £
						  
					 } catch (Exception e) //the operation is an operator
					 {
						  regex = this.handler.getOperators().get(operation.getOperation());
						  
						  //membership custom validation
						  if (operation.getOperation().equals("membership")) 
						  {
								int membershipCount = StringUtils.countMatches(formula, "#");
								if(membershipCount > 0) //formula uses membership
									 booleanFormula = true;
								Set<String> membershipPatterns = extractPattern(formula, MEMBERSHIP_PATTERN, 0); //extract every membership pattern
								if(membershipPatterns.size() != membershipCount) //check if there are the same number of # and classes
								{
									 errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, INVALID_MEMBERSHIP_ERROR);
									 return false;
								}

								for(String pattern : membershipPatterns)
								{
									 try
									 {
										  if(!checkMembershipValidity(pattern, metric)) //check if the pattern is valid, i.e. the class is inside the range of values
											 {
												  errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, CLASS_OUT_OF_RANGE_ERROR);
												  return false;
											 }
												 
											 else
												 formula = formula.replace(pattern, MEMBERSHIP_REPLACEMENT); //replace the pattern with ?$ (in order to check implicit multiplication)
									 }
									 catch(NumberFormatException ex)
									 {
										  errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, CLASS_OUT_OF_RANGE_ERROR);
										  return false;
									 }
									 
								}						
						  }
						  //regular operator substitution
						  else
						  {
								Pattern pattern = Pattern.compile(regex);
								Matcher regexMatcher = pattern.matcher(formula);
								
								while(regexMatcher.find())
								{
									 if(isBooleanOperator(operation.getOperation())) //check if the operator is boolean
										  booleanFormula = true;
									 String match = formula.substring(regexMatcher.start(), regexMatcher.end());
									 match = match.replace(regexMatcher.group(1), OPERATOR_REPLACEMENT);
									 formula = formula.replace(formula.substring(regexMatcher.start(), regexMatcher.end()), match);
								}
									 
						  }
					 }
				}
				if ((formula.length() > 0 && !formula.matches(VALID_RESULT_PATTERN)) || matchPattern(formula, MULTIPLICATION_PATTERN) && !multiplication)
				{
					 errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, INVALID_OPERATIONS_ERROR);
					 return false;
				}
				//set the right output value
				if(booleanFormula)
					 metric.setOutputValueType(MetricOutputValueTypeEnum.BOOLEAN);
				else
					 metric.setOutputValueType(MetricOutputValueTypeEnum.NUMERIC);
				return true;
		  }
		  errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, MISSING_MEASUREMENT_SCALE_ERROR);
		  return false;
	 
	 }
	 
	 /**
	  * Generic function to extract a given pattern from a string(formula)
	  * @param formula
	  * @param patternToExtract
	  * @return Set<String>
	  */
	 public static Set<String> extractPattern(String formula, String patternToExtract, int group)
	 {
		  Set<String> result = new HashSet<String>();
		  Pattern pattern = Pattern.compile(patternToExtract);
		  Matcher matcher = pattern.matcher(formula);

		  while (matcher.find())
		  {
				result.add(matcher.group(group));
		  }
		  return result;
	 }
	 
	 /**
	  * Generic function to match a given pattern on a string(formula)
	  * @param formula
	  * @param patternToMatch
	  * @return true if a match is found, false otherwise
	  */
	 public static boolean matchPattern(String formula, String patternToMatch)
	 {
		  Pattern pattern = Pattern.compile(patternToMatch);
		  Matcher matcher = pattern.matcher(formula);

		  boolean result = matcher.find();
		  return result;
	 }
	 
	 public static String substituteMetrics(String formula)
	 {
		  String[] patterns = {METRIC_PATTERN_BEGIN, METRIC_PATTERN_END, METRIC_PATTERN_MIDDLE, METRIC_PATTERN_ALONE};
		  Pattern pattern; 
		  Matcher regexMatcher;
		  for(String p : patterns)
		  {
				pattern = Pattern.compile(p);
				regexMatcher = pattern.matcher(formula);
				
				while(regexMatcher.find())
				{
					 formula = formula.replace(regexMatcher.group(1), METRIC_REPLACEMENT);
				}
		  }
		 return formula;
	 }
	 
	 /**
	  * 
	  * @param pattern the pattern to be checked i.e. #"entityclass"
	  * @param metric the current metric
	  * @return true id entityClass is an acceptable value according to the metric, i.e if entityClass in inside metric's range of values, false otherwise
	  */
	 public static boolean checkMembershipValidity(String pattern, AbstractMetric metric)
	 {
		  String entityClass = pattern.replace("#", "");
		  entityClass = entityClass.replace("_", "");
		  entityClass = entityClass.replaceAll("\"", "");
		  RangeOfValues rov = metric.getMeasurementScale().getRangeOfValues();
		  return rov.isIncluded(entityClass, true);	  
	 }
	 
	 public boolean isBooleanOperator(String operator)
	 {
		  if(!operator.equals("addition") && !operator.equals("subtraction") && !operator.equals("multiplication") && !operator.equals("ratio")) //boolean operator
				return true;
		  return false;
	 }

}
