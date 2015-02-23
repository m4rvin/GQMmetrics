package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.DefaultRangeOfValuesEnum;
import it.uniroma2.gqm.model.RangeOfValues;

import org.apache.commons.lang.NumberUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

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
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "rangeValues", "rangeValues", "Range Set is a required field");
		  } else
		  {
				if (rov.isNumeric())
				{
					 // verificare che il numeric type sia stato selezionato
					 ValidationUtils.rejectIfEmptyOrWhitespace(errors, "numberType", "numberType", "Numeric type must be specified");

					 // verificare il pattern del range solo se il numeric type è
					 // specificato
					 if (rov.isRange() && !rov.getNumberType().equals(""))
					 {
						  String[] ranges = rov.getRangeValues().split(",");
						  boolean found = false;
						  for (String range : ranges) // per ogni valore inserito
																// controllare il pattern del
																// range in base al tipo e se è
																// un range ammissibile
						  {
								switch (DefaultRangeOfValuesEnum.valueOf(rov.getNumberType()))
								{
   								case REAL_NUMBERS:
   									 if (!range.matches("^(\\[){1}(-)*[0-9]+(.[0-9]+)?(:){1}(-)*[0-9]+(.[0-9]+)?(\\])${1}"))
   									 {
   										  errors.rejectValue("rangeValues", "rangeValues", "Range Set pattern not valid");
   										  found = true;
   									 }
   									 break;
   								case INTEGER_NUMBERS:
   									 if (!range.matches("^(\\[){1}(-)*[0-9]+(:){1}(-)*[0-9]+(\\])${1}"))
   									 {
   										  errors.rejectValue("rangeValues", "rangeValues", "Range Set pattern not valid");
   										  found = true;
   									 }
   									 break;
   								default:
   									 if (!range.matches("^(\\[){1}[0-9]+(:){1}[0-9]+(\\])${1}"))
   									 {
   										  errors.rejectValue("rangeValues", "rangeValues", "Range Set pattern not valid");
   										  found = true;
   									 }
   									 break;
   							}
								if (found)
									 break;
								else
								{
									 if (!this.isValidRange(range))
									 {
										  errors.rejectValue("rangeValues", "rangeValues", "Ending point of a range must be gretater than starting point");
										  break;
									 }
								}
						  }
					 } else if (!rov.isRange() && !rov.getNumberType().equals(""))
					 {
						  // verificare che i valori dentro il rangeValues siano solo
						  // numeri
						  String[] values = rov.getRangeValues().split(",");
						  for (String val : values)
						  {
								if (!NumberUtils.isNumber(val) || !this.isConsistentNumber(val, rov.getNumberType()))
								{
									 errors.rejectValue("rangeValues", "rangeValues", "Range Set must contain only valid numbers");
									 break;
								}
						  }
					 }
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
		  float from = Float.parseFloat(rangeExpression.split(":")[0]);
		  float to = Float.parseFloat(rangeExpression.split(":")[1]);
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
