package com.easyshop.repository;

import com.easyshop.model.AddressModel;
import com.easyshop.model.MessageModel;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by pavan on 2/21/17.
 */
public interface MessageRepository extends CrudRepository<MessageModel, Long> {
    MessageModel findByCustId(long custId);
}
