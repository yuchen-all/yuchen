package yuchen.core.sys.manager;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import yuchen.common.utility.StringUtility;
import yuchen.core.sys.mapper.MemberRoleMapper;
import yuchen.core.sys.mapper.PermMapper;
import yuchen.core.sys.mapper.RoleMapper;
import yuchen.core.sys.mapper.RolePermMapper;
import yuchen.core.sys.model.PageModel;
import yuchen.core.sys.model.sys.Role;
import yuchen.core.sys.model.sys.RolePerm;
import yuchen.core.sys.model.sys.query.RoleQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by XR on 2016/8/31.
 */
@Component
public class RoleManager {
    private static Logger logger = LoggerFactory.getLogger(RoleManager.class);
    @Autowired
    private MemberRoleMapper memberRoleMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermMapper permMapper;
    @Autowired
    private RolePermMapper rolePermMapper;

    public List<Role> queryList(){
        try {
            return roleMapper.queryList();
        } catch (Exception e) {
            logger.error("RoleManager.queryList异常",e);
            throw e;
        }
    }

    public PageModel<Role> queryPageList(RoleQuery query){
        try {
            List<Role> list=roleMapper.queryPageList(query);
            int count=roleMapper.queryCountPage(query);
            PageModel<Role> pageModel=new PageModel<Role>(list,query.getCurrPage(),count,query.getPageSize());
            return pageModel;
        } catch (Exception e) {
            logger.error("RoleManager.queryPageList异常",e);
            throw e;
        }
    }

    /**
     * 查询用户的所有角色
     * @param memberid
     * @return
     */
    public List<Role> queryByMemberId(Long memberid){
        try {
            List<Role> list=new ArrayList<Role>();
            List<Long> ids=new ArrayList<Long>();
            String rolestr= memberRoleMapper.queryRolesByMemberId(memberid);
            ids= StringUtility.StringToListLong(rolestr);
            if(ids==null || ids.size()<1){
                return null;
            }
            list=roleMapper.queryByIds(ids);
            return list;
        } catch (Exception e) {
            logger.error("RoleManager.queryByMemberId异常",e);
            throw e;
        }
    }

    public Role queryById(Long id){
        try {
            return roleMapper.queryById(id);
        } catch (Exception e) {
            logger.error("RoleManager.queryById异常",e);
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean insertRole(Role role,String permids){
        try {
            role.setDelFlag(1);
            if (roleMapper.insertRoleAndReturnId(role)>0){
                RolePerm roleperm=new RolePerm();
                roleperm.setPermIds(permids);
                roleperm.setCreateTime(new Date());
                roleperm.setRoleId(role.getId());
                rolePermMapper.insertRolePerm(roleperm);
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("RoleManager.insertRole异常",e);
            throw e;
        }
    }

    public boolean updateRole(Role role,String permids){
        try {
            RolePerm roleperm=new RolePerm();
            roleperm.setPermIds(permids);
            roleperm.setCreateTime(new Date());
            roleperm.setRoleId(role.getId());
            if (rolePermMapper.updateByRoleId(roleperm)>0){
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("RoleManager.updateRole异常",e);
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean resetAdmin(){
        try {
            Role role= roleMapper.queryByName("admin");
            if (role!=null){
                List<String> perms= permMapper.queryIds();
                String permids= StringUtils.join(perms,",");
                RolePerm rolePerm= rolePermMapper.queryByRoleId(role.getId());
                rolePerm.setPermIds(permids);
                rolePerm.setUpdateTime(new Date());
                if (rolePermMapper.updateById(rolePerm)>0){
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            logger.error("RoleManager.resetAdmin异常",e);
            throw e;
        }
    }

    public int deleteRole(Long id){
        try {
            return roleMapper.deleteRole(id);
        } catch (Exception e) {
            logger.error("RoleManager.deleteRole异常",e);
            throw e;
        }
    }

    public Role queryByName(String rolename){
        Role role=new Role();
        return role;
    }

    public boolean deleteBatch(List<Long> ids){
        try {
            if (permMapper.deleteBatch(ids)>0){
                return true;
            }
        } catch (Exception e) {
            logger.error("RoleManager.deleteBatch异常",e);
            throw e;
        }
        return false;
    }
}
