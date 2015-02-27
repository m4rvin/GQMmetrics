package it.uniroma2.gqm.service;

import it.uniroma2.gqm.dao.RangeOfValuesDao;
import it.uniroma2.gqm.model.DefaultOperation;
import it.uniroma2.gqm.model.MeasurementScaleTypeEnum;
import it.uniroma2.gqm.model.RangeOfValues;

import java.util.List;

import org.appfuse.service.impl.GenericManagerImpl;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("defaultOperationManager")
public class DefaultOperationManagerImpl extends GenericManagerImpl<DefaultOperation, Long> implements DefaultOperationManager
{
	 private DefaultOperationDao defaultOperationDao;

	 @Autowired
	 public DefaultOperationManagerImpl(DefaultOperationDao defaultOperationDao)
	 {
		  super(defaultOperationDao);
		  this.defaultOperationDao = defaultOperationDao;
	 }

	 @Override
	 public JSONArray findBySupportedMeasurementScale(MeasurementScaleTypeEnum type)
	 {
		  if(type != null)
		  {
				return new JSONArray(this.defaultOperationDao.findBySupportedMeasurementScale(type));
		  }
	 }

}
