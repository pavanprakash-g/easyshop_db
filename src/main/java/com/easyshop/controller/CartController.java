package com.easyshop.controller;

import com.easyshop.model.CartModel;
import com.easyshop.repository.CartRepository;
import com.easyshop.repository.CommonRepository;
import com.easyshop.repository.UserRepository;
import com.easyshop.util.EasyShopUtil;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
/**
 * Created by pavan on 3/17/17.
 */
@CrossOrigin
@RestController
@RequestMapping("/cart")
public class CartController {

    private final static Logger logger = Logger.getLogger(CartController.class);

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CommonRepository commonRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/getCart", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getCart(HttpServletRequest request) throws Exception{
        if(!EasyShopUtil.isValidCustomer(userRepository, request)){
            return ResponseEntity.badRequest().body("Invalid Auth Token");
        }
        long custId = EasyShopUtil.getCustIdByToken(userRepository, request);
        logger.log(Level.INFO,"CUSTID:::"+custId);
        return ResponseEntity.ok(commonRepository.getCartDetails(custId));
    }

    @RequestMapping(value = "/addToCart", method = POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity addToCart(HttpServletRequest request, @RequestBody CartModel cartModel) throws Exception{
        if(!EasyShopUtil.isValidCustomer(userRepository, request)){
            return ResponseEntity.badRequest().body("Invalid Auth Token");
        }
        JSONObject response = new JSONObject();
        cartRepository.save(cartModel);
        response.put("status",true);
        response.put("cartCount",commonRepository.getCartCount(cartModel.getCustId()).size());
        return ResponseEntity.ok(response.toString());
    }

}
