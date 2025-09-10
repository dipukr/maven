package maven;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Sort extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private int data[] = {4,10,18,6,13,1,11,17,16,9,14,15,7,3,2,8,5,12};
	private int frameCount = 0;
	
	public Sort(int width, int height) {
		setVisible(true);
		setBackground(new Color(39, 40, 34));
		setPreferredSize(new Dimension(width, height));
	}
	
	public void swap(int i, int j) {
		int val = data[i];
		data[i] = data[j];
		data[j] = val;
	}
	
	public int minIndex(int start) {
		int min = start;
		for (int i = start; i < data.length; i++)
			if (data[i] < data[min])
				min = i;
		return min;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D gc = (Graphics2D) g;
		gc.setColor(Color.white);
		for (int i = 0; i < data.length; i++)
			for (int j = 0; j < data[i]; j++)
				gc.fillRect(50 + i * 50, 900 - j * 50, 49, 49);
		int minIndex = minIndex(frameCount);
		swap(frameCount, minIndex);
		frameCount++;
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {}
		if (frameCount != data.length)
			repaint();
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.add(new Sort(1000, 1000), BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}