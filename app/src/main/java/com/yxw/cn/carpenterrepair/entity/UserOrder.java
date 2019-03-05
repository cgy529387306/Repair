package com.yxw.cn.carpenterrepair.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by CY on 2018/12/1
 */
public class UserOrder implements Serializable {

    private boolean isNext;
    private List<ListBean> list;

    public boolean isIsNext() {
        return isNext;
    }

    public void setIsNext(boolean isNext) {
        this.isNext = isNext;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean  implements Serializable{
        /**
         * id : 47
         * commentId : 11
         * complainId : 14
         * orderSn : 20181201145708897223866
         * name : 叶小伟
         * mobile : 13950384701
         * province : 福建省
         * city : 福州市
         * district : 闽侯县
         * address : 福建省福州市闽侯县橘园洲立交桥
         * agencyId : 350121
         * locationLat : 26.059955
         * locationLng : 119.240462
         * totalPrice : 1600.0
         * payStatus : 1
         * handleStatus : 0
         * receiveStatus : 0
         * serviceStatus : 3
         * commentStatus : 1
         * complainStatus : 1
         * cancelStatus : 0
         * orderStatus : 1
         * contactClient:0
         * bookingDate : 2018-12-12
         * bookingTime : 00:00:00
         * src : 0
         * userId : 41
         * deptId : null
         * deptUserId : null
         * categoryId : 34
         * createTime : 2018-12-11 14:27:06
         * payTime : 2018-12-11 14:27:06
         * endTime : 2018-12-11 14:27:06
         * remark : 到
         * picList : [{"id":37,"orderId":47,"url":"http://fix-oos.oss-cn-beijing.aliyuncs.com/file/20181201/14570738287d89.jpg","desc":null,"sortNo":null}]
         * timelineList : [{"id":71,"orderId":47,"description":"用户下单成功","time":"2018-12-01 14:57:09","type":10,"showUser":null,"value1":null,"value2":null,"value3":null,"value4":null,"value5":null},{"id":298,"orderId":47,"description":"工程师 13655055195 已到达现场","time":"2018-12-11 14:12:29","type":40,"showUser":null,"value1":null,"value2":null,"value3":null,"value4":null,"value5":null},{"id":299,"orderId":47,"description":"工程师 13655055195 评估维修费用¥250元","time":"2018-12-11 14:12:42","type":45,"showUser":null,"value1":null,"value2":null,"value3":null,"value4":null,"value5":null},{"id":302,"orderId":47,"description":"客户13655055195 已支付评估费用¥250.00元","time":"2018-12-11 14:17:19","type":50,"showUser":null,"value1":null,"value2":null,"value3":null,"value4":null,"value5":null},{"id":303,"orderId":47,"description":"工程师 13655055195 开始服务","time":"2018-12-11 14:17:27","type":52,"showUser":null,"value1":null,"value2":null,"value3":null,"value4":null,"value5":null},{"id":305,"orderId":47,"description":"工程师 13655055195 服务完成","time":"2018-12-11 14:17:54","type":55,"showUser":null,"value1":null,"value2":null,"value3":null,"value4":null,"value5":null},{"id":304,"orderId":47,"description":"客户 13655055195 确认服务完成","time":"2018-12-11 14:17:54","type":60,"showUser":null,"value1":null,"value2":null,"value3":null,"value4":null,"value5":null},{"id":308,"orderId":47,"description":"客户 13655055195 投诉完成","time":"2018-12-11 14:27:47","type":75,"showUser":null,"value1":null,"value2":null,"value3":null,"value4":null,"value5":null},{"id":307,"orderId":47,"description":"客户 13655055195 评价完成","time":"2018-12-11 14:27:10","type":85,"showUser":null,"value1":null,"value2":null,"value3":null,"value4":null,"value5":null}]
         * orderItems : [{"id":50,"orderId":47,"description":"上门费","type":0,"price":50,"createTime":"2018-12-01 14:57:09","payStatus":0,"payOrderSn":null}]
         * comment : {"id":11,"orderId":47,"userId":62,"content":"评价内容","star":5,"picture1":null,"picture2":null,"picture3":null,"picture4":null,"picture5":null,"createTime":"2018-12-11 14:27:09","pictures":[]}
         * complain : {"id":14,"orderId":47,"userId":null,"content":"投诉内容","mobile":"13655055195","createTime":"2018-12-11 14:27:46","pictures":[]}
         * categoryName : null
         * showStatus : null
         * receiveTime: "2019-01-12 14:57:35"
         */

        private int id;
        private int commentId;
        private int complainId;
        private String orderSn;
        private String name;
        private String mobile;
        private String province;
        private String city;
        private String district;
        private String address;
        private int agencyId;
        private double locationLat;
        private double locationLng;
        private double totalPrice;
        private int payStatus;
        private int handleStatus;
        private int receiveStatus;
        private int serviceStatus;
        private int commentStatus;
        private int complainStatus;
        private int cancelStatus;
        private int orderStatus;
        private int contactClient;
        private String orderStatusName;
        private String bookingDate;
        private String bookingTime;
        private int src;
        private int userId;
        private Object deptId;
        private Object deptUserId;
        private int categoryId;
        private String createTime;
        private String payTime;
        private String endTime;
        private String remark;
        private CommentBean comment;
        private ComplainBean complain;
        private String categoryName;
        private String showStatus;
        private String receiveTime;
        private List<PicListBean> picList;
        private List<TimelineListBean> timelineList;
        private List<OrderItemsBean> orderItems;

        public int getContactClient() {
            return contactClient;
        }

        public void setContactClient(int contactClient) {
            this.contactClient = contactClient;
        }

        public String getOrderStatusName() {
            return orderStatusName;
        }

        public void setOrderStatusName(String orderStatusName) {
            this.orderStatusName = orderStatusName;
        }

        public String getReceiveTime() {
            return receiveTime;
        }

        public void setReceiveTime(String receiveTime) {
            this.receiveTime = receiveTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCommentId() {
            return commentId;
        }

        public void setCommentId(int commentId) {
            this.commentId = commentId;
        }

        public int getComplainId() {
            return complainId;
        }

        public void setComplainId(int complainId) {
            this.complainId = complainId;
        }

        public String getOrderSn() {
            return orderSn;
        }

        public void setOrderSn(String orderSn) {
            this.orderSn = orderSn;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getAgencyId() {
            return agencyId;
        }

        public void setAgencyId(int agencyId) {
            this.agencyId = agencyId;
        }

        public double getLocationLat() {
            return locationLat;
        }

        public void setLocationLat(double locationLat) {
            this.locationLat = locationLat;
        }

        public double getLocationLng() {
            return locationLng;
        }

        public void setLocationLng(double locationLng) {
            this.locationLng = locationLng;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
        }

        public int getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(int payStatus) {
            this.payStatus = payStatus;
        }

        public int getHandleStatus() {
            return handleStatus;
        }

        public void setHandleStatus(int handleStatus) {
            this.handleStatus = handleStatus;
        }

        public int getReceiveStatus() {
            return receiveStatus;
        }

        public void setReceiveStatus(int receiveStatus) {
            this.receiveStatus = receiveStatus;
        }

        public int getServiceStatus() {
            return serviceStatus;
        }

        public void setServiceStatus(int serviceStatus) {
            this.serviceStatus = serviceStatus;
        }

        public int getCommentStatus() {
            return commentStatus;
        }

        public void setCommentStatus(int commentStatus) {
            this.commentStatus = commentStatus;
        }

        public int getComplainStatus() {
            return complainStatus;
        }

        public void setComplainStatus(int complainStatus) {
            this.complainStatus = complainStatus;
        }

        public int getCancelStatus() {
            return cancelStatus;
        }

        public void setCancelStatus(int cancelStatus) {
            this.cancelStatus = cancelStatus;
        }

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getBookingDate() {
            return bookingDate;
        }

        public void setBookingDate(String bookingDate) {
            this.bookingDate = bookingDate;
        }

        public String getBookingTime() {
            return bookingTime;
        }

        public void setBookingTime(String bookingTime) {
            this.bookingTime = bookingTime;
        }

        public int getSrc() {
            return src;
        }

        public void setSrc(int src) {
            this.src = src;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public Object getDeptId() {
            return deptId;
        }

        public void setDeptId(Object deptId) {
            this.deptId = deptId;
        }

        public Object getDeptUserId() {
            return deptUserId;
        }

        public void setDeptUserId(Object deptUserId) {
            this.deptUserId = deptUserId;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getPayTime() {
            return payTime;
        }

        public void setPayTime(String payTime) {
            this.payTime = payTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public CommentBean getComment() {
            return comment;
        }

        public void setComment(CommentBean comment) {
            this.comment = comment;
        }

        public ComplainBean getComplain() {
            return complain;
        }

        public void setComplain(ComplainBean complain) {
            this.complain = complain;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getShowStatus() {
            return showStatus;
        }

        public void setShowStatus(String showStatus) {
            this.showStatus = showStatus;
        }

        public List<PicListBean> getPicList() {
            return picList;
        }

        public void setPicList(List<PicListBean> picList) {
            this.picList = picList;
        }

        public List<TimelineListBean> getTimelineList() {
            return timelineList;
        }

        public void setTimelineList(List<TimelineListBean> timelineList) {
            this.timelineList = timelineList;
        }

        public List<OrderItemsBean> getOrderItems() {
            return orderItems;
        }

        public void setOrderItems(List<OrderItemsBean> orderItems) {
            this.orderItems = orderItems;
        }

        public static class CommentBean {
            /**
             * id : 11
             * orderId : 47
             * userId : 62
             * content : 评价内容
             * star : 5
             * picture1 : null
             * picture2 : null
             * picture3 : null
             * picture4 : null
             * picture5 : null
             * createTime : 2018-12-11 14:27:09
             * pictures : []
             */

            private int id;
            private int orderId;
            private int userId;
            private String content;
            private int star;
            private Object picture1;
            private Object picture2;
            private Object picture3;
            private Object picture4;
            private Object picture5;
            private String createTime;
            private List<?> pictures;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getOrderId() {
                return orderId;
            }

            public void setOrderId(int orderId) {
                this.orderId = orderId;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getStar() {
                return star;
            }

            public void setStar(int star) {
                this.star = star;
            }

            public Object getPicture1() {
                return picture1;
            }

            public void setPicture1(Object picture1) {
                this.picture1 = picture1;
            }

            public Object getPicture2() {
                return picture2;
            }

            public void setPicture2(Object picture2) {
                this.picture2 = picture2;
            }

            public Object getPicture3() {
                return picture3;
            }

            public void setPicture3(Object picture3) {
                this.picture3 = picture3;
            }

            public Object getPicture4() {
                return picture4;
            }

            public void setPicture4(Object picture4) {
                this.picture4 = picture4;
            }

            public Object getPicture5() {
                return picture5;
            }

            public void setPicture5(Object picture5) {
                this.picture5 = picture5;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public List<?> getPictures() {
                return pictures;
            }

            public void setPictures(List<?> pictures) {
                this.pictures = pictures;
            }
        }

        public static class ComplainBean {
            /**
             * id : 14
             * orderId : 47
             * userId : null
             * content : 投诉内容
             * mobile : 13655055195
             * createTime : 2018-12-11 14:27:46
             * pictures : []
             */

            private int id;
            private int orderId;
            private Object userId;
            private String content;
            private String mobile;
            private String createTime;
            private List<?> pictures;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getOrderId() {
                return orderId;
            }

            public void setOrderId(int orderId) {
                this.orderId = orderId;
            }

            public Object getUserId() {
                return userId;
            }

            public void setUserId(Object userId) {
                this.userId = userId;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public List<?> getPictures() {
                return pictures;
            }

            public void setPictures(List<?> pictures) {
                this.pictures = pictures;
            }
        }

        public static class PicListBean implements Serializable{
            /**
             * id : 37
             * orderId : 47
             * url : http://fix-oos.oss-cn-beijing.aliyuncs.com/file/20181201/14570738287d89.jpg
             * desc : null
             * sortNo : null
             */

            private int id;
            private int orderId;
            private String url;
            private Object desc;
            private Object sortNo;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getOrderId() {
                return orderId;
            }

            public void setOrderId(int orderId) {
                this.orderId = orderId;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public Object getDesc() {
                return desc;
            }

            public void setDesc(Object desc) {
                this.desc = desc;
            }

            public Object getSortNo() {
                return sortNo;
            }

            public void setSortNo(Object sortNo) {
                this.sortNo = sortNo;
            }
        }

        public static class TimelineListBean implements Serializable{
            /**
             * id : 71
             * orderId : 47
             * description : 用户下单成功
             * time : 2018-12-01 14:57:09
             * type : 10
             * showUser : null
             * value1 : null
             * value2 : null
             * value3 : null
             * value4 : null
             * value5 : null
             */

            private int id;
            private int orderId;
            private String description;
            private String time;
            private int type;
            private Object showUser;
            private Object value1;
            private Object value2;
            private Object value3;
            private Object value4;
            private Object value5;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getOrderId() {
                return orderId;
            }

            public void setOrderId(int orderId) {
                this.orderId = orderId;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public Object getShowUser() {
                return showUser;
            }

            public void setShowUser(Object showUser) {
                this.showUser = showUser;
            }

            public Object getValue1() {
                return value1;
            }

            public void setValue1(Object value1) {
                this.value1 = value1;
            }

            public Object getValue2() {
                return value2;
            }

            public void setValue2(Object value2) {
                this.value2 = value2;
            }

            public Object getValue3() {
                return value3;
            }

            public void setValue3(Object value3) {
                this.value3 = value3;
            }

            public Object getValue4() {
                return value4;
            }

            public void setValue4(Object value4) {
                this.value4 = value4;
            }

            public Object getValue5() {
                return value5;
            }

            public void setValue5(Object value5) {
                this.value5 = value5;
            }
        }

        public static class OrderItemsBean implements Serializable{
            /**
             * id : 50
             * orderId : 47
             * description : 上门费
             * type : 0
             * price : 50.0
             * createTime : 2018-12-01 14:57:09
             * payStatus : 0
             * payOrderSn : null
             */

            private int id;
            private int orderId;
            private String description;
            private int type;
            private double price;
            private String createTime;
            private int payStatus;
            private Object payOrderSn;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getOrderId() {
                return orderId;
            }

            public void setOrderId(int orderId) {
                this.orderId = orderId;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getPayStatus() {
                return payStatus;
            }

            public void setPayStatus(int payStatus) {
                this.payStatus = payStatus;
            }

            public Object getPayOrderSn() {
                return payOrderSn;
            }

            public void setPayOrderSn(Object payOrderSn) {
                this.payOrderSn = payOrderSn;
            }
        }
    }

}
