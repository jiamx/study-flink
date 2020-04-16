package com.jmx.api;

/**
 *  @Created with IntelliJ IDEA.
 *  @author : jmx
 *  @Date: 2020/4/14
 *  @Time: 11:51
 *  
 */

public class UserBehavior {

    public long userId;         // 用户ID
    public long itemId;         // 商品ID
    public int  catId;          // 商品类目ID
    public int merchantId;      // 卖家ID
    public  int brandId;        // 品牌ID
    public String action;       // 用户行为, 包括("pv", "buy", "cart", "fav")
    public String gender;       // 性别
    public long timestamp;      // 行为发生的时间戳，单位秒

    public static UserBehavior of(long userId, long itemId, int catId , int merchantId, int brandId,String action,String gender,long timestamp) {
        UserBehavior behavior = new UserBehavior();
        behavior.userId = userId;
        behavior.itemId = itemId;
        behavior.catId = catId;
        behavior.merchantId = merchantId;
        behavior.brandId = brandId;
        behavior.action = action;
        behavior.gender = gender;
        behavior.timestamp = timestamp;

        return behavior;
    }

    @Override
    public String toString() {
        return "UserBehavior{" +
                "userId=" + userId +
                ", itemId=" + itemId +
                ", catId=" + catId +
                ", merchantId=" + merchantId +
                ", brandId=" + brandId +
                ", action='" + action + '\'' +
                ", gender='" + gender + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
