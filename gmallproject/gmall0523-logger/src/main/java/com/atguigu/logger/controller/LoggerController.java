package com.atguigu.logger.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * Author: Felix
 * Desc: 该 Controller 用于接收模拟生成的日志
 */

//@RestController = @Controller+@ResponseBody方法返回的Object会默认转换为json格式的字符串进行响应。所以一般返回json的都是用这个
//@Controller //对象创建交给Spring容器 方法返回的String会默认当作页面跳转。
@RestController



@Slf4j  //lomobook的快捷方法，创建一个log属性。

public class LoggerController {

    //spring自带对kafka的支持。
    @Autowired      //注入。把KafkaTemplate属性注入到LoggerController类，让它使用。下面就可以直接使用了。
    KafkaTemplate kafkaTemplate;   //创建对象之后要加@Autowired依赖注入，注入到当前类，知道是本类用。
    //配置kafka地址就取配置文件配。


    //private  final Logger log = LoggerFactory.getLogger(LoggerController.class);  //这一行可以用@Slf4j代替。

    //通过 requestMapping 匹配请求并交给方法处理 //@RequestMapping 这个注解可以注明是用来处理模拟器生成的数据。
    @RequestMapping("/applog") //表示把applog请求交给方法处理。设置方法的请求。

    //在模拟数据生成的代码中，我们将数据封装为 json，通过post传递给该Controller处理 所以我们通过@RequestBody 接收.
    //@ResponseBody  //@Controller加了这个注解，也代表是return的string不进行页面跳转，直接响应数据josn格式字符串返回结果直接写入HTTP response body中。
    public String applog(@RequestBody String jsonLog){  //作用：从请求体里获取数据。相当于request.getxxx

        System.out.println(jsonLog);
        //可以使用io流落盘，或者loggerback

////版本4-分支之前。
        //checkout
        
        //============================log落盘
        log.info(jsonLog);

        //=====================传递给kafka，之前注入了kafka。
        //字符串log转json对象
        JSONObject jsonObject = JSON.parseObject(jsonLog);

        JSONObject startJSON = jsonObject.getJSONObject("start");

        if(startJSON!=null){
            kafkaTemplate.send("gmall_start_0523",jsonLog); //给kafka发送到这个主题。
        }else {
            kafkaTemplate.send("gmall_event_0523",jsonLog);
        }

        return "success"; //@Controller默认情况下，返回string是页面跳转。加@ResponseBody就不跳。

    }
}