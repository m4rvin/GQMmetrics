package it.uniroma2.gqm.service;

import java.util.List;

import it.uniroma2.gqm.model.DefaultOperation;
import it.uniroma2.gqm.model.MeasurementScaleTypeEnum;
import it.uniroma2.gqm.dao.DefaultOperationDao;

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
	 public JSONArray findBySupportedMeasurementScaleJSONized(MeasurementScaleTypeEnum type)
	 {
		  if(type != null)
		  {
				return new JSONArray(this.defaultOperationDao.findBySupportedMeasurementScale(type));
		  }
		  return null;
	 }
	 
	 @Override
	 public List<Object> findBySupportedMeasurementScale(MeasurementScaleTypeEnum type)
	 {
		  if(type != null)
		  {
				return this.defaultOperationDao.findBySupportedMeasurementScale(type);
		  }
		  return null;
	 }

	@Override
	public List<DefaultOperation> findBySupportedMeasurementScaleOBJ(
			MeasurementScaleTypeEnum type) {
		if(type != null)
		  {
				return this.defaultOperationDao.findBySupportedMeasurementScaleOBJ(type);
		  }
		  return null;
	}

}
