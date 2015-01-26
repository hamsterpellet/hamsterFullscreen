package br.com.hamsterpellet.fullscreen.region;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;

import br.com.hamsterpellet.fullscreen.GamePanel;
import br.com.hamsterpellet.fullscreen.ScreenPage.MouseStatus;

public class ScreenColoredCursoredRect extends ScreenRect {

	public static final Color DEFAULT_FILL_COLOR = Color.PINK;
	
	protected ScreenColoredCursoredRect(double width, double height, ScreenRect parent) {
		/** NEVER CALL THIS, CALL FACTORY() INSTEAD **/
		
		super(width, height, parent);
		hoverCursor = defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
		fillColor = fillColorHover = fillColorPress = DEFAULT_FILL_COLOR;
		isFillColorHoverSet = isFillColorPressSet = false;
	}
	private static ScreenColoredCursoredRect factory(double width, double height, ScreenRect parent) {
		ScreenColoredCursoredRect r = new ScreenColoredCursoredRect(width, height, parent);
		r.getParent().addChild(r);
		return r;
	}
	
	public static ScreenColoredCursoredRect create(double width, double height, ScreenRect parent) {
		return factory(width, height, parent);
	}
	
	public static ScreenColoredCursoredRect create(double width, double height,
			ScreenRect parent, Color color) {
		ScreenColoredCursoredRect rect = create(width, height, parent);
		rect.fillColor = rect.fillColorHover = rect.fillColorPress = color;
		return rect;
	}
	
	private Cursor hoverCursor;
	private Cursor defaultCursor;
	
	private boolean isFillColorHoverSet;
	private boolean isFillColorPressSet;
	
	private Color fillColor;
	private Color fillColorHover;
	private Color fillColorPress;
	
	private Color getCurrentFillColor() {
		if (currentMouseStatus == MouseStatus.NORMAL) {
			return fillColor;
		}
		if (currentMouseStatus == MouseStatus.HOVER) {
			return fillColorHover;
		}
		return fillColorPress;
	}
	
	/** SETTERS **/
	
	public void setHoverCursor(int whichCursor) {
		setHoverCursor(Cursor.getPredefinedCursor(whichCursor));
	}
	public void setHoverCursor(Cursor whichCursor) {
		hoverCursor = whichCursor;
	}
	public void setDefaultCursor(int whichCursor) {
		setDefaultCursor(Cursor.getPredefinedCursor(whichCursor));
	}
	public void setDefaultCursor(Cursor whichCursor) {
		defaultCursor = whichCursor;
	}

	public void setFillColor(Color color) {
		setFillColor(color, MouseStatus.NORMAL);
	}
	public void setFillColor(Color color, MouseStatus whichStatus) {
		if (color == null) throw new IllegalArgumentException("Color can't be null!");
		if (whichStatus == MouseStatus.NORMAL) {
			fillColor = color;
			if (!isFillColorHoverSet) setFillColor(color, MouseStatus.HOVER);
			if (!isFillColorPressSet) setFillColor(color, MouseStatus.PRESSED); // this won't ever run but ..readability..!
		} else if (whichStatus == MouseStatus.HOVER) {
			fillColorHover = color;
			isFillColorHoverSet = true;
			if (!isFillColorPressSet) setFillColor(color, MouseStatus.PRESSED);
		} else {
			fillColorPress = color;
			isFillColorPressSet = true;
		}
	}
	

	@Override
	public void onMouseIn() {
		if (hoverCursor != null) {
			GamePanel.setCursor(hoverCursor);
		}
	}

	@Override
	public void onMouseOut() {
		if (defaultCursor != null) {
			GamePanel.setCursor(defaultCursor);
		}
	}

	@Override
	public void paint(Graphics2D g) {
		super.paint(g);
		
		Color oldColor = g.getColor();
		g.setColor(getCurrentFillColor());
		g.fillRect(getUpperXOnScreen(), getUpperYOnScreen(), getWidthOnScreen(), getHeightOnScreen());
		g.setColor(oldColor);
	}
	
}