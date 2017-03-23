package com.easyshop.repository;

import com.easyshop.model.OrderHdrModel;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by pavan on 2/21/17.
 */
public interface OrderHdrRepository extends CrudRepository<OrderHdrModel, Long> {

    Iterable<OrderHdrModel> findByCustId(int custId);

    OrderHdrModel findByOrderId(long orderId);

}
