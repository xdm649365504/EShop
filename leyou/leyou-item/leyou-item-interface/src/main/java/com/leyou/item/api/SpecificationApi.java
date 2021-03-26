package com.leyou.item.api;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("spec")
public interface SpecificationApi {

    @RequestMapping("params")
    public List<SpecParam> queryParamsByCid
            (@RequestParam(value="gid",required = false)Long gid,
             @RequestParam(value="cid",required = false)Long cid,
             @RequestParam(value="generic",required = false)Boolean generic,
             @RequestParam(value="searching",required = false)Boolean searching
            );

    @GetMapping("groups/param/{cid}")
    public List<SpecGroup> queryGroupsWithParam(@PathVariable("cid")Long cid);
}
