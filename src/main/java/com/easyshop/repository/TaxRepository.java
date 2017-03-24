package com.easyshop.repository;

import com.easyshop.model.TaxModel;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by pavan on 3/24/17.
 */
public interface TaxRepository extends CrudRepository<TaxModel, Long> {
    TaxModel findByZipcode(int zipcode);
}
