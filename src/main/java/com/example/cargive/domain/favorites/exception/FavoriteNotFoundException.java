package com.example.cargive.domain.favorites.exception;

import com.example.cargive.global.base.BaseResponseStatus;
import com.example.cargive.global.exception.BusinessException;

public class FavoriteNotFoundException extends BusinessException {
    public FavoriteNotFoundException() {
        super(BaseResponseStatus.FAVORITE_NOT_FOUND_ERROR);
    }
}
