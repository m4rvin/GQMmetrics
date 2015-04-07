package it.uniroma2.gqm.webapp.controller;

public class AggregatorHandler
{

	 /**
	  * Returns the sum of the measured values
	  * @param args
	  * @return
	  */
	 public static double executeSumAggregator(double... args)
	 {
		  double result = 0d;
		  for(double arg : args)
		  {
				result += arg;
		  }
		  return result;
	 }
	 
	 /**Returns the average value of the measured values 
	  * 
	  * @param args
	  * @return
	  */
	 public static double executeAvgValueAggregator(double... args)
	 {
		  double result = 0d;
		  int length = args.length;
		  for(double arg : args)
		  {
				result += arg;
		  }
		  return result/length;
	 }
	 
	 /**
	  * Returns the max value among the measured ones
	  * @param args
	  * @return
	  */
	 public static double executeMaxValueAggregator(double... args)
	 {
		  double result = args[0];
		  for(double arg : args)
		  {
				if(arg > result)
					 result = arg;
		  }
		  return result;
	 }
	 
	 /**
	  * Returns the min value among the measured ones
	  * @param args
	  * @return
	  */
	 public static double executeMinValueAggregator(double... args)
	 {
		  double result = args[0];
		  for(double arg : args)
		  {
				if(arg < result)
					 result = arg;
		  }
		  return result;
	 }
	 
	 /**
	  * Returns the variance of the measured values
	  * @param args
	  * @return
	  */
	 public static double executeVarianceAggregator(double... args)
	 {
		  double avgValue = AggregatorHandler.executeAvgValueAggregator(args);
		  double temp = 0d;
		  for(double arg : args)
		  {
				temp = (arg - avgValue) * (arg - avgValue);
		  }
		  return temp/(args.length - 1);
	 }
	 
	 /**
	  * Return the count of the occurrence of the value passed as first argument (args[0])
	  * @param args
	  * @return
	  */
	 public static int executeCounterAggregator(double... args)
	 {
		  double valueToBeCounted = args[0];
		  int counter = 0;
		  for(double arg : args)
		  {
				if(arg == valueToBeCounted)
					 counter++;
		  }
		  return counter;
	 }
	 
	 /**
	  * Returns the boolean AND value of the measured ones
	  * @param args
	  * @return
	  */
	 public static double executeAndAggregator(double... args)
	 {
		  for(double arg : args)
		  {
				if(arg == 0.0d)
					 return 0.0d;
		  }
		  return 1.0d;
	 }
	 
	 /**
	  * Returns the boolean OR value of the measured ones
	  * @param args
	  * @return
	  */
	 public static double executeOrAggregator(double... args)
	 {
		  for(double arg : args)
		  {
				if(arg == 1.0d)
					 return 1.0d;
		  }
		  return 0.0d;
	 }
}
