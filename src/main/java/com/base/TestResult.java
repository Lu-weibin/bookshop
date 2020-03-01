package com.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author luwb
 * @date 2020-03-01
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TestResult {

    private Integer code = StatusCode.OK;
    private Object data;

    public TestResult(Object date){
        this.data = date;
    }

}
