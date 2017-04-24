package albumCode;

import java.util.ArrayList;
import java.util.Scanner;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.net.MalformedURLException;
import java.net.URL;

import albumCode.GetFlickr;
import albumCode.Response;

/**
 * @author James Lee
 *
 */
/**
 *
 */
public class DemoGUI extends JFrame implements ActionListener {

	JTextField searchTagField = new JTextField("");
	JTextField numResultsStr = new JTextField("10");
	JPanel onePanel;
	JScrollPane oneScrollPanel;
	JButton testButton = new JButton("Test");
	JButton searchButton = new JButton("Search");
	JButton loadButton = new JButton("Load");
	JButton deleteButton = new JButton("Delete");
	JButton saveButton = new JButton("Save");
	JButton exitButton = new JButton("Exit");
	ArrayList<String> imgList = new ArrayList<>();
	ArrayList<SelectedItem> selectedImgs = new ArrayList<>();

	static int frameWidth = 800;
	static int frameHeight = 600;

	/**
	 * <p>
	 * This is the constrcutor of the class that creates a jframe
	 * </>
	 */
	public DemoGUI() {

		// create bottom subpanel with buttons, flow layout
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
		// add testButton to bottom subpanel
		buttonsPanel.add(testButton);
		buttonsPanel.add(loadButton);
		buttonsPanel.add(deleteButton);
		buttonsPanel.add(saveButton);
		buttonsPanel.add(exitButton);

		// add listener for testButton clicks
		testButton.addActionListener(this);
		loadButton.addActionListener(this);
		deleteButton.addActionListener(this);
		saveButton.addActionListener(this);
		exitButton.addActionListener(this);

		/*
		 * System.out.println("testButton at " + testButton.getClass().getName()
		 * + "@" + Integer.toHexString(hashCode())); System.out.println(
		 * "Components: "); Component comp[] = buttonsPanel.getComponents(); for
		 * (int i=0; i<comp.length; i++) {
		 * System.out.println(comp[i].getClass().getName() + "@" +
		 * Integer.toHexString(hashCode())); }
		 */

		// create middle subpanel with 2 text fields and button, border layout
		JPanel textFieldSubPanel = new JPanel(new FlowLayout());
		// create and add label to subpanel
		JLabel tl = new JLabel("Enter search tag:");
		textFieldSubPanel.add(tl);

		// set width of left text field
		searchTagField.setColumns(23);
		// add listener for typing in left text field
		searchTagField.addActionListener(this);
		// add left text field to middle subpanel
		textFieldSubPanel.add(searchTagField);
		// add search button to middle subpanel
		textFieldSubPanel.add(searchButton);
		// add listener for searchButton clicks
		searchButton.addActionListener(this);

		// create and add label to middle subpanel, add to middle subpanel
		JLabel tNum = new JLabel("max search results:");
		numResultsStr.setColumns(2);
		textFieldSubPanel.add(tNum);
		textFieldSubPanel.add(numResultsStr);

		// create and add panel to contain bottom and middle subpanels
		/*
		 * JPanel textFieldPanel = new JPanel(new BorderLayout());
		 * textFieldPanel.add(buttonsPanel, BorderLayout.SOUTH);
		 * textFieldPanel.add(textFieldSubPanel, BorderLayout.NORTH);
		 */
		JPanel textFieldPanel = new JPanel();
		textFieldPanel.setLayout(new BoxLayout(textFieldPanel, BoxLayout.Y_AXIS));
		textFieldPanel.add(textFieldSubPanel);
		textFieldPanel.add(buttonsPanel);

		// create top panel
		onePanel = new JPanel();
		onePanel.setLayout(new BoxLayout(onePanel, BoxLayout.Y_AXIS));

		// create scrollable panel for top panel
		oneScrollPanel = new JScrollPane(onePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		oneScrollPanel.setPreferredSize(new Dimension(frameWidth, frameHeight - 100));
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		// add scrollable panel to main frame
		add(oneScrollPanel);

		// add panel with buttons and textfields to main frame
		add(textFieldPanel);

	}

	public static void main(String[] args) throws Exception {
		DemoGUI frame = new DemoGUI();
		frame.setTitle("Swing GUI Demo");
		frame.setSize(frameWidth, frameHeight);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	/* 
	 * method to handle the click events on the labels
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == searchButton) {
			performSearch();
		} else if (e.getSource() == testButton) {
			performTest();
		} else if (e.getSource() == loadButton) {
			load();
		} else if (e.getSource() == deleteButton) {
			delete();
		} else if (e.getSource() == saveButton) {
			if (saveList()) {
				JOptionPane.showMessageDialog(this, "This list is saved successfully");
			} else {
				JOptionPane.showMessageDialog(this, "Error", "This list is not saved", JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getSource() == exitButton) {
			System.out.println("exit");
			System.exit(0);
		} else if (e.getSource() == searchTagField) {
			performSearch();
		}
	}

	/**
	 * the load method loads the list of urls from the file photoalbum
	 */
	private void load() {
		try {
			Scanner in = new Scanner(new FileInputStream("photoalbum"));
			imgList.clear();
			while (in.hasNext()) {
				String url = in.nextLine();
				imgList.add(url);
				System.out.println(url);
			}
			onePanel.removeAll();

			updateImages();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * this method deletes all the selected files
	 */
	private void delete() {
		for (SelectedItem item : selectedImgs) {
			System.out.println("Deleted photo " + onePanel.getComponentZOrder(item.label));
			imgList.remove(item.url);
			System.out.println(item.url);
			onePanel.remove(item.label);
		}
		selectedImgs.clear();
		repaint();
		this.revalidate();
	}

	/**
	 * method to handle action event on the test button
	 * it loads the image from the url provided int searchTagField
	 */
	private void performTest() {
		String url = this.searchTagField.getText();
		try {
			System.out.println("Test " + url);
			imgList.clear();
			imgList.add(url);
			updateImages();
			repaint();
			this.revalidate();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * the method to handle the action event on the search button
	 * <p>
	 * it searches the flickr database for the provided tag
	 * save the list on the imgList
	 * </p>
	 */
	private void performSearch() {
		String searchKey = this.searchTagField.getText();
		searchKey = searchKey.replace(" ", "%20");
		System.out.println("Search " + searchKey);
		int searchCount = Integer.parseInt(this.numResultsStr.getText());
		GetFlickr flickr = new GetFlickr();
		flickr.setPerPageCount(searchCount);
		flickr.setSearchKey(searchKey);

		try {
			Response response = flickr.search();
			for (PhotoInfo photo : response.photos.photo) {
				int farm = photo.farm;
				String server = photo.server;
				String id = photo.id;
				String secret = photo.secret;
				String photoUrl = "http://farm" + farm + ".static.flickr.com/" + server + "/" + id + "_" + secret
						+ ".jpg";
				System.out.println(photoUrl);
				imgList.add(photoUrl);
			}

			updateImages();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * The method update the list of images displayed on the Onpanel
	 * it also adds the mouse click lister to the labels
	 * @throws IOException
	 */
	private void updateImages() throws IOException {
		onePanel.removeAll();
		for (String img : imgList) {
			Image sImg = getScaledImage(img);
			JLabel lbl = new JLabel(new ImageIcon(sImg));
			// lbl.setSize(new Dimension(lbl.getIcon().getIconWidth() + 20,
			// 220));
			lbl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			lbl.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					lbl.setForeground(Color.DARK_GRAY);
					System.out.println("Selected photo " + onePanel.getComponentZOrder(lbl));
					if (selectedImgs.contains(new SelectedItem(lbl,img))) {
						selectedImgs.remove(new SelectedItem(lbl,img));
					} else {
						selectedImgs.add(new SelectedItem(lbl,img));
					}
				}
			});
			onePanel.add(lbl);
		}
		repaint();
		this.revalidate();

	}

	/**
	 * get the image in the provided url
	 * scale it to 200 pixel height while preseving the ratio
	 * @param url
	 * @return the scales Image
	 * @throws IOException
	 */
	public Image getScaledImage(String url) throws IOException {
		URL imgUrl = new URL(url.replace(" ", "%20"));

		BufferedImage img = ImageIO.read(imgUrl);
		double width = img.getWidth(null);
		double height = img.getHeight(null);

		int sWidth = (int) (width / height * 200);
		Image scaledImg = img.getScaledInstance(sWidth, 200, Image.SCALE_DEFAULT);

		return scaledImg;
	}

	/**
	 * Saves the list of images to the photo album file
	 * @return
	 */
	public boolean saveList() {

		try (BufferedWriter out = new BufferedWriter(new FileWriter("photoalbum"))) {
			for (String url : imgList) {
				out.write(url + "\n");
				System.out.println(url);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}