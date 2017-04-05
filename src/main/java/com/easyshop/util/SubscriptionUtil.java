package com.easyshop.util;

import com.easyshop.util.OrderUtil;
import com.easyshop.model.*;
import com.easyshop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Created by admin-hp on 2/4/17.
 */
@Service
public class SubscriptionUtil {

    @Autowired
    private OrderHdrRepository orderHdrRepository;

    @Autowired
    private OrderDtlRepository orderDtlRepository;

    @Autowired
    private SubscriptionOrderHdrRepository subscriptionOrderHdrRepository;

    @Autowired
    private SubscriptionOrderDtlRepository subscriptionOrderDtlRepository;

    @Autowired
    private NextDueDateRepository nextDueDateRepository;

    private final static Logger logger = Logger.getLogger(SubscriptionUtil.class);

    public static SubscriptionOrderHdrModel constructSubscriptionOrderHdr(SubscriptionOrderModel subscriptionOrderModel){
        SubscriptionOrderHdrModel subscriptionOrderHdrModel = new SubscriptionOrderHdrModel();
        subscriptionOrderHdrModel.setCustId(subscriptionOrderModel.getCustId());
        subscriptionOrderHdrModel.setSubsOrderCreatedDate(subscriptionOrderModel.getSubsOrderCreatedDate());
        subscriptionOrderHdrModel.setSubsOrderId(subscriptionOrderModel.getSubsOrderId());
        subscriptionOrderHdrModel.setSubsOrderStatus(subscriptionOrderModel.getSubsOrderStatus());
        subscriptionOrderHdrModel.setSubsOrderItemCount(subscriptionOrderModel.getSubsOrderItemCount());
        subscriptionOrderHdrModel.setSubsOrderTotal(subscriptionOrderModel.getSubsOrderTotal());
        subscriptionOrderHdrModel.setSubsOrderAddressId(subscriptionOrderModel.getSubsOrderAddressId());
        subscriptionOrderHdrModel.setSubsOrderUpdatedDate(subscriptionOrderModel.getSubsOrderUpdatedDate());
        return subscriptionOrderHdrModel;
    }

    public static void updateSubscriptionDtlModel(SubscriptionOrderModel subscriptionOrderModel, CatalogRepository catalogRepository) throws Exception{
        for(SubscriptionOrderDtlModel subscriptionOrderDtlModel : subscriptionOrderModel.getItems()){
            subscriptionOrderDtlModel.setSubsOrderId(subscriptionOrderModel.getSubsOrderId());
            CatalogModel catalogModel = catalogRepository.findByItemId(subscriptionOrderDtlModel.getSubsOrderItemId());
            if(catalogModel!= null) {
                if(catalogModel.getItemQuantity() - subscriptionOrderDtlModel.getSubsOrderItemQuantity() >=0) {
                    catalogModel.setItemQuantity(catalogModel.getItemQuantity() - subscriptionOrderDtlModel.getSubsOrderItemQuantity());
                    catalogRepository.save(catalogModel);
                }else{
                    throw new Exception("Stock Not available");
                }
            }else{
                throw new Exception("Item Not available");
            }
        }
    }

    public static List<SubscriptionOrderModel> getSubscriptionData(int id, SubscriptionOrderHdrRepository subscriptionOrderHdrRepository, SubscriptionOrderDtlRepository subscriptionOrderDtlRepository, CatalogRepository catalogRepository){
        List<SubscriptionOrderModel> subscriptionOrderModelList = new ArrayList<>();
        long subscriptionOrderId;
        if(id == 0){
            Iterable<SubscriptionOrderHdrModel> subscriptionOrderHdrModels = subscriptionOrderHdrRepository.findAll();
            for(SubscriptionOrderHdrModel subscriptionOrderHdrModel : subscriptionOrderHdrModels){
                SubscriptionOrderModel subscriptionOrderModel = new SubscriptionOrderModel();
                subscriptionOrderId = subscriptionOrderHdrModel.getSubsOrderId();
                Iterable<SubscriptionOrderDtlModel> subscriptionOrderDtlModels = subscriptionOrderDtlRepository.findBySubsOrderId(subscriptionOrderId);
                List<SubscriptionOrderDtlModel> subscriptionOrderDtlModelList = new ArrayList<>();
                for(SubscriptionOrderDtlModel subscriptionOrderDtlModel : subscriptionOrderDtlModels){
                    subscriptionOrderDtlModel.setSubsOrderItemName(catalogRepository.findByItemId(subscriptionOrderDtlModel.getSubsOrderItemId()).getItemName());
                    subscriptionOrderDtlModelList.add(subscriptionOrderDtlModel);
                }
                subscriptionOrderModel.setItems(subscriptionOrderDtlModelList);
                saveSubscriptionData(subscriptionOrderModel, subscriptionOrderHdrModel);
                subscriptionOrderModelList.add(subscriptionOrderModel);
            }
        }else{
            Iterable<SubscriptionOrderHdrModel> subscriptionOrderHdrModels = subscriptionOrderHdrRepository.findByCustId(id);
            for(SubscriptionOrderHdrModel subscriptionOrderHdrModel : subscriptionOrderHdrModels){
                SubscriptionOrderModel subscriptionOrderModel = new SubscriptionOrderModel();
                subscriptionOrderId = subscriptionOrderHdrModel.getSubsOrderId();
                Iterable<SubscriptionOrderDtlModel> subscriptionOrderDtlModels = subscriptionOrderDtlRepository.findBySubsOrderId(subscriptionOrderId);
                List<SubscriptionOrderDtlModel> subscriptionOrderDtlModelList = new ArrayList<>();
                for(SubscriptionOrderDtlModel subscriptionOrderDtlModel : subscriptionOrderDtlModels){
                    subscriptionOrderDtlModel.setSubsOrderItemName(catalogRepository.findByItemId(subscriptionOrderDtlModel.getSubsOrderItemId()).getItemName());
                    subscriptionOrderDtlModelList.add(subscriptionOrderDtlModel);
                }
                subscriptionOrderModel.setItems(subscriptionOrderDtlModelList);
                saveSubscriptionData(subscriptionOrderModel, subscriptionOrderHdrModel);
                subscriptionOrderModelList.add(subscriptionOrderModel);
            }
        }
        return subscriptionOrderModelList;
    }

    private static void saveSubscriptionData(SubscriptionOrderModel subscriptionOrderModel, SubscriptionOrderHdrModel subscriptionOrderHdrModel){
        subscriptionOrderModel.setSubsOrderId(subscriptionOrderHdrModel.getSubsOrderId());
        subscriptionOrderModel.setCustId(subscriptionOrderHdrModel.getCustId());
        subscriptionOrderModel.setSubsOrderItemCount(subscriptionOrderHdrModel.getSubsOrderItemCount());
        subscriptionOrderModel.setSubsOrderTotal(subscriptionOrderHdrModel.getSubsOrderTotal());
        subscriptionOrderModel.setSubsOrderStatus(subscriptionOrderHdrModel.getSubsOrderStatus());
        subscriptionOrderModel.setSubsOrderItemCount(subscriptionOrderHdrModel.getSubsOrderItemCount());
        subscriptionOrderModel.setSubsOrderTotal(subscriptionOrderHdrModel.getSubsOrderTotal());
        subscriptionOrderModel.setSubsOrderAddressId(subscriptionOrderHdrModel.getSubsOrderAddressId());
        subscriptionOrderModel.setSubsOrderCreatedDate(subscriptionOrderHdrModel.getSubsOrderCreatedDate());
        subscriptionOrderModel.setSubsOrderUpdatedDate(subscriptionOrderHdrModel.getSubsOrderUpdatedDate());
    }

    public static void updateSubscriptionStatus(SubscriptionOrderHdrRepository subscriptionOrderHdrRepository, SubscriptionOrderDtlRepository subscriptionOrderDtlRepository, SubscriptionOrderDtlModel subscriptionOrderDtlModel, String orderItemStatus){
        if("Return Approved".equals(orderItemStatus)) {
            SubscriptionOrderHdrModel subscriptionOrderHdrModel = subscriptionOrderHdrRepository.findBySubsOrderId(subscriptionOrderDtlModel.getSubsOrderId());
            subscriptionOrderHdrModel.setSubsOrderTotal(subscriptionOrderHdrModel.getSubsOrderTotal() - (subscriptionOrderDtlModel.getSubsOrderItemPrice() * subscriptionOrderDtlModel.getSubsOrderItemQuantity()));
            subscriptionOrderHdrRepository.save(subscriptionOrderHdrModel);
        }
        subscriptionOrderDtlModel.setSubsOrderItemStatus(orderItemStatus);
        subscriptionOrderDtlRepository.save(subscriptionOrderDtlModel);
    }

    public void updateNextDueDate(NextDueDateModel nextDueDateModel,Calendar date1){

        nextDueDateModel.setNextDueDate(date1);
        System.out.print("Time Updated:");
        System.out.println(nextDueDateModel.getNextDueDate());
        nextDueDateRepository.save(nextDueDateModel);
    }


    public OrderHdrModel constructHdrModel(SubscriptionOrderHdrModel subscriptionOrderHdrModel)
    {
        OrderHdrModel orderHdrModel = new OrderHdrModel();
        orderHdrModel.setOrderId(subscriptionOrderHdrModel.getSubsOrderId());
        orderHdrModel.setCustId(subscriptionOrderHdrModel.getCustId());
        orderHdrModel.setOrderItemCount(subscriptionOrderHdrModel.getSubsOrderItemCount());
        orderHdrModel.setOrderTotal(subscriptionOrderHdrModel.getSubsOrderTotal());
        orderHdrModel.setOrderStatus(subscriptionOrderHdrModel.getSubsOrderStatus());
        orderHdrModel.setOrderItemCount(subscriptionOrderHdrModel.getSubsOrderItemCount());
        orderHdrModel.setOrderTotal(subscriptionOrderHdrModel.getSubsOrderTotal());
        orderHdrModel.setOrderAddressId(subscriptionOrderHdrModel.getSubsOrderAddressId());
        orderHdrModel.setOrderCreatedDate(subscriptionOrderHdrModel.getSubsOrderCreatedDate());
        orderHdrModel.setOrderUpdatedDate(subscriptionOrderHdrModel.getSubsOrderUpdatedDate());
        return orderHdrModel;
    }

    public Vector<OrderDtlModel> constructDtlModel(Iterable <SubscriptionOrderDtlModel> subscriptionOrderDtlModels)
    {
        Vector<OrderDtlModel> orderDtlModels = new Vector<OrderDtlModel>();
        OrderDtlModel orderDtlModel = new OrderDtlModel();
        for(SubscriptionOrderDtlModel subscriptionOrderDtlModel0: subscriptionOrderDtlModels)
        {
            orderDtlModel.setOrderId(subscriptionOrderDtlModel0.getSubsOrderId());
            orderDtlModel.setOrderItemId(subscriptionOrderDtlModel0.getSubsOrderItemId());
            orderDtlModel.setOrderItemQuantity(subscriptionOrderDtlModel0.getSubsOrderItemQuantity());
            orderDtlModel.setOrderItemPrice(subscriptionOrderDtlModel0.getSubsOrderItemPrice());
            orderDtlModel.setOrderItemStatus(subscriptionOrderDtlModel0.getSubsOrderItemStatus());
            orderDtlModel.setOrderItemName(subscriptionOrderDtlModel0.getSubsOrderItemName());

            orderDtlModels.add(orderDtlModel);
        }
        return orderDtlModels;
    }

    @Scheduled(fixedDelay = 20000)
    public void checkNextDueDate()
    {
        System.out.println("Ha ha ha ha ha ha");
        Calendar date1 = Calendar.getInstance();
        OrderHdrModel orderHdrModel = new OrderHdrModel();
       Vector <OrderDtlModel> orderDtlModels;
        Iterable<NextDueDateModel> nextDueDateModels;
            nextDueDateModels=nextDueDateRepository.findAll();
            for(NextDueDateModel nextDueDateModel:nextDueDateModels)
            {
              //  System.out.println(nextDueDateModel.getNextDueDate().get(Calendar.MONTH));
              //  System.out.println(date1.get(Calendar.MONTH));
               // if(date1.compareTo(nextDueDateModel.getNextDueDate()) == 0)
                if(nextDueDateModel.getNextDueDate().before(date1))
                {
                    System.out.println("Date Matches");
                    orderHdrModel=constructHdrModel(subscriptionOrderHdrRepository.findBySubsOrderId(nextDueDateModel.getSubsOrderId()));
                    orderDtlModels=constructDtlModel(subscriptionOrderDtlRepository.findBySubsOrderId(nextDueDateModel.getSubsOrderId()));
                    orderHdrRepository.save(orderHdrModel);
                    for(OrderDtlModel orderDtlModel: orderDtlModels) {
                        orderDtlRepository.save(orderDtlModel);
                    }
                    date1.add(Calendar.MONTH, 1);
                    updateNextDueDate(nextDueDateModel,date1);
                    //nextDueDateRepository.save(updateNextDueDate(nextDueDateModel,date1));

                }
            }


    }
}

