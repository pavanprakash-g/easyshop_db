package com.easyshop.repository;

/**
 * Created by admin on 22/10/16.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Component
public class CommonRepository{

    @Autowired
    private JdbcTemplate template;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Map<String,Object>> getRowsOfTable(String tableName){
        return template.queryForList("select * from " + tableName);
    }

    public List<Map<String,Object>> getQuestionDetails(){
        return template.queryForList("SELECT a.id, a.title, a.description, a.tag, a.created_at, a.updated_at, a.user_Id, concat(concat(b.first_name,' '),b.last_name) name FROM question a inner join user b on a.user_id=b.id");
    }

    public <T> void save(T t){
        entityManager.persist(t);
    }

    public void resetCounter(String sequenceName, Long count){
        template.execute("ALTER SEQUENCE " + sequenceName + " RESTART WITH " + count + ";");
    }

    public List<Map<String,Object>> getMax(){
        return template.queryForList("SELECT max(id)+1 id from question");
    }

}
