package com.yxw.cn.carpenterrepair.entity;

import android.util.Log;

import com.yxw.cn.carpenterrepair.util.JsonHelper;
import com.yxw.cn.carpenterrepair.util.PreferencesHelper;


/**
 * 作者：cgy on 16/12/26 22:53
 * 邮箱：529387306@qq.com
 */
public class CurrentUser extends LoginInfo{

    //region 单例
    private static final String TAG = CurrentUser.class.getSimpleName();
    private static final String USER = "CurrentUser";
    private static CurrentUser me;
    /**
     * 单例
     * @return 当前用户对象
     */
    public static CurrentUser getInstance() {
        if (me == null) {
            me = new CurrentUser();
        }
        return me;
    }

    /**
     * 出生
     * <p>尼玛！终于出生了！！！</p>
     * <p>调用此方法查询是否登录过</p>
     * @return 出生与否
     */
    public boolean isLogin() {
        String json = PreferencesHelper.getInstance().getString(USER);
        me = JsonHelper.fromJson(json, CurrentUser.class);
        return me != null;
    }

    public boolean login(LoginInfo entity) {
        boolean born = false;
        String json = "";
        if (entity != null) {
            me.setToken(entity.getToken());
            me.setLastLoginTime(entity.getLastLoginTime());
            me.setMobile(entity.getMobile());
            me.setAvatar(entity.getAvatar());
            me.setUsername(entity.getUsername());
            me.setUserId(entity.getUserId());
            me.setNickname(entity.getNickname());
            me.setTags(entity.getTags());
            me.setExpire(entity.getExpire());
            me.setRole(entity.getRole());

            me.setIdentityCard(entity.getIdentityCard());
            me.setIdentityCardFront(entity.getIdentityCardFront());
            me.setIdentityCardBack(entity.getIdentityCardBack());
            me.setResidentName(entity.getResidentName());
            me.setResident(entity.getResident());
            me.setServiceDate(entity.getServiceDate());
            me.setServiceTime(entity.getServiceTime());
            me.setIdCardStatus(entity.getIdCardStatus());
            me.setAliplayAccount(entity.getAliplayAccount());
            json = JsonHelper.toJson(me);
            born = me != null;
        }
        // 生完了
        if (!born) {
            Log.e(TAG, "尼玛，流产了！！！");
        } else {
            PreferencesHelper.getInstance().putString(USER,json);
        }
        return born;
    }

    // endregion 单例

    /**
     * 退出登录清理用户信息
     */
    public void loginOut() {
        me = null;
        PreferencesHelper.getInstance().putString(USER, "");
    }
}
