package com.easyshop.repository;
import com.easyshop.model.NextDueDateModel;
import org.springframework.data.repository.CrudRepository;
/**
 * Created by admin-hp on 4/4/17.
 */
public interface NextDueDateRepository extends CrudRepository<NextDueDateModel, Long> {

    NextDueDateModel findBySubsOrderId(long orderId);


}
