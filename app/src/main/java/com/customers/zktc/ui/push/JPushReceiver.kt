package com.customers.zktc.ui.push

import android.content.Context
import android.content.Intent
import cn.jpush.android.api.CmdMessage
import cn.jpush.android.api.CustomMessage
import cn.jpush.android.api.JPushMessage
import cn.jpush.android.api.NotificationMessage
import cn.jpush.android.service.JPushMessageReceiver

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/4 16:07
 * Email: 1033144294@qq.com
 */
class JPushReceiver : JPushMessageReceiver() {

    override fun onMessage(context: Context?, customMessage: CustomMessage?) {
        super.onMessage(context, customMessage)
        processCustomMessage(context, customMessage)
    }

    override fun onNotifyMessageOpened(context: Context?, notificationMessage: NotificationMessage?) {
        super.onNotifyMessageOpened(context, notificationMessage)
        //打开自定义的Activity


    }

    override fun onMultiActionClicked(context: Context, intent: Intent) {
        super.onMultiActionClicked(context, intent)
        //        Log.e(TAG, "[onMultiActionClicked] 用户点击了通知栏按钮");
    }

    override fun onNotifyMessageArrived(
        context: Context?,
        notificationMessage: NotificationMessage?
    ) {
        super.onNotifyMessageArrived(context, notificationMessage)

    }

    override fun onNotifyMessageDismiss(
        context: Context?,
        notificationMessage: NotificationMessage?
    ) {
        super.onNotifyMessageDismiss(context, notificationMessage)
    }

    override fun onRegister(context: Context?, s: String?) {
        super.onRegister(context, s)
        //Log.e(TAG,"[onRegister] "+registrationId);
    }

    override fun onConnected(context: Context?, isConnected: Boolean) {
        //        Log.e(TAG,"[onConnected] "+isConnected);
    }


    override fun onCommandResult(context: Context?, cmdMessage: CmdMessage?) {
        //        Log.e(TAG,"[onCommandResult] "+cmdMessage);
    }

    override fun onTagOperatorResult(context: Context?, jPushMessage: JPushMessage?) {
        //        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context,jPushMessage);
        super.onTagOperatorResult(context, jPushMessage)
    }

    override fun onCheckTagOperatorResult(context: Context?, jPushMessage: JPushMessage?) {
        //        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context,jPushMessage);
        super.onCheckTagOperatorResult(context, jPushMessage)
    }

    override fun onAliasOperatorResult(context: Context?, jPushMessage: JPushMessage?) {
        //        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context,jPushMessage);
        super.onAliasOperatorResult(context, jPushMessage)
    }

    override fun onMobileNumberOperatorResult(context: Context?, jPushMessage: JPushMessage?) {
        //        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context,jPushMessage);
        super.onMobileNumberOperatorResult(context, jPushMessage)
    }

    //send msg to MainActivity
    private fun processCustomMessage(context: Context?, customMessage: CustomMessage?) {
    }
}