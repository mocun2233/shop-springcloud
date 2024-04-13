package cn.wolfcode.redis;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

/**
 * Created by wolfcode-lanxw
 */
@Getter
public enum UaaRedisKey {
    USERLOGIN_STRING("userLogin:",TimeUnit.DAYS,7),
    USERINFO_STRING("userInfo:",TimeUnit.DAYS,7);
    UaaRedisKey(String prefix){
        this .prefix = prefix;
    }
    UaaRedisKey(String prefix, TimeUnit unit, int expireTime){
        this.prefix = prefix;
        this.unit = unit;
        this.expireTime = expireTime;
    }
    public String getRealKey(String key){
        return this.prefix+key;
    }
    private String prefix;
    private TimeUnit unit;
    private int expireTime;
}
