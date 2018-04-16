package yuchen.core.sys.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import yuchen.common.utility.StringUtility;
import yuchen.core.sys.mapper.MemberRoleMapper;
import yuchen.core.sys.mapper.PermMapper;
import yuchen.core.sys.mapper.RolePermMapper;
import yuchen.core.sys.model.PageModel;
import yuchen.core.sys.model.sys.Perm;
import yuchen.core.sys.model.sys.query.PermQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XR on 2016/8/31.
 */
@Component
public class PermManager {
    @Autowired
    private PermMapper permMapper;
    @Autowired
    private MemberRoleMapper memberRoleDao;
    @Autowired
    private RolePermMapper rolePermDao;
    public List<Perm> queryByMemberIdAndParentId(Long memberid, Long parentid){
        List<Perm> list=new ArrayList<Perm>();
        List<Long> roleids=new ArrayList<Long>();
        String rolestr= memberRoleDao.queryRolesByMemberId(memberid);
        roleids= StringUtility.StringToListLong(rolestr);
        List<String> permstr= rolePermDao.queryPermIdByRoleIds(roleids);
        List<Long> ids=new ArrayList<Long>();
        for (String perm:permstr) {
            ids.addAll(StringUtility.StringToListLong(perm));
        }
        list=permMapper.queryByIdsAndParentId(ids,parentid);
        return list;
    }

    public List<Perm> queryByRoleIdAndType(Long roleid,Integer type){
        String perms= rolePermDao.queryPermsByRoleId(roleid);
        List<Long> ids=StringUtility.StringToListLong(perms);
        return permMapper.queryByIdsAndType(ids,type);
    }

    public List<String> queryUrlByMemberId(Long memberid){
        List<String> strings=new ArrayList<String>();
        List<Perm> list=new ArrayList<Perm>();
        List<Long> roleids=new ArrayList<Long>();
        String rolestr= memberRoleDao.queryRolesByMemberId(memberid);
        roleids= StringUtility.StringToListLong(rolestr);
        List<String> permstr= rolePermDao.queryPermIdByRoleIds(roleids);
        List<Long> ids=new ArrayList<Long>();
        for (String perm:permstr) {
            ids.addAll(StringUtility.StringToListLong(perm));
        }
        return permMapper.queryUrlByListId(ids);
    }

    public List<String> queryIds(){
        List<String> list=new ArrayList<String>();
        list= permMapper.queryIds();
        return list;
    }

    public PageModel<Perm> queryPageList(PermQuery query){
        List<Perm> list= permMapper.queryPageList(query);
        int count= permMapper.queryCountPage(query);
        PageModel<Perm> permPageModel=new PageModel<Perm>(list,query.getCurrPage(),count,query.getPageSize());
        return permPageModel;
    }

    public Perm queryById(Long id){
        return permMapper.queryById(id);
    }

    public List<Perm> queryByType(List<Integer> types){
        List<Perm> list=new ArrayList<Perm>();
        list= permMapper.queryByType(types);
        return list;
    }

    public List<Perm> queryByParentId(Long id){
        return permMapper.queryByParentId(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public int insertPerm(Perm perm){
        return permMapper.insertPerm(perm);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateById(Perm perm){
        return permMapper.updateById(perm);
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id){
        return permMapper.deleteById(id);
    }
}
