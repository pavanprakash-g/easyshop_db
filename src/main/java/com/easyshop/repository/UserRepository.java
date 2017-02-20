package com.easyshop.repository;

import com.easyshop.model.UserModel;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by admin on 22/10/16.
 */
public interface UserRepository extends CrudRepository<UserModel, Long> {

    UserModel findByCustEmailidAndCustPasswordAndActiveStatus(String custEmailId, String custPassword, boolean activeStatus);
	UserModel findByCustEmailidAndSecurityQuesAns(String custEmailId, String securityQuesAns);
	UserModel findByCustEmailid(String custEmailId);
	
    Iterable<UserModel> findAll();

    UserModel findByAuthToken(String authToken);

}
