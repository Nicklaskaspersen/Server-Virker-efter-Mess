/*
 * Licensed Materials - Property of IBM
 * 5724-Y36, 5724-Z55 
 * ÃÂ© Copyright IBM Corporation 1996, 2011. All Rights Reserved.
 *
 * Note to U.S. Government Users Restricted Rights: 
 * Use, duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 */
package GUI;

import ilog.views.gantt.*;
import ilog.views.gantt.swing.calendarview.IlvDayView;
import ilog.views.gantt.swing.calendarview.IlvMonthPanel;
import ilog.views.util.time.IlvCalendarUtil;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.MouseInputAdapter;


/**
 * This class is an interactor that can be registered via the {@link #connect} method
 * on the month panel of the Calendar View Example.
 */
public class MonthPanelInteractor {

  /**
   * An Escape keystroke.
   */
  private static final KeyStroke ESCAPE_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,
                                                                     0);
  /**
   * The example that the interactor is bound to.
   */
  private CalendarViewExample example;

 /**
   * The example's month panel that the interactor is bound to.
   */
  private IlvMonthPanel monthPanel;

  /**
   * The example's day view.
   */
  private IlvDayView dayView;

  /**
   * The example's chart.
   */
  private IlvHierarchyChart chart;

  /**
   * The example's day view customizer.
   */
  private JComponent dayViewCustomizer;

  /**
   * The input handler on the month panel.
   */
  private MonthPanelInputHandler monthPanelHandler;


  // =========================================
  // Instance Construction and Initialization
  // =========================================

  /**
   * Creates a new month panel interactor that can be used within a Calendar View
   * example. Use the {@link #connect} method to install the interactor on the example.
   */
  public MonthPanelInteractor() {
  }

  /**
   * Installs the interactor on the month panel of the specified example.
   *
   * @param example The example.
   */
  public void connect(CalendarViewExample example) {
    disconnect();
    this.example = example;
    monthPanel = example.getMonthView().getMonthPanel();
    dayView = example.getDayView();
    chart = example.getChart();
    dayViewCustomizer = example.getDayViewCustomizer();
    monthPanelHandler = new MonthPanelInputHandler();
    monthPanel.addMouseListener(monthPanelHandler);
    monthPanel.addMouseMotionListener(monthPanelHandler);
  }

  /**
   * Deinstalls the interactor from the current example to which it is connected.
   */
  public void disconnect() {
    if (example != null) {
      monthPanel.removeMouseListener(monthPanelHandler);
      monthPanel.removeMouseMotionListener(monthPanelHandler);
      example = null;
      monthPanel = null;
      dayView = null;
      chart = null;
      dayViewCustomizer = null;
      monthPanelHandler = null;
    }
  }

  /**
   * Returns the data model that the month panel is currently displaying,
   * or <code>null</code> if the interactor is not installed.
   *
   * @return The current Gantt data model, or <code>null</code> if the
   *         interactor is not installed.
   */
  public IlvGanttModel getGanttModel() {
    return (monthPanel != null)
           ? monthPanel.getGanttModel()
           : null;
  }


  // =========================================
  // Handling Mouse and Keyboard Events
  // =========================================

  /**
   * Updates the example's day view and the chart to display the specified date.
   *
   * @param cal The calendar date to display.
   */
  private void syncDisplayedDate(Calendar cal) {
    chart.setVisibleTime(IlvTimeUtil.subtract(cal.getTime(),
                                              chart.getVisibleDuration().divide(2)));
    dayView.setCalendar(cal);
  }

  /**
   * <code>MonthPanelInputHandler</code> is registered on the example's month panel to
   * handle mouse and keyboard input events.
   */
  private class MonthPanelInputHandler extends MouseInputAdapter implements
      ActionListener {

    /**
     * The month panel's original cursor.
     */
    private Cursor oldCursor = null;

    /**
     * Date corresponding to the last mouse down event, or <code>null</code> if the mouse
     * was pressed outside of a day cell.
     */
    private Calendar mouseDownCalendar;

    /**
     * The activity being dragged.
     */
    private IlvActivity activity;

    /**
     * The activity's original time interval when the mouse was pressed.
     */
    private IlvTimeInterval oldInterval;

    /**
     * The number of days that the activity has been dragged since the mouse was pressed.
     */
    private int dayDelta;

    /**
     * If an activity is being dragged, drags it to the specified mouse location within the
     * month panel. Otherwise, does nothing.
     *
     * @param pt The mouse location within the month panel.
     */
    private void doDrag(Point pt) {
      Calendar cal = monthPanel.getCellDate(pt);
      if (activity == null || cal == null) {
        return;
      }
      int newDelta = IlvCalendarUtil.elapsedDays(mouseDownCalendar, cal);
      if (newDelta != dayDelta) {
        dayDelta = newDelta;
        Calendar newStart = (Calendar) cal.clone();
        newStart.setTime(oldInterval.getStart());
        newStart.add(Calendar.DAY_OF_YEAR, dayDelta);
        IlvTimeInterval newInterval = new IlvTimeInterval(newStart.getTime(),
                                                          oldInterval.
                                                          getDuration());
        activity.setTimeInterval(newInterval);
        // When dragging an activity, center the new date in the Gantt sheet
        // and select the same date in the day panel.
        syncDisplayedDate(cal);
      }
    }

    /**
     * Ends the current activity drag session, if one is active.
     */
    private void endDrag() {
      if (activity != null) {
        // Finalize the data model adjustment session. If calculations have been deferred
        // that now cause the dragged activity to be rescheduled, then update the date
        // displayed so the activity is still visible.
        Date start0 = activity.getStartTime();
        getGanttModel().setAdjusting(false);
        Date start1 = activity.getStartTime();
        if (!start1.equals(start0)) {
          Calendar cal = monthPanel.getCalendar();
          cal.setTime(start1);
          syncDisplayedDate(cal);
        }

        activity = null;
        oldInterval = null;
        dayDelta = 0;
        monthPanel.unregisterKeyboardAction(ESCAPE_KEY);
      }
      if (oldCursor != null) {
        monthPanel.setCursor(oldCursor);
        oldCursor = null;
      }
    }

    /**
     * Aborts the current activity drag session, if one is active.
     */
    private void abortDrag() {
      if (activity != null) {
        activity.setTimeInterval(oldInterval);
        syncDisplayedDate(mouseDownCalendar);
      }
      endDrag();
    }

    /**
     * This method is invoked when a mouse button has been pressed on the month panel.
     *
     * @param e The mouse event.
     */
    public void mousePressed(MouseEvent e) {
      Point point = e.getPoint();
      mouseDownCalendar = monthPanel.getCellDate(point);
      if (mouseDownCalendar == null) {
        return;
      }

      // When pressing the mouse on a day cell, center the date in the Gantt sheet
      // and select the same date in the day panel.
      syncDisplayedDate(mouseDownCalendar);

      // Check for starting to drag an activity only if we are not already dragging.
      if (activity != null) {
        return;
      }

      // If the mouse was pressed on an activity bar, then setup for possible
      // dragging of the activity.
      activity = monthPanel.getActivity(point);
      if (activity != null) {
        // Make sure that we only drag activities in the data model and not holidays.
        IlvGanttModel ganttModel = getGanttModel();
        if (ganttModel != null && ganttModel.contains(activity)) {
          oldInterval = activity.getTimeInterval();
          dayDelta = 0;
          // Initiate a data model adjustment session, so that certain calculations
          // (e.g. the critical path, etc.) are deferred until dragging is completed.
          ganttModel.setAdjusting(true);
          oldCursor = monthPanel.getCursor();
          monthPanel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
          monthPanel.registerKeyboardAction(this,
                                            ESCAPE_KEY,
                                            JComponent.WHEN_IN_FOCUSED_WINDOW);
        }
        else {
          activity = null;
        }
      }
    }

    /**
     * This method is invoked when a mouse button has been clicked on the month panel.
     *
     * @param e The mouse event.
     */
    public void mouseClicked(MouseEvent e) {
      // If we have double-clicked on a day-cell, then make the day view visible.
      Point point = e.getPoint();
      if (e.getClickCount() == 2 && monthPanel.getCellDate(point) != null) {
        ((JTabbedPane) example.getCustomizerPanel()).setSelectedComponent(
            dayViewCustomizer);
      }
    }

    /**
     * This method is invoked when the mouse has been dragged in the month panel.
     *
     * @param e The mouse event.
     */
    public void mouseDragged(MouseEvent e) {
      doDrag(e.getPoint());
    }

    /**
     * This method is invoked when a mouse button has been released on the month panel.
     *
     * @param e The mouse event.
     */
    public void mouseReleased(MouseEvent e) {
      doDrag(e.getPoint());
      endDrag();
    }

    /**
     * This method is invoked when the ESC key has been pressed.
     *
     * @param e The event.
     */
    public void actionPerformed(ActionEvent e) {
      abortDrag();
    }
  }

}
