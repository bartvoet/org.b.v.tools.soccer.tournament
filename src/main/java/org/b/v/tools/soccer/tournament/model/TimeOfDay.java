package org.b.v.tools.soccer.tournament.model;

public class TimeOfDay implements Comparable<TimeOfDay> {

	private int hour,minutes;
	

	public int getHour() {
		return hour;
	}

	public int getMinutes() {
		return minutes;
	}

	public TimeOfDay(int i, int j) {
		this.hour=i;
		this.minutes=j;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hour;
		result = prime * result + minutes;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeOfDay other = (TimeOfDay) obj;
		if (hour != other.hour)
			return false;
		if (minutes != other.minutes)
			return false;
		return true;
	}

	@Override
	public int compareTo(TimeOfDay o) {
		if(this.hour>o.hour) {
			return 1;
		} else if(this.hour<o.hour) {
			return -1;
		}
		
		if(this.minutes>o.minutes) {
			return 1;
		} else if (this.minutes<o.minutes) {
			return -1;
		}
		
		return 0;
	}

}
