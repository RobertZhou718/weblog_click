package com.weblog.service.refer.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weblog.domain.RefererTop;
import com.weblog.domain.StayTimePV;
import com.weblog.mapper.RefererTopMapper;
import com.weblog.service.refer.RefererTopDimService;
import com.weblog.utils.JsonBean;
@Service
public class RefererTopDimServiceImpl implements RefererTopDimService{
	@Autowired(required = false)
	public RefererTopMapper refererTopMapper;
	@Override
	public JsonBean selectTop10() {
		// TODO Auto-generated method stub
		JsonBean json = new JsonBean();
		List<RefererTop> top10 = refererTopMapper.selectTop10();
		List<String> categories=new ArrayList<>();
    List<Integer> datas=new ArrayList<>();
    for (RefererTop bean:top10) {
        categories.add(bean.getHttpReferer());
        datas.add(bean.getTop10());
    }
    json.setCategories(categories);
    json.setDatas(datas);
    return json;
	}
	
}
