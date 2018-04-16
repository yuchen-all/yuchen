package yuchen.core.sys.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Logger logger = LoggerFactory.getLogger(MemberManager.class);
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private MemberRoleMapper memberRoleMapper;

    public List<Member> queryList(){
        try {
            return memberMapper.querylist();
        } catch (Exception e) {
            logger.error("MemberManager.queryList异常",e);
            throw e;
        }
    }

    public List<Member> queryPagelist(MemberQuery query){
        try {
            return memberMapper.queryPageList(query);
        } catch (Exception e) {
            logger.error("MemberManager.queryPagelist异常",e);
            throw e;
        }
    }

    public int queryCountPage(MemberQuery query){
        try {
            return memberMapper.queryCountPage(query);
        } catch (Exception e) {
            logger.error("MemberManager.queryCountPage异常",e);
            throw e;
        }
    }

    public Member queryByUsername(String username){
        try {
            return memberMapper.queryByUsername(username);
        } catch (Exception e) {
            logger.error("MemberManager.queryByUsername异常",e);
            throw e;
        }
    }

    public Member queryById(Long id){
        try {
            return memberMapper.queryById(id);
        } catch (Exception e) {
            logger.error("MemberManager.queryById异常",e);
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean insertMember(Member member,String ids){
        try {
            if (memberMapper.insertmember(member)>0){
                MemberRole memberRole=new MemberRole();
                memberRole.setRoleIds(ids);
                memberRole.setMemberId(member.getId());
                memberRole.setCreateTime(new Date());
                memberRoleMapper.insertMemberRole(memberRole);
                return true;
            }
        } catch (Exception e) {
            logger.error("MemberManager.insertMember异常",e);
            throw e;
        }
        return false;
    }

    public int deleteMember(Long id){
        try {
            return memberMapper.deleteMember(id);
        } catch (Exception e) {
            logger.error("MemberManager.deleteMember异常",e);
            throw e;
        }
    }

    public int updateStatus(Long memberid,Short status){
        try {
            return memberMapper.updateStatus(memberid,status);
        } catch (Exception e) {
            logger.error("MemberManager.updateStatus异常",e);
            throw e;
        }
    }
}
