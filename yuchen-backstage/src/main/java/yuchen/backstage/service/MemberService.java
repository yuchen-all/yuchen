package yuchen.backstage.service;

import org.springframework.stereotype.Service;
import yuchen.core.sys.dto.AuthPerm;
import yuchen.core.sys.dto.MemberDto;
import yuchen.core.sys.model.PageModel;
import yuchen.core.sys.model.sys.Member;
import yuchen.core.sys.model.sys.query.MemberQuery;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by alan.zheng on 2018/4/16.
 */
public interface MemberService {
    List<Member> querylist();

    PageModel<MemberDto> queryPageList(MemberQuery query);

    MemberDto queryById(Long id) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException;

    Member queryByUsername(String username);

    List<AuthPerm> queryAuthPerm(Long memberid);

    Member insertMember(Member member,String roleids);

    int deleteMember(Long id);

    int updateStatus(Long memberid,Short status);
}
