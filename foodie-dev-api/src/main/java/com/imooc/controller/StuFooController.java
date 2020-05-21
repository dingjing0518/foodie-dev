package com.imooc.controller;

import com.imooc.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class StuFooController {

    @Autowired
    private StuService stuService;

    @GetMapping(value = "/getStu")
    public Object getStu(int id) {
        return stuService.getStuInfo(id);
    }

    @PostMapping(value = "/saveStu")
    public Object saveStu() {
        stuService.saveStu();
        return "OK";
    }

    @PostMapping(value = "/updateStu")
    public Object updateStu(int id) {
        stuService.updateStu(id);
        return "OK";
    }

    @PostMapping(value = "/deleteStu")
    public Object deleteStu(int id) {
        stuService.deleteStu(id);
        return "OK";
    }
}
