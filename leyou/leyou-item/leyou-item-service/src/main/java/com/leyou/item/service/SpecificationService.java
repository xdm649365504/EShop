package com.leyou.item.service;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamMapper specParamMapper;


    public List<SpecGroup> querySpecGroupsByCid(Long cid) {
        SpecGroup record = new SpecGroup();
        record.setCid(cid);
        List<SpecGroup> groups = this.specGroupMapper.select(record);
        return groups;
    }

    public List<SpecParam> querySpecParamsByGid(Long gid,Long cid,Boolean generic,Boolean searching) {
        SpecParam record = new SpecParam();
        record.setGroupId(gid);
        record.setCid(cid);
        record.setGeneric(generic);
        record.setSearching(searching);
        return this.specParamMapper.select(record);

    }

    public List<SpecGroup> queryGroupsWithParam(Long cid) {
        List<SpecGroup> groups = this.querySpecGroupsByCid(cid);
         groups.forEach(group->{
             List<SpecParam> params = querySpecParamsByGid(group.getId(), null, null, null);
              group.setParams(params);
         });
         return groups;

    }
}
