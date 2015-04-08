package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.Aggregator;

import java.util.List;

public class AggregatorHandler
{
	 /**
	  * Execute the correct aggregation method and return its result
	  * @param aggregatorName name of the aggregator to be executed, it must be a valid aggregator name
	  * @param args list of measured values
	  * @return the result of the aggreagtion method
	  * @throws IllegalArgumentExeption if aggregatorName is not a valid aggregator name
	  */
	 public static Double executeAggregator(String aggregatorName, List<Double> args)
	 {
		  switch(Aggregator.valueOf(aggregatorName))
		  {
   		  case AVG:
   				return AggregatorHandler.executeAvgValueAggregator(args);
   		  case SUM:
   				return AggregatorHandler.executeSumAggregator(args);
   		  case VARIANCE:
   				return AggregatorHandler.executeVarianceAggregator(args);
   		  case MAX_VALUE:
   				return AggregatorHandler.executeMaxValueAggregator(args);
   		  case MIN_VALUE:
   				return AggregatorHandler.executeMinValueAggregator(args);
   		  default:
   				throw new IllegalArgumentException();
		  }
	 }
	 
	 /**
	  * Returns the sum of the measured values
	  * @param args
	  * @return
	  */
	 private static double executeSumAggregator(List<Double> args)
	 {
		  Double result = 0d;
		  for(Double arg : args)
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
	 private static Double executeAvgValueAggregator(List<Double> args)
	 {
		  Double result = 0d;
		  int length = args.size();
		  for(Double arg : args)
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
	 private static Double executeMaxValueAggregator(List<Double> args)
	 {
		  Double result = args.get(0);
		  for(Double arg : args)
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
	 private static Double executeMinValueAggregator(List<Double> args)
	 {
		  Double result = args.get(0);
		  for(Double arg : args)
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
	 private static Double executeVarianceAggregator(List<Double> args)
	 {
		  Double avgValue = AggregatorHandler.executeAvgValueAggregator(args);
		  Double temp = 0d;
		  for(Double arg : args)
		  {
				temp = (arg - avgValue) * (arg - avgValue);
		  }
		  return temp/(args.size() - 1);
	 }
 
	 /**
	  * Return the count of the occurrence of the value passed as first argument (args[0])
	  * @param args
	  * @return
	  */
/*	 private static int executeCounterAggregator(double... args)
	 {
		  double valueToBeCounted = args[0];
		  int counter = 0;
		  for(double arg : args)
		  {
				if(arg == valueToBeCounted)
					 counter++;
		  }
		  return counter;

	 } */
}
