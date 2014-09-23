package com.diamondshearts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.diamondshearts.models.Action;
import com.diamondshearts.models.Card;
import com.diamondshearts.models.Event;

public class CardView extends View {

	private Card card;

	private Paint textPaint;
	private Paint borderPaint;
	private float textHeight = 0;
	private Integer textColor = 0xff000000;
	private Integer width, height;
	private RectF border;
	private float density;

	public CardView(Context context) {
		super(context);
		init();
	}

	public CardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(resolveSize(width, widthMeasureSpec),
				resolveSize(height, heightMeasureSpec));
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
		invalidate();
		requestLayout();
	}

	private void init() {
		density = getResources().getDisplayMetrics().density;
		width = (int) density * 100;
		height = (int) density * 120;
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
		// Draw actions
		assert (card != null);
		String actions = "";
		for (Action action : card.getActions())
			actions += action.getSuit().getName() + action.getRank() + " ";
		actions.trim();

		float actionsX = (width - getTextWidth(actions)) / 2;
		float actionsY = 20 * density;
		canvas.drawText(actions, actionsX, actionsY, textPaint);

		// Draw events
		float inteval = 2 * density;
		float eventHeight = (textHeight + inteval) * card.getEvents().size()
				- inteval;
		float eventY = (height - actionsY - textHeight - eventHeight) / 2
				+ actionsY + textHeight;
		for (Event event : card.getEvents()) {
			String text = event.getName();
			float eventX = (width - getTextWidth(text)) / 2;
			canvas.drawText(text, eventX, eventY, textPaint);
			eventY += textHeight + inteval;
		}
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
}