package com.yao.spider.zimuku.service.impl;

import com.yao.spider.core.util.MyBatiesUtils;
import com.yao.spider.zhihu.dao.Impl.UserDaoImpl;
import com.yao.spider.zhihu.entity.User;
import com.yao.spider.zhihu.mapper.UserMapper;
import com.yao.spider.zimuku.dao.ZimuInfoMapper;
import com.yao.spider.zimuku.domain.ZimuInfo;
import com.yao.spider.zimuku.service.ZimuInfoService;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.xml.ws.Action;
import java.util.List;

public class ZimuInfoServiceImpl implements ZimuInfoService {
    private static Logger logger = LoggerFactory.getLogger(ZimuInfoService.class);

    public void batchInsert(List<ZimuInfo> zimuInfoList, SqlSession session) {

        try {
            ZimuInfoMapper mapper = session.getMapper(ZimuInfoMapper.class);
            mapper.batchInsert(zimuInfoList);
            session.commit();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {

        }
    }
}
