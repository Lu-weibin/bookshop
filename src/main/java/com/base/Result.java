package com.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private boolean flag = true;
    private String message = "success";
    private Object data;

    public Result(Object date) {
        this.data = date;
    }

    public Result(boolean flag, String message) {
        this.flag = flag;
        this.message = message;
    }
}
