/*
 * MainWindow.java
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

/**
 * This class is the main entry for ADings, which is a vocabulary trainer
 * program written in Java. It uses XML as its native format to store the
 * vocabularies.
 *
 * @author vanosten
 */
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

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
import net.vanosten.dings.consts.MessageConstants.Message;
import net.vanosten.dings.event.AppEvent;
import net.vanosten.dings.model.Preferences;
import net.vanosten.dings.present.PreferencesPresenter;
import net.vanosten.dings.utils.Toolbox;
import net.vanosten.dings.swing.helperui.XMLFileFilter;
import net.vanosten.dings.swing.helperui.StatusBar;
import net.vanosten.dings.uiif.*;


//Logging with log4J
import java.util.logging.Logger;
import java.util.logging.Level;

public class MainWindow extends JFrame implements IDingsMainWindow {
	private final static long serialVersionUID = 1L;

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
	private LearnByChoiceView learnByChoiceView;

	private IAppEventHandler parentController;

	private JMenuBar menuBar;
	private StatusBar statusBar;

	//The menus
	private JMenu fileMenu;
	private JMenu editMenu;
	private JMenu learnMenu;
	private JMenu toolsMenu;
	private JMenu helpMenu;

	//The menu items for fileMenu
	private JMenuItem newMI;    //not implemented yet: starts a new vocabulary file
	//should there also be a new based on an existing??
	private JMenuItem openMI;   //opens a vocabulary file
	private JMenuItem saveMI;   //saves the current vocabulary into the opened file
	private JMenuItem saveAsMI; //opens a file dialog to save vocabulary to a new file
	private JMenuItem infoMI;   //a view to edit the vocabulary properties
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
	private JMenuItem learnByChoiceMI;

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
	public MainWindow(IAppEventHandler aController) {
		super.setComponentOrientation(ComponentOrientation.getOrientation(Toolbox.getInstance().getCurrentLocalePointer()));
		//set pointers
		Preferences preferences = Toolbox.getInstance().getPreferencesPointer();
		this.parentController = aController;
		this.guiOrientation = ComponentOrientation.getOrientation(Toolbox.getInstance().getCurrentLocalePointer());

		//prepare look & feel
		setLookAndFeel();

		//prepare show
		int windowWidth  = 800;
		int windowHeight = 600;
		int locationX = 0;
		int locationY = 0;
		if (preferences.containsKey(Preferences.PROP_APP_WINDOW_WIDTH) && preferences.containsKey(Preferences.PROP_APP_WINDOW_HEIGHT)) {
			try {
				windowWidth = preferences.getIntProperty(Preferences.PROP_APP_WINDOW_WIDTH);
				windowHeight = preferences.getIntProperty(Preferences.PROP_APP_WINDOW_HEIGHT);
				locationX = preferences.getIntProperty(Preferences.PROP_APP_LOCATION_X);
				locationY = preferences.getIntProperty(Preferences.PROP_APP_LOCATION_Y);
			}
			catch (NumberFormatException e) {
				//do nothing because default values ok
			}
		}
		setBounds(locationX, locationY, windowWidth, windowHeight);

		//define the icon
		ImageIcon icon = DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_DINGS_32, "logo of DingsBums?!");
		if (null != icon) {
			this.setIconImage(icon.getImage());
		}

		//define closing behavior
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				AppEvent ape = new AppEvent(AppEvent.EventType.STATUS_EVENT);
				ape.setMessage(MessageConstants.Message.S_EXIT_APPLICATION);
				parentController.handleAppEvent(ape);
			}
		});

		//set the layout manager
		mainP = new JPanel();
		mainP.setLayout(new BorderLayout());
		//set the font sizes
		DingsSwingConstants.setFontSizes(mainP.getFont());

		this.getContentPane().add(mainP, BorderLayout.CENTER);

		//status bar
		statusBar = new StatusBar();
		this.getContentPane().add(statusBar, BorderLayout.PAGE_END);
		//key
		KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);
		mainP.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, "HELP");
		mainP.getActionMap().put("HELP", new AbstractAction() {
			public void actionPerformed(ActionEvent evt) {
				showHelp(MessageConstants.Message.H_ALL);
			}
		});

		//add the menu bar
		initializeMenuBar();
		setFileHistory(preferences.getFileHistoryPaths());
		setJMenuBar(menuBar);

		this.applyComponentOrientation(this.guiOrientation);
		//show the frame
		//Do not pack() to have the size correctly set
		this.setVisible(true);
	} //END public MainWindow()

	//implements IDingsMainWindow
	public void setLookAndFeel() {
		try {
			String systemLAF = UIManager.getSystemLookAndFeelClassName();
			if (logger.isLoggable(Level.FINEST)) {
				logger.logp(Level.FINEST, this.getClass().getName(), "MainWindow()", "System look and feel class name: " + systemLAF);
			}
			if (Toolbox.getInstance().getPreferencesPointer().getBooleanProperty(Preferences.PROP_SYSTEM_LAF)) {
				UIManager.setLookAndFeel(systemLAF);
			} else {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}
			SwingUtilities.updateComponentTreeUI(this);
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
	} //END public void setLookAndFeel()

	//implements IDingsMainWindow
	public void saveWindowLocationAndSize() {
		Dimension d = this.getSize();
		Point p = this.getLocationOnScreen();
		Preferences preferences = Toolbox.getInstance().getPreferencesPointer(); //locale Preferences pointer
		preferences.setIntProperty(Preferences.PROP_APP_WINDOW_WIDTH, d.width);
		preferences.setIntProperty(Preferences.PROP_APP_WINDOW_HEIGHT, d.height);
		preferences.setIntProperty(Preferences.PROP_APP_LOCATION_X, p.x);
		preferences.setIntProperty(Preferences.PROP_APP_LOCATION_Y, p.y);
	} //END public void saveWindowLocationAndSize()

	/**
	 * Shows the preferences dialog.
	 */
	private void showPreferences() {
		PreferencesEditView preferencesEditView = new PreferencesEditView(this, this.guiOrientation);
		PreferencesPresenter presenter = new PreferencesPresenter();
		presenter.setParentController(Toolbox.getInstance().getDings());
		int dialogWidth = 500;
		int dialogHeight = 400;
		Preferences preferences = Toolbox.getInstance().getPreferencesPointer(); //locale Preferences pointer
		if (preferences.containsKey(Preferences.PROP_PREF_DIALOG_WIDTH) && preferences.containsKey(Preferences.PROP_PREF_DIALOG_HEIGHT)) {
			try {
				dialogWidth = preferences.getIntProperty(Preferences.PROP_PREF_DIALOG_WIDTH);
				dialogHeight = preferences.getIntProperty(Preferences.PROP_PREF_DIALOG_HEIGHT);
			}
			catch (NumberFormatException e) {
				//if exception do nothing, because default width will be taken
			}
		}
		preferencesEditView.setSize(dialogWidth, dialogHeight);
		presenter.setEditView(preferencesEditView);
		preferencesEditView.init(presenter);
		preferencesEditView.setLocationRelativeTo(this);
		preferencesEditView.setVisible(true);
	} //END private void showPreferences()

	//implements IDingsMainWindow
	public void showHelp(Message screen) {
		if (screen == MessageConstants.Message.H_ALL) {
			//JOptionPane.showMessageDialog(this, "Sorry, not implemented yet!", "Help", JOptionPane.INFORMATION_MESSAGE);
			showMessageDialog("Help", "Sorry, not implemented yet!\nHave a look at the website http://dingsbums.vanosten.net/", Constants.INFORMATION_MESSAGE);
		} else {
			//JOptionPane.showMessageDialog(this, "DingsBums?! by vanosten", "About", JOptionPane.INFORMATION_MESSAGE);
			showMessageDialog("About", "DingsBums?! by vanosten\ndingsbums@vanosten.net", Constants.INFORMATION_MESSAGE);
		}
	} //END public void showHelp(Message)

	//implements IDingsMainWindow
	public String showFileChooser(String aFileName, boolean showOpen) {
		JFileChooser fc = new JFileChooser(aFileName);
		fc.setComponentOrientation(guiOrientation);
		if (showOpen) {
			fc.setDialogTitle("Open Learning Stack File");
		}
		else {
			fc.setDialogTitle("Save Learning Stack File As");
		}
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
		JOptionPane.showMessageDialog(this, aMessage, aTitle
				, getSwingMessageType(aMessageType)
				, DingsSwingConstants.getIconForMessageType(aMessageType));
	} //END public void showMessageDialog(String, String, int)

	//implements IDingsMainWindow
	public int showOptionDialog(String aTitle, String aMessage, int aMessageType, int anOptionType) {
		Object[] options; //the buttons to be displayed
		if (Constants.YES_NO_CANCEL_OPTION == anOptionType) {
			options = new Object[3];
			options[0] = Toolbox.getInstance().getLocalizedString("label.button.yes");
			options[1] = Toolbox.getInstance().getLocalizedString("label.button.no");
			options[2] = Toolbox.getInstance().getLocalizedString("label.button.cancel");
		}
		else {
			options = new Object[2];
			options[0] = Toolbox.getInstance().getLocalizedString("label.button.yes");
			options[1] = Toolbox.getInstance().getLocalizedString("label.button.no");
		}
		int answer = JOptionPane.showOptionDialog(this
						, aMessage
						, aTitle
						, getSwingOptionType(anOptionType)
						, getSwingMessageType(aMessageType)
						, DingsSwingConstants.getIconForMessageType(aMessageType)
						, options //the titles of buttons
						, options[0]); //default button title
		return getDingsAnswer(answer);
	} //END public int showOptionDialog(String, String, int, int)
	
	//implements IDingsMainWindow
	public boolean showLearningDirectionDialog() {
		Object[] options = {
			Toolbox.getInstance().getLocalizedString("label.button.yes")
			, Toolbox.getInstance().getLocalizedString("label.button.cancel")
		};
		JPanel myPanel = new JPanel();
		JLabel textL = new JLabel(Toolbox.getInstance().getLocalizedString("learndir.dialog.text"));
		JRadioButton baseTargetRB = new JRadioButton(Toolbox.getInstance().getLocalizedString("learndir.basetarget.label"));
		baseTargetRB.setMnemonic(Toolbox.getInstance().getLocalizedString("learndir.basetarget.mnemonic").charAt(0));
		JRadioButton targetBaseRB = new JRadioButton(Toolbox.getInstance().getLocalizedString("learndir.targetbase.label"));
		targetBaseRB.setMnemonic(Toolbox.getInstance().getLocalizedString("learndir.targetbase.mnemonic").charAt(0));
		ButtonGroup directionBG = new ButtonGroup();
		directionBG.add(baseTargetRB);
		directionBG.add(targetBaseRB);
		baseTargetRB.setSelected(true);
		GroupLayout layout = new GroupLayout(myPanel);
		myPanel.setLayout(layout);
		layout.setHorizontalGroup(layout.createSequentialGroup()
			.add(layout.createParallelGroup(GroupLayout.LEADING).add(textL).add(baseTargetRB).add(targetBaseRB))
		);
		layout.setVerticalGroup(layout.createSequentialGroup()
			.add(layout.createParallelGroup(GroupLayout.BASELINE).add(textL))
			.addPreferredGap(LayoutStyle.UNRELATED)
			.add(layout.createParallelGroup(GroupLayout.BASELINE).add(baseTargetRB))
			.addPreferredGap(LayoutStyle.RELATED)
			.add(layout.createParallelGroup(GroupLayout.BASELINE).add(targetBaseRB))
		);

		int answer = JOptionPane.showOptionDialog(this
					, myPanel
					, Toolbox.getInstance().getLocalizedString("learndir.dialog.title")
					, JOptionPane.OK_CANCEL_OPTION
					, JOptionPane.QUESTION_MESSAGE
					, DingsSwingConstants.getIconForMessageType(Constants.QUESTION_MESSAGE)
					, options
					, options[0]);
		if (JOptionPane.CANCEL_OPTION == answer) {
			return false;
		}
		//set the learning direction
		Toolbox.getInstance().setTargetAsked(baseTargetRB.isSelected());
		return true;
	}

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
		this.setVisible(false);
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
		unitsListView = new UnitsListView(this.getComponentOrientation());
		return unitsListView;
	} //END public IListView getUnitsListView()

	public IUnitEditView getUnitEditView() {
		unitEditView = new UnitEditView(Toolbox.getInstance().getLocalizedString("viewtitle.edit_unit")
				, this.guiOrientation
				, MessageConstants.Message.N_VIEW_UNITS_LIST);
		return unitEditView;
	} //END public IUnitEditView getUnitEditView()

	public IListView getCategoriesListView() {
		categoriesListView = new CategoriesListView(this.getComponentOrientation());
		return categoriesListView;
	} //END public IListView getCategoriesListView()

	public IUnitEditView getCategoryEditView() {
		categoryEditView = new UnitEditView(Toolbox.getInstance().getLocalizedString("viewtitle.edit_category")
				, this.guiOrientation
				, MessageConstants.Message.N_VIEW_CATEGORIES_LIST);
		return categoryEditView;
	} //ENDpublic IUnitEditView getCategoryEditView()

	public IEntriesListView getEntriesListView() {
		entriesListView = new EntriesListView(this.getComponentOrientation());
		return entriesListView;
	} //END public IEntriesListView getEntriesListView()

	public IEntryEditView getEntryEditView() {
		entryEditView = new EntryEditView(this.guiOrientation);
		return entryEditView;
	} //END public IEntryEditView getEntryEditView()

	public IListView getEntryTypesListView() {
		entryTypesListView = new EntryTypesListView(this.getComponentOrientation());
		return entryTypesListView;
	} //END public IListView getEntryTypesListView()

	public IEntryTypeEditView getEntryTypeEditView() {
		entryTypeEditView = new EntryTypeEditView(this.guiOrientation);
		return entryTypeEditView;
	} //END public IEntryTypeEditView getEntryTypeEditView()

	public IListView getEntryTypeAttributesListView() {
		entryTypeAttributesListView = new EntryTypeAttributesListView(this.getComponentOrientation());
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

	public ILearnByChoiceView getLearnByChoiceView() {
		learnByChoiceView = new LearnByChoiceView(this.guiOrientation);
		return learnByChoiceView;
	} //END public ILearnByChoiceView getLearnByChoiceView()

	public void showView(Message aView) {
		//clear the contents panel from other views
		mainP.removeAll();
		//set the panel
		if (aView == MessageConstants.Message.N_VIEW_WELCOME) {
			mainP.add(BorderLayout.CENTER, welcomeView);
		}
		else if (aView == MessageConstants.Message.N_VIEW_STATISTICS) {
			mainP.add(BorderLayout.CENTER, summaryView);
		}
		else if (aView == MessageConstants.Message.N_VIEW_INFOVOCAB_EDIT) {
			mainP.add(BorderLayout.CENTER, infoVocabEditView);
		}
		else if (aView == MessageConstants.Message.N_VIEW_UNITS_LIST) {
			mainP.add(BorderLayout.CENTER, unitsListView);
		}
		else if (aView == MessageConstants.Message.N_VIEW_UNIT_EDIT) {
			mainP.add(BorderLayout.CENTER, unitEditView);
		}
		else if (aView == MessageConstants.Message.N_VIEW_CATEGORIES_LIST) {
			mainP.add(BorderLayout.CENTER, categoriesListView);
		}
		else if (aView == MessageConstants.Message.N_VIEW_CATEGORY_EDIT) {
			mainP.add(BorderLayout.CENTER, categoryEditView);
		}
		else if (aView == MessageConstants.Message.N_VIEW_ENTRIES_LIST) {
			mainP.add(BorderLayout.CENTER, entriesListView);
		}
		else if (aView == MessageConstants.Message.N_VIEW_ENTRY_EDIT) {
			mainP.add(BorderLayout.CENTER, entryEditView);
		}
		else if (aView == MessageConstants.Message.N_VIEW_ENTRY_LEARNONE) {
			mainP.add(BorderLayout.CENTER, entryLearnOneView);
		}
		else if (aView == MessageConstants.Message.N_VIEW_ENTRYTYPES_LIST) {
			mainP.add(BorderLayout.CENTER, entryTypesListView);
		}
		else if (aView == MessageConstants.Message.N_VIEW_ENTRYTYPE_EDIT) {
			mainP.add(BorderLayout.CENTER, entryTypeEditView);
		}
		else if (aView == MessageConstants.Message.N_VIEW_ENTRYTYPE_ATTRIBUTES_LIST) {
			mainP.add(BorderLayout.CENTER, entryTypeAttributesListView);
		}
		else if (aView == MessageConstants.Message.N_VIEW_ENTRYTYPE_ATTRIBUTE_EDIT) {
			mainP.add(BorderLayout.CENTER, entryTypeAttributeEditView);
		}
		else if (aView == MessageConstants.Message.N_VIEW_ENTRIES_SELECTION) {
			mainP.add(BorderLayout.CENTER, entriesSelectionView);
		}
		else if (aView == MessageConstants.Message.N_VIEW_LEARN_BY_CHOICE) {
			mainP.add(BorderLayout.CENTER, learnByChoiceView);
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
		fileMenu = new JMenu(Toolbox.getInstance().getLocalizedString("label.menu.file"));
		fileMenu.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menu.file").charAt(0));
		//----
		newMI = new JMenuItem(Toolbox.getInstance().getLocalizedString("label.menuitem.file_new"));
		newMI.setIcon(DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_NEW_MI, "FIXME"));
		newMI.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menuitem.file_new").charAt(0));
		newMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		newMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.EventType.STATUS_EVENT);
				ape.setMessage(MessageConstants.Message.S_NEW_VOCABULARY);
				parentController.handleAppEvent(ape);
			}
		});
		//----
		openMI = new JMenuItem(Toolbox.getInstance().getLocalizedString("label.menuitem.file_open"));
		openMI.setIcon(DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_OPEN_MI, "FIXME"));
		openMI.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menuitem.file_open").charAt(0));
		openMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		openMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.EventType.STATUS_EVENT);
				ape.setMessage(MessageConstants.Message.S_OPEN_VOCABULARY);
				parentController.handleAppEvent(ape);
			}
		});
		//----
		saveMI = new JMenuItem(Toolbox.getInstance().getLocalizedString("label.menuitem.file_save"));
		saveMI.setIcon(DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_SAVE_MI, "FIXME"));
		saveMI.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menuitem.file_save").charAt(0));
		saveMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.EventType.STATUS_EVENT);
				ape.setMessage(MessageConstants.Message.S_SAVE_VOCABULARY);
				parentController.handleAppEvent(ape);
			}
		});
		//----
		saveAsMI = new JMenuItem(Toolbox.getInstance().getLocalizedString("label.menuitem.file_save_as"));
		saveAsMI.setIcon(DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_SAVE_AS_MI, "FIXME"));
		saveAsMI.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menuitem.file_save_as").charAt(0));
		saveAsMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.SHIFT_MASK + ActionEvent.CTRL_MASK));
		saveAsMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.EventType.STATUS_EVENT);
				ape.setMessage(MessageConstants.Message.S_SAVE_AS_VOCABULARY);
				parentController.handleAppEvent(ape);
			}
		});
		//----
		closeMI = new JMenuItem(Toolbox.getInstance().getLocalizedString("label.menuitem.file_close"));
		closeMI.setIcon(DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_CLOSE_MI, "FIXME"));
		closeMI.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menuitem.file_close").charAt(0));
		closeMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		closeMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.EventType.STATUS_EVENT);
				ape.setMessage(MessageConstants.Message.S_CLOSE_VOCABULARY);
				parentController.handleAppEvent(ape);
			}
		});
		//----
		quitMI = new JMenuItem(Toolbox.getInstance().getLocalizedString("label.menuitem.file_quit"));
		quitMI.setIcon(DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_EXIT_MI, "FIXME"));
		quitMI.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menuitem.file_quit").charAt(0));
		quitMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		quitMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.EventType.STATUS_EVENT);
				ape.setMessage(MessageConstants.Message.S_EXIT_APPLICATION);
				parentController.handleAppEvent(ape);
			}
		});
		addDefaultsToFileMenu();

		//editMenu
		editMenu = new JMenu(Toolbox.getInstance().getLocalizedString("label.menu.edit"));
		editMenu.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menu.edit").charAt(0));
		//----
		entriesMI = new JMenuItem(Toolbox.getInstance().getLocalizedString("label.menuitem.edit_edit_entries"));
		entriesMI.setIcon(DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_EMPTY_MI, "FIXME"));
		entriesMI.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menuitem.edit_edit_entries").charAt(0));
		entriesMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.EventType.NAV_EVENT);
				ape.setMessage(MessageConstants.Message.N_VIEW_ENTRIES_LIST);
				parentController.handleAppEvent(ape);
			}
		});
		editMenu.add(entriesMI);
		//----
		unitsMI = new JMenuItem(Toolbox.getInstance().getLocalizedString("label.menuitem.edit_edit_units"));
		unitsMI.setIcon(DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_EMPTY_MI, "FIXME"));
		unitsMI.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menuitem.edit_edit_units").charAt(0));
		unitsMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.EventType.NAV_EVENT);
				ape.setMessage(MessageConstants.Message.N_VIEW_UNITS_LIST);
				parentController.handleAppEvent(ape);
			}
		});
		editMenu.add(unitsMI);
		//----
		categoriesMI = new JMenuItem(Toolbox.getInstance().getLocalizedString("label.menuitem.edit_edit_categories"));
		categoriesMI.setIcon(DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_EMPTY_MI, "FIXME"));
		categoriesMI.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menuitem.edit_edit_categories").charAt(0));
		categoriesMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.EventType.NAV_EVENT);
				ape.setMessage(MessageConstants.Message.N_VIEW_CATEGORIES_LIST);
				parentController.handleAppEvent(ape);
			}
		});
		editMenu.add(categoriesMI);
		//----
		entryTypesMI = new JMenuItem(Toolbox.getInstance().getLocalizedString("label.menuitem.edit_edit_entry_types"));
		entryTypesMI.setIcon(DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_EMPTY_MI, "FIXME"));
		entryTypesMI.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menuitem.edit_edit_entry_types").charAt(0));
		entryTypesMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.EventType.NAV_EVENT);
				ape.setMessage(MessageConstants.Message.N_VIEW_ENTRYTYPES_LIST);
				parentController.handleAppEvent(ape);
			}
		});
		editMenu.add(entryTypesMI);
		//----
		entryTypeAttributesMI = new JMenuItem(Toolbox.getInstance().getLocalizedString("label.menuitem.edit_edit_entry_type_attributes"));
		entryTypeAttributesMI.setIcon(DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_EMPTY_MI, "FIXME"));
		entryTypeAttributesMI.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menuitem.edit_edit_entry_type_attributes").charAt(0));
		entryTypeAttributesMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.EventType.NAV_EVENT);
				ape.setMessage(MessageConstants.Message.N_VIEW_ENTRYTYPE_ATTRIBUTES_LIST);
				parentController.handleAppEvent(ape);
			}
		});
		editMenu.add(entryTypeAttributesMI);
		//----
		editMenu.addSeparator();
		//----
		infoMI = new JMenuItem(Toolbox.getInstance().getLocalizedString("label.menuitem.edit_edit_properties"));
		infoMI.setIcon(DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_INFO_MI, "FIXME"));
		infoMI.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menuitem.edit_edit_properties").charAt(0));
		infoMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.EventType.NAV_EVENT);
				ape.setMessage(MessageConstants.Message.N_VIEW_INFOVOCAB_EDIT);
				parentController.handleAppEvent(ape);
			}
		});
		editMenu.add(infoMI);
		//----
		editMenu.addSeparator();
		//----
		preferencesMI = new JMenuItem(Toolbox.getInstance().getLocalizedString("label.menuitem.edit_edit_preferences"));
		preferencesMI.setIcon(DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_PREFERENCES_MI, "FIXME"));
		preferencesMI.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menuitem.edit_edit_preferences").charAt(0));
		preferencesMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				showPreferences();
			}
		});
		editMenu.add(preferencesMI);

		//learn menu
		learnMenu = new JMenu(Toolbox.getInstance().getLocalizedString("label.menu.learn"));
		learnMenu.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menu.learn").charAt(0));
		//----
		learnOneByOneMI = new JMenuItem(Toolbox.getInstance().getLocalizedString("label.menuitem.learn_one"));
		learnOneByOneMI.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menuitem.learn_one").charAt(0));
		learnOneByOneMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.EventType.NAV_EVENT);
				ape.setMessage(MessageConstants.Message.N_VIEW_ENTRY_LEARNONE);
				parentController.handleAppEvent(ape);
			}
		});
		learnByChoiceMI = new JMenuItem(Toolbox.getInstance().getLocalizedString("label.menuitem.learn_by_choice"));
		learnByChoiceMI.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menuitem.learn_by_choice").charAt(0));
		learnByChoiceMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.EventType.NAV_EVENT);
				ape.setMessage(MessageConstants.Message.N_VIEW_LEARN_BY_CHOICE);
				parentController.handleAppEvent(ape);
			}
		});
		learnMenu.add(learnOneByOneMI);
		learnMenu.add(learnByChoiceMI);

		//tools menu
		toolsMenu = new JMenu(Toolbox.getInstance().getLocalizedString("label.menu.tools"));
		toolsMenu.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menu.tools").charAt(0));
		//----
		selectMI = new JMenuItem(Toolbox.getInstance().getLocalizedString("label.menuitem.tools_select_entries"));
		selectMI.setIcon(DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_EMPTY_MI, "FIXME"));
		selectMI.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menuitem.tools_select_entries").charAt(0));
		selectMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.EventType.NAV_EVENT);
				ape.setMessage(MessageConstants.Message.N_VIEW_ENTRIES_SELECTION);
				parentController.handleAppEvent(ape);
			}
		});
		toolsMenu.add(selectMI);
		//----
		toolsMenu.addSeparator();
		//----
		resetScoreAllMI = new JMenuItem(Toolbox.getInstance().getLocalizedString("label.menuitem.tools_reset_score_all"));
		resetScoreAllMI.setIcon(DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_EMPTY_MI, "FIXME"));
		resetScoreAllMI.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menuitem.tools_reset_score_all").charAt(0));
		resetScoreAllMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.EventType.DATA_EVENT);
				ape.setMessage(MessageConstants.Message.D_ENTRIES_RESET_SCORE_ALL);
				parentController.handleAppEvent(ape);
			}
		});
		toolsMenu.add(resetScoreAllMI);
		//----
		resetScoreSelMI = new JMenuItem(Toolbox.getInstance().getLocalizedString("label.menuitem.tools_reset_score_current"));
		resetScoreSelMI.setIcon(DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_EMPTY_MI, "FIXME"));
		resetScoreSelMI.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menuitem.tools_reset_score_current").charAt(0));
		resetScoreSelMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.EventType.DATA_EVENT);
				ape.setMessage(MessageConstants.Message.D_ENTRIES_RESET_SCORE_SEL);
				parentController.handleAppEvent(ape);
			}
		});
		toolsMenu.add(resetScoreSelMI);
		//----
		toolsMenu.addSeparator();
		//----
		statisticsMI = new JMenuItem(Toolbox.getInstance().getLocalizedString("label.menuitem.tools_display_stats"));
		statisticsMI.setIcon(DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_STATS_MI, "FIXME"));
		statisticsMI.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menuitem.tools_display_stats").charAt(0));
		statisticsMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.EventType.NAV_EVENT);
				ape.setMessage(MessageConstants.Message.N_VIEW_STATISTICS);
				parentController.handleAppEvent(ape);
			}
		});
		toolsMenu.add(statisticsMI);
		//----
		saveStatsMI = new JMenuItem(Toolbox.getInstance().getLocalizedString("label.menuitem.tools_save_stats"));
		saveStatsMI.setIcon(DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_EMPTY_MI, "FIXME"));
		saveStatsMI.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menuitem.tools_save_stats").charAt(0));
		saveStatsMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.EventType.DATA_EVENT);
				ape.setMessage(MessageConstants.Message.D_STATISTICS_SAVE_SET);
				parentController.handleAppEvent(ape);
			}
		});
		toolsMenu.add(saveStatsMI);

		//helpMenu
		helpMenu = new JMenu(Toolbox.getInstance().getLocalizedString("label.menu.help"));
		helpMenu.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menu.help").charAt(0));
		//----
		contentsMI = new JMenuItem(Toolbox.getInstance().getLocalizedString("label.menuitem.help_contents"));
		contentsMI.setIcon(DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_HELP_MI, "FIXME"));
		contentsMI.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menuitem.help_contents").charAt(0));
		newMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		contentsMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		contentsMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.EventType.HELP_EVENT);
				ape.setMessage(MessageConstants.Message.H_ALL);
				parentController.handleAppEvent(ape);
			}
		});
		helpMenu.add(contentsMI);
		//----
		aboutMI = new JMenuItem(Toolbox.getInstance().getLocalizedString("label.menuitem.help_about"));
		aboutMI.setIcon(DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_EMPTY_MI, "FIXME"));
		aboutMI.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.menuitem.help_about").charAt(0));
		aboutMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AppEvent ape = new AppEvent(AppEvent.EventType.HELP_EVENT);
				ape.setMessage(MessageConstants.Message.H_ABOUT);
				parentController.handleAppEvent(ape);
			}
		});
		helpMenu.add(aboutMI);

		//put all into menu bar
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(learnMenu);
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
		infoMI.setEnabled(aStatus);
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
		learnByChoiceMI.setEnabled(aLearnOneStatus); //FIXME: this is evt. not true due to need for more entries
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
						AppEvent ape = new AppEvent(AppEvent.EventType.STATUS_EVENT);
						ape.setMessage(MessageConstants.Message.S_OPEN_VOCABULARY);
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
	private final static class FileHistoryMenuItem extends JMenuItem {
		private final static long serialVersionUID = 1L;

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
	} //END private final class FileHistoryMenuItem extends JMenuItem

} //END public class MainWindow extends JFrame implements IDingsMainWindow
