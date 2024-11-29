package org.xi.maple.common.annotation;

public interface Jsr303ValidGroup {

    /**
     * 对象新增
     */
    interface Post {
    }

    /**
     * 修改对象所有字段
     */
    interface Put {
    }

    /**
     * 修改对象要修改的字段
     */
    interface Patch {
    }
}
