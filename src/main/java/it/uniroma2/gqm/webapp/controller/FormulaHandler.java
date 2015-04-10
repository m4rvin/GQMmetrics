package it.uniroma2.gqm.webapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

}
