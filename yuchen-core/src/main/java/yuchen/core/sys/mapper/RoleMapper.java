package yuchen.core.sys.mapper;

import org.apache.ibatis.annotations.Param;
import yuchen.core.sys.model.sys.Role;
import yuchen.core.sys.model.sys.query.RoleQuery;

import java.util.List;

/**
 * Created by XR on 2016/8/22.
 */
public interface RoleMapper {
    List<Role> queryByIds(List<Long> ids);

    Role queryById(Long id);

    List<Role> queryList();

    List<Role> queryPageList(@Param("query") RoleQuery query);

    int queryCountPage(@Param("query") RoleQuery query);

    int insertRoleAndReturnId(Role role);

    int updateRole(Role role);

    Role queryByName(String name);

    int deleteRole(Long id);

    int deleteBatch(@Param("ids")List ids);
}
