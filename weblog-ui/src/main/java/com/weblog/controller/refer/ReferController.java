package com.weblog.controller.refer;

import com.weblog.service.refer.BrowserDimService;
import com.weblog.service.refer.OSDimService;
import com.weblog.service.refer.ProvinceDimService;
import com.weblog.service.refer.RefererTopDimService;
import com.weblog.service.refer.StayTimeDimService;
import com.weblog.utils.JsonBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/refer")
public class ReferController {
    @Autowired
    private OSDimService osDimService;
    @Autowired
    private BrowserDimService browserDimService;
    @Autowired
    private ProvinceDimService provinceDimService;
    @Autowired
    private StayTimeDimService stayTimeDimService;
    @Autowired
    private RefererTopDimService refererTopDimService;
    @RequestMapping("/os")
    public @ResponseBody JsonBean getOS()
    {
        return this.osDimService.getOS();
    }

    @RequestMapping("/browser")
    public @ResponseBody JsonBean getBrowser()
    {
        return this.browserDimService.getBrowser();
    }

    @RequestMapping("/province")
    public @ResponseBody JsonBean getProvince()
    {
        return this.provinceDimService.getProvince();
    }
    @RequestMapping("/staytime")
    public @ResponseBody JsonBean getStayTime()
    {
        return this.stayTimeDimService.getStayTimePV();
    }
    @RequestMapping("/referertop")
    public @ResponseBody JsonBean getRefererTop()
    {
        return this.refererTopDimService.selectTop10();
    }
}
