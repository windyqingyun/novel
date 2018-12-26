package com.jeeplus.modules.bus.entity;

import java.util.Date;

/**
 * 查询书籍记录实体
 */
public class SearchLog {

    private String id;

    private String bookId;

    private Integer isDelete;

    private Date createDate;

    private Integer isExis;//是否存在书，0存在，1不存在

    private String searchCount;//搜索次数

    private String searchName;//搜索书的名字

    private String bookName;//书的原名字

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getIsExis() {
        return isExis;
    }

    public void setIsExis(Integer isExis) {
        this.isExis = isExis;
    }

    public String getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(String searchCount) {
        this.searchCount = searchCount;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
}
