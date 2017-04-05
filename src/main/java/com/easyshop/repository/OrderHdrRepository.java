package com.easyshop.repository;

import com.easyshop.model.OrderHdrModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by pavan on 2/21/17.
 */
public interface OrderHdrRepository extends CrudRepository<OrderHdrModel, Long> {

    Iterable<OrderHdrModel> findByCustIdOrderByOrderHdrIdDesc(int custId);

    OrderHdrModel findByOrderId(long orderId);

    Iterable<OrderHdrModel> findAllByOrderByOrderHdrIdDesc();

    List<OrderHdrModel> findByCustId(int id);
}

