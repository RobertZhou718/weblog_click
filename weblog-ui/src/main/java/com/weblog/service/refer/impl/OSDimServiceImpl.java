package com.weblog.service.refer.impl;

import com.weblog.domain.OSPV;
import com.weblog.mapper.OSPVMapper;
import com.weblog.service.refer.OSDimService;
import com.weblog.utils.JsonBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weblog.utils.KeyVlaue;
import java.util.LinkedList;
import java.util.List;

@Service
public class OSDimServiceImpl implements OSDimService{

    @Autowired(required = false)
    private OSPVMapper ospvMapper;

    @Override
    public JsonBean getOS() {
        List<OSPV> lists=this.ospvMapper.getListByTime();
        JsonBean json=new JsonBean();
        List<KeyVlaue> kvs=new LinkedList<>();

        for (OSPV o:lists)
        {
            KeyVlaue kv=new KeyVlaue(o.getPvs(),o.getOs());
            kvs.add(kv);
        }
        json.setKeyValues(kvs);
        return json;
    }
}
