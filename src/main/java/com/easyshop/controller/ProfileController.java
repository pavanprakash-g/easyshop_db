package com.easyshop.controller;

import com.easyshop.model.UserModel;
import com.easyshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/custDetails", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getCustomerDetails(@RequestParam(value= "id", required = false, defaultValue ="0" ) Long id){
        if(id == 0) {
            return ResponseEntity.ok(userRepository.findAll());
        }else{
            return ResponseEntity.ok(userRepository.findOne(id));
        }
    }

    @RequestMapping(value = "/custDetails", method = PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity updateCustomerStatus(@RequestParam("custId") Long custId, @RequestParam("activeStatus") boolean activeStatus){
        UserModel userModel = userRepository.findOne(custId);
        userModel.setActiveStatus(activeStatus);
        userRepository.save(userModel);
        return ResponseEntity.ok(userRepository.findAll());
    }
}
