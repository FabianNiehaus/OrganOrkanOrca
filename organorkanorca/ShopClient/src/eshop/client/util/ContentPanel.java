package eshop.client.util;

import java.awt.Dimension;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

// TODO: Auto-generated Javadoc
/**
 * The Class ContentPanel.
 */
public class ContentPanel extends JPanel {

	/** The lower area. */
	JPanel	lowerArea	= new JPanel(new MigLayout());
	
	/** The upper area. */
	JPanel	upperArea	= new JPanel(new MigLayout());

	/**
	 * Instantiates a new content panel.
	 *
	 * @param upperAreaContent
	 *           the upper area content
	 * @param lowerAreaContent
	 *           the lower area content
	 */
	public ContentPanel(JPanel upperAreaContent, JPanel lowerAreaContent) {
		setLayout(new MigLayout());
		upperArea.setPreferredSize(new Dimension(900, 400));
		lowerArea.setPreferredSize(new Dimension(900, 400));
		this.upperArea = upperAreaContent;
		this.lowerArea = lowerAreaContent;
		this.add(upperArea, "wrap, dock center");
		this.add(lowerArea, "dock center");
	}
}
