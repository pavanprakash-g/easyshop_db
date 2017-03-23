package com.easyshop.repository;

import com.easyshop.model.OrderDtlModel;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by pavan on 2/21/17.
 */
public interface OrderDtlRepository extends CrudRepository<OrderDtlModel, Long> {

    Iterable<OrderDtlModel> findByOrderId(long orderId);

}
