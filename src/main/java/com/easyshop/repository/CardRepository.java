package com.easyshop.repository;

import com.easyshop.model.CardModel;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by pavan on 2/21/17.
 */
public interface CardRepository extends CrudRepository<CardModel, Long> {

    CardModel findByCardId(long cardId);

}
