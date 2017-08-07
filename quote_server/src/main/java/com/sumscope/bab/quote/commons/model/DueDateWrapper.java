package com.sumscope.bab.quote.commons.model;

import java.util.Date;

/**
 * Created by shaoxu.wang on 2017/1/3.
 */
public class DueDateWrapper {
    private Date dueDateBegin;

    private Date dueDateEnd;

    public Date getDueDateBegin() {
        return dueDateBegin;
    }

    public void setDueDateBegin(Date dueDateBegin) {
        this.dueDateBegin = dueDateBegin;
    }

    public Date getDueDateEnd() {
        return dueDateEnd;
    }

    public void setDueDateEnd(Date dueDateEnd) {
        this.dueDateEnd = dueDateEnd;
    }
}
