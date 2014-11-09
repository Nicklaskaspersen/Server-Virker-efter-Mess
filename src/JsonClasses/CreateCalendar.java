package JsonClasses;

public class CreateCalendar implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7236537878041886003L;
	private String overallID = "createCalendar";
	private String CalendarName;
	private String userName;
	private int publicOrPrivate;
	
	//Getters and setters for everything, bitch
	public String getOverallID() {
		return overallID;
	}
	public void setOverallID(String overallID) {
		this.overallID = overallID;
	}
	public String getCalendarName() {
		return CalendarName;
	}
	public void setCalendarName(String CalendarName) {
		this.CalendarName = CalendarName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getPublicOrPrivate() {
		return publicOrPrivate;
	}
	public void setPublicOrPrivate(int publicPrivate) {
		this.publicOrPrivate = publicPrivate;
	}

}
