package com.diamondshearts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.diamondshearts.models.Action;
import com.diamondshearts.models.Card;
import com.diamondshearts.models.Event;

/**
 * Custom View that contains UI of a card.
 * 
 * @author Fei Tang & Kimple Ke(co-author)
 */
public class CardView extends View {
	/** The card */
	private Card card;

	/** Add paint on text */
	private Paint textPaint;

	/** Add paint on background */
	private Paint backgroundPaint;

	/** Text height */
	private float textHeight;

	/** Color of text */
	private Integer textColor;

	/** The background color */
	private Integer backgroundColor;

	/** Card width and height */
	private Integer width, height;

	/** Card border */
	private RectF border;

	/** Screen density in android or dots per inch(dpi) */
	private float density;
	
	private Drawable cardBoarder;

	/**
	 * Initialize a card view.
	 * 
	 * @param context
	 *            The context that view is in.
	 */
	public CardView(Context context) {
		super(context);

		// specify background color and text(color,height)
		backgroundColor = Color.WHITE;
		textColor = Color.BLACK;
		textHeight = 0;

		// get the android screen density
		density = getResources().getDisplayMetrics().density;

		// calculate the width of the player view
		width = (int) density * 100;

		// calculate the height of the player view
		height = (int) density * 120;

		// adjust the border width of the player view
		float borderWidth = 2 * density;

		// create the rectangular border specifying its top, left, right, bottom
		border = new RectF(borderWidth, borderWidth, width - borderWidth, height
				- borderWidth);
		
		cardBoarder = getResources().getDrawable(R.drawable.card_border);

		// Initialize text paint
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(textColor);
		textHeight = 14 * getResources().getDisplayMetrics().scaledDensity;
		textPaint.setTextSize(textHeight);

		// Initialize background paint
		backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		backgroundPaint.setColor(backgroundColor);
		backgroundPaint.setStyle(Paint.Style.FILL);
		backgroundPaint.setTextSize(textHeight);

		// Set up OnLongClickListener that allows card view to be dragged when
		// long clicked
		setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				CardView cardView = (CardView) v;
				if (cardView.getCard().getOwner().getTable().isMyTurn()) {
					startDrag(null, new DragShadowBuilder(v), v, 0);
					v.setVisibility(INVISIBLE);
				}
				return false;
			}
		});
	}

	/**
	 * Change the background Color of player view
	 */
	@Override
	public void setBackgroundColor(int color) {
		backgroundColor = color;
	}

	@Override
	/**
	 * Called to determine the size requirements for this view and all of its children.
	 * @param widthMeasureSpec
	 * 						 The width requirement
	 * @param heightMeasureSpec
	 * 						 The height requirement
	 * */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(resolveSize(width, widthMeasureSpec),
				resolveSize(height, heightMeasureSpec));
	}

	@Override
	/**
	 * Called when the view should render its content.
	 * @param canvas
	 * 				The canvas on which to draw the scroll-bars
	 * */
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// Draw background and border
		backgroundPaint.setColor(backgroundColor);
		canvas.drawRoundRect(border, 10 * density, 10 * density,
				backgroundPaint);
		
		cardBoarder.setBounds(0,0,width,height);
		cardBoarder.draw(canvas);

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
		float inteval = 10 * density;
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

	/**
	 * Return the text width of a string
	 * 
	 * @param string
	 *            The string to test
	 * @return The text width
	 */
	public float getTextWidth(String string) {
		float[] widths;
		widths = new float[string.length()];
		textPaint.getTextWidths(string, widths);
		float totalWidth = 0;
		for (float i : widths)
			totalWidth += i;
		return totalWidth;
	}

	/**
	 * Access the card
	 * 
	 * @return card The card
	 * */
	public Card getCard() {
		return card;
	}

	/**
	 * Modify the card
	 * 
	 * @param card
	 *            The card
	 * */
	public void setCard(Card card) {
		this.card = card;
		invalidate();
		requestLayout();
	}
}
