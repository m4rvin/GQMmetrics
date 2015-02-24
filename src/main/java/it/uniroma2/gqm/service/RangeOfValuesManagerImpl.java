package it.uniroma2.gqm.service;

import it.uniroma2.gqm.dao.RangeOfValuesDao;
import it.uniroma2.gqm.model.MeasurementScaleTypeEnum;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.RangeOfValues;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("rangeOfValuesManager")
public class RangeOfValuesManagerImpl extends
		GenericManagerImpl<RangeOfValues, Long> implements RangeOfValuesManager {

	private RangeOfValuesDao rangeOfValuesDao;
	
	@Autowired
	public RangeOfValuesManagerImpl(RangeOfValuesDao rangeOfValuesDao){
		super(rangeOfValuesDao);
		this.rangeOfValuesDao = rangeOfValuesDao;
	}
	
	@Override
	public List<RangeOfValues> findByProject(Project project) {
		if(project !=null)
    		return rangeOfValuesDao.findByProject(project.getId());
    	else
    		return null;
	}

	@Override
	public RangeOfValues findById(Long id) {
		return get(id);
	}

	@Override
	public RangeOfValues saveRangeOfValues(RangeOfValues rangeOfValues) {
			return rangeOfValuesDao.save(rangeOfValues);
	}

   @Override
   public List<RangeOfValues> findBySupportedMeasurementScale(MeasurementScaleTypeEnum type)
   {
   	 if(type != null)
   		  return this.rangeOfValuesDao.findBySupportedMeasurementScale(type);
   	 return null;
   }

}
