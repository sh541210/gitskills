package com.iyin.sign.system.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * @ClassName: CaptchaUtil
 * @Description: 关于验证码的工具类
 * @Author: 陈利
 * @CreateDate: 2018/8/24 14:27
 * @UpdateUser: 陈利
 * @UpdateDate: 2018/8/24 14:27
 * @Version: 0.0.1
 */
public class CaptchaUtil {

	/**
	 * 验证码字符集
	 */
	private static final char[] CHARS = { '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
			'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B',
			'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
			'Y', 'Z' };
	/**
	 * 字符数量
	 */
	private static final int SIZE = 4;
	/**
	 * 干扰线数量
	 */
	private static final int LINES = 0;
	/**
	 * 宽度
	 */
	private static final int WIDTH = 78;
	/**
	 * 高度
	 */
	private static final int HEIGHT = 35;
	/**
	 * 字体大小
	 */
	private static final int FONT_SIZE = 30;

	/**
	 * 生成随机验证码及图片 Object[0]：验证码字符串； Object[1]：验证码图片。
	 */
	public static Object[] createImage() {
		StringBuilder sb = new StringBuilder();
		// 1.创建空白图片
		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		// 2.获取图片画笔
		Graphics graphic = image.getGraphics();
		// 3.设置画笔颜色
		graphic.setColor(Color.LIGHT_GRAY);
		// 4.绘制矩形背景
		graphic.fillRect(0, 0, WIDTH, HEIGHT);
		// 5.画随机字符
		Random ran = new Random();
		for (int i = 0; i < SIZE; i++) {
			// 取随机字符索引
			int n = ran.nextInt(CHARS.length);
			// 设置随机颜色
			graphic.setColor(getRandomColor());
			// 设置字体大小
			graphic.setFont(new Font(null, Font.BOLD + Font.ITALIC, FONT_SIZE));
			// 画字符
			graphic.drawString(CHARS[n] + "", i * WIDTH / SIZE, HEIGHT * 4 / 5);
			// 记录字符
			sb.append(CHARS[n]);
		}
		// 6.画干扰线
		for (int i = 0; i < LINES; i++) {
			// 设置随机颜色
			graphic.setColor(getRandomColor());
			// 随机画线
			graphic.drawLine(ran.nextInt(WIDTH), ran.nextInt(HEIGHT), ran.nextInt(WIDTH), ran.nextInt(HEIGHT));
		}
		// 7.返回验证码和图片
		return new Object[] { sb.toString(), image };
	}

	/**
	 * 随机取色
	 */
	public static Color getRandomColor() {
		Random ran = new Random();
		return new Color(ran.nextInt(256), ran.nextInt(256), ran.nextInt(256));
	}

	public static void main(String[] args) throws IOException {
		Object[] objs = createImage();
		BufferedImage image = (BufferedImage) objs[1];
		OutputStream os = new FileOutputStream("d:/1.png");
		ImageIO.write(image, "png", os);
		os.close();
	}
}
