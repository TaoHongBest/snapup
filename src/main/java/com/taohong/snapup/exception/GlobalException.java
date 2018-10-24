package com.taohong.snapup.exception;

import com.taohong.snapup.result.CodeMsg;

/**
 * @author taohong on 22/10/2018
 */
public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private CodeMsg cm;

    public GlobalException(CodeMsg cm) {
        super(cm.toString());
        this.cm = cm;
    }

    public CodeMsg getCm() {
        return cm;
    }

}
