package com.tasfe.sis.auth.service;

import com.tasfe.sis.auth.model.AuthInfos;

/**
 * Created by Lait on 2017/7/27.
 */
public interface AuthService {


    boolean authentication(AuthInfos authInfos);


}
