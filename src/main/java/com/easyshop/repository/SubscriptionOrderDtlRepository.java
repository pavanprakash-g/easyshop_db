package com.easyshop.repository;
import com.easyshop.model.SubscriptionOrderDtlModel;
import org.springframework.data.repository.CrudRepository;
/**
 * Created by admin-hp on 2/4/17.
 */
public interface SubscriptionOrderDtlRepository extends CrudRepository<SubscriptionOrderDtlModel, Long> {

    Iterable<SubscriptionOrderDtlModel> findBySubsOrderId(long orderId);

    SubscriptionOrderDtlModel findBySubsOrderIdAndSubsOrderItemId(long orderId, int itemId);

}