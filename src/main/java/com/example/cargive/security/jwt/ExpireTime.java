package com.example.cargive.security.jwt;

public enum ExpireTime{

    //  엑세스 토큰 유효 기간
    //ACCESS_TOKEN_EXPIRE_TIME(1000 * 60 * 60 * 24); // 24시간
    //ACCESS_TOKEN_EXPIRE_TIME(1000 * 60 * 60 * 168); // 1주
    ACCESS_TOKEN_EXPIRE_TIME(1000 * 60 * 60 * 336); // 2주



    private final long time;

    ExpireTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}
