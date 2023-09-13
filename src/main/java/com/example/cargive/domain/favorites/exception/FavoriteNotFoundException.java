package com.example.cargive.domain.Favorites.exception;

import com.example.cargive.global.error.ErrorCode;
import com.example.cargive.global.exception.BusinessException;

public class FavoriteNotFoundException extends BusinessException {
    public FavoriteNotFoundException() {
        super(ErrorCode.FAVORITE_NOT_FOUND_ERROR);
    }
}
