/*
 * MainWindow.java
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

/**
 * This class is the main entry for ADings, which is a vocabulary trainer
 * program written in Java. It uses XML as its native format to store the
 * vocabularies.
 *
 * @author Rick Gruber
 * @version
 */
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Cursor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.FontMetrics;

import java.io.File;

import net.vanosten.dings.event.IAppEventHandler;
import net.vanosten.dings.consts.Constants;
import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.event.AppEvent;
import net.vanosten.dings.model.ADings;
import net.vanosten.dings.model.Preferences;
import net.vanosten.dings.swing.helperui.XMLFileFilter;
import net.vanosten.dings.swing.helperui.StatusBar;
import net.vanosten.dings.uiif.*;


//Logging with log4J
import java.util.logging.Logger;
import java.util.logging.Level;

public class MainWindow extends JFrame implements IDingsMainWindow {

	/** Holds the applications preferences and properties */
	private transient Preferences preferences = null;

	/** The main Panel */
	private JPanel mainP;
	
	/** The component/gui orientation for international apps */
	protected ComponentOrientation guiOrientation;
	
	private WelcomeView welcomeView;
	private SummaryView summaryView;
	private InfoVocabEditView infoVocabEditView;
	private UnitsListView unitsListView;
	private UnitEditView unitEditView;
	private CategoriesListView categoriesListView;
	private UnitEditView categoryEditView;
	private EntriesListView entriesListView;
	private EntryEditView entryEditView;
	private EntryTypesListView entryTypesListView;
	private EntryTypeEditView entryTypeEditView;
	private EntryTypeAttributesListView entryTypeAttributesListView;
	private EntryTypeAttributeEditView entryTypeAttributeEditView;
	private EntryLearnOneView entryLearnOneView;
	private EntriesSelectionView entriesSelectionView;

	private IAppEventHandler parentController;
	
	private JMenuBar menuBar;
	private StatusBar statusBar;

	//The menus
	private JMenu fileMenu;
	private JMenu editMenu;
	private JMenu goMenu; //the different views
	private JMenu toolsMenu;
	private JMenu helpMenu;

	//The menu items for fileMenu
	private JMenuItem newMI;    //not implemented yet: starts a new vocabulary file
	//should there also be a new based on an existing??
	private JMenuItem openMI;   //opens a vocabulary file
	private JMenuItem saveMI;   //saves the current vocabulary into the opened file
	private JMenuItem saveAsMI; //opens a file dialog to save vocabulary to a new file
	private JMenuItem propertiesMI;   //a view to edit the vocabulary properties
	private JMenuItem closeMI; //closes a vocabulary file
	private JMenuItem quitMI;   //exit from the application

	//the menu items for the editMenu
	private JMenuItem preferencesMI;  //edit the application preferences

	//The menu items for the goMenu
	private JMenuItem entryTypesMI; //a table of available entry types
	private JMenuItem entryTypeAttributesMI; //a table of available entry type attributes
	private JMenuItem unitsMI;    //a table of available units
	private JMenuItem categoriesMI;    //a table of available categories
	private JMenuItem entriesMI;    //a table of the currently chosen entries
	private JMenuItem learnOneByOneMI;

	//The menu items for the toolsMenu
	private JMenuItem selectMI; //to choose the current selection
	private JMenuItem resetScoreAllMI; //to reset score to 1 in all entries
	private JMenuItem resetScoreSelMI; //to reset score to 1 in current selection
	private JMenuItem statisticsMI; //statistics about the current selection
	private JMenuItem saveStatsMI; //save the current learning statistics
	//private JMenuItem searchMI; //to search for words

	//The menu items for the helpMenu
	private JMenuItem contentsMI;   //not implemented yet: the help (context sensitive)
	private JMenuItem aboutMI; //a short notice about this program

	
	/** 
	 * The logging logger. The base package structure is chosen as name 
	 * in order to have the other loggers in the divers classes inherit the settings (level).
	 * */
	private static Logger logger = Logger.getLogger("net.vanosten.dings.swing.MainWindow");

	/**
	 * The Constructor. It sets up the frame with a default set of menus and at
	 * program start the go view is displayed.
	 */
	public MainWindow(IAppEventHandler aController, Preferences thePreferences, ComponentOrientation aComponentOrientation) {
		super.setComponentOrientation(aComponentOrientation);
		//set pointers
		this.parentController = aController;
		this.preferences = thePreferences;
		this.guiOrientation = aComponentOrientation;
		
		//prepare look & feel
		try {
			String systemLAF = UIManager.getSystemLookAndFeelClassName();
			if (logger.isLoggable(Level.FINEST)) {
				logger.logp(Level.FINEST, this.getClass().getName(), "MainWindow()", "System look and feel class name: " + systemLAF);
			}			
			if (Boolean.valueOf(preferences.getProperty(Preferences.PROP_SYSTEM_LAF)).booleanValue()) {
				UIManager.setLookAndFeel (systemLAF);
			}
		}
		catch (UnsupportedLookAndFeelException evt) {
			if (logger.isLoggable(Level.FINEST)) {
				logger.logp(Level.FINEST, this.getClass().getName(), "MainWindow()", "System specific look and feel could not be found: " + evt.getMessage());
			}			
		}
		catch (Exception evt) {
			if (logger.isLoggable(Level.FINEST)) {
				logger.logp(Level.FINEST, this.getClass().getName(), "MainWindow()", "Look and feel property could not be properly translated to boolean: " + evt.getMessage());
			}			
		}

		//prepare show
		int windowWidth  = 800;
		int windowHeight = 600;
		int locationX = 0;
		int locationY = 0;
		if (preferences.containsKey(Preferences.PROP_APP_WINDOW_WIDTH) && preferences.containsKey(Preferences.PROP_APP_WINDOW_HEIGHT)) {
			try {
				windowWidth = Integer.parseInt(preferences.getProperty(Preferences.PROP_APP_WINDOW_WIDTH));
				windowHeight = Integer.parseInt(preferences.getProperty(Preferences.PROP_APP_WINDOW_HEIGHT));
				locationX = Integer.parseInt(preferences.getProperty(Preferences.PROP_APP_LOCATION_X));
				locationY = Integer.parseInt(preferences.getProperty(Preferences.PROP_APP_LOCATION_Y));
			}
			catch (NumberFormatException e) {
				//do nothing because default values ok
			}
		}
		setBounds(locationX, locationY, windowWidth, windowHeight);
		
		//define the icon
		ImageIcon icon = Constants.createImageIcon(Constants.IMG_DINGS_32, "logo of the " + ADings.APP_NAME + " application");
		if (null != icon) {
			this.setIconImage(icon.getImage());
		}

		//define closing behavior
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				AppEvent ape = new AppEvent(AppEvent.STATUS_EVENT);
				ape.setMessage(MessageConstants.S_EXIT_APPLICATION);
				parentController.handleAppEvent(ape);
			}
		});
		
		//set the layout manager
		mainP = new JPanel();
		mainP.setLayout(new BorderLayout());
		//set the font sizes
		Constants.setFontSizes(mainP.getFont());

		this.getContentPane().add(mainP, BorderLayout.CENTER);

		//status bar
		statusBar = new StatusBar();
		this.getContentPane().add(statusBar, BorderLayout.PAGE_END);		

		//add the menu bar
		initializeMenuBar();
		setFileHistory(preferences.getFileHistoryPaths());
		setJMenuBar(menuBar);

		this.applyComponentOrientation(this.guiOrientation);
		//show the frame
		//Do not pack() to have the size correctly set
		this.setVisible(true);
	} //End public MainWindow()
	
	//	implements IDingsMainWindow
	public void saveWindowLocationAndSize() {
		Dimension d = this.getSize();
		Point p = this.getLocationOnScreen();
		preferences.setProperty(Preferences.PROP_APP_WINDOW_WIDTH, Integer.toString(d.width));
		preferences.setProperty(Preferences.PROP_APP_WINDOW_HEIGHT, Integer.toString(d.height));
		preferences.setProperty(Preferences.PROP_APP_LOCATION_X, Integer.toString(p.x));
		preferences.setProperty(Preferences.PROP_APP_LOCATION_Y, Integer.toString(p.y));
	} //END public void saveWindowLocationAndSize()

	/**
	 * Shows the preferences dialog.
	 */
	private void showPreferences() {
		PreferencesEditView preferencesEditView = new PreferencesEditView(this, this.guiOrientation);
		int dialogWidth = 500;
		int dialogHeight = 400;
		if (preferences.containsKey(Preferences.PROP_PREF_DIALOG_WIDTH) && preferences.containsKey(Preferences.PROP_PREF_DIALOG_HEIGHT)) {
			try {
				dialogWidth = Integer.parseInt(preferences.getProperty(Preferences.PROP_PREF_DIALOG_WIDTH));
				dialogHeight = Integer.parseInt(preferences.getProperty(Preferences.PROP_PREF_DIALOG_HEIGHT));
			}
			catch (NumberFormatException e) {
			}
		}
		preferencesEditView.setSize(dialogWidth, dialogHeight);
		preferences.setEditView(preferencesEditView);
		preferencesEditView.init(preferences);
		preferencesEditView.setLocationRelativeTo(this);
		preferencesEditView.setVisible(true);
	} //END private void showPreferences()
	
	//implements IDingsMainWindow
	public void showHelp(String screen) {
		JOptionPane.showMessageDialog(this, "Sorry, not implemented yet! " + screen, "Help", JOptionPane.INFORMATION_MESSAGE);
	} //END public void showHelp(String)

	//implements IDingsMainWindow
	public String showFileChooser(String aFileName, boolean showOpen) {
		JFileChooser fc = new JFileChooser(aFileName);
		fc.setDialogTitle("Open Vocabulary File");
		fc.setMultiSelectionEnabled(false);
		fc.addChoosableFileFilter(new XMLFileFilter());
		int returnValue;
		if (showOpen) returnValue = fc.showOpenDialog(this);
		else returnValue = fc.showSaveDialog(this);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			return file.getPath();
		}
		return Constants.CANCELLED_INPUT;
	} //END private boolean showFileChooser()

	//implements IDingsMainWindow
	public void showMessageDialog(String aTitle, String aMessage, int aMessageType) {
		JOptionPane.showMessageDialog(this, aMessage, aTitle, getSwingMessageType(aMessageType));
	} //END public void showMessageDialog(String, String, int)

	//implements IDingsMainWindow
	public int showOptionDialog(String aTitle, String aMessage, int aMessageType, int anOptionType) {
		Object[] options; //the buttons to be displayed
		if (Constants.YES_NO_CANCEL_OPTION == anOptionType) {
			options = new Object[3];
			options[0] = "Yes";
			options[1] = "No";
			options[2] = "Cancel";
		}
		else {
			options = new Object[2];
			options[0] = "Yes";
			options[1] = "No";			
		}
		int answer = JOptionPane.showOptionDialog(this
						, aMessage
						, aTitle
						, getSwingMessageType(aMessageType)
						, getSwingOptionType(anOptionType)
						, null //don't use a custom Icon
						, options //the titles of buttons
						, options[0]); //default button title
		return getDingsAnswer(answer);
	} //END public int showOptionDialog(String, String, int, int)
	
	private int getDingsAnswer(int aSwingAnswer) {
		int dingsAnswer = -1;
		switch(aSwingAnswer) {
			case JOptionPane.YES_OPTION: dingsAnswer = Constants.YES_OPTION; break;
			case JOptionPane.NO_OPTION: dingsAnswer = Constants.NO_OPTION; break;
			case JOptionPane.CANCEL_OPTION: dingsAnswer = Constants.CANCEL_OPTION; break;
			default:
			if (logger.isLoggable(Level.FINEST)) {
				logger.logp(Level.FINEST, this.getClass().getName(), "getDingsAnswer()", "Answer type not found: " + aSwingAnswer);
			}			
		}
		return dingsAnswer;
	} //END private int getDingsAnswer(int aSwingAnswer)

	private int getSwingMessageType(int aDingsMessageType) {
		int swingMessageType = -1;
		switch(aDingsMessageType) {
			case Constants.INFORMATION_MESSAGE: swingMessageType = JOptionPane.INFORMATION_MESSAGE; break;
			case Constants.ERROR_MESSAGE: swingMessageType = JOptionPane.ERROR_MESSAGE; break;
			case Constants.WARNING_MESSAGE: swingMessageType = JOptionPane.WARNING_MESSAGE; break;
			case Constants.QUESTION_MESSAGE: swingMessageType = JOptionPane.QUESTION_MESSAGE; break;
			default:
				if (logger.isLoggable(Level.FINEST)) {
					logger.logp(Level.FINEST, this.getClass().getName(), "getSwingMessageType()", "Message type not found: " + aDingsMessageType);
				}			
		}
		return swingMessageType;
	} //END private int getSwingMessageType(int)

	private int getSwingOptionType(int aDingsOptionType) {
		int swingOptionType = -1;
		switch(aDingsOptionType) {
			case Constants.YES_NO_OPTION: swingOptionType = JOptionPane.YES_NO_OPTION; break;
			case Constants.YES_NO_CANCEL_OPTION: swingOptionType = JOptionPane.YES_NO_CANCEL_OPTION; break;
			default:
				if (logger.isLoggable(Level.FINEST)) {
					logger.logp(Level.FINEST, this.getClass().getName(), "getSwingOptionType()", "Option type not found: " + aDingsOptionType);
				}			
		}
		return swingOptionType;
	} //END private int getSwingOptionType(int)

	//implements IDingsMainWindow
	public void setApplicationTitle(String aTitle) {
		this.setTitle(aTitle);
	} //END public void setApplicationTitle(String)
	
	//implements IdingsMainWindow
	public void setStatusBarStatusText(String aStatusText, String aSelectionText) {
		statusBar.setStatusText(aStatusText, aSelectionText);
	} //END public void setStatusBarStatusText(String, String)
	
	//implements IDingsMainWindow
	public void setWaitCursor(boolean enable) {
		if (enable) {
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		}
		else {
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	} //END public void setWaitCursor(boolean)
	
	//implements IDingsMainWindow
	public void hideMainWindow() {
		this.hide();
	} //END public void hideMainWindow()

	//=========== Navigation ==============================================================//
	
	public IWelcomeView getWelcomeView() {
		welcomeView = new WelcomeView(this.getComponentOrientation());
		return welcomeView;
	} //END public IWelcomeView getWelcomeView()
	
	public ISummaryView getSummaryView() {
		summaryView = new SummaryView(this.guiOrientation);
		return summaryView;
	} //END public ISummaryView getSummaryView()
	
	public IInfoVocabEditView getInfoVocabEditView() {
		infoVocabEditView = new InfoVocabEditView(this.getComponentOrientation());
		return infoVocabEditView;
	} //END public IInfoVocabEditView getInfoVocabEditView()
	
	public IListView getUnitsListView() {
		unitsListView = new UnitsListView();
		return unitsListView;
	} //END public IListView getUnitsListView()
	
	public IUnitEditView getUnitEditView() {
		unitEditView = new UnitEditView("Edit Unit", this.guiOrientation, MessageConstants.N_VIEW_UNITS_LIST);
		return unitEditView;
	} //END public IUnitEditView getUnitEditView()
	
	public IListView getCategoriesListView() {
		categoriesListView = new CategoriesListView();
		return categoriesListView;
	} //END public IListView getCategoriesListView()
	
	public IUnitEditView getCategoryEditView() {
		categoryEditView = new UnitEditView("Edit Unit", this.guiOrientation, MessageConstants.N_VIEW_CATEGORIES_LIST);
		return categoryEditView;
	} //ENDpublic IUnitEditView getCategoryEditView()
	
	public IEntriesListView getEntriesListView() {
		entriesListView = new EntriesListView();
		return entriesListView;
	} //END public IEntriesListView getEntriesListView()
	
	public IEntryEditView getEntryEditView() {
		entryEditView = new EntryEditView(this.guiOrientation);
		return entryEditView;
	} //END public IEntryEditView getEntryEditView()
	
	public IListView getEntryTypesListView() {
		entryTypesListView = new EntryTypesListView();
		return entryTypesListView;
	} //END public IListView getEntryTypesListView()
	
	public IEntryTypeEditView getEntryTypeEditView() {
		entryTypeEditView = new EntryTypeEditView(this.guiOrientation);
		return entryTypeEditView;
	} //END public IEntryTypeEditView getEntryTypeEditView()
	
	public IListView getEntryTypeAttributesListView() {
		entryTypeAttributesListView = new EntryTypeAttributesListView();
		return entryTypeAttributesListView;
	} //END public IListView getEntryTypeAttributesListView()
	
	public IEntryTypeAttributeEditView getEntryTypeAttributeEditView() {
		entryTypeAttributeEditView = new EntryTypeAttributeEditView(this.guiOrientation);
		return entryTypeAttributeEditView;
	} //END public IEntryTypeAttributeEditView getEntryTypeAttributeEditView()
	
	public IEntryLearnOneView getEntryLearnOneView() {
		entryLearnOneView = new EntryLearnOneView(this.guiOrientation);
		return entryLearnOneView;
	} //END public IEntryLearnOneView getEntryLearnOneView()
	
	public IEntriesSelectionView getEntriesSelectionView() {
		entriesSelectionView = new EntriesSelectionView(this.guiOrientation);
		return entriesSelectionView;
	} //END public IEntriesSelectionView getEntriesSelectionView()

	public void showView(String aView) {
		//clear the contents panel from other views
		mainP.removeAll();
		//set the panel
		if (aView.equals(MessageConstants.N_VIEW_WELCOME)) {
			mainP.add(BorderLayout.CENTER, welcomeView);
		}
		else if (aView.equals(MessageConstants.N_VIEW_STATISTICS)) {
			mainP.add(BorderLayout.CENTER, summaryView);
		}
		else if (aView.equals(MessageConstants.N_VIEW_INFOVOCAB_EDIT)) {
			mainP.add(BorderLayout.CENTER, infoVocabEditView);
		}
		else if (aView.equals(MessageConstants.N_VIEW_UNITS_LIST)) {
			mainP.add(BorderLayout.CENTER, unitsListView);
		}
		else if (aView.equals(MessageConstants.N_VIEW_UNIT_EDIT)) {
			mainP.add(BorderLayout.CENTER, unitEditView);
		}
		else if (aView.equals(MessageConstants.N_VIEW_CATEGORIES_LIST)) {
			mainP.add(BorderLayout.CENTER, categoriesListView);
		}
		else if (aView.equals(MessageConstants.N_VIEW_CATEGORY_EDIT)) {
			mainP.add(BorderLayout.CENTER, categoryEditView);
		}
		else if (aView.equals(MessageConstants.N_VIEW_ENTRIES_LIST)) {
			mainP.add(BorderLayout.CENTER, entriesListView);
		}
		else if (aView.equals(MessageConstants.N_VIEW_ENTRY_EDIT)) {
			mainP.add(BorderLayout.CENTER, entryEditView);
		}
		else if (aView.equals(MessageConstants.N_VIEW_ENTRY_LEARNONE)) {
			mainP.add(BorderLayout.CENTER, entryLearnOneView);
		}
		else if (aView.equals(MessageConstants.N_VIEW_ENTRYTYPES_LIST)) {
			mainP.add(BorderLayout.CENTER, entryTypesListView);
		}
		else if (aView.equals(MessageConstants.N_VIEW_ENTRYTYPE_EDIT)) {
			mainP.add(BorderLayout.CENTER, entryTypeEditView);
		}
		else if (aView.equals(MessageConstants.N_VIEW_ENTRYTYPE_ATTRIBUTES_LIST)) {
			mainP.add(BorderLayout.CENTER, entryTypeAttributesListView);
		}
		else if (aView.equals(MessageConstants.N_VIEW_ENTRYTYPE_ATTRIBUTE_EDIT)) {
			mainP.add(BorderLayout.CENTER, entryTypeAttributeEditView);
		}
		else if (aView.equals(MessageConstants.N_VIEW_ENTRIES_SELECTION)) {
			mainP.add(BorderLayout.CENTER, entriesSelectionView);
		}
		else {
			if (logger.isLoggable(Level.FINEST)) {
				logger.logp(Level.FINEST, this.getClass().getName(), "showView()", "View not processed: " + aView);
			}			
		}
		//make a validation
		doValidation();
	} //END public void showView(String
	
	/**
	 * Makes sure that the new view is shown properly on the screen.
	 * TODO: is repainting and validation really needed?
	 */
	private void doValidation() {
		mainP.invalidate();
		mainP.validate();
		mainP.repaint();
	} //END private void doValidation()
	
	//=========== Menus ==============================================================//

	private void initializeMenuBar() {
		menuBar = new JMenuBar();
		
		//fileMenu
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic("F".charAt(0));
		//----
		newMI = new JMenuItem("New", Constants.createImageIcon(Constants.IMG_NEW_16, "FIXME"));
		newMI.setMnemonic("N".charAt(0));
		newMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		newMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.STATUS_EVENT);
				ape.setMessage(MessageConstants.S_NEW_VOCABULARY);
				parentController.handleAppEvent(ape);
			}
		});
		//----
		openMI = new JMenuItem("Open ...", Constants.createImageIcon(Constants.IMG_OPEN_16, "FIXME"));
		openMI.setMnemonic("O".charAt(0));
		openMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		openMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.STATUS_EVENT);
				ape.setMessage(MessageConstants.S_OPEN_VOCABULARY);
				parentController.handleAppEvent(ape);
			}
		});
		//----
		saveMI = new JMenuItem("Save", Constants.createImageIcon(Constants.IMG_SAVE_16, "FIXME"));
		saveMI.setMnemonic("S".charAt(0));
		saveMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.STATUS_EVENT);
				ape.setMessage(MessageConstants.S_SAVE_VOCABULARY);
				parentController.handleAppEvent(ape);
			}
		});
		//----
		saveAsMI = new JMenuItem("Save As ...", Constants.createImageIcon(Constants.IMG_SAVE_AS_16, "FIXME"));
		saveAsMI.setMnemonic("A".charAt(0));
		saveAsMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.SHIFT_MASK + ActionEvent.CTRL_MASK));
		saveAsMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.STATUS_EVENT);
				ape.setMessage(MessageConstants.S_SAVE_AS_VOCABULARY);
				parentController.handleAppEvent(ape);
			}
		});
		//----
		propertiesMI = new JMenuItem("Properties", Constants.createImageIcon(Constants.IMG_PROPERTIES_16, "FIXME"));
		propertiesMI.setMnemonic("P".charAt(0));
		propertiesMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.NAV_EVENT);
				ape.setMessage(MessageConstants.N_VIEW_INFOVOCAB_EDIT);
				parentController.handleAppEvent(ape);
			}
		});
		//----
		closeMI = new JMenuItem("Close", Constants.createImageIcon(Constants.IMG_CLOSE_16, "FIXME"));
		closeMI.setMnemonic("C".charAt(0));
		closeMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		closeMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.STATUS_EVENT);
				ape.setMessage(MessageConstants.S_CLOSE_VOCABULARY);
				parentController.handleAppEvent(ape);
			}
		});
		//----
		quitMI = new JMenuItem("Quit", Constants.createImageIcon(Constants.IMG_EXIT_16, "FIXME"));
		quitMI.setMnemonic("Q".charAt(0));
		quitMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		quitMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.STATUS_EVENT);
				ape.setMessage(MessageConstants.S_EXIT_APPLICATION);
				parentController.handleAppEvent(ape);
			}
		});
		addDefaultsToFileMenu();

		//editMenu
		editMenu = new JMenu("Edit");
		editMenu.setMnemonic("E".charAt(0));
		//----
		preferencesMI = new JMenuItem("Preferences", Constants.createImageIcon(Constants.IMG_PREFERENCES_16, "FIXME"));
		preferencesMI.setMnemonic("P".charAt(0));
		editMenu.add(preferencesMI);
		preferencesMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				showPreferences();
			}
		});


		//goMenu
		goMenu = new JMenu("Go");
		goMenu.setMnemonic("G".charAt(0));
		//----
		learnOneByOneMI = new JMenuItem("Learn One by One");
		learnOneByOneMI.setMnemonic("O".charAt(0));
		learnOneByOneMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.NAV_EVENT);
				ape.setMessage(MessageConstants.N_VIEW_ENTRY_LEARNONE);
				parentController.handleAppEvent(ape);
			}
		});
		goMenu.add(learnOneByOneMI);
		//----
		goMenu.addSeparator();
		//----
		entriesMI = new JMenuItem("Edit Entries");
		entriesMI.setMnemonic("E".charAt(0));
		entriesMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.NAV_EVENT);
				ape.setMessage(MessageConstants.N_VIEW_ENTRIES_LIST);
				parentController.handleAppEvent(ape);
			}
		});
		goMenu.add(entriesMI);
		//----
		unitsMI = new JMenuItem("Edit Units");
		unitsMI.setMnemonic("U".charAt(0));
		unitsMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.NAV_EVENT);
				ape.setMessage(MessageConstants.N_VIEW_UNITS_LIST);
				parentController.handleAppEvent(ape);
			}
		});
		goMenu.add(unitsMI);
		//----
		categoriesMI = new JMenuItem("Edit Categories");
		categoriesMI.setMnemonic("C".charAt(0));
		categoriesMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.NAV_EVENT);
				ape.setMessage(MessageConstants.N_VIEW_CATEGORIES_LIST);
				parentController.handleAppEvent(ape);
			}
		});
		goMenu.add(categoriesMI);
		//----
		entryTypesMI = new JMenuItem("Edit Entry Types");
		entryTypesMI.setMnemonic("T".charAt(0));
		entryTypesMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.NAV_EVENT);
				ape.setMessage(MessageConstants.N_VIEW_ENTRYTYPES_LIST);
				parentController.handleAppEvent(ape);
			}
		});
		goMenu.add(entryTypesMI);
		entryTypeAttributesMI = new JMenuItem("Edit Entry Type Attributes");
		entryTypeAttributesMI.setMnemonic("A".charAt(0));
		entryTypeAttributesMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.NAV_EVENT);
				ape.setMessage(MessageConstants.N_VIEW_ENTRYTYPE_ATTRIBUTES_LIST);
				parentController.handleAppEvent(ape);
			}
		});
		goMenu.add(entryTypeAttributesMI);
		//----
		goMenu.addSeparator();
		//----
		statisticsMI = new JMenuItem("Display Statistics");
		statisticsMI.setMnemonic("D".charAt(0));
		statisticsMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.NAV_EVENT);
				ape.setMessage(MessageConstants.N_VIEW_STATISTICS);
				parentController.handleAppEvent(ape);
			}
		});
		goMenu.add(statisticsMI);

		
		//tools menu
		toolsMenu = new JMenu("Tools");
		toolsMenu.setMnemonic("T".charAt(0));
		//----
		selectMI = new JMenuItem("Select Entries");
		selectMI.setMnemonic("S".charAt(0));
		selectMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.NAV_EVENT);
				ape.setMessage(MessageConstants.N_VIEW_ENTRIES_SELECTION);
				parentController.handleAppEvent(ape);
			}
		});
		toolsMenu.add(selectMI);
		//----
		toolsMenu.addSeparator();
		//----
		resetScoreAllMI = new JMenuItem("Reset Score All");
		resetScoreAllMI.setMnemonic("A".charAt(0));
		resetScoreAllMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.DATA_EVENT);
				ape.setMessage(MessageConstants.D_ENTRIES_RESET_SCORE_ALL);
				parentController.handleAppEvent(ape);
			}
		});
		toolsMenu.add(resetScoreAllMI);
		//----
		resetScoreSelMI = new JMenuItem("Reset Score Current Selection");
		resetScoreSelMI.setMnemonic("C".charAt(0));
		resetScoreSelMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.DATA_EVENT);
				ape.setMessage(MessageConstants.D_ENTRIES_RESET_SCORE_SEL);
				parentController.handleAppEvent(ape);
			}
		});
		toolsMenu.add(resetScoreSelMI);
		//----
		toolsMenu.addSeparator();
		//----
		saveStatsMI = new JMenuItem("Save Current Learning Statistics");
		saveStatsMI.setMnemonic("L".charAt(0));
		saveStatsMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.DATA_EVENT);
				ape.setMessage(MessageConstants.D_STATISTICS_SAVE_SET);
				parentController.handleAppEvent(ape);
			}
		});
		toolsMenu.add(saveStatsMI);


		//helpMenu
		helpMenu = new JMenu("Help");
		helpMenu.setMnemonic("H".charAt(0));
		//----
		contentsMI = new JMenuItem("Contents", Constants.createImageIcon(Constants.IMG_HELP_16, "FIXME"));
		contentsMI.setMnemonic("C".charAt(0));
		//contentsMI.setAccelerator(KeyStroke.getKeyStrokeForEvent(KeyEvent.VK_F1));
		contentsMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.HELP_EVENT);
				ape.setMessage(MessageConstants.H_ALL);
				parentController.handleAppEvent(ape);
			}
		});
		helpMenu.add(contentsMI);
		//----
		aboutMI = new JMenuItem("About", Constants.createImageIcon(Constants.IMG_ABOUT_16, "FIXME"));
		aboutMI.setMnemonic("A".charAt(0));
		aboutMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.HELP_EVENT);
				ape.setMessage(MessageConstants.H_ABOUT);
				parentController.handleAppEvent(ape);
			}
		});
		helpMenu.add(aboutMI);

		//put all into menu bar
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(goMenu);
		menuBar.add(toolsMenu);
		menuBar.add(helpMenu);
	} //END private void initializeMenuBar()
	
	private void addDefaultsToFileMenu() {
		//remove all existing
		fileMenu.removeAll();
		//add the defaults incl. separators
		fileMenu.add(newMI);
		fileMenu.add(openMI);
		fileMenu.addSeparator();
		fileMenu.add(saveMI);
		fileMenu.add(saveAsMI);
		fileMenu.addSeparator();
		fileMenu.add(propertiesMI);
		fileMenu.addSeparator();
		fileMenu.add(closeMI);
		fileMenu.add(quitMI);
	} //END private void addDefaultsToFileMenu()
	
	//implements IDingsMainWindow
	public void setSaveMIEnabled(boolean aStatus) {
		saveMI.setEnabled(aStatus);
	} //END public void setSaveMIEnabled(boolean)
	
	//implements IDingsMainWindow
	public void setOpenVocabEnabled(boolean aStatus) {
		saveAsMI.setEnabled(aStatus);
		closeMI.setEnabled(aStatus);
		unitsMI.setEnabled(aStatus);
		categoriesMI.setEnabled(aStatus);
		entryTypesMI.setEnabled(aStatus);
		entryTypeAttributesMI.setEnabled(aStatus);
		propertiesMI.setEnabled(aStatus);
		statisticsMI.setEnabled(aStatus);
	} //END public void setOpenVocabEnabled(boolean)
	
	//implements IDingsMainWindow
	public void setEntriesMIEnabled(boolean aStatus) {
		entriesMI.setEnabled(aStatus);
	} //END public void setEntriesMIEnabled(boolean)
	
	//implements IDingsMainWindow
	public void setEntriesOkEnabled(boolean aStatus) {
		//searchMI.setEnabled(aStatus);
		selectMI.setEnabled(aStatus);
		saveStatsMI.setEnabled(aStatus);
	} //END public void setEntriesOkEnabled(boolean)
	
	public void setResetScoreMIsEnabled(boolean aStatus) {
		resetScoreAllMI.setEnabled(aStatus);
		resetScoreSelMI.setEnabled(aStatus);		
	} //END public void setResetScoreMIsEnabled(boolean)
	
	//implements IDingsMainWindow
	public void setLearningMIsEnabled(boolean aLearnOneStatus) {
		learnOneByOneMI.setEnabled(aLearnOneStatus);		
	} //END ppublic void setLearningMIsEnabled(boolean)
	
	//implements IDingsMainWindow
	public void setFileHistory(String[][] aFileHistory) {
		if (aFileHistory.length > 0) {
			//remove existing and add default MenuItems
			addDefaultsToFileMenu();
			//add a separator
			fileMenu.addSeparator();
			//add the file history menu items
			String menuText;
			for (int i = 0; i < aFileHistory.length; i++) {
				final String path = aFileHistory[i][0];
				menuText = Integer.toString(i + 1) + " " + aFileHistory[i][1];
				FileHistoryMenuItem fooMI = new FileHistoryMenuItem(menuText);
				fooMI.setMnemonic(Integer.toString(i +1).charAt(0));
				fooMI.setToolTipText(path);
				fooMI.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						AppEvent ape = new AppEvent(AppEvent.STATUS_EVENT);
						ape.setMessage(MessageConstants.S_OPEN_VOCABULARY);
						ape.setDetails(path);
						parentController.handleAppEvent(ape);
					}
				});
				fileMenu.add(fooMI);
			}
		}
		//else do nothing
	} //END public void setFileHistory(String[][])
	
	/**
	 * A special MenuItem that displays the whole file path
	 * in the tooltip text.
	 * This inner class reuses some of the code in a class presented
	 * in <a href="https://www.javaworld.com/javatips/jw-javatip119_p.html"">JavaWorld Java Tip 119</a>.
	 */
	private final class FileHistoryMenuItem extends JMenuItem {
		public FileHistoryMenuItem(String aText) {
			super(aText);
		} //END public FileHistoryMenuItem(String)
		
		//overrides JMenuItem
		public Point getToolTipLocation(MouseEvent evt) {
			Graphics g = getGraphics();
			FontMetrics fm = g.getFontMetrics(g.getFont());
			int prefixWidth = fm.stringWidth("8"); //to simulate the width of a digit. We assume file history length < 10
			int x = SwingConstants.TRAILING + SwingConstants.LEADING -1 + prefixWidth;
			return new Point(x,0);
		} //END public Point getToolTipLocation(MouseEvent)
	} //private final class FileHistoryMenuItem extends JMenuItem
	
} //End public class ADings
