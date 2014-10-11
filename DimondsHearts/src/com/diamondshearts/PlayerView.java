package com.diamondshearts;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

import com.diamondshearts.models.Card;
import com.diamondshearts.models.Player;
import com.diamondshearts.models.Table;
import com.google.android.gms.common.images.ImageManager;

/**
 * Custom View that contains UI of a player.
 * 
 * @author Fei Tang & Kimple Ke(co-author)
 */
public class PlayerView extends View {
	/** The player */
	private Player player;

	/** Add paint on text */
	private Paint textPaint;

	/** Add paint on border */
	private Paint borderPaint;

	/** Add paint on background */
	private Paint backgroundPaint;

	/** Text height */
	private float textHeight;

	/** Color of text */
	private Integer textColor;

	/** The background color */
	private Integer borderColor;

	/** Player view width and height */
	private Integer width, height;

	/** Player view border */
	private RectF border;

	/** Screen density in android or dots per inch(dpi) */
	private float density;

	private Drawable playerImage;

	/**
	 * Initialize a player view.
	 * 
	 * @param context
	 *            The context that view is in.
	 */
	public PlayerView(Context context) {
		super(context);

		// specify background color and text(color,height)
		borderColor = Color.TRANSPARENT;
		textColor = Color.BLACK;
		textHeight = 0;

		// get the android screen density
		density = getResources().getDisplayMetrics().density;

		// calculate the width of the player view
		width = (int) density * 110;

		// calculate the height of the player view
		height = (int) density * 80;

		// adjust the border width of the player view
		float borderWidth = 2 * density;

		// create the rectangular border specifying its top, left, right, bottom
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
		borderPaint.setColor(borderColor);

		// Initialize background paint
		backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		backgroundPaint.setColor(Color.TRANSPARENT);
		backgroundPaint.setStyle(Paint.Style.FILL);
		backgroundPaint.setTextSize(textHeight);

		// Setup OnDragListener to handle card drag to player
		setOnDragListener(new OnDragListener() {
			@Override
			public boolean onDrag(View v, DragEvent event) {
				switch (event.getAction()) {
				// Signals the start of a drag and drop operation of a card.
				case DragEvent.ACTION_DRAG_STARTED:
					return event.getLocalState().getClass() == CardView.class;
					// Signals to a View that the drag point has entered the
					// bounding
					// box of the player View.
				case DragEvent.ACTION_DRAG_ENTERED:
					PlayerView playerView = (PlayerView) v;
					playerView.setBorderColor(Color.GREEN);
					v.invalidate();
					return true;
					// Sent to a View after ACTION_DRAG_ENTERED if the drag
					// shadow
					// is still within the player View's bounding box.
				case DragEvent.ACTION_DRAG_LOCATION:
					return true;
					// Signals that the user has moved the drag shadow outside
					// the
					// bounding box of the player View.
				case DragEvent.ACTION_DRAG_EXITED:
					playerView = (PlayerView) v;
					playerView.resetColor();
					v.invalidate();
					return true;
					// Signals to a View that the user has released the drag
					// shadow,
					// and the drag point is within the bounding box of the
					// player
					// View.
				case DragEvent.ACTION_DROP:
					playerView = (PlayerView) v;
					CardView cardView = (CardView) event.getLocalState();
					onDrop(playerView, cardView);
					return true;
					// Signals to a View that the drag and drop operation has
					// concluded.
				case DragEvent.ACTION_DRAG_ENDED:
					cardView = (CardView) event.getLocalState();
					cardView.setVisibility(VISIBLE);
				default:
					// Error!!
					break;
				}
				return false;
			}

			/**
			 * @param playerView
			 * @param cardView
			 */
			private void onDrop(PlayerView playerView, CardView cardView) {
				GameActivity activity = null;
				Card card = cardView.getCard();
				if (getContext().getClass() == GameActivity.class) {
					activity = (GameActivity) getContext();
					if (card.play(playerView.getPlayer())) {
						((ViewGroup) cardView.getParent()).removeView(cardView);
						card.getOwner().getHand().remove(card);
						card.setOwner(null);
						Table table = playerView.getPlayer().getTable();						
						table.setPlayerLastHit(playerView.getPlayer());
						ArrayList<Card> cardPlayed = table.getCardPlayed();
						cardPlayed.add(card);
						if (cardPlayed.size() > 10)
							cardPlayed.remove(0);
						activity.finishTurn();
					} else {
						activity.showMessage("I cant play like that.", 1000);
					}
					playerView.resetColor();
					playerView.invalidate();
				}
			}
		});
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
		// canvas.drawRect(border, borderPaint);

		float imageTop = 24 * density;
		float imageLeft = 6 * density;
		float imageRight = 56 * density;
		float imageBottom = 74 * density;

		if (playerImage == null)
			playerImage = getResources().getDrawable(R.drawable.photo);
		playerImage.setBounds((int) imageLeft, (int) imageTop,
				(int) imageRight, (int) imageBottom);
		playerImage.draw(canvas);

		borderPaint.setColor(borderColor);
		canvas.drawRect(border, borderPaint);

		textPaint.setColor(textColor);
		// Draw name
		assert (player != null);
		String name = player.getName();
		float nameX = (width - getTextWidth(name)) / 2;
		float nameY = 10 * density + textHeight / 2;
		textPaint.setFakeBoldText(true);
		canvas.drawText(name, nameX, nameY, textPaint);

		// Draw labels
		float startY = 18 * density;
		textPaint.setFakeBoldText(false);
		for (String text : player.getLabels()) {
			float labelX = imageRight
					+ (width - imageRight - getTextWidth(text)) / 2;
			float labelY = startY + textHeight / 2 + 10 * density;
			canvas.drawText(text, labelX, labelY, textPaint);
			startY += 20 * density;
		}
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

	/**
	 * Access the player
	 * 
	 * @return player The player
	 * */
	public Player getPlayer() {
		return player;
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

	public void resetColor() {
		if (player.getTable().isPlayerLastHit(player))
			setBorderColor(0xFF33B5E5);
		else if (player.getTable().isPlayerTurn(player))
			setBorderColor(Color.RED);
		else
			setBorderColor(Color.TRANSPARENT);
	}

	/**
	 * Change the background Color of player view
	 * 
	 * @param color
	 *            The background color
	 */
	public void setBorderColor(int color) {
		borderColor = color;
		if (color == Color.TRANSPARENT)
			textColor = Color.BLACK;
		else
			textColor = color;
	}

	/**
	 * Modify the player
	 * 
	 * @param player
	 *            The player
	 * */
	public void setPlayer(Player player) {
		this.player = player;
		resetColor();
		if (player.getImageUri() != null)
			ImageManager.create(getContext()).loadImage(
					new ImageManager.OnImageLoadedListener() {

						@Override
						public void onImageLoaded(Uri uri, Drawable image,
								boolean isRequestedDrawable) {
							playerImage = image;
							PlayerView.this.invalidate();
						}
					}, player.getImageUri());
		invalidate();
		requestLayout();
	}
}
