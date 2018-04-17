package yuchen.core.sys.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Logger logger = LoggerFactory.getLogger(PermManager.class);
    @Autowired
    private PermMapper permMapper;
    @Autowired
    private MemberRoleMapper memberRoleMapper;
    @Autowired
    private RolePermMapper rolePermDao;
    public List<Perm> queryByMemberIdAndParentId(Long memberid, Long parentid){
        List<Perm> list= null;
        try {
            list = new ArrayList<Perm>();
            List<Long> roleids=new ArrayList<Long>();
            String rolestr= memberRoleMapper.queryRolesByMemberId(memberid);
            roleids= StringUtility.StringToListLong(rolestr);
            List<String> permstr= rolePermDao.queryPermIdByRoleIds(roleids);
            List<Long> ids=new ArrayList<Long>();
            for (String perm:permstr) {
                ids.addAll(StringUtility.StringToListLong(perm));
            }
            list=permMapper.queryByIdsAndParentId(ids,parentid);
        } catch (Exception e) {
            logger.error("PermManager.queryByMemberIdAndParentId异常",e);
            throw e;
        }
        return list;
    }

    public List<Perm> queryByRoleIdAndType(Long roleid,Integer type){
        try {
            String perms= rolePermDao.queryPermsByRoleId(roleid);
            List<Long> ids=StringUtility.StringToListLong(perms);
            return permMapper.queryByIdsAndType(ids,type);
        } catch (Exception e) {
            logger.error("PermManager.queryByRoleIdAndType异常",e);
            throw e;
        }
    }

    public List<String> queryUrlByMemberId(Long memberid){
        try {
            List<String> strings=new ArrayList<String>();
            List<Perm> list=new ArrayList<Perm>();
            List<Long> roleids=new ArrayList<Long>();
            String rolestr= memberRoleMapper.queryRolesByMemberId(memberid);
            roleids= StringUtility.StringToListLong(rolestr);
            List<String> permstr= rolePermDao.queryPermIdByRoleIds(roleids);
            List<Long> ids=new ArrayList<Long>();
            for (String perm:permstr) {
                ids.addAll(StringUtility.StringToListLong(perm));
            }
            return permMapper.queryUrlByListId(ids);
        } catch (Exception e) {
            logger.error("PermManager.queryUrlByMemberId异常",e);
            throw e;
        }
    }

    public List<String> queryIds(){
        try {
            List<String> list=new ArrayList<String>();
            list= permMapper.queryIds();
            return list;
        } catch (Exception e) {
            logger.error("PermManager.queryIds异常",e);
            throw e;
        }
    }

    public PageModel<Perm> queryPageList(PermQuery query){
        try {
            List<Perm> list= permMapper.queryPageList(query);
            int count= permMapper.queryCountPage(query);
            PageModel<Perm> permPageModel=new PageModel<Perm>(list,query.getCurrPage(),count,query.getPageSize());
            return permPageModel;
        } catch (Exception e) {
            logger.error("PermManager.queryPageList异常",e);
            throw e;
        }
    }

    public Perm queryById(Long id){
        try {
            return permMapper.queryById(id);
        } catch (Exception e) {
            logger.error("PermManager.queryById异常",e);
            throw e;
        }
    }

    public List<Perm> queryByType(List<Integer> types){
        try {
            List<Perm> list=new ArrayList<Perm>();
            list= permMapper.queryByType(types);
            return list;
        } catch (Exception e) {
            logger.error("PermManager.queryByType异常",e);
            throw e;
        }
    }

    public List<Perm> queryByParentId(Long id){
        try {
            return permMapper.queryByParentId(id);
        } catch (Exception e) {
            logger.error("PermManager.queryByParentId异常",e);
            throw e;
        }
    }

    public int insertPerm(Perm perm){
        try {
            perm.setDelFlag(1);
            return permMapper.insertPerm(perm);
        } catch (Exception e) {
            logger.error("PermManager.insertPerm异常",e);
            throw e;
        }
    }

    public int updateById(Perm perm){
        try {
            return permMapper.updateById(perm);
        } catch (Exception e) {
            logger.error("PermManager.updateById异常",e);
            throw e;
        }
    }

    public int deleteById(Long id){
        try {
            return permMapper.deleteById(id);
        } catch (Exception e) {
            logger.error("PermManager.deleteById异常",e);
            throw e;
        }
    }
}
