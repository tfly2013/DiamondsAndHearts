package com.diamondshearts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.diamondshearts.models.Player;

public class PlayerView extends View {

	private Player player;
	private Paint textPaint;
	private Paint borderPaint;
	private float textHeight = 0;
	private Integer textColor = 0xff000000;
	private Integer width, height;
	private RectF border;
	private float density;

	public PlayerView(Context context) {
		super(context);
		init();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(resolveSize(width, widthMeasureSpec),
				resolveSize(height, heightMeasureSpec));
	}

	private void init() {
		density = getResources().getDisplayMetrics().density;
		width = (int) density * 150;
		height = (int) density * 50;
		float boarderWith = 2 * density;
		border = new RectF(boarderWith, boarderWith, width - boarderWith,
				height - boarderWith);

		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(textColor);
		textHeight = 15 * density;
		textPaint.setTextSize(textHeight);

		borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setTextSize(textHeight);
		borderPaint.setStrokeWidth(boarderWith);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// Draw name
		assert (player != null);
		String name = player.getName();

		float nameX = (width - getTextWidth(name)) / 2;
		float nameY = 10 * density + textHeight / 2;
		canvas.drawText(name, nameX, nameY, textPaint);

		// Draw line
		float startX, startY, stopX, stopY;
		startY = stopY = nameY + textHeight / 2;
		startX = 2 * density;
		stopX = width - 2 * density;
		canvas.drawLine(startX, startY, stopX, stopY, borderPaint);

		// Draw labels
		String label = "";
		for (String text : player.getLabels())
			label += text + " ";
		label = label.trim();
		float labelX = (width - getTextWidth(label)) / 2;
		float labelY = startY + textHeight / 2 + 8 * density;
		canvas.drawText(label, labelX, labelY, textPaint);
		canvas.drawRoundRect(border, 10 * density, 10 * density, borderPaint);
	}

	public float getTextWidth(String string) {
		float[] widths;
		widths = new float[string.length()];
		textPaint.getTextWidths(string, widths);
		float totalWidth = 0;
		for (float i : widths)
			totalWidth += i;
		return totalWidth;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
