package com.easyshop.repository;
import com.easyshop.model.SubscriptionOrderHdrModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by admin-hp on 3/4/17.
 */
public interface SubscriptionOrderHdrRepository extends CrudRepository<SubscriptionOrderHdrModel, Long> {

    Iterable<SubscriptionOrderHdrModel> findByCustIdOrderBySubsOrderHdrIdDesc(int custId);

    SubscriptionOrderHdrModel findBySubsOrderId(long subsOrderId);

    Iterable<SubscriptionOrderHdrModel> findAllByOrderBySubsOrderHdrIdDesc();

    List<SubscriptionOrderHdrModel> findByCustId(int id);
}
