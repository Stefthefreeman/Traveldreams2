package ru.aviasales.template.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ImageView;

import java.math.BigDecimal;

import ru.aviasales.template.R;

/**
 * Widget that lets users select a minimum and maximum value on a given numerical range. The range value types can be one of Long, Double, Integer, Float, Short, Byte or BigDecimal.<br />
 * <br />
 * Improved {@link MotionEvent} handling for smoother use, anti-aliased painting for improved aesthetics.
 *
 * @param <T> The Number type of the range values. One of Long, Double, Integer, Float, Short, Byte or BigDecimal.
 * @author Stephan Tittel (stephan.tittel@kom.tu-darmstadt.de)
 * @author Peter Sinnott (psinnott@gmail.com)
 * @author Thomas Barrasso (tbarrasso@sevenplusandroid.org)
 */
public class RangeSeekBar<T extends Number> extends ImageView {
	private final Drawable thumbDrawable = getResources().getDrawable(R.drawable.seek_bar_thumb);
	private final float thumbWidth = getResources().getDimension(R.dimen.filters_seekbar_thumb_width_and_height);
	private final float thumbHalfWidth = 0.5f * getResources().getDimension(R.dimen.filters_seekbar_thumb_width_and_height);
	private final float thumbHalfHeight = 0.5f * (getResources().getDimension(R.dimen.filters_seekbar_thumb_width_and_height) +
			(getResources().getDimension(R.dimen.filters_seekbar_thumb_padding_top_and_bottom)) * 2f);
	private final Drawable seekBarBgDrawable = getResources().getDrawable(R.drawable.filters_range_seekbar_bg);
	private final Drawable seekBarActiveDrawable = getResources().getDrawable(R.drawable.filters_range_seekbar_active);
	private final float lineHeight = getResources().getDimension(R.dimen.filters_seekbar_height);
	private final float padding = thumbHalfWidth + getResources().getDimension(R.dimen.range_seekbar_bg_padding);
	private final T absoluteMinValue, absoluteMaxValue;
	private final NumberType numberType;
	private final double absoluteMinValuePrim, absoluteMaxValuePrim;
	private double normalizedMinValue = 0d;
	private double normalizedMaxValue = 1d;
	private Thumb pressedThumb = null;
	private boolean notifyWhileDragging = false;
	private OnRangeSeekBarChangeListener<T> listener;
	private double stepSize;

	/**
	 * An invalid pointer id.
	 */
	public static final int INVALID_POINTER_ID = 255;

	// Localized constants from MotionEvent for compatibility
	// with API < 8 "Froyo".
	public static final int ACTION_POINTER_UP = 0x6, ACTION_POINTER_INDEX_MASK = 0x0000ff00, ACTION_POINTER_INDEX_SHIFT = 8;

	private float mDownMotionX;
	private int mActivePointerId = INVALID_POINTER_ID;

	private int mScaledTouchSlop;
	private boolean mIsDragging;

	/**
	 * Creates a new RangeSeekBar.
	 *
	 * @param absoluteMinValue The minimum value of the selectable range.
	 * @param absoluteMaxValue The maximum value of the selectable range.
	 * @param context
	 * @throws IllegalArgumentException Will be thrown if min/max value type is not one of Long, Double, Integer, Float, Short, Byte or BigDecimal.
	 */
	public RangeSeekBar(T absoluteMinValue, T absoluteMaxValue, Context context) throws IllegalArgumentException {
		super(context);
		this.absoluteMinValue = absoluteMinValue;
		this.absoluteMaxValue = absoluteMaxValue;
		absoluteMinValuePrim = absoluteMinValue.doubleValue();
		absoluteMaxValuePrim = absoluteMaxValue.doubleValue();
		stepSize = absoluteMaxValuePrim - absoluteMinValuePrim;
		numberType = NumberType.fromNumber(absoluteMinValue);
		// make RangeSeekBar focusable. This solves focus handling issues in case EditText widgets are being used along with the RangeSeekBar within ScollViews.
		setFocusable(true);
		setFocusableInTouchMode(true);
		init();
	}

	private final void init() {
		mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}

	public boolean isNotifyWhileDragging() {
		return notifyWhileDragging;
	}

	/**
	 * Should the widget notify the listener callback while the user is still dragging a thumb? Default is false.
	 *
	 * @param flag
	 */
	public void setNotifyWhileDragging(boolean flag) {
		this.notifyWhileDragging = flag;
	}

	/**
	 * Returns the absolute minimum value of the range that has been set at construction time.
	 *
	 * @return The absolute minimum value of the range.
	 */
	public T getAbsoluteMinValue() {
		return absoluteMinValue;
	}

	/**
	 * Returns the absolute maximum value of the range that has been set at construction time.
	 *
	 * @return The absolute maximum value of the range.
	 */
	public T getAbsoluteMaxValue() {
		return absoluteMaxValue;
	}

	/**
	 * Returns the currently selected min value.
	 *
	 * @return The currently selected min value.
	 */
	public T getSelectedMinValue() {
		return normalizedToValue(normalizedMinValue);
	}

	/**
	 * Sets the currently selected minimum value. The widget will be invalidated and redrawn.
	 *
	 * @param value The Number value to set the minimum value to. Will be clamped to given absolute minimum/maximum range.
	 */
	public void setSelectedMinValue(T value) {
		// in case absoluteMinValue == absoluteMaxValue, avoid division by zero when normalizing.
		if (0 == (absoluteMaxValuePrim - absoluteMinValuePrim)) {
			setNormalizedMinValue(0d);
		} else {
			setNormalizedMinValue(valueToNormalized(value));
		}
	}

	/**
	 * Returns the currently selected max value.
	 *
	 * @return The currently selected max value.
	 */
	public T getSelectedMaxValue() {
		return normalizedToValue(normalizedMaxValue);
	}

	/**
	 * Sets the currently selected maximum value. The widget will be invalidated and redrawn.
	 *
	 * @param value The Number value to set the maximum value to. Will be clamped to given absolute minimum/maximum range.
	 */
	public void setSelectedMaxValue(T value) {
		// in case absoluteMinValue == absoluteMaxValue, avoid division by zero when normalizing.
		if (0 == (absoluteMaxValuePrim - absoluteMinValuePrim)) {
			setNormalizedMaxValue(1d);
		} else {
			setNormalizedMaxValue(valueToNormalized(value));
		}
	}

	/**
	 * Registers given listener callback to notify about changed selected values.
	 *
	 * @param listener The listener to notify about changed selected values.
	 */
	public void setOnRangeSeekBarChangeListener(OnRangeSeekBarChangeListener<T> listener) {
		this.listener = listener;
	}

	/**
	 * Handles thumb selection and movement. Notifies listener callback on certain events.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (!isEnabled())
			return false;

		int pointerIndex;

		final int action = event.getAction();
		switch (action & MotionEvent.ACTION_MASK) {

			case MotionEvent.ACTION_DOWN:
				// Remember where the motion event started
				mActivePointerId = event.getPointerId(event.getPointerCount() - 1);
				pointerIndex = event.findPointerIndex(mActivePointerId);
				mDownMotionX = event.getX(pointerIndex);

				pressedThumb = evalPressedThumb(mDownMotionX);

				// Only handle thumb presses.
				if (pressedThumb == null)
					return super.onTouchEvent(event);

				setPressed(true);
				invalidate();
				onStartTrackingTouch();
				trackTouchEvent(event);
				attemptClaimDrag();

				break;
			case MotionEvent.ACTION_MOVE:
				if (pressedThumb != null) {

					if (mIsDragging) {
						trackTouchEvent(event);
					} else {
						// Scroll to follow the motion event
						pointerIndex = event.findPointerIndex(mActivePointerId);
						final float x = event.getX(pointerIndex);

						if (Math.abs(x - mDownMotionX) > mScaledTouchSlop) {
							setPressed(true);
							invalidate();
							onStartTrackingTouch();
							trackTouchEvent(event);
							attemptClaimDrag();
						}
					}

					if (notifyWhileDragging && listener != null) {
						listener.onRangeSeekBarValuesChanged(this, getSelectedMinValue(), getSelectedMaxValue());
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				if (mIsDragging) {
					trackTouchEvent(event);
					onStopTrackingTouch();
					setPressed(false);
				} else {
					// Touch up when we never crossed the touch slop threshold
					// should be interpreted as a tap-seek to that location.
					onStartTrackingTouch();
					trackTouchEvent(event);
					onStopTrackingTouch();
				}

				pressedThumb = null;
				invalidate();
				if (listener != null) {
					listener.onRangeSeekBarValuesChanged(this, getSelectedMinValue(), getSelectedMaxValue());
				}
				break;
			case MotionEvent.ACTION_POINTER_DOWN: {
				final int index = event.getPointerCount() - 1;
				mDownMotionX = event.getX(index);
				mActivePointerId = event.getPointerId(index);
				invalidate();
				break;
			}
			case MotionEvent.ACTION_POINTER_UP:
				onSecondaryPointerUp(event);
				invalidate();
				break;
			case MotionEvent.ACTION_CANCEL:
				if (mIsDragging) {
					onStopTrackingTouch();
					setPressed(false);
				}
				invalidate(); // see above explanation
				break;
		}
		return true;
	}

	private final void onSecondaryPointerUp(MotionEvent ev) {
		final int pointerIndex = (ev.getAction() & ACTION_POINTER_INDEX_MASK) >> ACTION_POINTER_INDEX_SHIFT;

		final int pointerId = ev.getPointerId(pointerIndex);
		if (pointerId == mActivePointerId) {
			// This was our active pointer going up. Choose
			// a new active pointer and adjust accordingly.
			final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
			mDownMotionX = ev.getX(newPointerIndex);
			mActivePointerId = ev.getPointerId(newPointerIndex);
		}
	}

	private final void trackTouchEvent(MotionEvent event) {
		final int pointerIndex = event.findPointerIndex(mActivePointerId);
		final float x = event.getX(pointerIndex);

		if (Thumb.MIN.equals(pressedThumb)) {
			if (screenToNormalized(x) < normalizedMaxValue - screenToNormalized(Math.round(thumbWidth * 2.5))) {
				setNormalizedMinValue(Math.round(screenToNormalized(x) * stepSize) / stepSize);
			}
		} else if (Thumb.MAX.equals(pressedThumb)) {
			if (screenToNormalized(x) > normalizedMinValue + screenToNormalized(Math.round(thumbWidth * 2.5))) {
				setNormalizedMaxValue(Math.round(screenToNormalized(x) * stepSize) / stepSize);
			}
		}

		listener.onRangeSeekBarTracking(this, getSelectedMinValue(), getSelectedMaxValue());
	}

	/**
	 * Tries to claim the user's drag motion, and requests disallowing any ancestors from stealing events in the drag.
	 */
	private void attemptClaimDrag() {
		if (getParent() != null) {
			getParent().requestDisallowInterceptTouchEvent(true);
		}
	}

	/**
	 * This is called when the user has started touching this widget.
	 */
	void onStartTrackingTouch() {
		mIsDragging = true;
	}

	/**
	 * This is called when the user either releases his touch or the touch is canceled.
	 */
	void onStopTrackingTouch() {
		mIsDragging = false;
	}

	/**
	 * Ensures correct size of the widget.
	 */
	@Override
	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = 200;
		if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)) {
			width = MeasureSpec.getSize(widthMeasureSpec);
		}
		int height = thumbDrawable.getIntrinsicHeight();
		if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)) {
			height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec));
		}
		setMeasuredDimension(width, height);
	}

	/**
	 * Draws the widget on the given canvas.
	 */
	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// draw seek bar background line
		final Rect bounds = new Rect(Math.round(padding),
				Math.round(0.5f * (getHeight() - lineHeight)),
				Math.round(getWidth() - padding),
				Math.round((0.5f * (getHeight() + lineHeight))));

		if (seekBarBgDrawable != null) {
			seekBarBgDrawable.setBounds(bounds);
			seekBarBgDrawable.draw(canvas);
		}

		// draw seek bar active range line

		bounds.left = Math.round(normalizedToScreen(normalizedMinValue));
		bounds.right = Math.round(normalizedToScreen(normalizedMaxValue));
		if (seekBarActiveDrawable != null) {
			seekBarActiveDrawable.setBounds(bounds);
			seekBarActiveDrawable.draw(canvas);
		}

		// draw minimum thumb
		drawThumb(normalizedToScreen(normalizedMinValue), Thumb.MIN.equals(pressedThumb), canvas, false);

		// draw maximum thumb
		drawThumb(normalizedToScreen(normalizedMaxValue), Thumb.MAX.equals(pressedThumb), canvas, true);
	}

	/**
	 * Overridden to save instance state when device orientation changes. This method is called automatically if you assign an id to the RangeSeekBar widget using the {@link #setId(int)} method. Other members of this class than the normalized min and max values don't need to be saved.
	 */
	@Override
	protected Parcelable onSaveInstanceState() {
		final Bundle bundle = new Bundle();
		bundle.putParcelable("SUPER", super.onSaveInstanceState());
		bundle.putDouble("MIN", normalizedMinValue);
		bundle.putDouble("MAX", normalizedMaxValue);
		return bundle;
	}

	/**
	 * Overridden to restore instance state when device orientation changes. This method is called automatically if you assign an id to the RangeSeekBar widget using the {@link #setId(int)} method.
	 */
	@Override
	protected void onRestoreInstanceState(Parcelable parcel) {
		final Bundle bundle = (Bundle) parcel;
		super.onRestoreInstanceState(bundle.getParcelable("SUPER"));
		normalizedMinValue = bundle.getDouble("MIN");
		normalizedMaxValue = bundle.getDouble("MAX");
	}

	/**
	 * Draws the "normal" resp. "pressed" thumb image on specified x-coordinate.
	 *
	 * @param screenCoord The x-coordinate in screen space where to draw the image.
	 * @param pressed     Is the thumb currently in "pressed" state?
	 * @param canvas      The canvas to draw upon.
	 */
	private void drawThumb(float screenCoord, boolean pressed, Canvas canvas, boolean isMax) {
		if (isMax) {
			Rect bounds = new Rect((int) (screenCoord - thumbHalfWidth), (int) ((0.5 * getHeight()) - thumbHalfHeight),
					(int) (screenCoord + thumbHalfWidth), (int) ((0.5 * getHeight()) + thumbHalfHeight));
			thumbDrawable.setBounds(bounds);
			thumbDrawable.draw(canvas);
		} else {
			Rect bounds = new Rect((int) (screenCoord - thumbHalfWidth), (int) ((0.5 * getHeight()) - thumbHalfHeight),
					(int) (screenCoord + thumbHalfWidth), (int) ((0.5 * getHeight()) + thumbHalfHeight));
			thumbDrawable.setBounds(bounds);
			thumbDrawable.draw(canvas);
		}
	}

	/**
	 * Decides which (if any) thumb is touched by the given x-coordinate.
	 *
	 * @param touchX The x-coordinate of a touch event in screen space.
	 * @return The pressed thumb or null if none has been touched.
	 */
	private Thumb evalPressedThumb(float touchX) {
		Thumb result = null;
		boolean minThumbPressed = isInThumbRange(touchX, normalizedMinValue);
		boolean maxThumbPressed = isInThumbRange(touchX, normalizedMaxValue);
		if (minThumbPressed && maxThumbPressed) {
			// if both thumbs are pressed (they lie on top of each other), choose the one with more room to drag. this avoids "stalling" the thumbs in a corner, not being able to drag them apart anymore.
			result = (touchX / getWidth() > 0.5f) ? Thumb.MIN : Thumb.MAX;
		} else if (minThumbPressed) {
			result = Thumb.MIN;
		} else if (maxThumbPressed) {
			result = Thumb.MAX;
		}
		return result;
	}

	/**
	 * Decides if given x-coordinate in screen space needs to be interpreted as "within" the normalized thumb x-coordinate.
	 *
	 * @param touchX               The x-coordinate in screen space to check.
	 * @param normalizedThumbValue The normalized x-coordinate of the thumb to check.
	 * @return true if x-coordinate is in thumb range, false otherwise.
	 */
	private boolean isInThumbRange(float touchX, double normalizedThumbValue) {
		return Math.abs(touchX - normalizedToScreen(normalizedThumbValue)) <= thumbHalfWidth + 20;
	}

	/**
	 * Sets normalized min value to value so that 0 <= value <= normalized max value <= 1. The View will get invalidated when calling this method.
	 *
	 * @param value The new normalized min value to set.
	 */
	public void setNormalizedMinValue(double value) {
		normalizedMinValue = Math.max(0d, Math.min(1d, Math.min(value, normalizedMaxValue)));
		invalidate();
	}

	/**
	 * Sets normalized max value to value so that 0 <= normalized min value <= value <= 1. The View will get invalidated when calling this method.
	 *
	 * @param value The new normalized max value to set.
	 */
	public void setNormalizedMaxValue(double value) {
		normalizedMaxValue = Math.max(0d, Math.min(1d, Math.max(value, normalizedMinValue)));
		invalidate();
	}

	/**
	 * Converts a normalized value to a Number object in the value space between absolute minimum and maximum.
	 *
	 * @param normalized
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private T normalizedToValue(double normalized) {
		return (T) numberType.toNumber(absoluteMinValuePrim + normalized * (absoluteMaxValuePrim - absoluteMinValuePrim));
	}

	/**
	 * Converts the given Number value to a normalized double.
	 *
	 * @param value The Number value to normalize.
	 * @return The normalized double.
	 */
	private double valueToNormalized(T value) {
		if (0 == absoluteMaxValuePrim - absoluteMinValuePrim) {
			// prevent division by zero, simply return 0.
			return 0d;
		}
		return (value.doubleValue() - absoluteMinValuePrim) / (absoluteMaxValuePrim - absoluteMinValuePrim);
	}

	/**
	 * Converts a normalized value into screen space.
	 *
	 * @param normalizedCoord The normalized value to convert.
	 * @return The converted value in screen space.
	 */
	private float normalizedToScreen(double normalizedCoord) {
		return (float) (padding + normalizedCoord * (getWidth() - 2 * padding));
	}

	/**
	 * Converts screen space x-coordinates into normalized values.
	 *
	 * @param screenCoord The x-coordinate in screen space to convert.
	 * @return The normalized value.
	 */
	private double screenToNormalized(float screenCoord) {
		int width = getWidth();
		if (width <= 2 * padding) {
			// prevent division by zero, simply return 0.
			return 0d;
		} else {
			double result = (screenCoord - padding) / (width - 2 * padding);
			return Math.min(1d, Math.max(0d, result));
		}
	}

	/**
	 * Callback listener interface to notify about changed range values.
	 *
	 * @param <T> The Number type the RangeSeekBar has been declared with.
	 * @author Stephan Tittel (stephan.tittel@kom.tu-darmstadt.de)
	 */
	public interface OnRangeSeekBarChangeListener<T> {
		void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, T minValue, T maxValue);

		void onRangeSeekBarTracking(RangeSeekBar<?> tRangeSeekBar, T selectedMinValue, T selectedMaxValue);
	}

	/**
	 * Thumb constants (min and max).
	 */
	private static enum Thumb {
		MIN, MAX
	}

	/**
	 * Utility enumaration used to convert between Numbers and doubles.
	 *
	 * @author Stephan Tittel (stephan.tittel@kom.tu-darmstadt.de)
	 */
	private static enum NumberType {
		LONG, DOUBLE, INTEGER, FLOAT, SHORT, BYTE, BIG_DECIMAL;

		public static <E extends Number> NumberType fromNumber(E value) throws IllegalArgumentException {
			if (value instanceof Long) {
				return LONG;
			}
			if (value instanceof Double) {
				return DOUBLE;
			}
			if (value instanceof Integer) {
				return INTEGER;
			}
			if (value instanceof Float) {
				return FLOAT;
			}
			if (value instanceof Short) {
				return SHORT;
			}
			if (value instanceof Byte) {
				return BYTE;
			}
			if (value instanceof BigDecimal) {
				return BIG_DECIMAL;
			}
			throw new IllegalArgumentException("Number class '" + value.getClass().getName() + "' is not supported");
		}

		public Number toNumber(double value) {
			switch (this) {
				case LONG:
					return new Long((long) value);
				case DOUBLE:
					return value;
				case INTEGER:
					return new Integer((int) value);
				case FLOAT:
					return new Float(value);
				case SHORT:
					return new Short((short) value);
				case BYTE:
					return new Byte((byte) value);
				case BIG_DECIMAL:
					return new BigDecimal(value);
			}
			throw new InstantiationError("can't convert " + this + " to a Number object");
		}
	}
}