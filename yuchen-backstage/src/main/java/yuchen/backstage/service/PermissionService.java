package yuchen.backstage.service;

import yuchen.core.sys.dto.PermLevelDto;
import yuchen.core.sys.model.PageModel;
import yuchen.core.sys.model.sys.Perm;
import yuchen.core.sys.model.sys.query.PermQuery;

import java.util.List;
import java.util.Set;

/**
 * Created by alan.zheng on 2018/4/16.
 */
public interface PermissionService {
    PageModel<Perm> queryPageList(PermQuery query);

    Perm queryById(Long id);

    List<PermLevelDto> queryPermByLevel();

    List<Perm> queryByType(List<Integer> types);

    Set<String> queryUrlsByMemberId(Long id);

    int insertPerm(Perm perm);

    int updatePerm(Perm perm);

    int deletePerm(Long id);

    boolean deleteBatch(String ids);
}
