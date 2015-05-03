package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.DefaultRangeOfValuesEnum;
import it.uniroma2.gqm.model.RangeOfValues;

import org.apache.commons.lang.NumberUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@SuppressWarnings("deprecation")
@Component(value = "rangeOfValueValidator")
public class RangeOfValueValidator implements Validator
{

	 @Override
	 public boolean supports(Class<?> clazz)
	 {
		  return RangeOfValues.class.equals(clazz);
	 }

	 @Override
	 public void validate(Object target, Errors errors)
	 {
		  RangeOfValues rov = (RangeOfValues) target;

		  if (rov.isDefaultRange())
		  {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "numberType", "numberType", "Range Set is a required field");
				rov.setRange(true);
				rov.setRangeValues("none");
		  } 
		  else
		  {
				if (rov.isNumeric())
				{
					 // verificare che il numeric type sia stato selezionato
					 ValidationUtils.rejectIfEmptyOrWhitespace(errors, "numberType", "numberType", "Numeric type must be specified");
					 if(errors.hasErrors())
							 return;					

					 // verificare il pattern del range
					 if (rov.isRange())
					 {
						  String[] ranges = rov.getRangeValues().split(",");
						  for (String range : ranges) // per ogni valore inserito
																// controllare il pattern del
																// range in base al tipo e se è
																// un range ammissibile
						  {
								switch (DefaultRangeOfValuesEnum.valueOf(rov.getNumberType()))
								{
								case REAL_NUMBERS:
									 if (!range.matches("^\\[-?(\\d+(\\.\\d+)?|\\binf\\b):(-?\\d+(\\.\\d+)?|\\binf\\b)\\]$"))
									 {
										  errors.rejectValue("rangeValues", "rangeValues", "Range Set pattern not valid");
										  return;
									 }
									 break;
								case INTEGER_NUMBERS:
									 if (!range.matches("^\\[(-?\\d+|-\\binf\\b):(-?\\d+|\\binf\\b)\\]$"))
									 {
										  errors.rejectValue("rangeValues", "rangeValues", "Range Set pattern not valid");
										  return;
									 }
									 break;
								default:
									 if (!range.matches("^\\[\\d+:(\\d+|\\binf\\b)\\]$"))
									 {
										  errors.rejectValue("rangeValues", "rangeValues", "Range Set pattern not valid");
										  return;
									 }
									 break;
								}
								if (!this.isValidRange(range))
								{
									 errors.rejectValue("rangeValues", "rangeValues", "Ending point of a range must be gretater than starting point");
									 return;
								}
						  }
					 }
					 else
					 {
						  ValidationUtils.rejectIfEmptyOrWhitespace(errors, "rangeValues", "rangeValues", "Range Set cannot be empty");
							
							if(errors.hasErrors())
								 return;
						  // verificare che i valori dentro il rangeValues siano solo
						  // numeri
						  String[] values = rov.getRangeValues().split(",");						  
						  for (String val : values)
						  {								
								if (!NumberUtils.isNumber(val) || !this.isConsistentNumber(val, rov.getNumberType()))
								{
									 errors.rejectValue("rangeValues", "rangeValues", "Range Set must contain only valid numbers");
									 return;
								}
						  }
					 }
				}
				else //controlla solo che il campo dei valori accettati non sia nullo
				{
					 ValidationUtils.rejectIfEmptyOrWhitespace(errors, "rangeValues", "rangeValues", "Range Set cannot be empty");
					 if(errors.hasErrors())
							 return;					
				}
		  }
	 }

	 /*
	  * controlla che il range sia consistente, i.e. il secondo membro deve
	  * essere più grande del primo
	  */
	 private boolean isValidRange(String rangeExpression)
	 {
		  rangeExpression = rangeExpression.replaceAll("\\[", "");
		  rangeExpression = rangeExpression.replaceAll("\\]", "");
		  String[] range = rangeExpression.split(":");
		  double value, from = 0, to = 0;
		  for(int i = 0; i < range.length; i++)
		  {
				if(range[i].equals("-inf"))
					 value = Double.NEGATIVE_INFINITY;
				else if(range[i].equals("inf"))
					 value = Double.POSITIVE_INFINITY;
				else
					 value = Double.parseDouble(range[i]);
				
				if(i == 0)
					 from = value;
				else
					 to = value;
		  }
		  
		  return to > from;
	 }

	 /*
	  * controlla che un numero sia consistente con il tipo specificato
	  */
	 private boolean isConsistentNumber(String num, String type)
	 {
		  System.out.println(num + " " + type);
		  switch (DefaultRangeOfValuesEnum.valueOf(type))
		  {
		  case NATURAL_NUMBERS:
				return num.indexOf("-") == -1 && num.indexOf(".") == -1;
		  case INTEGER_NUMBERS:
				return num.indexOf(".") == -1;
		  default:
				return true;
		  }
	 }
}
