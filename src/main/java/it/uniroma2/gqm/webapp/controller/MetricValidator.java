package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.CollectingTypeEnum;
import it.uniroma2.gqm.model.CombinedMetric;
import it.uniroma2.gqm.model.DefaultOperation;
import it.uniroma2.gqm.model.MetricOutputValueTypeEnum;
import it.uniroma2.gqm.model.QuestionMetric;
import it.uniroma2.gqm.model.RangeOfValues;
import it.uniroma2.gqm.model.SimpleMetric;
import it.uniroma2.gqm.service.ComplexMetricManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

	 public static final String METRIC_PATTERN_MIDDLE = "[^\"](_[\\d\\p{L}]+_)[^\"]";
	 public static final String METRIC_PATTERN_BEGIN = "^(_[\\d\\p{L}]+_)[^\"]";
	 public static final String METRIC_PATTERN_END = "[^\"](_[\\d\\p{L}]+_)$";
	 public static final String METRIC_PATTERN_ALONE = "^(_[\\d\\p{L}]+_)$";
	 public static final String METRIC_NAME_PATTERN = "^[\\d\\p{L}]+$";

	 public static final String ENTITY_CLASS_PATTERN = "\"(_.*?_)\""; //FIXME underscore
	 
	 private static final String THIS_PATTERN = "(_){1}(this){1}(_){1}";
	 private static final String MULTIPLICATION_PATTERN = "(\\$){2}|\\d+(\\$)|(\\$)\\d+|(\\$)(£)|\\d+(£)|(£)\\d+|\\)\\d+|\\)(\\$)|\\)(£)";
	 private static final String MEMBERSHIP_PATTERN_WITH_THIS = "(_this_)#\"(_.*?_)\"";
	 private static final String LOWER_PATTERN_WITH_THIS 			= "(_this_)<\"(_.*?_)\"";
	 private static final String LOWER_OR_EQUAL_PATTERN_WITH_THIS 	= "(_this_)<=\"(_.*?_)\"";
	 private static final String GREATER_PATTERN_WITH_THIS 			= "(_this_)>\"(_.*?_)\"";
	 private static final String GREATER_OR_EQUAL_PATTERN_WITH_THIS = "(_this_)>=\"(_.*?_)\"";
	 
	 private static final String LOWER_PATTERN_WITHOUT_THIS 			= "(_[\\d\\p{L}]+_)<\"(_.*?_)\"";
	 private static final String LOWER_OR_EQUAL_PATTERN_WITHOUT_THIS 	= "(_[\\d\\p{L}]+_)<=\"(_.*?_)\"";
	 private static final String GREATER_PATTERN_WITHOUT_THIS 			= "(_[\\d\\p{L}]+_)>\"(_.*?_)\"";
	 private static final String GREATER_OR_EQUAL_PATTERN_WITHOUT_THIS 	= "(_[\\d\\p{L}]+_)>=\"(_.*?_)\"";
	 private static final String MEMBERSHIP_PATTERN_WITHOUT_THIS = "(_[\\d\\p{L}]+_)#\"(_.*?_)\"";
	 
	  
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
	 
	 private static Map<String, String> comparison_operators_with_this;
	 private static Map<String, String> comparison_operators_without_this;

	 static
	 {
		 //WITH THIS
		 comparison_operators_with_this = new HashMap<String, String>();
		 comparison_operators_with_this.put("membership", MEMBERSHIP_PATTERN_WITH_THIS);
		 comparison_operators_with_this.put("greater than", GREATER_PATTERN_WITH_THIS);
		 comparison_operators_with_this.put("lower than", LOWER_PATTERN_WITH_THIS);
		 comparison_operators_with_this.put("greater or equal than", GREATER_OR_EQUAL_PATTERN_WITH_THIS);
		 comparison_operators_with_this.put("lower or equal than", LOWER_OR_EQUAL_PATTERN_WITH_THIS);
		 
		 //WITHOUT THIS
		 comparison_operators_without_this = new HashMap<String, String>();
		 comparison_operators_without_this.put("membership", MEMBERSHIP_PATTERN_WITHOUT_THIS);
		 comparison_operators_without_this.put("greater than", GREATER_PATTERN_WITHOUT_THIS);
		 comparison_operators_without_this.put("lower than", LOWER_PATTERN_WITHOUT_THIS);
		 comparison_operators_without_this.put("greater or equal than", GREATER_OR_EQUAL_PATTERN_WITHOUT_THIS);
		 comparison_operators_without_this.put("lower or equal than", LOWER_OR_EQUAL_PATTERN_WITHOUT_THIS);
	 }
	 
	 @Autowired
	 private FormulaHandler handler;
	 
	 @Autowired
	 private ComplexMetricManager complexMetricManager;
	 
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
		  metric.setOutputValueType(null);

		  if(metric.getCollectingType().equals(CollectingTypeEnum.MULTIPLE_VALUE))
		  {
			  SimpleMetric sm = (SimpleMetric) metric;
			  if(sm.getAggregator().equals(""))
			  {
					errors.rejectValue("aggregator", "aggregator", "Aggregator must be selected");
					return;
			  }
		  }
		  String metric_name = metric.getName();
		  if(!metric_name.matches(METRIC_NAME_PATTERN))
				errors.rejectValue("name", "name", "Name must be composed only by letters or numbers");
		  

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
		  
		  String[] formula_blocks = formula.split("&&|\\|\\|"); //split formula in blocks separated by && or ||
		  boolean  unique_block = (formula_blocks.length == 1) ? true : false;
		  
		  //ho almeno 2 blocchetti (quindi ognuno deve essere un blocchetto boolean)
		   for(String block: formula_blocks)
		   {
			   /*if blocchetto == stringa vuota //FIXME fatto in validate precedentemente TODO rimuovere
			   	exit errore*/
		
			   	
			   		List<String> metrics= extractAllPattern(block, METRIC_PATTERN_BEGIN, 1);
			   		metrics.addAll(extractAllPattern(block, METRIC_PATTERN_MIDDLE, 1));
			   		metrics.addAll(extractAllPattern(block, METRIC_PATTERN_END, 1));
			   		metrics.addAll(extractAllPattern(block, METRIC_PATTERN_ALONE, 1));

			   		int undefined_counter = 0;
		   			int numeric_counter = 0;
		   			boolean boolean_metric = false;
		   			
			   		for(String m: metrics)
			   		{
			   			//retrieve metric from db e check of its type constraints (related with other metric type)
			   			m = m.replaceAll("_", ""); //clean the metric name
			   			
			   			
			   			AbstractMetric metricObj;
			   			
			   			if(m.equals("this")) //simple metric case
			   				 metricObj = null;
			   			else
			   				 metricObj = complexMetricManager.findMetricByName(m);

			   			
				   		if(metricObj != null && metricObj.getOutputValueType().equals(MetricOutputValueTypeEnum.UNDEFINED))
				   		{
				   			undefined_counter++;
				   			if(undefined_counter > 1)
				   			{
   								errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, "NO MULTIPLE UNDEFINED METRIC SUPPORTED");  //non possono esistere + metriche undefined in un blocco
   								return;
				   			}
				   			else if(undefined_counter == 1)
					   		{
   								if(!unique_block) //we are analyzing a multiple block formula
   								{
   						   			if (block.length()-2 == m.length()) // una metrica undefined non può stare da sola: almeno un operatore o altro vicino (poi controlliamo se è corretta l'operazione)
   						   			{
   						   				errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, "NO SINGLE UNDEFINED METRIC SUPPORTED");
   						   				return;
   						   			}
   						   			if(numeric_counter > 0)
   						   			{
   						   				errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, "NO MIXED UNDEFINED AND NUMERIC METRIC SUPPORTED"); //non si possono combinare metriche numeriche e undefined senza operatori di && oppure ||
   						   				return;
   						   			}
   								}
   								else
   								{
   									if (block.length()-2 == m.length()) // un unico block, lunghezza == metric name => formula è uguale a M1 (undefined)
   						   			{
   						   				metric.setOutputValueType(MetricOutputValueTypeEnum.UNDEFINED);
   						   				return;
   						   			}
   								}
					   		}
				   		}
				   		else if(metricObj != null && metricObj.getOutputValueType().equals(MetricOutputValueTypeEnum.BOOLEAN))
				   		{
				   			if (block.length()-2 != m.length())
				   			{
				   				errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, "BOOLEAN METRIC IS NOT ALONE");
				   				return;
				   			}
				   			else{
				   				if(unique_block)
					   				metric.setOutputValueType(MetricOutputValueTypeEnum.BOOLEAN);
					   			boolean_metric = true;

				   				break;//this is the only metric found, so the block is correct (it is composed by an single boolean metric)
				   			}
				   		}
				   		else if(metricObj != null && metricObj.getOutputValueType().equals(MetricOutputValueTypeEnum.NUMERIC))
				   		{
				   			numeric_counter++;
				   			if(undefined_counter > 0)
				   			{
				   				errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, "MIXED UNDEFINED AND NUMERIC METRIC UNSUPPORTED"); //non si possono combinare metriche numeriche e undefined senza operatori di && oppure ||
				   				return;
				   			}
				   		}
				   		else //simple metric case: _this_ found
				   		{
				   			 if(unique_block)
				   			 {
				   				  if(block.length()-2 == m.length())
				   				  {
				   						if(metric.getMeasurementScale().getRangeOfValues().isNumeric())
				   						{
				   							 metric.setOutputValueType(MetricOutputValueTypeEnum.NUMERIC);
				   							 return;
				   						}
				   						else
				   						{
				   							 metric.setOutputValueType(MetricOutputValueTypeEnum.UNDEFINED);
				   							 return;
				   						}
				   						
				   				  }
				   			 }
				   			 else
				   			 {
				   				  if(block.length()-2 == m.length())
				   				  {
				   						errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, "WRONG USAGE OF _THIS_");
				   				  }
				   			 }
				   		}
			   		}
			   		
			   		if(boolean_metric)
			   			break; //the block is correct. It is composed only by a metric and this metric is boolean
			   		
			   		List<String> comparison_operators_regex = new ArrayList<String>();
			   		
			   		//retrieve the correct comparison operator list
			   		comparison_operators_regex.add(this.handler.getOperators().get("greater than"));
		   			comparison_operators_regex.add(this.handler.getOperators().get("greater or equal than"));
		   			comparison_operators_regex.add(this.handler.getOperators().get("lower than"));
		   			comparison_operators_regex.add(this.handler.getOperators().get("lower or equal than"));
		   			
			   		if(numeric_counter > 0 || (metric instanceof SimpleMetric && metric.getMeasurementScale().getRangeOfValues().isNumeric()))
			   		{
			   			comparison_operators_regex.add(this.handler.getOperators().get("equality"));
			   		}
			   		else if(undefined_counter > 0 || (metric instanceof SimpleMetric && !metric.getMeasurementScale().getRangeOfValues().isNumeric()))
			   		{
			   			comparison_operators_regex.add(this.handler.getOperators().get("membership"));
			   		}
			   		
			   		int comparison_operators_number = ComparisonOperatorsInBlockEqualsToOne(comparison_operators_regex, block); //number of comparison operators found in block formula
			   		
			   		if(unique_block)
			   		{
			   			 if(comparison_operators_number == 0 && metric.getOutputValueType() == null)
			   				  metric.setOutputValueType(MetricOutputValueTypeEnum.NUMERIC);
			   			 else if(comparison_operators_number == 1 && metric.getOutputValueType() == null)
			   				  metric.setOutputValueType(MetricOutputValueTypeEnum.BOOLEAN);
			   			 else if(comparison_operators_number > 1)
			   			 {
			   				  errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, "ERROR: It is not possible to use multiple comparison operators not separated by && or ||");
			   				  return;
			   			 }
			   		}
			   		else //block case
			   		{
			   			 if(comparison_operators_number != 1)
			   			 {
			   				  errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, "ERROR: It is not possible to use multiple comparison operators not separated by && or ||");
			   				  return;
			   			 }
			   		}
			   	
						if(metric.getMeasurementScale().getRangeOfValues().isNumeric())
						{
							NUMERICcheckAllowedOperationsAndOperators(block, metric, errors); //NUMERICparsa&sostituisce operazioni alla stringa del blocchetto
						}
						else //ROV NOT numeric
						{
							NOTNUMERICcheckAllowedOperationsAndOperators(block, metric, errors); //NotNUMERICparsa&sostituisce operazioni alla stringa del blocchetto
						}
			}
		   if(formula_blocks.length > 1)
			   metric.setOutputValueType(MetricOutputValueTypeEnum.BOOLEAN); //the entire formula is boolean
		   else
		   {
			   if(metric.getOutputValueType() == null)// metric type not yet defined. It should be numeric
	   				metric.setOutputValueType(MetricOutputValueTypeEnum.NUMERIC);
		   }
		   		
	 }
	 
	 /**
	  * check if only a comparison operator is in the block string
	  * @param comparisonOperators
	  * @param block
	  * @return
	  */
	 private int ComparisonOperatorsInBlockEqualsToOne(List<String> comparisonOperators, String block)
	 {
		 Pattern pattern;
		 Matcher matcher;
		 int counter = 0;
		 for(String operator : comparisonOperators)
		 {
			 pattern = Pattern.compile(operator);
			 matcher = pattern.matcher(block);
			 while(matcher.find())
			 {
				 counter++;
			 }
		 }
		 return counter;
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
	 
	 public boolean NUMERICcheckAllowedOperationsAndOperators(String formula, AbstractMetric metric, Errors errors )
	 {
		 formula = substituteMetrics(formula);

		 boolean multiplication = false;
		  
		  if (metric.getMeasurementScale() != null)
		  {
			    //for each operation supported by this metric
				for (DefaultOperation operation : metric.getMeasurementScale().getOperations()) //iterate over all the allowed operators and operations
				{
					 String regex;
					 if (operation.getOperation().equals("multiplication"))
						  multiplication = true;
					 
					 try 
					 {
						 //substitute every occurence of the operator in this iteration
						  regex = this.handler.getOperations().get(operation.getOperation());
						  formula = formula.replaceAll(regex, OPERATION_REPLACEMENT); //replace the operation with £
						  
					 } catch (Exception e) //the operation is an operator
					 {
						  regex = this.handler.getOperators().get(operation.getOperation());
						  if (operation.getOperation().equals("membership")) 
						  {
							  continue;
						  }
						  else{
							//substitute operator (if found in the formula check if it is boolean and set the metric as booleanmetrics)
							  formula = substituteStandardOperator(regex, formula); //updated_formula used only as a temporary variable
						  }
					 }
				}
				//check if any operation is not supported (look for symbol different by £ or ? or ?$ (or any other replacement pattern)
				if ((formula.length() > 0 && !formula.matches(VALID_RESULT_PATTERN)) || matchPattern(formula, MULTIPLICATION_PATTERN) && !multiplication)
				{
					 errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, INVALID_OPERATIONS_ERROR);
					 return false;
				}
				return true;
		  }
		  errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, MISSING_MEASUREMENT_SCALE_ERROR);
		  return false;
	 }
	 
	 
	 public boolean NOTNUMERICcheckAllowedOperationsAndOperators(String formula, AbstractMetric metric, Errors errors )
	 {
		  boolean multiplication = false;
		  formula = " " + formula + " "; //put spaces to force a correct analysis  using regex expression
		  if (metric.getMeasurementScale() != null)
		  {
			  
			    //for each operation supported by this metric
				for (DefaultOperation operation : metric.getMeasurementScale().getOperations()) //iterate over all the allowed operators and operations
				{
					 String regex;
					 try 
					 {
					  regex = this.handler.getOperators().get(operation.getOperation());
					/*  
					  //membership custom validation (extract membership parameter and check if its symbol is supported by the ROV)
					  if (operation.getOperation().equals("membershsip")) 
					  {
							int membershipCount = StringUtils.countMatches(formula, "#");
							Set<String> membershipPatterns = extractPattern(formula, MEMBERSHIP_PATTERN, 0); //extract every membership pattern
							if(membershipPatterns.size() != membershipCount) //check if there are the same number of # and classes
							{
								 errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, INVALID_MEMBERSHIP_ERROR);
								 return false;
							}

							//TODO FIXME controllare se a sinistra c'è un nome di metrica!
							
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
					  } */
					  
					  if (operation.getOperation().equals("membership") || operation.getOperation().equals("greater than") || operation.getOperation().equals("lower than") || operation.getOperation().equals("greater or equal than") || operation.getOperation().equals("lower or equal than")) 
					  {
						  if(metric.getClass().equals(SimpleMetric.class))
						  {
							  String pattern = comparison_operators_with_this.get(operation.getOperation());

							  Set<String> matches= extractPattern(formula, pattern, 0); //look for the correctness of the block (i.e. this>"_asdasd_")
							  if (matches.size() > 1)
							  {
								  errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, "ERRORE in a section of the formula"); //TODO refine error
								  return false;
							  }
							  String match = matches.iterator().next(); //match contains the first (and only) occurence of the formula we're looking for (i.e. this>"_asdasd_")
							  
							  String entityclass = match.split(regex)[1];  //retrieve the right operand ( a classes i.e. "_XS_")
							  try
							  {
								  if(!checkClassesValidity(entityclass, metric)) //check if the right operand is included in ROV
								  {
									  errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, CLASS_OUT_OF_RANGE_ERROR);
									  return false;
								  }
								  else
								  {
									  formula = formula.replace(match, "$?$"); //replace the the bloc with $?$ (e.g. this>"_sadas_" now is $?$, i.e. metric-operator-metric )
									  formula = formula.trim();
								  }
							  }
							  catch(NumberFormatException ex)
							  {
								  errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, CLASS_OUT_OF_RANGE_ERROR);
								  return false;
							  }
						  }
						  if(metric.getClass().equals(CombinedMetric.class))
						  {
					  		/* NOT implemented
					  		 * check left operand is (UNDEFINED metric)
					  		check right operand is (UNDEFINED metric)
					  		OR */
							   		
							String pattern = comparison_operators_without_this.get(operation.getOperation());
					   		
					   		Set<String> matches= extractPattern(formula, pattern, 0); //look for the correctness of the block (e.g M1>"_asdasd_")
							  if (matches.size() > 1)
							  {
								  errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, "ERRORE in a section of the formula"); //TODO refine error
								  return false;
							  }
							  String match = matches.iterator().next(); //match contains the first (and only) occurence of the formula we're looking for (e.g M1>"_asdasd_")
							  
							  String entityclass = match.split(regex)[1];  //retrieve the right operand ( a classes i.e. "_XS_")
							  try
							  {
								  if(!checkClassesValidity(entityclass, metric)) //check if the right operand is included in ROV
								  {
									  errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, CLASS_OUT_OF_RANGE_ERROR);
									  return false;
								  }
								  else
								  {
									  formula = formula.replace(match, "$?$"); //replace the the bloc with $?$ (e.g. this>"_sadas_" now is $?$, i.e. metric-operator-metric )
									  formula = formula.trim();
								  }
							  }
							  catch(NumberFormatException ex)
							  {
								  errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, CLASS_OUT_OF_RANGE_ERROR);
								  return false;
							  }
						  }
					  }
					 } catch (Exception e) //the operation is an operation
					 {
						continue;
					 }
				}
				//check if any operation is not supported (look for symbol different by £ or ? or ?$ (or any other replacement pattern)
				if ((formula.length() > 0 && !formula.matches(VALID_RESULT_PATTERN)) || matchPattern(formula, MULTIPLICATION_PATTERN) && !multiplication)
				{
					 errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, INVALID_OPERATIONS_ERROR);
					 return false;
				}
				return true;
		  }
		  errors.rejectValue(FORMULA_FIELD, FORMULA_FIELD, MISSING_MEASUREMENT_SCALE_ERROR);
		  return false;
	 
	 }
	 
	 
	 
	 /**
	  * Check if the formula contains only allows operations and operators
	  * @param formula formula whose operators and operations have to be checked
	  * @param metric AbstractMetric the formula belongs to
	  * @param errors Map where to save possible errors
	  * @return
	  */
	 /*public boolean checkAllowedOperationsAndOperators(String formula, AbstractMetric metric, Errors errors )
	 {
		  boolean multiplication = false;
		  boolean booleanFormula = false;
		  
		  metric.setOutputValueType(null);

		  if (metric.getMeasurementScale() != null)
		  {
			  
			  //TODO
			  if formula == metric
						 metric.setOutputValueType(MetricOutputValueTypeEnum.UNDEFINED);
					  return true;
			  
			  
			    //for each operation supported by this metric
				for (DefaultOperation operation : metric.getMeasurementScale().getOperations()) //iterate over all the allowed operators and operations
				{
					 if (operation.getOperation().equals("multiplication"))
						  multiplication = true;
					 
					 String regex;
					 try 
					 {
						 //substitute every occurence of the operator in this iteration
						  regex = this.handler.getOperations().get(operation.getOperation());
						  
						  //TODO if ROV !NUMERIC error exit (mathematical operations not supported)
						  aassa
						  
						  formula = formula.replaceAll(regex, OPERATION_REPLACEMENT); //replace the operation with £
						  
					 } catch (Exception e) //the operation is an operator
					 {
						  regex = this.handler.getOperators().get(operation.getOperation());
						  
						  //membership custom validation (extract membership parameter and check if its symbol is supported by the ROV)
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
						  else if(!metric.getMeasurementScale().getRangeOfValues().isNumeric()){
							  
							  if operator is NOT boolean{
								  if formula.matches(regex operator) //verifico se uso quell'operatore 
								  {
									  exit (errore uso operatore matematico dove non supportato)
								  }
							  }
							  else if(operation.getOperation().equals("equality"))
							  {
								  exit (errore uso operatore EQUALITY dove non supportato. usa membership)

							  }
							  else if(operation.getOperation().equals("and") || operation.getOperation().equals("or"))
							  {
								//substitute operator (if found in the formula check if it is boolean and set the metric as booleanmetrics)
								  String updated_formula = substituteStandardOperator(regex, formula); //updated_formula used only as a temporary variable
								  if(!updated_formula.equals(formula) && isBooleanOperator(operation.getOperation()))
									  booleanFormula = true;
								  formula = updated_formula;
							  }
							  else //rov numeric operator boolean (but not && , ||)=> <,>,<=,>=,
							  {
								  if(metric.getClass().equals(SimpleMetric.class))
								  {
									   check left operand is (this)
									   check right operand is a symbol in the correct syntax
								  }
								  if(metric.getClass().equals(CombinedMetric.class))
								  {
									  		check left operand is (UNDEFINED metric)
									  		check right operand is (UNDEFINED metric)
									   OR
									   		check left operand is (UNDEFINED metric)
									   		check right operand is a symbol in the correct syntax
								  }
							}
							  
						  }
						  else{
							  //substitute operator (if found in the formula check if it is boolean and set the metric as booleanmetrics)
							  String updated_formula = substituteStandardOperator(regex, formula); //updated_formula used only as a temporary variable
							  if(!updated_formula.equals(formula) && isBooleanOperator(operation.getOperation()))
								  booleanFormula = true;
							  formula = updated_formula;
						  }
					 }
				}
				//check if any operation is not supported (look for symbol different by £ or ? or ?$ (or any other replacement pattern)
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
	 
	 }*/
	 
	 private String substituteStandardOperator(String regex, String formula){
		 
		 Pattern pattern = Pattern.compile(regex);
		 Matcher regexMatcher = pattern.matcher(formula);
			
		 while(regexMatcher.find())
		 {
			String match = formula.substring(regexMatcher.start(), regexMatcher.end());
			match = match.replace(regexMatcher.group(1), OPERATOR_REPLACEMENT);
			formula = formula.replace(formula.substring(regexMatcher.start(), regexMatcher.end()), match);
		 }
			
		 return formula;
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
	  * Generic function to extract a given pattern from a string(formula)
	  * @param formula
	  * @param patternToExtract
	  * @return Set<String>
	  */
	 public static List<String> extractAllPattern(String formula, String patternToExtract, int group)
	 {
		  List<String> result = new ArrayList<String>();
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
	 public static boolean checkMembershipValidity(String pattern, AbstractMetric metric) throws NumberFormatException
	 {
		  String entityClass = pattern.replace("#", "");
		  entityClass = entityClass.replace("_", "");
		  entityClass = entityClass.replaceAll("\"", "");
		  RangeOfValues rov = metric.getMeasurementScale().getRangeOfValues();
		  return rov.isIncluded(entityClass, true);	  
	 }
	 
	 public static boolean checkClassesValidity(String pattern, AbstractMetric metric) throws NumberFormatException
	 {
		  String entityClass = pattern.replace("_", "");
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
