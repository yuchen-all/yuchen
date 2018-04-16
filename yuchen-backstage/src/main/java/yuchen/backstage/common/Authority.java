package yuchen.backstage.common;

/**
 * Created by alan.zheng on 2018/4/16.
 */
public class Authority {
    public boolean isPermission(String perm){
        String strings=PermissionUtility.getPerms();
        String[] perms=strings.split(",");
        boolean isauth=false;
        for (String permstr:perms) {
            if (permstr.equals(perm)){
                isauth=true;
                break;
            }
        }
        return isauth;
    }
}
