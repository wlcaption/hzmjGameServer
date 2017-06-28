package com.dyz.gameserver.msg.processor;

import com.context.ErrorCode;
import com.dyz.gameserver.Avatar;
import com.dyz.gameserver.commons.initial.AppCf;
import com.dyz.gameserver.commons.message.ClientRequest;
import com.dyz.gameserver.commons.session.GameSession;
import com.dyz.gameserver.logic.RoomLogic;
import com.dyz.gameserver.manager.RoomManager;
import com.dyz.gameserver.msg.processor.common.INotAuthProcessor;
import com.dyz.gameserver.msg.processor.common.MsgProcessor;
import com.dyz.gameserver.msg.response.ErrorResponse;
import com.dyz.gameserver.msg.response.messageBox.MessageBoxResponse;
import com.dyz.gameserver.msg.response.roomcard.RoomCardChangerResponse;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * Created by kevin on 2016/8/2.
 */
public class UpdateRoomCardMsgProcessor extends MsgProcessor implements INotAuthProcessor {
    @Override
    public void process(GameSession gameSession, ClientRequest request) throws Exception {
        Avatar avatar = gameSession.getRole(Avatar.class);
        if(avatar != null){
        int roomCard = Integer.parseInt(request.getString());
            if(AppCf.inAppPay && roomCard > 0) {
                avatar.updateRoomCard(roomCard);
                roomCard = avatar.avatarVO.getAccount().getRoomcard();
                avatar.getSession().sendMsg(new RoomCardChangerResponse(1, roomCard));
            } else {
                avatar.updateRoomCard();
                roomCard = avatar.avatarVO.getAccount().getRoomcard();
                avatar.getSession().sendMsg(new RoomCardChangerResponse(1, roomCard));
            }
        }
    }
}
