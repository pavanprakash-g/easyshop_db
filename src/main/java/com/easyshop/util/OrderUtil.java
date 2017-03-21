package com.easyshop.util;

import com.easyshop.model.CatalogModel;
import com.easyshop.model.OrderDtlModel;
import com.easyshop.model.OrderHdrModel;
import com.easyshop.model.OrderModel;
import com.easyshop.repository.CatalogRepository;
import org.apache.log4j.Logger;


/**
 * Created by pavan on 2/5/17.
 */
public class OrderUtil {

    private final static Logger logger = Logger.getLogger(OrderUtil.class);

    public static OrderHdrModel constructOrderHdr(OrderModel orderModel){
        OrderHdrModel orderHdrModel = new OrderHdrModel();
        orderHdrModel.setCustId(orderModel.getCustId());
        orderHdrModel.setOrderCreatedDate(orderModel.getOrderCreatedDate());
        orderHdrModel.setOrderId(orderModel.getOrderId());
        orderHdrModel.setOrderStatus(orderModel.getOrderStatus());
        orderHdrModel.setOrderItemCount(orderModel.getOrderItemCount());
        orderHdrModel.setOrderItemCount(orderModel.getOrderItemCount());
        orderHdrModel.setOrderTotal(orderModel.getOrderTotal());
        orderHdrModel.setOrderAddressId(orderModel.getOrderAddressId());
        orderHdrModel.setOrderUpdatedDate(orderModel.getOrderUpdatedDate());
        return orderHdrModel;
    }

    public static void updateDtlModel(OrderModel orderModel, CatalogRepository catalogRepository) throws Exception{
        for(OrderDtlModel orderDtlModel : orderModel.getItems()){
            orderDtlModel.setOrderId(orderModel.getOrderId());
            CatalogModel catalogModel = catalogRepository.findByItemId(orderDtlModel.getOrderItemId());
            if(catalogModel!= null) {
                if(catalogModel.getItemQuantity() - orderDtlModel.getOrderItemQuantity() >=0) {
                    catalogModel.setItemQuantity(catalogModel.getItemQuantity() - orderDtlModel.getOrderItemQuantity());
                    catalogRepository.save(catalogModel);
                }else{
                    throw new Exception("Stock Not available");
                }
            }else{
                throw new Exception("Item Not available");
            }
        }
    }

}
