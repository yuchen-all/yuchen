package yuchen.core.sys.mapper;


import yuchen.core.sys.model.sys.MemberRole;

/**
 * Created by XR on 2016/8/23.
 */
public interface MemberRoleMapper {

    String queryRolesByMemberId(Long memberid);

    int insertMemberRole(MemberRole memberRoles);

    int deleteByMemberId(long memberid);

    MemberRole queryByMemberId(Long memberId);

    int updateMemberRole(MemberRole memberRole);
}
