package com.leyou.user.controller;

import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import org.apache.commons.lang.BooleanUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping
@Validated
public class UserController {

    @Autowired
    private UserService userService;
    /**
    　　* @description: TODO
    　　* @param [data, type]
    　　* @return org.springframework.http.ResponseEntity<java.lang.Boolean>
    　　* @methodName checkUser
    　　* @author Administrator
    　　* @date 2021/2/21 16:27 
    　　*/
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkUser(@PathVariable("data")String data, @PathVariable("type")Integer type){
      Boolean bool=  this.userService.checkUser(data,type);
      if(bool == null){
          return  ResponseEntity.badRequest().build();
      }

      return  ResponseEntity.ok(bool);
    }
    @PostMapping("code")
    public ResponseEntity<Void> sendVerifyCode(@RequestParam("phone")String phone){
        this.userService.sendVerifyCode(phone);
        return  ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, @Length(min=6,max=7,message = "验证码格式错误") @RequestParam("code")String code){
        boolean bool = this.userService.register(user, code);
        HttpHeaders headers= new  HttpHeaders();
        if(BooleanUtils.isNotTrue(bool)){
            headers.set("message", "register failed");
            return  new ResponseEntity<>(null,headers,HttpStatus.BAD_REQUEST);
        }
        else{
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @GetMapping("query")
    public ResponseEntity<User> query(@RequestParam("username")String username, @RequestParam("password")String password){
        User user = this.userService.queryUser(username,password);
        if(user == null){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return  ResponseEntity.ok(user);
    }
    
}
