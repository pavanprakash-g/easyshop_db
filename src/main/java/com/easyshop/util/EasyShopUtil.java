package com.easyshop.util;

import com.easyshop.model.*;
import com.easyshop.repository.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
/**
 * Created by pavan on 2/5/17.
 */
public class EasyShopUtil {

    private final static Logger logger = Logger.getLogger(EasyShopUtil.class);

    private static final String X_AUTH_TOKEN= "X-AUTH-TOKEN";
    public static String getRandonUDID(){
        UUID id = UUID.randomUUID();
        return id.toString();
    }

    public static boolean isValidCustomer(UserRepository userRepository, HttpServletRequest request){
        UserModel user = userRepository.findByAuthToken(request.getHeader(X_AUTH_TOKEN));
        if(user!=null){
            return true;
        }
        return false;
    }

    public static long getCustIdByToken(UserRepository userRepository, HttpServletRequest request){
        UserModel user = userRepository.findByAuthToken(request.getHeader(X_AUTH_TOKEN));
        if(user!=null){
            return user.getCustId();
        }
        return 0;
    }
    public static long validateCart(CatalogRepository catalogRepository, List<Map<String, Object>> items){
        long requestedQuantity = 0;
        long availableQuantity = 0;
        long itemId;
        for(int i=0; i<items.size(); i++){
            Map<String, Object> item = items.get(i);
            requestedQuantity = (Long) item.get("itemCount");
            itemId = (Long) item.get("itemId");
            availableQuantity = catalogRepository.findByItemId(itemId).getItemQuantity();
            if(availableQuantity < requestedQuantity){
                return itemId;
            }
        }
        return 0;
    }

    public static Long getMaxId(String table, String column, CommonRepository commonRepository) {
        Map<String, Object> map = commonRepository.getMaxId(table,column);
        if(map.get("newId") != null){
            return (Long)map.get("newId");
        }else{
            return 1L;
        }
    }

    public static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    public static void deleteAll(long custId,AddressRepository addressRepository, CardRepository cardRepository,OrderHdrRepository orderHdrRepository,OrderDtlRepository orderDtlRepository,SubscriptionOrderHdrRepository subscriptionOrderHdrRepository,SubscriptionOrderDtlRepository subscriptionOrderDtlRepository, CartRepository cartRepository){
        addressRepository.delete(addressRepository.findByCustId(custId));
        cardRepository.delete(cardRepository.findByCustId((int)custId));
        cartRepository.delete(cartRepository.findByCustId(custId));
        for(OrderHdrModel orderHdrModel : orderHdrRepository.findByCustId((int)custId)) {
            orderDtlRepository.delete(orderDtlRepository.findByOrderId(orderHdrModel.getOrderId()));
            orderHdrRepository.delete(orderHdrModel);
        }
        for(SubscriptionOrderHdrModel subscriptionOrderHdrModel : subscriptionOrderHdrRepository.findByCustId((int)custId)) {
            subscriptionOrderDtlRepository.delete(subscriptionOrderDtlRepository.findBySubsOrderId(subscriptionOrderHdrModel.getSubsOrderId()));
            subscriptionOrderHdrRepository.delete(subscriptionOrderHdrModel);
        }

    }
}
