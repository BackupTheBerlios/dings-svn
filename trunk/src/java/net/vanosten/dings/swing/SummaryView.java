/*
 * SummaryView.java
 * :tabSize=4:indentSize=4:noTabs=false:
 *
 * DingsBums?! A flexible flashcard application written in Java.
 * Copyright (C) 2002, 03, 04, 05, 2006 Rick Gruber-Riemer (dingsbums@vanosten.net)
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

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.ComponentOrientation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.util.Vector;

import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.event.AppEvent;
import net.vanosten.dings.event.IAppEventHandler;
import net.vanosten.dings.utils.Toolbox;
import net.vanosten.dings.uiif.ISummaryView;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.time.TimeSeriesCollection;

public class SummaryView extends AViewWithButtons implements ISummaryView {
	private final static long serialVersionUID = 1L;

	/** Lets the user choose the chart type / current statistic */
	private JComboBox chartTypeCB;

	/** Whether all entries should be taken into account or only chosen */
	private JCheckBox chosenCB;

	/** The button to (re)draw the chart based on the current chart properties */
	private JButton redrawB;

	/** The panel containing a chart to display statistics */
	private JPanel chartP;

	/** The panel containing the chart properties */
	private JPanel chartPropertiesP;

	public SummaryView(ComponentOrientation aComponentOrientation) {
		super(Toolbox.getInstance().getLocalizedString("viewtitle.summary"), aComponentOrientation);
		initializeGUI();
		this.setGUIOrientation();
	} //END public SummaryView(ComponentOrientation)

	//implements AViewWithButtons
	protected void initializeMainP() {
		mainP = new JPanel();
		BorderLayout bl = new BorderLayout();
		bl.setVgap(DingsSwingConstants.SP_V_G);
		mainP.setLayout(bl);

		initComponents();
		initializeChartPropertiesPanel();

		mainP.add(chartP, BorderLayout.CENTER);
		mainP.add(chartPropertiesP, BorderLayout.PAGE_END);
	} //END protected void initializeMainP()

	private void initComponents() {
		chartP = new JPanel();
		chartP.setLayout(new BorderLayout());
		//to begin with we show an information
		JLabel infoL = new JLabel("Choose the chart type belwo and press the \"Update View\" button to show statistics.");
		infoL.setEnabled(false); //only for display
		infoL.setHorizontalAlignment(SwingConstants.CENTER);
		chartP.add(infoL, BorderLayout.CENTER);

		Vector<String> items = new Vector<String>();
		items.add("Entries by " + Toolbox.getInstance().getInfoPointer().getUnitLabel());
		items.add("Entries by " + Toolbox.getInstance().getInfoPointer().getCategoryLabel());
		items.add("Entries by Entry Type");
		items.add("Entries by Score");
		items.add("Entry Scores over Time"); //do not change sequence of elements, as showGraph() and checkChosenStattype rely on it
		chartTypeCB = new JComboBox(items);
		chartTypeCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (4 == chartTypeCB.getSelectedIndex()) {
					chosenCB.setEnabled(false);
				} else {
					chosenCB.setEnabled(true);
				}
			}
		});

		chosenCB = new JCheckBox("Only Selected Entries", false);
		chosenCB.setMnemonic("O".charAt(0));
	} //END private void initComponents()

	private void initializeChartPropertiesPanel() {
		chartPropertiesP = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		chartPropertiesP.setLayout(gbl);

		TitledBorder title;
		title = BorderFactory.createTitledBorder("Chart Properties");
		chartPropertiesP.setBorder(title);

		JLabel typeL = new JLabel("Chart Type:");
		typeL.setDisplayedMnemonic("C".charAt(0));
		typeL.setLabelFor(chartTypeCB);
		JLabel emptyL = new JLabel();

		Insets vghg = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		//----type
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = vghg;
		gbl.setConstraints(typeL, gbc);
		chartPropertiesP.add(typeL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbl.setConstraints(chartTypeCB, gbc);
		chartPropertiesP.add(chartTypeCB);
		//----
		gbc.gridx = 2;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(emptyL, gbc);
		chartPropertiesP.add(emptyL);
		//----chosen
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbl.setConstraints(chosenCB, gbc);
		chartPropertiesP.add(chosenCB);
	} //END private void initializeChartPropertiesPanel()

	//implements AViewWithButtons
	protected void initializeButtonP() {
		buttonsP = new JPanel();
		buttonsP.setLayout(new FlowLayout(FlowLayout.TRAILING, 0, 0));
		buttonsP.add(redrawB);
	} //END protected void initializeButtonP()

	//implements AViewWithButtons
	protected final void initButtonComponents() {
		redrawB = new JButton(Toolbox.getInstance().getLocalizedString("label.button.update_view")
				, DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_REDRAW_BTN, "FIXME"));
		redrawB.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.button.update_view").charAt(0));
		redrawB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				showChart();
			}
		});
	} //END protected final void initButtonComponents()

	//overrides the method in AViewWithButton
	public boolean init(IAppEventHandler aController) {
		boolean foo = super.init(aController);
		if (false == foo) {
			return false;
		}
		return true;
	} //END public boolean init()

	/**
	 * Shows the chosen grahp in the display area.
	 */
	private void showChart() {
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR)); //will be set back in placeChart()
		AppEvent evt = new AppEvent(AppEvent.EventType.DATA_EVENT);
		switch(chartTypeCB.getSelectedIndex()) {
			case 0: evt.setMessage(MessageConstants.Message.D_SUMMARY_VIEW_DISPLAY_UNITS); break;
			case 1: evt.setMessage(MessageConstants.Message.D_SUMMARY_VIEW_DISPLAY_CATEGORIES); break;
			case 2: evt.setMessage(MessageConstants.Message.D_SUMMARY_VIEW_DISPLAY_ENTRYTYPES); break;
			case 3: evt.setMessage(MessageConstants.Message.D_SUMMARY_VIEW_DISPLAY_SCORES); break;
			default: evt.setMessage(MessageConstants.Message.D_SUMMARY_VIEW_DISPLAY_TIMELINE); break;
		}
		evt.setDetails(Boolean.toString(chosenCB.isSelected()));
		controller.handleAppEvent(evt);
	} //END private void showChart()

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

		placeChart(chart);
	} //END public void displayHorizontalBarChart(...)

	//implemnts ISummaryView
	public void displayTimeSeriesChart(final TimeSeriesCollection averageScore
			, final int maxScoreRange
			, final TimeSeriesCollection numberOfEntries
			, final int maxTotalRange) {
		final JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"Time Series"
				, "Date"
				, "Average Score"
				, averageScore
				, true
				, true
				, false
			);

		chart.setBackgroundPaint(Color.white);
		final XYPlot plot = chart.getXYPlot();
		plot.setOrientation(PlotOrientation.VERTICAL);
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		final StandardXYItemRenderer renderer1 = (StandardXYItemRenderer) plot.getRenderer();
		renderer1.setPaint(Color.blue);

		//axis 1
		final NumberAxis axis1 = new NumberAxis("Average Score");
		axis1.setLabelPaint(Color.blue);
		axis1.setTickLabelPaint(Color.blue);
		axis1.setRange(0.0d, maxScoreRange + 1);
		plot.setRangeAxis(0, axis1);

		//axis 2
		final NumberAxis axis2 = new NumberAxis("Number of Entries");
		axis2.setLabelPaint(Color.red);
		axis2.setTickLabelPaint(Color.red);
		axis2.setRange(0.0d, 10 * (((int) (maxTotalRange / 10)) + 1));
		plot.setRangeAxis(1, axis2);

		plot.setDataset(1, numberOfEntries);
		plot.mapDatasetToRangeAxis(1, 1);
		final StandardXYItemRenderer renderer2 = new StandardXYItemRenderer();
		renderer2.setPaint(Color.red);
		plot.setRenderer(1, renderer2);

		placeChart(chart);
	} //END public void displayTimeSeriesChart(XYDataset, XYDataSet)

	/**
	 * Places a chart object into the GUI and makes sure it is painted.
	 * @param aChart
	 */
	private final void placeChart(JFreeChart aChart) {
		chartP.removeAll();
		chartP.add(new ChartPanel(aChart), BorderLayout.CENTER);
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		mainP.invalidate();
		mainP.validate();
		mainP.repaint();
	} //END private final void placeChart(JFreeChart)
} //END public class SummaryView extends AViewWithButtons implements ISummaryView
