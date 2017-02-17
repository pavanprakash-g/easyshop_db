package com.easyshop.repository;

import com.easyshop.model.TestModel;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by admin on 22/10/16.
 */
public interface TestRepository extends CrudRepository<TestModel, Long> {

    /*@Query("SELECT a.id, a.title, a.description, a.tag, a.created_at, a.updated_at, a.user_Id, concat(concat(b.first_name,' '),b.last_name) name FROM question a inner join user b on a.user_id=a.id")
    List<QuestionsModel> findAllQuestions();*/
}
