package org.mujeeb.mosquemanager.util;

import android.app.Activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

	public static final Date addDaysToDate(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}
	
	public static final Date addHoursToDate(Date date, int hours) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, hours);
		return cal.getTime();
	}
	
	public static final Date addMinutesToDate(Date date, int minutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minutes);
		return cal.getTime();
	}
	
	public static final Date parseDate(String date) throws ParseException { // yyyy-MM-dd

		return new SimpleDateFormat("yyyy-MM-dd").parse(date);
	}
	
	public static final Date parseDateTime(String date) { // yyyy-MM-dd
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
			
		}catch(Exception ex) {
			return null;
		}
	}

	public static final String formatSystemDate(Date date){ // yyyy-MM-dd

		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}
	
	public static final Date getDateFromTime(int hour, int minute) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static final Date getDateFromTime(Date date, int hour, int minute) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static final Date applyTimeToDate(Date date, int hour, int minute) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static final int compareOnlyTime(Date date1, Date date2) {
		Calendar cal1 = new GregorianCalendar();
		cal1.setTime(date1);
		Calendar cal2 = new GregorianCalendar();
		cal2.setTime(date2);
		
		cal1.set(Calendar.DATE, 1);
		cal1.set(Calendar.MONTH, 1);
		cal1.set(Calendar.YEAR, 2017);
		cal2.set(Calendar.DATE, 1);
		cal2.set(Calendar.MONTH, 1);
		cal2.set(Calendar.YEAR, 2017);
		
		return cal1.compareTo(cal2);
	}
	
	public static final int compareOnlyDate(Date date1, Date date2) {
		Calendar cal1 = new GregorianCalendar();
		cal1.setTime(date1);
		Calendar cal2 = new GregorianCalendar();
		cal2.setTime(date2);
		
		cal1.set(Calendar.HOUR_OF_DAY, 0);
		cal1.set(Calendar.MINUTE, 0);
		cal1.set(Calendar.SECOND, 0);
		cal1.set(Calendar.MILLISECOND, 0);
		cal2.set(Calendar.HOUR_OF_DAY, 0);
		cal2.set(Calendar.MINUTE, 0);
		cal2.set(Calendar.SECOND, 0);
		cal2.set(Calendar.MILLISECOND, 0);
		
		return cal1.compareTo(cal2);
	}
	
	public static final String getDateDescription(Activity activity, Date date) {
		Calendar cal1 = new GregorianCalendar();
		cal1.setTime(new Date());
		Calendar cal2 = new GregorianCalendar();
		cal2.setTime(date);
		
		cal1.set(Calendar.HOUR_OF_DAY, 0);
		cal1.set(Calendar.MINUTE, 0);
		cal1.set(Calendar.SECOND, 0);
		cal1.set(Calendar.MILLISECOND, 0);
		cal2.set(Calendar.HOUR_OF_DAY, 0);
		cal2.set(Calendar.MINUTE, 0);
		cal2.set(Calendar.SECOND, 0);
		cal2.set(Calendar.MILLISECOND, 0);

		cal1.add(Calendar.DATE, -1);
		if(cal1.compareTo(cal2) == 0) {
			return "Yesterday";
		}

		cal1.add(Calendar.DATE, 1);
		if(cal1.compareTo(cal2) == 0) {
			
			return "Today";
			
		}
		
		cal1.add(Calendar.DATE, 1);
		if(cal1.compareTo(cal2) == 0) {
			return "Tomorrow";
		}
		
		cal1.add(Calendar.DATE, 1);
		if(cal1.compareTo(cal2) == 0) {
			return "Day after Tomorrow";
		}
		
		return DateUtil.formatFullDate(activity, date);
	}

	public static final String getDateTimeDescription(Activity activity, Date date) {

		String str = getDateDescription(activity, date);
		str = str + ", " + formatOnlyTime(date);
		return str;
	}
	
	public static final boolean isWeekDay(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		if(cal.get(Calendar.DAY_OF_WEEK) >= Calendar.MONDAY && cal.get(Calendar.DAY_OF_WEEK) <= Calendar.FRIDAY) {
			return true;
		} else {
			return false;
		}
	}
	
	public static final boolean isWeekEnd(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String formatFullDate(Activity activity, Date date) {
//		return new SimpleDateFormat("EEEEEEEE, MMMMMMMM dd").format(date);
		DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(activity);
		return dateFormat.format(date);
	}
	
	public static String formatTime(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd hh:mm a").format(date);
	}
	
	public static String formatOnlyTime(Date date) {
		return new SimpleDateFormat("hh:mm a").format(date);
	}
	
	public static String formatTimeForDeliverySlot(Date date) {
		return "" + date.getTime();
	}
	
	public static String formatTime24HR(Date date) {
		return new SimpleDateFormat("HH:mm").format(date);
	}

	public static Date setDate(Date date, Date startTime) {
		
		Calendar dateCal = new GregorianCalendar();
		dateCal.setTime(date);
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(startTime);
		cal.set(Calendar.DATE, dateCal.get(Calendar.DATE));
		cal.set(Calendar.MONTH, dateCal.get(Calendar.MONTH));
		cal.set(Calendar.YEAR, dateCal.get(Calendar.YEAR));
		return cal.getTime();
	}

	public static int getDay(Date pDate) {
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(pDate);
		return cal.get(Calendar.DAY_OF_WEEK);
	}	
	
	public static void main(String[] args) {
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DATE, 0);
		System.out.println(getDateDescription(null, cal.getTime()));
	}

	public static String getFormattedAutoGeneratedTime() {
		return new SimpleDateFormat("dd-MM-yyyy_HH-mm").format(new Date());
	}
}
