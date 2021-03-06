package com.dyz.gameserver.msg.processor.joinroom;

import com.context.ErrorCode;
import com.dyz.gameserver.Avatar;
import com.dyz.gameserver.commons.message.ClientRequest;
import com.dyz.gameserver.commons.session.GameSession;
import com.dyz.gameserver.logic.RoomLogic;
import com.dyz.gameserver.manager.GameSessionManager;
import com.dyz.gameserver.manager.RoomManager;
import com.dyz.gameserver.msg.processor.common.INotAuthProcessor;
import com.dyz.gameserver.msg.processor.common.MsgProcessor;
import com.dyz.gameserver.msg.response.ErrorResponse;
import com.dyz.persist.util.GlobalUtil;
import net.sf.json.JSONObject;

/**
 * 
 * @author luck
 * 加入房间
 */
public class JoinRoomMsgProcessor extends MsgProcessor implements
		INotAuthProcessor {

	public JoinRoomMsgProcessor() {
	}

	@Override
	public void process(GameSession gameSession, ClientRequest request)
			throws Exception {
		if(GlobalUtil.checkIsLogin(gameSession)) {
			JSONObject json = JSONObject.fromObject(request.getString());
			int roomId = (int)json.get("roomId");
			//if (avatar != null) {
				RoomLogic roomLogic = RoomManager.getInstance().getRoom(roomId);
				if (roomLogic != null) {
					Avatar avatar = gameSession.getRole(Avatar.class);
					if(avatar.avatarVO.getRoomId() != 0){
						//avatar.getSession().sendMsg(new ErrorResponse(ErrorCode.Error_000017));
						// 在房间则 直接回到房间
						//把session放入到GameSessionManager,并且移除以前的session
						GameSessionManager.getInstance().putGameSessionInHashMap(gameSession,avatar.getUuId());
						roomLogic.returnBackAction(avatar);
						return;
					} else {
						for (Avatar ava :roomLogic.getPlayerList()) {
						    if (ava.getUuId() == avatar.getUuId()) {
								GameSessionManager.getInstance().putGameSessionInHashMap(gameSession,avatar.getUuId());
								roomLogic.returnBackAction(avatar);
								return;
							}
						}
					}
					roomLogic.intoRoom(avatar);
					//boolean joinResult = roomLogic.intoRoom(avatar);
					/*if(joinResult) {
						//system.out.println("加入房间成功");
					}else{
						//system.out.println("加入房间失败");
					}*/
				} else {
					gameSession.sendMsg(new ErrorResponse(ErrorCode.Error_000018));
				}
			//}
			//system.out.println("roomId --> " + roomId);
		}
		else{
			//system.out.println("该用户还没有登录");
			gameSession.destroyObj();
		}
	}

}
