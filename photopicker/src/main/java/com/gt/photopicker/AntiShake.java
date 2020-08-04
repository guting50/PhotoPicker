package com.gt.photopicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

/**
 * 防止点击事件重复调用
 */
public class AntiShake {

    private static LimitQueue<OneClick> queue = new LimitQueue<>(20);
    public static int CLICK_DELAY_TIME = 1000;

    public static boolean check(Object o) {
        String flag;
        if (o == null) {
            flag = Thread.currentThread().getStackTrace()[2].getMethodName();
        } else {
            flag = o.toString();
        }
        for (OneClick util : queue.getArrayList()) {
            if (util.getMethodName().equals(flag)) {
                return util.check();
            }
        }
        OneClick clickUtil = new OneClick(flag);
        queue.offer(clickUtil);
        return clickUtil.check();
    }

    public static boolean check(Object o, int duration) {
        CLICK_DELAY_TIME = duration;
        return check(o);
    }

    static class OneClick {
        private String methodName;
        private long lastClickTime = 0;

        public OneClick(String methodName) {
            this.methodName = methodName;
        }

        public String getMethodName() {
            return methodName;
        }

        public boolean check() {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                return false;
            } else {
                return true;
            }
        }
    }

    static class LimitQueue<E> {

        private int limitedSize;

        private LinkedList<E> linkedList = new LinkedList<>();

        public LimitQueue(int size) {
            this.limitedSize = size;
        }

        public void offer(E e) {
            if (linkedList.size() >= limitedSize) {
                linkedList.poll();
            }
            linkedList.offer(e);
        }

        public E get(int position) {
            return linkedList.get(position);
        }

        public E getLast() {
            return linkedList.getLast();
        }

        public E getFirst() {
            return linkedList.getFirst();
        }

        public int getLimit() {
            return limitedSize;
        }

        public void setLimitedSize(int size) {
            this.limitedSize = size;
        }

        public int size() {
            return linkedList.size();
        }

        public ArrayList<E> getArrayList() {
            ArrayList<E> arrayList = new ArrayList<>();
            for (int i = 0; i < linkedList.size(); i++) {
                arrayList.add(linkedList.get(i));
            }
            return arrayList;
        }

        @Override
        public String toString() {
            StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < linkedList.size(); i++) {
                buffer.append(linkedList.get(i));
                buffer.append(" ");
            }
            return buffer.toString();
        }
    }
}
