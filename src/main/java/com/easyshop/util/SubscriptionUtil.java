package com.easyshop.util;

import com.easyshop.util.OrderUtil;
import com.easyshop.model.*;
import com.easyshop.repository.*;
import org.apache.log4j.Level;
import org.json.JSONObject;
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
    private CatalogRepository catalogRepository;

    @Autowired
    private SubscriptionOrderHdrRepository subscriptionOrderHdrRepository;

    @Autowired
    private SubscriptionOrderDtlRepository subscriptionOrderDtlRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private NextDueDateRepository nextDueDateRepository;

    @Autowired
    private TaxRepository taxRepository;

    @Autowired
    private CommonRepository commonRepository;

    @Autowired
    private MessageRepository messageRepository;

    private final static Logger logger = Logger.getLogger(SubscriptionUtil.class);

    public static SubscriptionOrderHdrModel constructSubscriptionOrderHdr(SubscriptionOrderModel subscriptionOrderModel, SubscriptionOrderHdrModel subscriptionOrderHdrModel){
        if(subscriptionOrderHdrModel == null) {
            subscriptionOrderHdrModel = new SubscriptionOrderHdrModel();
        }
        subscriptionOrderHdrModel.setCustId(subscriptionOrderModel.getCustId());
        subscriptionOrderHdrModel.setSubsOrderCreatedDate(subscriptionOrderModel.getSubsOrderCreatedDate());
        subscriptionOrderHdrModel.setSubsOrderId(subscriptionOrderModel.getSubsOrderId());
        subscriptionOrderHdrModel.setSubsOrderStatus(subscriptionOrderModel.getSubsOrderStatus());
        subscriptionOrderHdrModel.setSubsOrderItemCount(subscriptionOrderModel.getSubsOrderItemCount());
        subscriptionOrderHdrModel.setSubsOrderTotal(subscriptionOrderModel.getSubsOrderTotal());
        subscriptionOrderHdrModel.setSubsOrderAddressId(subscriptionOrderModel.getSubsOrderAddressId());
        subscriptionOrderHdrModel.setSubsOrderBillingAddrId(subscriptionOrderModel.getSubsOrderBillingAddrId());
        subscriptionOrderHdrModel.setSubsOrderUpdatedDate(new Date());
        subscriptionOrderModel.setSubsOrderCreatedDate(new Date());
        subscriptionOrderHdrModel.setSubsOrderCreatedDate(new Date());

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

    public static List<SubscriptionOrderModel> getSubscriptionData(int id, SubscriptionOrderHdrRepository subscriptionOrderHdrRepository, SubscriptionOrderDtlRepository subscriptionOrderDtlRepository, CatalogRepository catalogRepository, NextDueDateRepository nextDueDateRepository, TaxRepository taxRepository, AddressRepository addressRepository){
        List<SubscriptionOrderModel> subscriptionOrderModelList = new ArrayList<>();
        long subscriptionOrderId;
        long totalAmount;
        long taxAmount;
        long itemPrice;
        float taxPercentage;
        Iterable<SubscriptionOrderHdrModel> subscriptionOrderHdrModels = subscriptionOrderHdrRepository.findByCustId(id);
        for(SubscriptionOrderHdrModel subscriptionOrderHdrModel : subscriptionOrderHdrModels) {
            totalAmount = 0L;
            taxAmount = 0L;
            SubscriptionOrderModel subscriptionOrderModel = new SubscriptionOrderModel();
            subscriptionOrderId = subscriptionOrderHdrModel.getSubsOrderId();
            Iterable<SubscriptionOrderDtlModel> subscriptionOrderDtlModels = subscriptionOrderDtlRepository.findBySubsOrderId(subscriptionOrderId);
            taxPercentage = taxRepository.findByZipcode(addressRepository.findByAddressId(subscriptionOrderHdrModel.getSubsOrderAddressId()).getZipcode()).getTaxPercentage();
            List<SubscriptionOrderDtlModel> subscriptionOrderDtlModelList = new ArrayList<>();
            for (SubscriptionOrderDtlModel subscriptionOrderDtlModel : subscriptionOrderDtlModels) {
                itemPrice = (long)catalogRepository.findByItemId(subscriptionOrderDtlModel.getSubsOrderItemId()).getItemPrice();
                subscriptionOrderDtlModel.setSubsOrderItemName(catalogRepository.findByItemId(subscriptionOrderDtlModel.getSubsOrderItemId()).getItemName());
                subscriptionOrderDtlModel.setSubsOrderItemPrice(itemPrice);
                subscriptionOrderDtlModelList.add(subscriptionOrderDtlModel);
                totalAmount += itemPrice*subscriptionOrderDtlModel.getSubsOrderItemQuantity();
                taxAmount += (taxPercentage/100)*itemPrice*subscriptionOrderDtlModel.getSubsOrderItemQuantity();
            }
            subscriptionOrderHdrModel.setSubsOrderTotal(totalAmount+taxAmount);
            subscriptionOrderHdrModel.setTaxAmount(taxAmount);
            subscriptionOrderModel.setItems(subscriptionOrderDtlModelList);
            saveSubscriptionData(subscriptionOrderModel, subscriptionOrderHdrModel);
            subscriptionOrderModel.setNextDueDate(nextDueDateRepository.findBySubsOrderId(subscriptionOrderId).getNextDueDate());
            subscriptionOrderModel.setSubscriptionType(nextDueDateRepository.findBySubsOrderId(subscriptionOrderId).getSubscriptionType());
            subscriptionOrderModelList.add(subscriptionOrderModel);
        }
        return subscriptionOrderModelList;
    }

    private static void saveSubscriptionData(SubscriptionOrderModel subscriptionOrderModel, SubscriptionOrderHdrModel subscriptionOrderHdrModel){
        subscriptionOrderModel.setSubsOrderHdrId(subscriptionOrderHdrModel.getSubsOrderHdrId());
        subscriptionOrderModel.setSubsOrderId(subscriptionOrderHdrModel.getSubsOrderId());
        subscriptionOrderModel.setCustId(subscriptionOrderHdrModel.getCustId());
        subscriptionOrderModel.setSubsOrderItemCount(subscriptionOrderHdrModel.getSubsOrderItemCount());
        subscriptionOrderModel.setSubsOrderTotal(subscriptionOrderHdrModel.getSubsOrderTotal());
        subscriptionOrderModel.setSubsOrderStatus(subscriptionOrderHdrModel.getSubsOrderStatus());
        subscriptionOrderModel.setSubsOrderItemCount(subscriptionOrderHdrModel.getSubsOrderItemCount());
        subscriptionOrderModel.setSubsOrderTotal(subscriptionOrderHdrModel.getSubsOrderTotal());
        subscriptionOrderModel.setTaxAmount(subscriptionOrderHdrModel.getTaxAmount());
        subscriptionOrderModel.setSubsOrderAddressId(subscriptionOrderHdrModel.getSubsOrderAddressId());
        subscriptionOrderModel.setSubsOrderBillingAddrId(subscriptionOrderHdrModel.getSubsOrderBillingAddrId());
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
        logger.log(Level.INFO,"date1::::"+date1);
        nextDueDateModel.setNextDueDate(date1);
        System.out.print("Time Updated:");
        //System.out.println(nextDueDateModel.getNextDueDate());
        nextDueDateRepository.save(nextDueDateModel);
    }

    public static NextDueDateModel constructNextDueDate(SubscriptionOrderModel subscriptionOrderModel, NextDueDateModel nextDueDateModel)
    {
        if(nextDueDateModel == null) {
            nextDueDateModel = new NextDueDateModel();
        }
        logger.log(Level.INFO,subscriptionOrderModel.toString());
        int subs_type=subscriptionOrderModel.getSubscriptionType();
        nextDueDateModel.setSubscriptionType(subs_type);

        if(subs_type==1){
            Date date1= subscriptionOrderModel.getSubsOrderCreatedDate();
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            cal1.add(Calendar.MONTH, 1);
            nextDueDateModel.setNextDueDate(cal1);
        }else if(subs_type==2){
            Date date1= subscriptionOrderModel.getSubsOrderCreatedDate();
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            cal1.add(Calendar.MONTH, 2);
            nextDueDateModel.setNextDueDate(cal1);
        }else if(subs_type==3){
            Date date1= subscriptionOrderModel.getSubsOrderCreatedDate();
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            cal1.add(Calendar.MONTH, 3);
            nextDueDateModel.setNextDueDate(cal1);
        }else if(subs_type==6){
            Date date1= subscriptionOrderModel.getSubsOrderCreatedDate();
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            cal1.add(Calendar.MONTH, 6);
            nextDueDateModel.setNextDueDate(cal1);
        }else if(subs_type==12){
            Date date1= subscriptionOrderModel.getSubsOrderCreatedDate();
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            cal1.add(Calendar.MONTH, 12);
            nextDueDateModel.setNextDueDate(cal1);
        }
        nextDueDateModel.setSubsOrderId(subscriptionOrderModel.getSubsOrderId());
        return nextDueDateModel;
    }

    public OrderHdrModel constructHdrModel(SubscriptionOrderHdrModel subscriptionOrderHdrModel) {
        OrderHdrModel orderHdrModel = new OrderHdrModel();
        orderHdrModel.setOrderId(EasyShopUtil.getMaxId("order_hdr","order_id",commonRepository));
        orderHdrModel.setCustId(subscriptionOrderHdrModel.getCustId());
        orderHdrModel.setOrderItemCount(subscriptionOrderHdrModel.getSubsOrderItemCount());
        orderHdrModel.setOrderTotal(subscriptionOrderHdrModel.getSubsOrderTotal());
        orderHdrModel.setOrderStatus(subscriptionOrderHdrModel.getSubsOrderStatus());
        orderHdrModel.setOrderItemCount(subscriptionOrderHdrModel.getSubsOrderItemCount());
        orderHdrModel.setOrderTotal(subscriptionOrderHdrModel.getSubsOrderTotal());
        orderHdrModel.setOrderAddressId(subscriptionOrderHdrModel.getSubsOrderAddressId());
        orderHdrModel.setOrderCreatedDate(subscriptionOrderHdrModel.getSubsOrderCreatedDate());
        orderHdrModel.setOrderUpdatedDate(subscriptionOrderHdrModel.getSubsOrderUpdatedDate());
        orderHdrModel.setExpectedDeliveryDate(subscriptionOrderHdrModel.getSubsOrderUpdatedDate());
        return orderHdrModel;

    }


    public Vector<OrderDtlModel> constructDtlModel(long orderId, Iterable <SubscriptionOrderDtlModel> subscriptionOrderDtlModels) throws Exception{
        Vector<OrderDtlModel> orderDtlModels = new Vector<OrderDtlModel>();
        OrderDtlModel orderDtlModel = new OrderDtlModel();
        for(SubscriptionOrderDtlModel subscriptionOrderDtlModel0: subscriptionOrderDtlModels){
            orderDtlModel.setOrderId(orderId);
            orderDtlModel.setOrderItemId(subscriptionOrderDtlModel0.getSubsOrderItemId());
            orderDtlModel.setOrderItemQuantity(subscriptionOrderDtlModel0.getSubsOrderItemQuantity());
            orderDtlModel.setOrderItemPrice(subscriptionOrderDtlModel0.getSubsOrderItemPrice());
            orderDtlModel.setOrderItemStatus(subscriptionOrderDtlModel0.getSubsOrderItemStatus());
            orderDtlModel.setOrderItemName(subscriptionOrderDtlModel0.getSubsOrderItemName());
            orderDtlModels.add(orderDtlModel);
            CatalogModel catalogModel = catalogRepository.findByItemId(subscriptionOrderDtlModel0.getSubsOrderItemId());
            if(catalogModel.getItemQuantity() - orderDtlModel.getOrderItemQuantity() >=0) {
                catalogModel.setItemQuantity(catalogModel.getItemQuantity() - orderDtlModel.getOrderItemQuantity());
                catalogRepository.save(catalogModel);
            }else{
                logger.log(Level.INFO,"added the schema for ");
                throw new Exception("Stock is not there for "+catalogRepository.findByItemId(orderDtlModel.getOrderItemId()).getItemName()+". " +
                        " Order will be created when the stock is available.");
            }
        }
        return orderDtlModels;
    }

    @Scheduled(fixedDelay = 20000)
    public void checkNextDueDate()
    {
        logger.log(Level.INFO,"Inside the scheduler");

        float taxPercentage;
        long totalAmount = 0;
        long taxAmount = 0;
        int itemCount = 0;
        OrderHdrModel orderHdrModel = new OrderHdrModel();
        Vector <OrderDtlModel> orderDtlModels = null;
        Iterable<NextDueDateModel> nextDueDateModels;
        nextDueDateModels=nextDueDateRepository.findAll();
        try {
            for (NextDueDateModel nextDueDateModel : nextDueDateModels) {
                Calendar date1 = Calendar.getInstance();
                Calendar date2 = Calendar.getInstance();
                if (nextDueDateModel.getNextDueDate().before(date1)) {
                    System.out.println("Date Matches");
                    orderHdrModel = constructHdrModel(subscriptionOrderHdrRepository.findBySubsOrderId(nextDueDateModel.getSubsOrderId()));
                    taxPercentage = taxRepository.findByZipcode(addressRepository.findByAddressId(orderHdrModel.getOrderAddressId()).getZipcode()).getTaxPercentage();
                    orderDtlModels = constructDtlModel(orderHdrModel.getOrderId(), subscriptionOrderDtlRepository.findBySubsOrderId(nextDueDateModel.getSubsOrderId()));
                    for (OrderDtlModel orderDtlModel : orderDtlModels) {
                        orderDtlModel.setOrderItemPrice((long) catalogRepository.findByItemId(orderDtlModel.getOrderItemId()).getItemPrice());
                        totalAmount += orderDtlModel.getOrderItemPrice() * orderDtlModel.getOrderItemQuantity();
                        taxAmount += (taxPercentage / 100) * orderDtlModel.getOrderItemPrice() * orderDtlModel.getOrderItemQuantity();
                        orderDtlRepository.save(orderDtlModel);
                    }
                    orderHdrModel.setTaxAmount(taxAmount);
                    orderHdrModel.setOrderTotal(totalAmount + taxAmount);
                    orderHdrModel.setOrderItemCount(itemCount);
                    int subs_type = nextDueDateModel.getSubscriptionType();
                    if (subs_type == 1) {
                        date1.add(Calendar.MONTH, 1);
                        date2.add(Calendar.DAY_OF_MONTH, 5);
                        Date delivery_date = date2.getTime();
                        updateNextDueDate(nextDueDateModel, date1);
                        orderHdrModel.setExpectedDeliveryDate(delivery_date);
                    } else if (subs_type == 2) {
                        date1.add(Calendar.MONTH, 2);
                        date2.add(Calendar.DAY_OF_MONTH, 5);
                        Date delivery_date = date2.getTime();
                        updateNextDueDate(nextDueDateModel, date1);
                        orderHdrModel.setExpectedDeliveryDate(delivery_date);
                    } else if (subs_type == 3) {
                        logger.log(Level.INFO, "current::::" + date1.getTime());
                        date1.add(Calendar.MONTH, 3);
                        logger.log(Level.INFO, "added::::" + date1.getTime());
                        date2.add(Calendar.DAY_OF_MONTH, 5);
                        Date delivery_date = date2.getTime();
                        updateNextDueDate(nextDueDateModel, date1);
                        orderHdrModel.setExpectedDeliveryDate(delivery_date);
                    } else if (subs_type == 6) {
                        date1.add(Calendar.MONTH, 6);
                        date2.add(Calendar.DAY_OF_MONTH, 5);
                        Date delivery_date = date2.getTime();
                        updateNextDueDate(nextDueDateModel, date1);
                        orderHdrModel.setExpectedDeliveryDate(delivery_date);
                    } else if (subs_type == 12) {
                        date1.add(Calendar.MONTH, 12);
                        date2.add(Calendar.DAY_OF_MONTH, 5);
                        Date delivery_date = date2.getTime();
                        updateNextDueDate(nextDueDateModel, date1);
                        orderHdrModel.setExpectedDeliveryDate(delivery_date);
                    }
                    logger.log(Level.INFO, orderHdrModel.toString());
                    orderHdrRepository.save(orderHdrModel);
                }
            }
        }catch (Exception ex){
            logger.log(Level.INFO,"test"+ex);
            MessageModel messageModel = new MessageModel();
            messageModel.setCustId(3);
            messageModel.setMessageContent(ex.toString());
            messageModel.setRead(false);
            messageModel.setMessageTime(new Date());
            messageRepository.save(messageModel);
            logger.log(Level.INFO,ex);
        }
    }
}

