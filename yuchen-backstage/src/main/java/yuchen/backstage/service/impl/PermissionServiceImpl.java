package yuchen.backstage.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yuchen.backstage.service.PermissionService;
import yuchen.core.sys.dto.PermLevelDto;
import yuchen.core.sys.manager.PermManager;
import yuchen.core.sys.model.PageModel;
import yuchen.core.sys.model.sys.Perm;
import yuchen.core.sys.model.sys.query.PermQuery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by alan.zheng on 2018/4/16.
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    private static Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);
    @Autowired
    private PermManager permManager;
    public PageModel<Perm> queryPageList(PermQuery query) {
        return permManager.queryPageList(query);
    }

    public int insertPerm(Perm perm) {
        return permManager.insertPerm(perm);
    }

    public int updatePerm(Perm perm) {
        return permManager.updateById(perm);
    }

    public int deletePerm(Long id) {
        return permManager.deleteById(id);
    }

    public Perm queryById(Long id) {
        return permManager.queryById(id);
    }

    public List<PermLevelDto> queryPermByLevel() {
        List<PermLevelDto> list=new ArrayList<PermLevelDto>();
        List<Perm> permList1= permManager.queryByParentId((long)0);
        for (Perm perm1:permList1) {
            PermLevelDto dto1=new PermLevelDto();
            dto1.setId(perm1.getId());
            dto1.setDisplayName(perm1.getDisplayName());
            List<PermLevelDto> list2=new ArrayList<PermLevelDto>();
            List<Perm> permList2= permManager.queryByParentId(perm1.getId());
            for (Perm perm2:permList2) {
                PermLevelDto dto2=new PermLevelDto();
                dto2.setId(perm2.getId());
                dto2.setDisplayName(perm2.getDisplayName());
                List<PermLevelDto> list3=new ArrayList<PermLevelDto>();
                List<Perm> permList3=permManager.queryByParentId(perm2.getId());
                for (Perm perm3:permList3) {
                    PermLevelDto dto3=new PermLevelDto();
                    dto3.setId(perm3.getId());
                    dto3.setDisplayName(perm3.getDisplayName());
                    list3.add(dto3);
                }
                dto2.setPermLevelDtos(list3);
                list2.add(dto2);
            }
            dto1.setPermLevelDtos(list2);
            list.add(dto1);
        }
        return list;
    }

    public List<Perm> queryByType(List<Integer> types) {
        return permManager.queryByType(types);
    }

    public Set<String> queryUrlsByMemberId(Long id) {
        Set<String> stringSet =new HashSet<String>();
        List<String> urls=permManager.queryUrlByMemberId(id);
        for (String url:urls) {
            stringSet.add(url);
        }
        return stringSet;
    }

    @Override
    public boolean deleteBatch(String ids) {
        try {
            if (StringUtils.isEmpty(ids)){
                return false;
            }
            List<Long> idList = new ArrayList<>();
            String[] idArray = ids.split(",");
            if (idArray!=null && idArray.length>0){
                for (String idstr:idArray) {
                    idList.add(Long.parseLong(idstr));
                }
            }
            return permManager.deleteBatch(idList);
        } catch (Exception e) {
            logger.error("PermServiceImpl.deleteBatch异常",e);
        }
        return false;
    }
}
