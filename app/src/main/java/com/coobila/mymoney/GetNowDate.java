package com.coobila.mymoney;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/* loaded from: classes.dex */
public class GetNowDate {
    public int GetDateYear() {
        Calendar c = Calendar.getInstance();
        return c.get(1);
    }

    public int GetDateMonth() {
        Calendar c = Calendar.getInstance();
        return c.get(2);
    }

    public int GetDateDay() {
        Calendar c = Calendar.getInstance();
        return c.get(5);
    }

    public String GetDate() {
        Calendar c = Calendar.getInstance();
        String y = String.valueOf(c.get(1));
        String m = String.valueOf(c.get(2) + 1);
        String d = String.valueOf(c.get(5));
        if (y.length() == 1) {
            y = "0" + y;
        }
        if (m.length() == 1) {
            m = "0" + m;
        }
        if (d.length() == 1) {
            d = "0" + d;
        }
        String NowDate = String.valueOf(y) + "/" + m + "/" + d;
        return NowDate;
    }

    public String GetYYYYMMDD() {
        Calendar c = Calendar.getInstance();
        String y = String.valueOf(c.get(1));
        String m = String.valueOf(c.get(2) + 1);
        String d = String.valueOf(c.get(5));
        if (y.length() == 1) {
            y = "0" + y;
        }
        if (m.length() == 1) {
            m = "0" + m;
        }
        if (d.length() == 1) {
            d = "0" + d;
        }
        String NowDate = String.valueOf(y) + m + d;
        return NowDate;
    }

    public String GetCDate() {
        Calendar c = Calendar.getInstance();
        String y = String.valueOf(c.get(1));
        String m = String.valueOf(c.get(2) + 1);
        String d = String.valueOf(c.get(5));
        if (y.length() == 1) {
            y = "0" + y;
        }
        if (m.length() == 1) {
            m = "0" + m;
        }
        if (d.length() == 1) {
            d = "0" + d;
        }
        String NowDate = String.valueOf(y) + " 年 " + m + " 月 " + d + " 日";
        return NowDate;
    }

    public String GetCDateStart() {
        Calendar c = Calendar.getInstance();
        String y = String.valueOf(c.get(1));
        String m = String.valueOf(c.get(2) + 1);
        String d = String.valueOf(c.get(5));
        if (y.length() == 1) {
            y = "0" + y;
        }
        if (m.length() == 1) {
            m = "0" + m;
        }
        if (d.length() == 1) {
            String str = "0" + d;
        }
        String NowDate = String.valueOf(y) + " 年 " + m + " 月 01 日";
        return NowDate;
    }

    public String CDateToEDate(String InputDate) {
        String NowDate = InputDate.replace(" 年 ", "/");
        return NowDate.replace(" 月 ", "/").replace(" 日", "");
    }

    public String EDateToCDate(String InputDate) {
        String NowDate = InputDate.replaceFirst("/", " 年 ");
        return String.valueOf(NowDate.replaceFirst("/", " 月 ")) + " 日";
    }

    public Double GetCDateYear(String InputDate) {
        String NowDate = InputDate.replace(" 年 ", "/");
        return Double.valueOf(NowDate.replace(" 月 ", "/").replace(" 日", "").substring(0, 4));
    }

    public Double GetCDateMonth(String InputDate) {
        String NowDate = InputDate.replace(" 年 ", "/");
        return Double.valueOf(NowDate.replace(" 月 ", "/").replace(" 日", "").substring(5, 7));
    }

    public Double GetCDateDay(String InputDate) {
        String NowDate = InputDate.replace(" 年 ", "/");
        return Double.valueOf(NowDate.replace(" 月 ", "/").replace(" 日", "").substring(8, 10));
    }

    public Double GetMonthDay() {
        Calendar cal = Calendar.getInstance();
        return Double.valueOf(cal.getActualMaximum(5));
    }

    public int GetSetMonthDay(int Year, int Month) {
        Calendar c = Calendar.getInstance();
        c.set(1, Year);
        c.set(2, Month - 1);
        return c.getActualMaximum(5);
    }

    public String GetTime() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String strDate = sdFormat.format(date);
        return strDate;
    }

    public String GetDataKey() throws InterruptedException {
        Calendar Cld = Calendar.getInstance();
        try {
            Thread.sleep(80L);
        } catch (Exception e) {
        }
        String YY = String.valueOf(Cld.get(1));
        String MM = String.valueOf(Cld.get(2) + 1);
        String DD = String.valueOf(Cld.get(5));
        String HH = String.valueOf(Cld.get(11));
        String mm = String.valueOf(Cld.get(12));
        String SS = String.valueOf(Cld.get(13));
        String MI = String.valueOf(Cld.get(14));
        if (MM.length() == 1) {
            MM = "0" + MM;
        }
        if (DD.length() == 1) {
            DD = "0" + DD;
        }
        if (HH.length() == 1) {
            HH = "0" + HH;
        }
        if (mm.length() == 1) {
            mm = "0" + mm;
        }
        if (SS.length() == 1) {
            SS = "0" + SS;
        }
        if (MI.length() == 1) {
            MI = "00" + MI;
        }
        if (MI.length() == 2) {
            MI = "0" + MI;
        }
        String curTime = String.valueOf(YY) + MM + DD + HH + mm + SS + MI;
        return curTime;
    }
}