/*
 * SummaryView.java
 * :tabSize=4:indentSize=4:noTabs=false:
 *
 * Copyright (C) 2002, 2003 Rick Gruber (rick@vanosten.net)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package net.vanosten.dings.swing;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;

import java.util.Vector;

import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.consts.Constants;
import net.vanosten.dings.event.AppEvent;
import net.vanosten.dings.event.IAppEventHandler;
import net.vanosten.dings.uiif.ISummaryView;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.BarRenderer;
import org.jfree.data.CategoryDataset;

public class SummaryView extends AViewWithButtons implements ISummaryView {
	
	/** Lets the user choose the chart type / current statistic */
	private JComboBox statTypeCB;
	
	//to choose how many days back the stats should reach
	private JTextField lastLearnedBeforeTF;
	private JLabel lastLearnedBeforeL;
	
	/** Whether all entries should be taken into account or only chosen */
	private JCheckBox chosenCB;
	
	/** Displays the chosen statistics chosen with <code>statTypeCB</code> */
	private JButton showGraphB;
	
	/** The panel containing a chart to display statistics */
	private JPanel chartPanel;
	
	private ChartPanel currentChartPanel;
	
	public SummaryView(ComponentOrientation aComponentOrientation) {
		super("Statistics", aComponentOrientation);
		initializeGUI();
		this.setGUIOrientation();
	} //End public SummaryView(ComponentOrientation)
	
	//implements AViewWithButtons
	protected void initializeMainP() {
		mainP = new JPanel();
		
		initComponents();
		
		mainP.add(chartPanel);
	}	//End protected void initializeMainP()
	
	private void initComponents() {
		chartPanel = new JPanel();
		chartPanel.setLayout(new BorderLayout());
		chosenCB = new JCheckBox("Take Only Selected Entries Into Account");
		chosenCB.setSelected(false);
	} //END private void initComponents()
	
	//implements AViewWithButtons
	protected void initializeButtonP() {
		buttonsP = new JPanel();
		buttonsP.setLayout(new BoxLayout(buttonsP, BoxLayout.LINE_AXIS));
		buttonsP.add(statTypeCB);
		buttonsP.add(Box.createRigidArea(new Dimension(DingsSwingConstants.SP_H_G, 0)));
		buttonsP.add(lastLearnedBeforeL);
		buttonsP.add(Box.createRigidArea(new Dimension(DingsSwingConstants.SP_H_C, 0)));
		buttonsP.add(lastLearnedBeforeTF);
		buttonsP.add(Box.createHorizontalGlue());
		buttonsP.add(showGraphB);
	} //END protected void initializeButtonP()
	
	//implements AViewWithButtons
	protected final void initButtonComponents() {
		Vector items = new Vector();
		items.add("Entries by Unit");
		items.add("Entries by Category");
		items.add("Entries by Entry Type");
		items.add("Entries by Score");
		items.add("Entry Scores over Time"); //do not chang sequence of elements, as showGraph() and checkChosenStattype rely on it
		statTypeCB = new JComboBox(items);
		statTypeCB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				checkChosenStatType();
			}
		});
		
		lastLearnedBeforeTF = new JTextField();
		lastLearnedBeforeTF.setText(Integer.toString(0));
		lastLearnedBeforeTF.setColumns(5);
		lastLearnedBeforeTF.setEnabled(false);
		lastLearnedBeforeL = new JLabel("Last learned before (days)");
		lastLearnedBeforeL.setDisplayedMnemonic(("L").charAt(0));
		lastLearnedBeforeL.setLabelFor(lastLearnedBeforeTF);
		lastLearnedBeforeL.setEnabled(false);

		showGraphB = new JButton("Show Graph", Constants.createImageIcon(Constants.IMG_EMPTY_24x1, ""));
		showGraphB.setMnemonic("S".charAt(0));
		showGraphB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				showGraph();
			}
		});
	} //END protected final void initButtonComponents()
	
	//overrides the method in AViewWithButton
	public boolean init(IAppEventHandler aController) {
		boolean foo = super.init(aController);
		if (false == foo) {
			return false;
		}
		AppEvent evt = new AppEvent(AppEvent.DATA_EVENT);
		evt.setMessage(MessageConstants.D_SUMMARY_VIEW_DISPLAY_UNITS);
		evt.setDetails(Boolean.toString(chosenCB.isSelected()));
		controller.handleAppEvent(evt);
		return true;
	} //END public boolean init()
	
	/**
	 * Checks which item in the list over graphs is chosen and updates the 
	 * enabled status of <code>lastLearnedBeforeL</code> and <code>lastLearnedBeforeTF</code>
	 * accordingly. The elements are only enabled if scores over time is enabled.
	 */
	private void checkChosenStatType() {
		String foo = (String) statTypeCB.getSelectedItem();
		if (5 == statTypeCB.getSelectedIndex()) {
			lastLearnedBeforeTF.setEnabled(true);
			lastLearnedBeforeL.setEnabled(true);
		}
		else {
			lastLearnedBeforeTF.setEnabled(false);
			lastLearnedBeforeL.setEnabled(false);
		}
	} //END private void checkChosenStatType()
	
	/**
	 * Shows the chosen grahp in the display area.
	 */
	private void showGraph() {
		AppEvent evt = new AppEvent(AppEvent.DATA_EVENT);
		if (0 == statTypeCB.getSelectedIndex()) {
			evt.setMessage(MessageConstants.D_SUMMARY_VIEW_DISPLAY_UNITS);
		}
		else if (1 == statTypeCB.getSelectedIndex()) {
			evt.setMessage(MessageConstants.D_SUMMARY_VIEW_DISPLAY_CATEGORIES);
		}
		else if (2 == statTypeCB.getSelectedIndex()) {
			evt.setMessage(MessageConstants.D_SUMMARY_VIEW_DISPLAY_ENTRYTYPES);
		}
		else if (3 == statTypeCB.getSelectedIndex()) {
			evt.setMessage(MessageConstants.D_SUMMARY_VIEW_DISPLAY_SCORES);
		}
		else if (4 == statTypeCB.getSelectedIndex()) {
			evt.setMessage(MessageConstants.D_SUMMARY_VIEW_DISPLAY_TIMELINE);
		}
		evt.setDetails(Boolean.toString(chosenCB.isSelected()));
		controller.handleAppEvent(evt);
	} //END private void showGraph()
	
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    public void displayHorizontalBarChart(final CategoryDataset dataset, final String title, final String categoryTitle, final String valueTitle) {
    	// create the chart...
    	final JFreeChart chart = ChartFactory.createBarChart(
    			title // chart title
				, categoryTitle // domain axis label
				, valueTitle // range axis label
				, dataset // data
				, PlotOrientation.HORIZONTAL // orientation
				, true // include legend
				, true // tooltips?
				, false // URLs?
				);
    	// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
    	// set the background color for the chart...
    	chart.setBackgroundPaint(Color.white);
    	
    	// get a reference to the plot for further customisation...
    	final CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		
		// set the range axis to display integers only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		
		// disable bar outlines...
		final BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);
		// OPTIONAL CUSTOMISATION COMPLETED.
		if (null != currentChartPanel) {
			chartPanel.remove(currentChartPanel);
		}
		currentChartPanel = new ChartPanel(chart);
		chartPanel.add(currentChartPanel, BorderLayout.CENTER);
		mainP.invalidate();
		mainP.validate();
		mainP.repaint();
		
    } //END public void displayHorizontalBarChart(...)
} //END public class SummaryView extends AViewWithButtons implements ISummaryView
/*


--code--
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.sql.*;
import entrak.sqlclient.*;

public class DynamicDataDemo_test extends JApplet implements ActionListener {

double[] value01ARR;
Date[] date01ARR;

double[] value02ARR;
Date[] date02ARR;

private TimeSeries series01;
private TimeSeries series02;


date01ARR = new Date[100000];

value01ARR = new double[100000];

date01ARR[k01++] = (Date) result.getObject("datetime");
value01ARR[j01++] = Double.parseDouble(result.getString("value"));
date02ARR = new Date[100000];
value02ARR = new double[100000];
int j02=0;

date02ARR[k02++] = (Date) result.getObject("datetime");
value02ARR[j02++] = Double.parseDouble(result.getString("value"));

this.series01 = new TimeSeries("Office01", Millisecond.class);
this.series02 = new TimeSeries("Office02", Millisecond.class);

TimeSeriesCollection dataset = new TimeSeriesCollection();
dataset.addSeries(series01);
dataset.addSeries(series02);

JFreeChart chart = ChartFactory.createTimeSeriesChart("","Time", "degC",dataset, true, true, false);

XYPlot plot = chart.getXYPlot();
axis = plot.getDomainAxis();
axis.setAutoRange(true);
axis.setFixedAutoRange(600000000.0);

JPanel content = new JPanel(new BorderLayout());

ChartPanel chartPanel = new ChartPanel(chart);
chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
chartPanel.setBorder(BorderFactory.createCompoundBorder(
BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(
"Temperature"),BorderFactory.createEmptyBorder(0,0,0,0)),
chartPanel.getBorder()));

content.add(chartPanel, BorderLayout.CENTER);

textArea = new JTextArea(20,27);
//textArea.setForeground(Color.red);
textArea.setFont(new Font("Serif", Font.BOLD, 12));
textArea.setEditable(false);
textArea.setLineWrap(true);
textArea.setWrapStyleWord(true);
JScrollPane StextArea = new JScrollPane(textArea);
StextArea.setBorder(BorderFactory.createCompoundBorder(
BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(
"Critical values"),BorderFactory.createEmptyBorder(0,0,0,0)),
StextArea.getBorder()));
content.add(StextArea, BorderLayout.EAST);

JButton startB = new JButton("Start");
startB.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent e) {

startAnimation();

} });

JButton stopB = new JButton("Stop");
stopB.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent e) {

stopAnimation();

} });

//---
JSlider valuesPerSecond = new JSlider(JSlider.HORIZONTAL,
0, 10, FPS_INIT);
valuesPerSecond.addChangeListener(new SliderListener());

valuesPerSecond.setMajorTickSpacing(2);
valuesPerSecond.setMinorTickSpacing(1);
valuesPerSecond.setPaintTicks(true);
valuesPerSecond.setPaintLabels(true);

JLabel delayL = new JLabel("Delay:");

JPanel topP = new JPanel();
topP.add(delayL);
topP.add(valuesPerSecond);

//---
JSlider zooming = new JSlider(JSlider.HORIZONTAL,
0, 10, ZOO_INIT);
zooming.addChangeListener(new SliderListener1());

zooming.setMajorTickSpacing(2);
zooming.setMinorTickSpacing(1);
zooming.setPaintTicks(true);
zooming.setPaintLabels(true);

JLabel zoomL = new JLabel("Zoom:");
JPanel downP = new JPanel();
downP.add(zoomL);
downP.add(zooming);
//---

JPanel slidersP = new JPanel();
BoxLayout slidersB = new BoxLayout(slidersP, BoxLayout.Y_AXIS);
slidersP.setLayout(slidersB);
slidersP.add(topP);
slidersP.add(downP);

JButton arrangeVisitorB = new JButton("Arrange visitor");
JButton nurseB = new JButton("Nurse");
JButton firefighterB = new JButton("Firefighter");
JPanel buttonsP = new JPanel();
buttonsP.add(startB);
buttonsP.add(stopB);
buttonsP.add(slidersP);
buttonsP.add(arrangeVisitorB);
buttonsP.add(nurseB);
buttonsP.add(firefighterB);

content.add(buttonsP, BorderLayout.SOUTH);

setContentPane(content);

timer = new Timer(delay, this);
timer.setInitialDelay(delay);
timer.setCoalesce(true);
}

class SliderListener implements ChangeListener {
public void stateChanged(ChangeEvent e) {
JSlider source = (JSlider)e.getSource();
if (!source.getValueIsAdjusting()) {
int fps = (int)source.getValue();
if (fps == 0) {
if (!frozen) stopAnimation();
} else {
delay = 1000 / fps;
timer.setDelay(delay);
if (frozen) startAnimation();
}
}
}
}

class SliderListener1 implements ChangeListener {
public void stateChanged(ChangeEvent e) {
JSlider source = (JSlider)e.getSource();
if (!source.getValueIsAdjusting()) {
int fps = (int)source.getValue();
if (fps == 0) {
if (!frozen) stopAnimation();
} else {
axis.setFixedAutoRange(600000000.0/fps);
if (frozen) startAnimation();
}
}
}
}


public static void main(String[] args) {
DynamicDataDemo_test demo = new DynamicDataDemo_test();
demo.setVisible(true);
}
}


*/
