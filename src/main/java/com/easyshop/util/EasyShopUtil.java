package com.easyshop.util;

import com.easyshop.model.UserModel;
import com.easyshop.repository.CommonRepository;
import com.easyshop.repository.UserRepository;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
/**
 * Created by pavan on 2/5/17.
 */
public class EasyShopUtil {

    @Autowired
    private CommonRepository commonRepository;

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
        logger.log(Level.INFO,"AuthToken"+request.getHeader(X_AUTH_TOKEN));
        UserModel user = userRepository.findByAuthToken(request.getHeader(X_AUTH_TOKEN));
        if(user!=null){
            return user.getCustId();
        }
        return 0;
    }
}
