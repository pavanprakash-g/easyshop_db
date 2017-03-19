package com.easyshop.repository;

import com.easyshop.model.CartModel;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by pavan on 2/21/17.
 */
public interface CartRepository extends CrudRepository<CartModel, Long> {

    Iterable<CartModel> findByCustId(long custId);

    Iterable<CartModel> findByItemId(int itemId);

    CartModel findTopByCustId(long custId);
}
