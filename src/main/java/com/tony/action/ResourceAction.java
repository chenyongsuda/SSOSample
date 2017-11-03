package com.tony.action;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by chnho02796 on 2017/11/3.
 */
@RestController
public class ResourceAction {
    @RequestMapping("/product/{id}")
    public String GetProduct(@RequestParam int id){
        return "product id is :" + id;
    }
}
