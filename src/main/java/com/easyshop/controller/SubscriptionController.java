package com.easyshop.controller;
import com.easyshop.model.SubscriptionOrderDtlModel;
import com.easyshop.model.SubscriptionOrderHdrModel;
import com.easyshop.model.SubscriptionOrderModel;
import com.easyshop.repository.*;
import com.easyshop.util.EasyShopUtil;
import com.easyshop.util.SubscriptionUtil;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Created by admin-hp on 2/4/17.
 */
@CrossOrigin
@RestController
@RequestMapping("/subscriptionOrder")
public class SubscriptionController {

    private final static Logger logger = Logger.getLogger(SubscriptionController.class);


    @Autowired
    private CommonRepository commonRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionOrderHdrRepository subscriptionOrderHdrRepository;

    @Autowired
    private SubscriptionOrderDtlRepository subscriptionOrderDtlRepository;

    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private CartRepository cartRepository;

    @RequestMapping(value = "/getSubscriptionOrders", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getSubscriptionOrders(HttpServletRequest request, @RequestParam(name = "custId", required = false, defaultValue ="0") int id) throws Exception{
        if(!EasyShopUtil.isValidCustomer(userRepository, request)){
            return ResponseEntity.badRequest().body("Invalid Auth Token");
         }
        return ResponseEntity.ok(SubscriptionUtil.getSubscriptionData(id, subscriptionOrderHdrRepository, subscriptionOrderDtlRepository, catalogRepository));
    }

    @RequestMapping(value = "/createSubscriptionOrders", method = POST, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createSubscriptionOrder(HttpServletRequest request, @RequestBody SubscriptionOrderModel subscriptionOrderModel) throws Exception {
        if (!EasyShopUtil.isValidCustomer(userRepository, request)) {
            return ResponseEntity.badRequest().body("Invalid Auth Token");
        }
        long orderId = EasyShopUtil.getMaxId("subs_order_hdr", "subs_order_id", commonRepository);
        subscriptionOrderModel.setSubsOrderId(orderId);
        SubscriptionOrderHdrModel subscriptionOrderHdrModel = SubscriptionUtil.constructSubscriptionOrderHdr(subscriptionOrderModel);
        subscriptionOrderHdrRepository.save(subscriptionOrderHdrModel);
        SubscriptionUtil.updateSubscriptionDtlModel(subscriptionOrderModel, catalogRepository);
        subscriptionOrderDtlRepository.save(subscriptionOrderModel.getItems());
        cartRepository.delete(cartRepository.findByCustId(subscriptionOrderModel.getCustId()));
        JSONObject response = new JSONObject();
        response.put("status", true);
        return ResponseEntity.ok(response.toString());
    }


    @RequestMapping(value = "/updateSubscriptionOrder", method = PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity updateSubscriptionOrder(HttpServletRequest request, @RequestParam(name = "orderId") long orderId, @RequestParam(name = "orderStatus") String orderStatus) throws Exception{
        if(!EasyShopUtil.isValidCustomer(userRepository, request)){
            return ResponseEntity.badRequest().body("Invalid Auth Token");
        }
        SubscriptionOrderHdrModel subscriptionOrderHdrModel = subscriptionOrderHdrRepository.findBySubsOrderId(orderId);
        subscriptionOrderHdrModel.setSubsOrderStatus(orderStatus);
        subscriptionOrderHdrRepository.save(subscriptionOrderHdrModel);
        JSONObject resp = new JSONObject();
        resp.put("status", true);
        return ResponseEntity.ok(resp.toString());
    }

    @RequestMapping(value = "/updateSubscriptionOrderItem", method = PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity updateSubscriptionOrderItem(HttpServletRequest request, @RequestParam(name = "orderId") long orderId, @RequestParam(name = "orderItemId") int orderItemId, @RequestParam(name = "orderItemStatus") String orderItemStatus) throws Exception{
        if(!EasyShopUtil.isValidCustomer(userRepository, request)){
            return ResponseEntity.badRequest().body("Invalid Auth Token");
        }
        SubscriptionOrderDtlModel subscriptionOrderDtlModel = subscriptionOrderDtlRepository.findBySubsOrderIdAndSubsOrderItemId(orderId, orderItemId);
        SubscriptionUtil.updateSubscriptionStatus(subscriptionOrderHdrRepository, subscriptionOrderDtlRepository, subscriptionOrderDtlModel, orderItemStatus);
        JSONObject resp = new JSONObject();
        resp.put("status", true);
        return ResponseEntity.ok(resp.toString());
    }
}
