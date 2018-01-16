package com.yiyi.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sdyang
 * @create 2018-01-16 10:51
 **/
public class MyException extends RuntimeException{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public MyException(){
        super();
    }

    public MyException(String str){
        super(str);
        logger.error(str);
    }
}
