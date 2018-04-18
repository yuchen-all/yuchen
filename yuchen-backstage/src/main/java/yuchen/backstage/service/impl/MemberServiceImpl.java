package yuchen.backstage.service.impl;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
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
        PageModel<MemberDto> pageModel= null;
        try {
            List<MemberDto> listDto=new ArrayList<MemberDto>();
            List<Member> list= memberManager.queryPagelist(query);
            int count=memberManager.queryCountPage(query);
            for (Member member:list) {
                MemberDto memberDto=new  MemberDto();
                PropertyUtils.copyProperties(memberDto,member);
                List<MemberRoleDto> liststr=new ArrayList<MemberRoleDto>();
                List<Role> rolelist= roleManager.queryByMemberId(member.getId());
                MemberRoleDto memberRoleDto = null;
                if (rolelist!=null){
                    for (Role role:rolelist) {
                        memberRoleDto =new MemberRoleDto();
                        memberRoleDto.setId(role.getId());
                        memberRoleDto.setName(role.getDisplayName());
                        liststr.add(memberRoleDto);
                    }
                }
                memberDto.setRoles(liststr);
                listDto.add(memberDto);
            }
            pageModel = new PageModel<MemberDto>(listDto,query.getCurrPage(),count,query.getPageSize());
        } catch (Exception e) {
            logger.error("MemberServiceImpl.queryPageList异常",e);
        }
        return pageModel;
    }

    public MemberDto queryDtoById(Long id){
        MemberDto memberDto=new MemberDto();
        Member member=memberManager.queryById(id);
        try {
            PropertyUtils.copyProperties(memberDto,member);
        } catch (Exception e) {
            logger.error("属性转换异常",e);
            return null;
        }
        //List<Role> rolelist= roleDao.queryByMemberId(id);
        List<Role> rolelist=roleManager.queryByMemberId(id);
        List<MemberRoleDto> memberRoleDtos=new ArrayList<MemberRoleDto>();
        MemberRoleDto memberRoleDto = null;
        if (rolelist!=null){
            for (Role role:rolelist) {
                memberRoleDto =new MemberRoleDto();
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
        AuthPerm authperm = null;
        AuthPerm authperm2 = null;
        for (Perm perm:perms) {
            authperm =new AuthPerm();
            authperm.setName(perm.getDisplayName());
            authperm.setUrl(perm.getUrl());
            List<Perm> perms2= permManager.queryByMemberIdAndParentId(memberid,perm.getId());
            List<AuthPerm> authpermlist2=new ArrayList<AuthPerm>();
            for (Perm perm2:perms2) {
                authperm2 =new AuthPerm();
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
        try {
            memberManager.insertMember(member,roleids);
            return member;
        } catch (Exception e) {
            logger.error("MemberServiceImpl.insertMember异常",e);
            throw e;
        }
    }

    public int deleteMember(Long id) {
        return memberManager.deleteMember(id);
    }

    public int updateStatus(Long memberid, Short status) {
        return memberManager.updateStatus(memberid,status);
    }

    @Override
    public Member queryById(Long id) {
        return memberManager.queryById(id);
    }

    @Override
    public boolean updateMember(Member member, String roleids) {
        return memberManager.updateMember(member,roleids);
    }

    @Override
    public boolean deleteBatch(String ids) {
        try {
            if (StringUtils.isEmpty(ids)){
                return false;
            }
            List<Long> idList = new ArrayList<>();
            String[] idArray = ids.split(",");
            if (idArray!=null && idArray.length>0){
                for (String idstr:idArray) {
                    idList.add(Long.parseLong(idstr));
                }
            }
            return memberManager.deleteBatch(idList);
        } catch (Exception e) {
            logger.error("MemberServiceImpl.deleteBatch异常",e);
        }
        return false;
    }
}
