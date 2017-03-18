package com.easyshop.controller;

import com.easyshop.model.CatalogModel;
import com.easyshop.repository.CatalogRepository;
import com.easyshop.repository.CommonRepository;
import com.easyshop.repository.UserRepository;
import com.easyshop.util.EasyShopUtil;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by admin-hp on 19/2/17.
 */
@CrossOrigin
@RestController
@RequestMapping("/catalog")
public class CatalogController {
    private final static Logger logger = Logger.getLogger(LoginController.class);

    @Autowired
    private CommonRepository commonRepository;

    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/getItem", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getItem(@RequestParam("itemName") String itemName) throws Exception{
        JSONObject response = new JSONObject();
        JSONObject getItem = new JSONObject();
        CatalogModel catalogModel = catalogRepository.findByItemName(itemName);
        if(catalogModel!=null) {
            response.put("status", true);
            response.put("itemName",catalogModel.getItemName());
            getItem.put("getItem",response);
            return ResponseEntity.ok(getItem.toString());
        }else {
            response.put("status", false);
            response.put("message","Item not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response.toString());
        }
    }

    @RequestMapping(value = "/deleteItem", method = DELETE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity deleteItem(HttpServletRequest request, @RequestParam("itemId") long itemId) throws Exception{
        if(!EasyShopUtil.isValidCustomer(userRepository, request)){
            return ResponseEntity.badRequest().body("Invalid Auth Token");
        }
        JSONObject response = new JSONObject();
        JSONObject deleteItem = new JSONObject();
        CatalogModel catalogModel = catalogRepository.findByItemId(itemId);
        if(catalogModel!=null) {
            response.put("status", true);
            response.put("itemId",catalogModel.getItemId());
            deleteItem.put("getItem",response);
            catalogRepository.delete(itemId);
            return ResponseEntity.ok(deleteItem.toString());
        }else {
            response.put("status", false);
            response.put("message","Item not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response.toString());
        }
    }

    @RequestMapping(value = "/getAllItem", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getAllItem() throws Exception{
        JSONObject response = new JSONObject();
        JSONObject singleItem = new JSONObject();
        JSONObject getAllItem = new JSONObject();
        //CatalogModel catalogModel = catalogRepository.findByItemName(itemName);
        List <CatalogModel> items = null;
            if(catalogRepository.findAll() != null) {
                for(CatalogModel item: catalogRepository.findAll()) {
                    items.add(item);
                    singleItem.put("itemId",item.getItemId());
                    singleItem.put("itemName",item.getItemName());
                    singleItem.put("itemDescription",item.getItemDescription());
                    singleItem.put("itemPrice",item.getItemPrice());
                    singleItem.put("itemQuantity",item.getItemQuantity());
                }
            }
            else {
                response.put("status", false);
                response.put("message","Item not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response.toString());
            }
            response.put("status", true);
            response.put("item",singleItem);
            getAllItem.put("getAllItem",response);
            return ResponseEntity.ok(getAllItem.toString());
    }

    @RequestMapping(value = "/createItem", method = POST, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createItem(@Valid @RequestBody CatalogModel catalogModel) throws Exception{
        JSONObject responseObject = new JSONObject();
        try {
            if(catalogRepository.findByItemName(catalogModel.getItemName()) == null) {
                CatalogModel resp= null;
                resp = catalogRepository.save(catalogModel);
                JSONObject info = new JSONObject();
                info.put("itemName", resp.getItemName());
                //logger.log(Level.INFO, "Response" + catalogRepository.save(catalogModels));
                return ResponseEntity.ok(catalogRepository.findAll());
            }else {
                responseObject.put("status",false);
                responseObject.put("message","Item Already Exists");
                return ResponseEntity.badRequest().build();
            }
        }catch (Exception e){
            responseObject.put("status",false);
            responseObject.put("message",e.getMessage());
            logger.log(Level.ERROR, "Unexpected exception",e);
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/itemDetails", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getItemDetails(HttpServletRequest request, @RequestParam(value = "itemId", required = false, defaultValue ="0" ) Long itemId) throws Exception{
        if(!EasyShopUtil.isValidCustomer(userRepository, request)){
            return ResponseEntity.badRequest().body("Invalid Auth Token");
        }
        JSONObject responseObject = new JSONObject();
        if(itemId == 0) {
            return ResponseEntity.ok(catalogRepository.findAll());
        }else if(itemId == -1){
            responseObject.put("Item_1",catalogRepository.findOne((long) 1));
            responseObject.put("Item_2",catalogRepository.findOne((long) 2));
            responseObject.put("Item_3",catalogRepository.findOne((long) 3));
            return ResponseEntity.ok(catalogRepository.findOne(itemId));
        }else{
            return ResponseEntity.ok(responseObject.toString());
        }
    }

    @RequestMapping(value = "/updateItem", method = PUT, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity updateItem(HttpServletRequest request, @Valid @RequestBody CatalogModel catalogModel) throws Exception{
        if(!EasyShopUtil.isValidCustomer(userRepository, request)){
            return ResponseEntity.badRequest().body("Invalid Auth Token");
        }
        JSONObject responseObject = new JSONObject();
        responseObject.put("status",true);
        catalogRepository.save(catalogModel);
        return ResponseEntity.ok(responseObject.toString());
    }

    private CatalogController() {
    }
}
