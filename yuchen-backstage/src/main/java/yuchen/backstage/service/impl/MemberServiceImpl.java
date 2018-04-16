package yuchen.backstage.service.impl;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yuchen.backstage.service.MemberService;
import yuchen.core.sys.dto.AuthPerm;
import yuchen.core.sys.dto.MemberDto;
import yuchen.core.sys.dto.MemberRoleDto;
import yuchen.core.sys.manager.MemberManager;
import yuchen.core.sys.manager.PermManager;
import yuchen.core.sys.manager.RoleManager;
import yuchen.core.sys.model.PageModel;
import yuchen.core.sys.model.sys.Member;
import yuchen.core.sys.model.sys.Perm;
import yuchen.core.sys.model.sys.Role;
import yuchen.core.sys.model.sys.query.MemberQuery;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan.zheng on 2018/4/16.
 */
@Service
public class MemberServiceImpl implements MemberService {
    private static Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private MemberManager memberManager;
    @Autowired
    private PermManager permManager;

    public List<Member> querylist() {
        return memberManager.queryList();
    }

    public PageModel<MemberDto> queryPageList(MemberQuery query) {
        List<MemberDto> listDto=new ArrayList<MemberDto>();
        List<Member> list= memberManager.queryPagelist(query);
        int count=memberManager.queryCountPage(query);
        for (Member member:list) {
            MemberDto memberDto=new  MemberDto();
            try {
                PropertyUtils.copyProperties(memberDto,member);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            List<MemberRoleDto> liststr=new ArrayList<MemberRoleDto>();
            List<Role> rolelist= roleManager.queryByMemberId(member.getId());
            if (rolelist!=null){
                for (Role role:rolelist) {
                    MemberRoleDto memberRoleDto=new MemberRoleDto();
                    memberRoleDto.setId(role.getId());
                    memberRoleDto.setName(role.getDisplayName());
                    liststr.add(memberRoleDto);
                }
            }
            memberDto.setRoles(liststr);
            listDto.add(memberDto);
        }
        PageModel<MemberDto> pageModel=new PageModel<MemberDto>(listDto,query.getCurrPage(),count,query.getPageSize());
        return pageModel;
    }

    public MemberDto queryById(Long id) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        MemberDto memberDto=new MemberDto();
        Member member=memberManager.queryById(id);
        PropertyUtils.copyProperties(memberDto,member);
        //List<Role> rolelist= roleDao.queryByMemberId(id);
        List<Role> rolelist=roleManager.queryByMemberId(id);
        List<MemberRoleDto> memberRoleDtos=new ArrayList<MemberRoleDto>();
        if (rolelist!=null){
            for (Role role:rolelist) {
                MemberRoleDto memberRoleDto=new MemberRoleDto();
                memberRoleDto.setId(role.getId());
                memberRoleDto.setName(role.getDisplayName());
                memberRoleDtos.add(memberRoleDto);
            }
        }
        memberDto.setRoles(memberRoleDtos);
        return memberDto;
    }

    public Member queryByUsername(String username) {
//        AuthMemberDto authMemberDto=new AuthMemberDto();
        //Member member= memberDao.queryByUsername(username);
        Member member=memberManager.queryByUsername(username);
//        if (member!=null){
//            authMemberDto.setId(member.getId());
//            authMemberDto.setPhone(member.getPhone());
//            authMemberDto.setAddress(member.getAddress());
//            authMemberDto.setImgUrl(member.getImgUrl());
//            authMemberDto.setUserName(member.getUserName());
//            authMemberDto.setPassword(member.getPassword());
//            authMemberDto.setStatus(member.getStatus());
//
//            List<Perm> perms= permDao.queryByMemberIdAndParentId(member.getId(),(long)0);
//            List<AuthPerm> authpermlist=new ArrayList<AuthPerm>();
//            for (Perm perm:perms) {
//                AuthPerm authperm=new AuthPerm();
//                authperm.setName(perm.getDisplayName());
//                authperm.setUrl(perm.getUrl());
//                List<Perm> perms2= permDao.queryByMemberIdAndParentId(member.getId(),perm.getId());
//                List<AuthPerm> authpermlist2=new ArrayList<AuthPerm>();
//                for (Perm perm2:perms2) {
//                    AuthPerm authperm2=new AuthPerm();
//                    authperm2.setName(perm2.getDisplayName());
//                    authperm2.setUrl(perm2.getUrl());
//                    authpermlist2.add(authperm2);
//                }
//                authpermlist.add(authperm);
//            }
//            authMemberDto.setRoles(authpermlist);
//        }
        return member;
    }

    public List<AuthPerm> queryAuthPerm(Long memberid) {
        List<AuthPerm> authpermlist=new ArrayList<AuthPerm>();
        List<Perm> perms= permManager.queryByMemberIdAndParentId(memberid,(long)0);
        for (Perm perm:perms) {
            AuthPerm authperm=new AuthPerm();
            authperm.setName(perm.getDisplayName());
            authperm.setUrl(perm.getUrl());
            List<Perm> perms2= permManager.queryByMemberIdAndParentId(memberid,perm.getId());
            List<AuthPerm> authpermlist2=new ArrayList<AuthPerm>();
            for (Perm perm2:perms2) {
                AuthPerm authperm2=new AuthPerm();
                authperm2.setName(perm2.getDisplayName());
                authperm2.setUrl(perm2.getUrl());
                authpermlist2.add(authperm2);
            }
            authperm.setRoles(authpermlist2);
            authpermlist.add(authperm);
        }
        return authpermlist;
    }

    public Member insertMember(Member member, String roleids) {
        memberManager.insertMember(member,roleids);
        return member;
    }

    public int deleteMember(Long id) {
        return memberManager.deleteMember(id);
    }

    public int updateStatus(Long memberid, Short status) {
        return memberManager.updateStatus(memberid,status);
    }
}
