package com.diamondshearts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.diamondshearts.models.Action;
import com.diamondshearts.models.Card;
import com.diamondshearts.models.Event;

public class CardView extends View {
	private Card card;

	private Paint textPaint;
	private Paint borderPaint;
	private Paint backgroundPaint;
	private float textHeight = 0;
	private Integer textColor = Color.BLACK;
	private Integer backgroundColor = Color.WHITE;
	private Integer width, height;
	private RectF border;
	private float density;

	public CardView(Context context) {
		super(context);

		density = getResources().getDisplayMetrics().density;
		width = (int) density * 100;
		height = (int) density * 120;
		float borderWith = 2 * density;
		border = new RectF(borderWith, borderWith, width - borderWith, height
				- borderWith);

		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(textColor);
		textHeight = 15 * density;
		textPaint.setTextSize(textHeight);

		borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setTextSize(textHeight);
		borderPaint.setStrokeWidth(borderWith);

		backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		backgroundPaint.setColor(backgroundColor);
		backgroundPaint.setStyle(Paint.Style.FILL);
		backgroundPaint.setTextSize(textHeight);
		setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				startDrag(null, new DragShadowBuilder(v), v, 0);
				v.setVisibility(GONE);
				return false;
			}
		});
	}
	
	public void setBackgroundColor(int color) {
		backgroundColor = color;
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

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// Draw background and border
		canvas.drawRoundRect(border, 10 * density, 10 * density,
				backgroundPaint);
		canvas.drawRoundRect(border, 10 * density, 10 * density, borderPaint);

		// Draw actions
		assert (card != null);
		String actions = "";
		for (Action action : card.getActions())
			actions += action.getSuit().getName() + action.getRank() + " ";
		actions = actions.trim();

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