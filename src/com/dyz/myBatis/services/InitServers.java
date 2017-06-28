package com.dyz.myBatis.services;

import com.dyz.gameserver.commons.initial.AppCf;
import com.dyz.gameserver.msg.response.contact.ContactResponse;
import com.dyz.myBatis.model.ContactWay;
import com.dyz.persist.util.TaskTimer;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by kevin on 2016/6/17.
 */
public class InitServers {



    public void initServersFun() throws IOException {
        Reader reader = Resources.getResourceAsReader("myBatisConfig.xml");
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);

        //===============================================================
        AccountService.getInstance().initSetSession(sessionFactory);
        NoticeTableService.getInstance().initSetSession(sessionFactory);
        ContactWayService.getInstance().initSetSession(sessionFactory);
        StandingsService.getInstance().initSetSession(sessionFactory);
        StandingsDetailService.getInstance().initSetSession(sessionFactory);
        RoomInfoService.getInstance().initSetSession(sessionFactory);
        StandingsRelationService.getInstance().initSetSession(sessionFactory);
        StandingsAccountRelationService.getInstance().initSetSession(sessionFactory);
        PlayRecordService.getInstance().initSetSession(sessionFactory);

        ContactWay contactWay =  ContactWayService.getInstance().selectByPrimaryKey(1);
        if (contactWay != null && contactWay.getContent().equalsIgnoreCase("inAppPay")) {
            AppCf.inAppPay = true;
        } else {
            AppCf.inAppPay = false;
        }


        //TaskTimer.showTimer();
        TaskTimer.headBag();
    }

    private static InitServers initServers = new InitServers();

    public static InitServers getInstance(){
        return initServers;
    }
}
