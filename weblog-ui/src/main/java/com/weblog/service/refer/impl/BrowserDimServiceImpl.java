package com.weblog.service.refer.impl;

import com.weblog.domain.BrowserPV;
import com.weblog.domain.OSPV;
import com.weblog.mapper.BrowserPVMapper;
import com.weblog.service.refer.BrowserDimService;
import com.weblog.utils.JsonBean;
import com.weblog.utils.KeyVlaue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class BrowserDimServiceImpl implements BrowserDimService {
    @Autowired(required = false)
    private BrowserPVMapper browserPVMapper;
    @Override
    public JsonBean getBrowser() {
        List<BrowserPV> browserPVS=this.browserPVMapper.getList();
        JsonBean json=new JsonBean();
        List<KeyVlaue> kvs=new LinkedList<>();
        for (BrowserPV o:browserPVS)
        {
            KeyVlaue kv=new KeyVlaue(new Long(o.getPvs()),o.getBrowser());
            kvs.add(kv);
        }
        json.setKeyValues(kvs);
        return json;
    }
}
