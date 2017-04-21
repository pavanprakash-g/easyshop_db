package com.easyshop.repository;

import com.easyshop.model.ShipmentModel;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by pavan on 2/21/17.
 */
public interface ShipmentRepository extends CrudRepository<ShipmentModel, Long> {

    ShipmentModel findByShipmentId(long shipmentId);

}
