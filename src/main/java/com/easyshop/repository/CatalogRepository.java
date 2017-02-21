package com.easyshop.repository;

import com.easyshop.model.CatalogModel;
import org.springframework.data.repository.CrudRepository;

import java.util.Iterator;
import java.util.List;

/**
 * Created by admin on 22/10/16.
 */
public interface CatalogRepository extends CrudRepository<CatalogModel, Long> {

    //CatalogModel findByCustEmailidAndCustPasswordAndActiveStatus(String custEmailId, String custPassword, boolean activeStatus);
	//CatalogModel findByCustEmailidAndSecurityQuesAns(String custEmailId, String securityQuesAns);
	CatalogModel findByItemid(long itemid);
    CatalogModel findByItemName(String itemName);
    Iterable<CatalogModel> findAll();

}
