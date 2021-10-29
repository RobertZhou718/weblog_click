package com.weblog.service.refer.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weblog.domain.StayTimePV;
import com.weblog.mapper.StayTimePVMapper;
import com.weblog.service.refer.StayTimeDimService;
import com.weblog.utils.JsonBean;
@Service
public class StayTimeDimServiceImpl implements StayTimeDimService {
	@Autowired(required = false)
  private StayTimePVMapper stayTimePVMapper;

	@Override
	public JsonBean getStayTimePV() {
		JsonBean json=new JsonBean();
    List<StayTimePV> hourPvs= this.stayTimePVMapper.selectList();
    
    List<String> categories=new ArrayList<>();
    List<Integer> datas=new ArrayList<>();
    for (StayTimePV bean:hourPvs) {
        categories.add(bean.getRequestUrl());
        datas.add(bean.getStayTime());
    }
    json.setCategories(categories);
    json.setDatas(datas);
    return json;
	}
	
}
