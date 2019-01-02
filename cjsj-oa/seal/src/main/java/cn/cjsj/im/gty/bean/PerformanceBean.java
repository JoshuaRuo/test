package cn.cjsj.im.gty.bean;

import java.util.List;

/**
 * Created by LuoYang on 2018/3/29.
 */

public class PerformanceBean {

    private Count count;
    private List<Object> cjsjBorrowList;

    public Count getCount() {
        return count;
    }

    public void setCount(Count count) {
        this.count = count;
    }

    public List<Object> getCjsjBorrowList() {
        return cjsjBorrowList;
    }

    public void setCjsjBorrowList(List<Object> cjsjBorrowList) {
        this.cjsjBorrowList = cjsjBorrowList;
    }

    public class Count {
        private double total;
        private double money;

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }
    }
}
