package yuchen.core.sys.mapper;

import org.apache.ibatis.annotations.Param;
import yuchen.core.sys.model.sys.Member;
import yuchen.core.sys.model.sys.query.MemberQuery;

import java.util.List;

/**
 * Created by XR on 2016/8/22.
 */
public interface MemberMapper {
    List<Member> queryList();

    List<Member> queryPageList(@Param("query") MemberQuery query);

    int queryCountPage(@Param("query") MemberQuery query);

    Member queryByUsername(String username);

    Member queryById(Long id);

    int insertMember(Member member);

    int deleteMember(Long id);

    int updateStatus(@Param("memberid") Long memberid, @Param("status") Short status);

    int updateMember(@Param("member")Member member);

    int deleteBatch(@Param("ids")List ids);
}
