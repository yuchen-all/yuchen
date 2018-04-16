package yuchen.core.sys.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import yuchen.core.sys.mapper.MemberMapper;
import yuchen.core.sys.mapper.MemberRoleMapper;
import yuchen.core.sys.model.sys.Member;
import yuchen.core.sys.model.sys.MemberRole;
import yuchen.core.sys.model.sys.query.MemberQuery;

import java.util.Date;
import java.util.List;

/**
 * Created by XR on 2016/8/31.
 */
@Component
public class MemberManager {
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private MemberRoleMapper memberRoleMapper;

    public List<Member> querylist(){
        return memberMapper.querylist();
    }

    public List<Member> queryPagelist(MemberQuery query){
        return memberMapper.queryPageList(query);
    }

    public int queryCountPage(MemberQuery query){
        return memberMapper.queryCountPage(query);
    }

    public Member queryByUsername(String username){
        return memberMapper.queryByUsername(username);
    }

    public Member queryById(Long id){
        return memberMapper.queryById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean insertmember(Member member,String ids){
        if (memberMapper.insertmember(member)>0){
            MemberRole memberRole=new MemberRole();
            memberRole.setRoleIds(ids);
            memberRole.setMemberId(member.getId());
            memberRole.setCreateTime(new Date());
            memberRoleMapper.insertMemberRole(memberRole);
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteMember(Long id){
        return memberMapper.deleteMember(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateStatus(Long memberid,Short status){
        return memberMapper.updateStatus(memberid,status);
    }
}
