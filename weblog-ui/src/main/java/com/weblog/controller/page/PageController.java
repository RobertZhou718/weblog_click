package com.weblog.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/page")
public class PageController {
		@RequestMapping("/main")
	  public String getMain()
	  {
	      return "main";
	  }
	
    @RequestMapping("/os")
    public String getOS()
    {
        return "os";
    }

    @RequestMapping("/browser")
    public String getBrowser()
    {
        return "browser";
    }

    @RequestMapping("/province")
    public String getProvince()
    {
        return "province";
    }

    @RequestMapping("/map")
    public String getMap()
    {
        return "map";
    }
    @RequestMapping("/stay")
    public String getStayTime()
    {
        return "stay";
    }
    @RequestMapping("/referer")
    public String getRefererTop()
    {
        return "referertop";
    }
}
