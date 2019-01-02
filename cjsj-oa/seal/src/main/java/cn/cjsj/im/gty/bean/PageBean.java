package cn.cjsj.im.gty.bean;

/**
 * Created by LuoYang on 2018/1/3.
 * 分页管理model
 */

public class PageBean {
    private int pageSize;
    private int currentPage;
    private int totalCount;
    private String pageName;
    private String pageSizeName;
    private boolean showTotal;
    private int first;
    private int last;
    private int totalPage;
    private int [] pageArr;
    private boolean hasNextPage;
    private int nextPage;
    private int lastPage;
    private boolean firstPage;
    private int previousPage;
    private boolean hasPreviousPage;
    private int thisPageFirstElementNumber;
    private int thisPageLastElementNumber;
    private int [] linkPageNumbers;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPageSizeName() {
        return pageSizeName;
    }

    public void setPageSizeName(String pageSizeName) {
        this.pageSizeName = pageSizeName;
    }

    public boolean isShowTotal() {
        return showTotal;
    }

    public void setShowTotal(boolean showTotal) {
        this.showTotal = showTotal;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int[] getPageArr() {
        return pageArr;
    }

    public void setPageArr(int[] pageArr) {
        this.pageArr = pageArr;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    public int getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(int previousPage) {
        this.previousPage = previousPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public int getThisPageFirstElementNumber() {
        return thisPageFirstElementNumber;
    }

    public void setThisPageFirstElementNumber(int thisPageFirstElementNumber) {
        this.thisPageFirstElementNumber = thisPageFirstElementNumber;
    }

    public int getThisPageLastElementNumber() {
        return thisPageLastElementNumber;
    }

    public void setThisPageLastElementNumber(int thisPageLastElementNumber) {
        this.thisPageLastElementNumber = thisPageLastElementNumber;
    }

    public int[] getLinkPageNumbers() {
        return linkPageNumbers;
    }

    public void setLinkPageNumbers(int[] linkPageNumbers) {
        this.linkPageNumbers = linkPageNumbers;
    }
}
