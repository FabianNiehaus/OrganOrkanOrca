package eshop.client.util;

import java.awt.Dimension;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class ContentPanel extends JPanel {

	JPanel	upperArea	= new JPanel(new MigLayout());
	JPanel	lowerArea	= new JPanel(new MigLayout());

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
