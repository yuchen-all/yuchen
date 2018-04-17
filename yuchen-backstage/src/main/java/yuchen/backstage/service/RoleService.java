package yuchen.backstage.service;

import yuchen.core.sys.dto.RolePermDto;
import yuchen.core.sys.model.PageModel;
import yuchen.core.sys.model.sys.Role;
import yuchen.core.sys.model.sys.query.RoleQuery;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by alan.zheng on 2018/4/16.
 */
public interface RoleService {
    List<Role> queryByMemberId(Long memberid);

    List<Role> queryList();

    PageModel<Role> queryPageList(RoleQuery query);

    boolean insertRole(Role role,String ids);

    boolean updateRole(Role role,String ids);

    RolePermDto queryDtoById(Long id) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException;

    int deleteRole(Long id);

    boolean resetadmin();

    boolean deleteBatch(String ids);
}
