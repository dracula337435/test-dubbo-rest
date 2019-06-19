package org.dracula.test.dubbo.test3;

import java.io.Serializable;

/**
 * @author dk
 */
public class SomeParam implements Serializable {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
