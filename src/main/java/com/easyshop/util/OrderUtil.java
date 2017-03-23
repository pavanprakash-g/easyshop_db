package com.easyshop.util;

import com.easyshop.model.CatalogModel;
import com.easyshop.model.OrderDtlModel;
import com.easyshop.model.OrderHdrModel;
import com.easyshop.model.OrderModel;
import com.easyshop.repository.CatalogRepository;
import com.easyshop.repository.CommonRepository;
import com.easyshop.repository.OrderDtlRepository;
import com.easyshop.repository.OrderHdrRepository;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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

    public static List<OrderModel> getData(int id, OrderHdrRepository orderHdrRepository, OrderDtlRepository orderDtlRepository, CatalogRepository catalogRepository){
        List<OrderModel> orderModelList = new ArrayList<>();
        long orderId;
        if(id == 0){
            Iterable<OrderHdrModel> orderHdrModels = orderHdrRepository.findAll();
            for(OrderHdrModel orderHdrModel : orderHdrModels){
                OrderModel orderModel = new OrderModel();
                orderId = orderHdrModel.getOrderId();
                Iterable<OrderDtlModel> orderDtlModels = orderDtlRepository.findByOrderId(orderId);
                List<OrderDtlModel> orderDtlModelList = new ArrayList<>();
                for(OrderDtlModel orderDtlModel : orderDtlModels){
                    orderDtlModel.setOrderItemName(catalogRepository.findByItemId(orderDtlModel.getOrderItemId()).getItemName());
                    orderDtlModelList.add(orderDtlModel);
                }
                orderModel.setItems(orderDtlModelList);
                saveData(orderModel, orderHdrModel);
                orderModelList.add(orderModel);
            }
        }else{
            Iterable<OrderHdrModel> orderHdrModels = orderHdrRepository.findByCustId(id);
            for(OrderHdrModel orderHdrModel : orderHdrModels){
                OrderModel orderModel = new OrderModel();
                orderId = orderHdrModel.getOrderId();
                Iterable<OrderDtlModel> orderDtlModels = orderDtlRepository.findByOrderId(orderId);
                List<OrderDtlModel> orderDtlModelList = new ArrayList<>();
                for(OrderDtlModel orderDtlModel : orderDtlModels){
                    orderDtlModel.setOrderItemName(catalogRepository.findByItemId(orderDtlModel.getOrderItemId()).getItemName());
                    orderDtlModelList.add(orderDtlModel);
                }
                orderModel.setItems(orderDtlModelList);
                saveData(orderModel, orderHdrModel);
                orderModelList.add(orderModel);
            }
        }
        return orderModelList;
    }

    private static void saveData(OrderModel orderModel, OrderHdrModel orderHdrModel){
        orderModel.setOrderId(orderHdrModel.getOrderId());
        orderModel.setCustId(orderHdrModel.getCustId());
        orderModel.setOrderItemCount(orderHdrModel.getOrderItemCount());
        orderModel.setOrderTotal(orderHdrModel.getOrderTotal());
        orderModel.setOrderStatus(orderHdrModel.getOrderStatus());
        orderModel.setOrderItemCount(orderHdrModel.getOrderItemCount());
        orderModel.setOrderTotal(orderHdrModel.getOrderTotal());
        orderModel.setOrderAddressId(orderHdrModel.getOrderAddressId());
        orderModel.setOrderUpdatedDate(orderHdrModel.getOrderUpdatedDate());
        orderModel.setOrderUpdatedDate(orderHdrModel.getOrderUpdatedDate());
    }

    public static void updateStatus(OrderHdrRepository orderHdrRepository, OrderDtlRepository orderDtlRepository, OrderDtlModel orderDtlModel, String orderItemStatus){
        if("Return Approved".equals(orderItemStatus)) {
            OrderHdrModel orderHdrModel = orderHdrRepository.findByOrderId(orderDtlModel.getOrderId());
            orderHdrModel.setOrderTotal(orderHdrModel.getOrderTotal() - (orderDtlModel.getOrderItemPrice() * orderDtlModel.getOrderItemQuantity()));
            orderHdrRepository.save(orderHdrModel);
        }
        orderDtlModel.setOrderItemStatus(orderItemStatus);
        orderDtlRepository.save(orderDtlModel);
    }

}
