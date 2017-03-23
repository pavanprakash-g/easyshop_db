package com.easyshop.controller;

import com.easyshop.model.OrderDtlModel;
import com.easyshop.model.OrderHdrModel;
import com.easyshop.model.OrderModel;
import com.easyshop.repository.*;
import com.easyshop.util.EasyShopUtil;
import com.easyshop.util.OrderUtil;
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

/**
 * Created by pavan on 3/19/17.
 */
@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {
    private final static Logger logger = Logger.getLogger(OrderController.class);


    @Autowired
    private CommonRepository commonRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderHdrRepository orderHdrRepository;

    @Autowired
    private OrderDtlRepository orderDtlRepository;

    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private CartRepository cartRepository;

    @RequestMapping(value = "/getOrders", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getOrders(HttpServletRequest request, @RequestParam(name = "id", required = false, defaultValue ="0") int id) throws Exception{
        if(!EasyShopUtil.isValidCustomer(userRepository, request)){
            return ResponseEntity.badRequest().body("Invalid Auth Token");
        }
        return ResponseEntity.ok(OrderUtil.getData(id, orderHdrRepository, orderDtlRepository));
    }

    @RequestMapping(value = "/createOrders", method = POST, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createOrder(HttpServletRequest request, @RequestBody OrderModel orderModel) throws Exception{
        if(!EasyShopUtil.isValidCustomer(userRepository, request)){
            return ResponseEntity.badRequest().body("Invalid Auth Token");
        }
        long orderId = EasyShopUtil.getMaxId("order_hdr", "order_id", commonRepository);
        orderModel.setOrderId(orderId);
        OrderHdrModel orderHdrModel = OrderUtil.constructOrderHdr(orderModel);
        orderHdrRepository.save(orderHdrModel);
        OrderUtil.updateDtlModel(orderModel, catalogRepository);
        orderDtlRepository.save(orderModel.getItems());
        cartRepository.delete(cartRepository.findByCustId(orderModel.getCustId()));
        JSONObject response = new JSONObject();
        response.put("status", true);
        return ResponseEntity.ok(response.toString());
    }
}
