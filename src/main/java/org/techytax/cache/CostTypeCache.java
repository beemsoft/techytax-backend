package org.techytax.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.techytax.domain.CostType;
import org.techytax.repository.CostTypeRepository;

import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class CostTypeCache {

	@Autowired
	CostTypeRepository costTypeDao;

	private Map<Long, CostType> costTypeMap = null;

	public CostType getCostType(long id) throws Exception {

		if (costTypeMap == null) {
			fill();
		}
		return costTypeMap.get(id);
	}

	@Transactional
	private void fill() throws Exception {
		costTypeMap = new HashMap<>();
		for (CostType costType : costTypeDao.findAll()) {
			costTypeMap.put(costType.getId(), costType);
		}
	}

	@Transactional
	public Collection<CostType> getCostTypes() throws Exception {
		if (costTypeMap == null) {
			fill();
		}
		return costTypeMap.values();
	}

}
