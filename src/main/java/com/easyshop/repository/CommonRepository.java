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

    public List<Map<String,Object>> getCartCount(long custId){
        return template.queryForList("SELECT * from cart where CUST_ID="+custId);
    }

    public List<Map<String,Object>> getCartDetails(long custId){
        return template.queryForList("SELECT i.ITEM_ID itemId, i.ITEM_NAME itemName, i.ITEM_PRICE itemPrice, SUM(ITEM_PRICE) totalPrice, count(*) itemCount from item i inner join cart c on i.ITEM_ID=c.ITEM_ID where cust_id="+custId+" group by i.ITEM_ID");
    }

    public Map<String, Object> getMaxId(String table, String column){
        return template.queryForMap("select max("+column+")+1 newId from "+table);
    }

}
