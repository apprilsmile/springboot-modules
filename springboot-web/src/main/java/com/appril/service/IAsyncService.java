package com.appril.service;

/**
 * @author zhangyang
 * @date 2020/10/22
 * @description
 **/
public interface IAsyncService {
    public void sendMessage() throws Exception;
    public void sendEmail()throws Exception;
}
