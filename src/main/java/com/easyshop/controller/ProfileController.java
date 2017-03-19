package com.easyshop.controller;

import com.easyshop.model.AddressModel;
import com.easyshop.model.UserModel;
import com.easyshop.repository.AddressRepository;
import com.easyshop.repository.CardRepository;
import com.easyshop.repository.UserRepository;
import com.easyshop.util.EasyShopUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Created by pavan on 2/6/17.
 */
@CrossOrigin
@RestController
@RequestMapping("/profile")
public class ProfileController {
    public final static Logger logger = Logger.getLogger("ProfileController.class");
    private static final String X_AUTH_TOKEN= "X-AUTH-TOKEN";

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    CardRepository cardRepository;

    @RequestMapping(value = "/custDetails", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getCustomerDetails(HttpServletRequest request, @RequestParam(value= "id", required = false, defaultValue ="0" ) Long id){
        if(!EasyShopUtil.isValidCustomer(userRepository, request)){
            return ResponseEntity.badRequest().body("Invalid Auth Token");
        }
        if(id == 0) {
            return ResponseEntity.ok(userRepository.findAll());
        }else{
            return ResponseEntity.ok(userRepository.findOne(id));
        }
    }

    @RequestMapping(value = "/custDetails", method = PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity updateCustomerStatus(HttpServletRequest request, @RequestBody UserModel userModel){
        if(!EasyShopUtil.isValidCustomer(userRepository, request)){
            return ResponseEntity.badRequest().body("Invalid Auth Token");
        }
        userRepository.save(userModel);
        return ResponseEntity.ok(userRepository.findAll());
    }

    @RequestMapping(value = "/address", method = PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity udpateAddress(HttpServletRequest request, @RequestBody AddressModel addressModel){
        if(!EasyShopUtil.isValidCustomer(userRepository, request)){
            return ResponseEntity.badRequest().body("Invalid Auth Token");
        }
        addressRepository.save(addressModel);
        return ResponseEntity.ok("success");
    }

    @RequestMapping(value = "/address", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getAddresses(HttpServletRequest request){
        if(!EasyShopUtil.isValidCustomer(userRepository, request)){
            return ResponseEntity.badRequest().body("Invalid Auth Token");
        }
        return ResponseEntity.ok(addressRepository.findAll());
    }

    @RequestMapping(value = "/cards", method = PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getCards(HttpServletRequest request){
        if(!EasyShopUtil.isValidCustomer(userRepository, request)){
            return ResponseEntity.badRequest().body("Invalid Auth Token");
        }
        return ResponseEntity.ok(cardRepository.findAll());
    }
}
