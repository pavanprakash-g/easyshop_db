package com.easyshop.util;

import com.easyshop.model.UserModel;
import com.easyshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
/**
 * Created by pavan on 2/5/17.
 */
public class EasyShopUtil {

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
}
