package com.diamondshearts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

import com.diamondshearts.models.Card;
import com.diamondshearts.models.Player;

public class PlayerView extends View {

	private Player player;
	private Paint textPaint;
	private Paint borderPaint;
	private Paint backgroundPaint;
	private float textHeight = 0;
	private Integer textColor = Color.BLACK;
	private Integer backgroundColor;
	private Integer width, height;
	private RectF border;
	private float density;

	public PlayerView(Context context) {
		super(context);
		density = getResources().getDisplayMetrics().density;
		width = (int) density * 150;
		height = (int) density * 50;
		float boarderWith = 2 * density;
		border = new RectF(boarderWith, boarderWith, width - boarderWith,
				height - boarderWith);
		backgroundColor = Color.WHITE;

		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(textColor);
		textHeight = 15 * density;
		textPaint.setTextSize(textHeight);

		borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setTextSize(textHeight);
		borderPaint.setStrokeWidth(boarderWith);

		backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		backgroundPaint.setColor(backgroundColor);
		backgroundPaint.setStyle(Paint.Style.FILL);
		backgroundPaint.setTextSize(textHeight);

		setOnDragListener(new OnDragListener() {

			@Override
			public boolean onDrag(View v, DragEvent event) {
				switch (event.getAction()) {
				case DragEvent.ACTION_DRAG_STARTED:
					return event.getLocalState().getClass() == CardView.class;
				case DragEvent.ACTION_DRAG_ENTERED:
					v.setBackgroundColor(Color.RED);
					v.invalidate();
					return true;
				case DragEvent.ACTION_DRAG_LOCATION:
					return true;
				case DragEvent.ACTION_DRAG_EXITED:
					v.setBackgroundColor(Color.WHITE);
					v.invalidate();
					return true;
				case DragEvent.ACTION_DROP:
					CardView cardView = (CardView)event.getLocalState();
					Card card = cardView.getCard();
					ViewGroup hand = (ViewGroup)cardView.getParent();
					hand.removeView(cardView);
					player.getHand().remove(card);	
					card.play(player);
					
					v.setBackgroundColor(Color.WHITE);
					v.invalidate();					
					return true;
				case DragEvent.ACTION_DRAG_ENDED:
					cardView = (CardView)event.getLocalState();
					cardView.setVisibility(VISIBLE);
				default:
					// Error!!
					break;
				}
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

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// Draw background and border
		backgroundPaint.setColor(backgroundColor);
		canvas.drawRoundRect(border, 10 * density, 10 * density,
				backgroundPaint);
		canvas.drawRoundRect(border, 10 * density, 10 * density, borderPaint);

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
