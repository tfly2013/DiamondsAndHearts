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

/**
 * Custom View that contains UI of a player.
 * 
 * @author Fei Tang & Kimple Ke(co-author)
 */
public class PlayerView extends View {
	/**The player*/
	private Player player;
	
	/**Add paint on text*/
	private Paint textPaint;
	
	/**Add paint on border*/
	private Paint borderPaint;
	
	/**Add paint on background*/
	private Paint backgroundPaint;
	
	/**Text height*/
	private float textHeight;
	
	/**Color of text*/
	private Integer textColor;
	
	/**The background color*/
	private Integer backgroundColor;
	
	/**Player view width and height*/
	private Integer width, height;
	
	/**Player view border*/
	private RectF border;
	
	/**Screen density in android or dots per inch(dpi)*/
	private float density;

	/**
	 * Initialize a player view.
	 * @param context
	 *            The context that view is in.
	 */
	public PlayerView(Context context) {
		super(context);
		
		//specify background color and text(color,height)
		backgroundColor = Color.WHITE;
		textColor = Color.BLACK;
		textHeight = 0;
		
		//get the android screen density
		density = getResources().getDisplayMetrics().density;
		
		//calculate the width of the player view
		width = (int) density * 150;
		
		//calculate the height of the player view
		height = (int) density * 50;
		
		//adjust the border width of the player view
		float borderWidth = 2 * density;
		
		//create the rectangular border specifying its top, left, right, bottom 
		border = new RectF(borderWidth, borderWidth, width - borderWidth,
				height - borderWidth);

		// Initialize text paint
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(textColor);
		textHeight = 15 * getResources().getDisplayMetrics().scaledDensity;
		textPaint.setTextSize(textHeight);

		// Initialize border paint
		borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setTextSize(textHeight);
		borderPaint.setStrokeWidth(borderWidth);

		// Initialize background paint
		backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		backgroundPaint.setColor(backgroundColor);
		backgroundPaint.setStyle(Paint.Style.FILL);
		backgroundPaint.setTextSize(textHeight);

		// Setup OnDragListener to handle card drag to player
		setOnDragListener(new OnDragListener() {
			@Override
			public boolean onDrag(View v, DragEvent event) {
				switch (event.getAction()) {
				//Signals the start of a drag and drop operation of a card.
				case DragEvent.ACTION_DRAG_STARTED:
					return event.getLocalState().getClass() == CardView.class;
				//Signals to a View that the drag point has entered the bounding
			    //box of the player View.
				case DragEvent.ACTION_DRAG_ENTERED:
					v.setBackgroundColor(0xFF33B5E5);
					v.invalidate();
					return true;
				//Sent to a View after ACTION_DRAG_ENTERED if the drag shadow
				//is still within the player View's bounding box.
				case DragEvent.ACTION_DRAG_LOCATION:
					return true;
				//Signals that the user has moved the drag shadow outside the
				//bounding box of the player View.
				case DragEvent.ACTION_DRAG_EXITED:
					PlayerView playerView = (PlayerView)v;
					if (player.getTable().getPlayerThisTurn().equals(player))
						playerView.setBackgroundColor(Color.RED);
					else
						playerView.setBackgroundColor(Color.WHITE);
					v.invalidate();
					return true;
				//Signals to a View that the user has released the drag shadow,
				//and the drag point is within the bounding box of the player
				//View.
				case DragEvent.ACTION_DROP:		
					playerView = (PlayerView)v;
					CardView cardView = (CardView) event.getLocalState();
					Card card = cardView.getCard();
					ViewGroup hand = (ViewGroup) cardView.getParent();
					hand.removeView(cardView);
					playerView.getPlayer().getHand().remove(card);
					card.play(playerView.getPlayer());
					if (player.getTable().getPlayerThisTurn().equals(player))
						playerView.setBackgroundColor(Color.RED);
					else
						playerView.setBackgroundColor(Color.WHITE);
					v.invalidate();
					return true;
				//Signals to a View that the drag and drop operation has
				//concluded.
				case DragEvent.ACTION_DRAG_ENDED:
					cardView = (CardView) event.getLocalState();
					cardView.setVisibility(VISIBLE);
				default:
					// Error!!
					break;
				}
				return false;
			}
		});
	}

	/**
	 * Change the background Color of player view
	 * @param color
	 * 			  The background color
	 */
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
	 * Access the player
	 * @return player
	 * 				 The player
	 * */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Modify the player
	 * @param player
	 * 				The player
	 * */
	public void setPlayer(Player player) {
		this.player = player;
		if (player.getTable().getPlayerThisTurn().equals(player))
			backgroundColor = Color.RED;
		else
			backgroundColor = Color.WHITE;
		invalidate();
		requestLayout();
	}
}
