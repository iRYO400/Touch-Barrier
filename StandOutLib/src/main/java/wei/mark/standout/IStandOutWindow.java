package wei.mark.standout;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RemoteViews;

import java.util.List;

import wei.mark.standout.StandOutWindow.StandOutLayoutParams;
import wei.mark.standout.constants.StandOutFlags;
import wei.mark.standout.ui.Window;

public interface IStandOutWindow {
    /**
     * Return the icon resource for every window in this implementation. The
     * icon will appear in the default implementations of the system window
     * decoration and notifications.
     *
     * @return The icon.
     */
    int getAppIcon();

    /**
     * Return the name of every window in this implementation. The name will
     * appear in the default implementations of the system window decoration
     * title and notification titles.
     *
     * @return The name.
     */
    String getAppName();

    /**
     * Create a new {@link View} corresponding to the id, and add it as a child
     * to the frame. The view will become the contents of this StandOut window.
     * The view MUST be newly created, and you MUST attach it to the frame.
     *
     * <p>
     * If you are inflating your view from XML, make sure you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)} to attach your
     * view to frame. Set the ViewGroup to be frame, and the boolean to true.
     *
     * <p>
     * If you are creating your view programmatically, make sure you use
     * {@link FrameLayout#addView(View)} to add your view to the frame.
     *
     * @param id    The id representing the window.
     * @param frame The {@link FrameLayout} to attach your view as a child to.
     */
    void createAndAttachView(int id, FrameLayout frame);

    /**
     * Return the {@link StandOutWindow#getParams(int, Window)} for the corresponding id.
     * The system will set the layout params on the view for this StandOut
     * window. The layout params may be reused.
     *
     * @param id     The id of the window.
     * @param window The window corresponding to the id. Given as courtesy, so you
     *               may get the existing layout params.
     * @return The {@link StandOutWindow#getParams(int, Window)} corresponding to the id.
     * The layout params will be set on the window. The layout params
     * returned will be reused whenever possible, minimizing the number
     * of times getParams() will be called.
     */
    StandOutLayoutParams getParams(int id, Window window);

    /**
     * Implement this method to change modify the behavior and appearance of the
     * window corresponding to the id.
     *
     * <p>
     * You may use any of the flags defined in {@link StandOutFlags}. This
     * method will be called many times, so keep it fast.
     *
     * <p>
     * Use bitwise OR (|) to set flags, and bitwise XOR (^) to unset flags. To
     * test if a flag is set, use {@link Utils#isSet(int, int)}.
     *
     * @param id The id of the window.
     * @return A combination of flags.
     */
    default int getFlags(int id) {
        return 0;
    }

    /**
     * Implement this method to set a custom title for the window corresponding
     * to the id.
     *
     * @param id The id of the window.
     * @return The title of the window.
     */
    default String getTitle(int id) {
        return getAppName();
    }

    /**
     * Implement this method to set a custom icon for the window corresponding
     * to the id.
     *
     * @param id The id of the window.
     * @return The icon of the window.
     */
    default int getIcon(int id) {
        return getAppIcon();
    }

    /**
     * Return the title for the persistent notification. This is called every
     * time {@link #show(int)} is called.
     *
     * @param id The id of the window shown.
     * @return The title for the persistent notification.
     */
    default String getPersistentNotificationTitle(int id) {
        return getAppName() + " Running";
    }

    /**
     * Return the message for the persistent notification. This is called every
     * time {@link #show(int)} is called.
     *
     * @param id The id of the window shown.
     * @return The message for the persistent notification.
     */
    default String getPersistentNotificationMessage(int id) {
        return "";
    }

    /**
     * Return the intent for the persistent notification. This is called every
     * time {@link #show(int)} is called.
     *
     * <p>
     * The returned intent will be packaged into a {@link PendingIntent} to be
     * invoked when the user clicks the notification.
     *
     * @param id The id of the window shown.
     * @return The intent for the persistent notification.
     */
    default Intent getPersistentNotificationIntent(int id) {
        return null;
    }

    /**
     * Return the icon resource for every hidden window in this implementation.
     * The icon will appear in the default implementations of the hidden
     * notifications.
     *
     * @return The icon.
     */
    default int getHiddenIcon() {
        return getAppIcon();
    }

    /**
     * Return the title for the hidden notification corresponding to the window
     * being hidden.
     *
     * @param id The id of the hidden window.
     * @return The title for the hidden notification.
     */
    default String getHiddenNotificationTitle(int id) {
        return getAppName() + " Hidden";
    }

    /**
     * Return the message for the hidden notification corresponding to the
     * window being hidden.
     *
     * @param id The id of the hidden window.
     * @return The message for the hidden notification.
     */
    default String getHiddenNotificationMessage(int id) {
        return "";
    }

    /**
     * Return the intent for the hidden notification corresponding to the window
     * being hidden.
     *
     * <p>
     * The returned intent will be packaged into a {@link PendingIntent} to be
     * invoked when the user clicks the notification.
     *
     * @param id The id of the hidden window.
     * @return The intent for the hidden notification.
     */
    default Intent getHiddenNotificationIntent(int id) {
        return null;
    }

    default RemoteViews getPersistentNotificationRemoteViews(int id) {
        return null;
    }

    /**
     * Return a hidden {@link Notification} for the corresponding id. The system
     * will request a notification for every id that is hidden.
     *
     * <p>
     * If null is returned, StandOut will assume you do not wish to support
     * hiding this window, and will {@link #close(int)} it for you.
     *
     * <p>
     * See the StandOutExample project for an implementation of
     * {@link #getHiddenNotification(int)} that for every hidden window keeps a
     * notification which restores that window upon user's click.
     *
     * @param id The id of the window.
     * @return The {@link Notification} corresponding to the id or null.
     */
    Notification getHiddenNotification(int id);

    /**
     * Implement this method to set a custom theme for all windows in this
     * implementation.
     *
     * @return The theme to set on the window, or 0 for device default.
     */
    default int getThemeStyle() {
        return 0;
    }

    /**
     * You probably want to leave this method alone and implement
     * {@link #getDropDownItems(int)} instead. Only implement this method if you
     * want more control over the drop down menu.
     *
     * <p>
     * Implement this method to set a custom drop down menu when the user clicks
     * on the icon of the window corresponding to the id. The icon is only shown
     * when {@link StandOutFlags#FLAG_DECORATION_SYSTEM} is set.
     *
     * @param id The id of the window.
     * @return The drop down menu to be anchored to the icon, or null to have no
     * dropdown menu.
     */
    PopupWindow getDropDown(int id);

    /**
     * Implement this method to be alerted to touch events in the body of the
     * window corresponding to the id.
     *
     * <p>
     * Note that even if you set {FLAG_DECORATION_SYSTEM}, you will not
     * receive touch events from the system window decorations.
     *
     * @param id     The id of the view, provided as a courtesy.
     * @param window The window corresponding to the id, provided as a courtesy.
     * @param view   The view where the event originated from.
     * @param event  See linked method.
     * @see {@link View.OnTouchListener#onTouch(View, MotionEvent)}
     */
    default boolean onTouchBody(int id, Window window, View view,
                                MotionEvent event) {
        return false;
    }

    /**
     * Implement this method to be alerted to when the window corresponding to
     * the id is moved.
     *
     * @param id     The id of the view, provided as a courtesy.
     * @param window The window corresponding to the id, provided as a courtesy.
     * @param view   The view where the event originated from.
     * @param event  See linked method.
     * @see {@link #onTouchHandleMove(int, Window, View, MotionEvent)}
     */
    default void onMove(int id, Window window, View view, MotionEvent event) {

    }

    /**
     * Implement this method to be alerted to when the window corresponding to
     * the id is resized.
     *
     * @param id     The id of the view, provided as a courtesy.
     * @param window The window corresponding to the id, provided as a courtesy.
     * @param view   The view where the event originated from.
     * @param event  See linked method.
     * @see {@link #onTouchHandleResize(int, Window, View, MotionEvent)}
     */
    default void onResize(int id, Window window, View view, MotionEvent event) {

    }

    /**
     * Implement this callback to be alerted when a window corresponding to the
     * id is about to be shown. This callback will occur before the view is
     * added to the window manager.
     *
     * @param id     The id of the view, provided as a courtesy.
     * @param window The view about to be shown.
     * @return Return true to cancel the view from being shown, or false to
     * continue.
     * @see #show(int)
     */
    default boolean onShow(int id, Window window) {
        return false;
    }

    /**
     * Implement this callback to be alerted when a window corresponding to the
     * id is about to be hidden. This callback will occur before the view is
     * removed from the window manager and {@link #getHiddenNotification(int)}
     * is called.
     *
     * @param id     The id of the view, provided as a courtesy.
     * @param window The view about to be hidden.
     * @return Return true to cancel the view from being hidden, or false to
     * continue.
     * @see #hide(int)
     */
    default boolean onHide(int id, Window window) {
        return false;
    }

    /**
     * Implement this callback to be alerted when a window corresponding to the
     * id is about to be closed. This callback will occur before the view is
     * removed from the window manager.
     *
     * @param id     The id of the view, provided as a courtesy.
     * @param window The view about to be closed.
     * @return Return true to cancel the view from being closed, or false to
     * continue.
     * @see #close(int)
     */
    default boolean onClose(int id, Window window) {
        return false;
    }

    /**
     * Implement this callback to be alerted when all windows are about to be
     * closed. This callback will occur before any views are removed from the
     * window manager.
     *
     * @return Return true to cancel the views from being closed, or false to
     * continue.
     * @see #closeAll()
     */
    default boolean onCloseAll() {
        return false;
    }

    /**
     * Implement this method to populate the drop down menu when the user clicks
     * on the icon of the window corresponding to the id. The icon is only shown
     * when {@link StandOutFlags#FLAG_DECORATION_SYSTEM} is set.
     *
     * @param id The id of the window.
     * @return The list of items to show in the drop down menu, or null or empty
     * to have no dropdown menu.
     */
    default List<DropDownListItem> getDropDownItems(int id) {
        return null;
    }

    /**
     * Implement this callback to be alerted when a window corresponding to the
     * id has received some data. The sender is described by fromCls and fromId
     * if the sender wants a result. To send a result, use
     * {@link #sendData(int, Class, int, int, Bundle)}.
     *
     * @param id          The id of your receiving window.
     * @param requestCode The sending window provided this request code to declare what
     *                    kind of data is being sent.
     * @param data        A bundle of parceleable data that was sent to your receiving
     *                    window.
     * @param fromCls     The sending window's class. Provided if the sender wants a
     *                    result.
     * @param fromId      The sending window's id. Provided if the sender wants a
     *                    result.
     */
    default void onReceiveData(int id, int requestCode, Bundle data,
                               Class<? extends IStandOutWindow> fromCls, int fromId) {

    }

    /**
     * Implement this callback to be alerted when a window corresponding to the
     * id is about to be updated in the layout. This callback will occur before
     * the view is updated by the window manager.
     *
     * @param id     The id of the window, provided as a courtesy.
     * @param window The window about to be updated.
     * @param params The updated layout params.
     * @return Return true to cancel the window from being updated, or false to
     * continue.
     * @see #updateViewLayout(int, BaseStandOutLayoutParams)
     */
    default boolean onUpdate(int id, Window window, BaseStandOutLayoutParams params) {
        return false;
    }

    /**
     * Implement this callback to be alerted when a window corresponding to the
     * id is about to be bought to the front. This callback will occur before
     * the window is brought to the front by the window manager.
     *
     * @param id     The id of the window, provided as a courtesy.
     * @param window The window about to be brought to the front.
     * @return Return true to cancel the window from being brought to the front,
     * or false to continue.
     * @see #bringToFront(int)
     */
    default boolean onBringToFront(int id, Window window) {
        return false;
    }

    /**
     * Implement this callback to be alerted when a window corresponding to the
     * id is about to have its focus changed. This callback will occur before
     * the window's focus is changed.
     *
     * @param id     The id of the window, provided as a courtesy.
     * @param window The window about to be brought to the front.
     * @param focus  Whether the window is gaining or losing focus.
     * @return Return true to cancel the window's focus from being changed, or
     * false to continue.
     * @see #focus(int)
     */
    default boolean onFocusChange(int id, Window window, boolean focus) {
        return false;
    }

    /**
     * Implement this callback to be alerted when a window corresponding to the
     * id receives a key event. This callback will occur before the window
     * handles the event with {@link Window#dispatchKeyEvent(KeyEvent)}.
     *
     * @param id     The id of the window, provided as a courtesy.
     * @param window The window about to receive the key event.
     * @param event  The key event.
     * @return Return true to cancel the window from handling the key event, or
     * false to let the window handle the key event.
     * @see {@link Window#dispatchKeyEvent(KeyEvent)}
     */
    default boolean onKeyEvent(int id, Window window, KeyEvent event) {
        return false;
    }

    /**
     * Show or restore a window corresponding to the id. Return the window that
     * was shown/restored.
     *
     * @param id The id of the window.
     * @return The window shown.
     */
    Window show(int id);

    /**
     * Hide a window corresponding to the id. Show a notification for the hidden
     * window.
     *
     * @param id The id of the window.
     */
    void hide(int id);

    /**
     * Close a window corresponding to the id.
     *
     * @param id The id of the window.
     */
    void close(int id);

    /**
     * Close all existing windows.
     */
    void closeAll();

    /**
     * Send {@link android.os.Parcelable} data in a {@link Bundle} to a new or existing
     * windows. The implementation of the recipient window can handle what to do
     * with the data. To receive a result, provide the id of the sender.
     *
     * @param fromId      Provide the id of the sending window if you want a result.
     * @param toCls       The Service's class extending {@link StandOutWindow} that is
     *                    managing the receiving window.
     * @param toId        The id of the receiving window.
     * @param requestCode Provide a request code to declare what kind of data is being
     *                    sent.
     * @param data        A bundle of parceleable data to be sent to the receiving
     *                    window.
     */
    void sendData(int fromId,
                  Class<? extends IStandOutWindow> toCls, int toId, int requestCode,
                  Bundle data);

    /**
     * Bring the window corresponding to this id in front of all other windows.
     * The window may flicker as it is removed and restored by the system.
     *
     * @param id The id of the window to bring to the front.
     */
    void bringToFront(int id);

    /**
     * Request focus for the window corresponding to this id. A maximum of one
     * window can have focus, and that window will receive all key events,
     * including Back and Menu.
     *
     * @param id The id of the window.
     * @return True if focus changed successfully, false if it failed.
     */
    boolean focus(int id);

    /**
     * Remove focus for the window corresponding to this id. Once a window is
     * unfocused, it will stop receiving key events.
     *
     * @param id The id of the window.
     * @return True if focus changed successfully, false if it failed.
     */
    boolean unfocus(int id);

    /**
     * Remove focus for the window, which could belong to another application.
     * Since we don't allow windows from different applications to directly
     * interact with each other, except for
     * {@link #sendData(Context, Class, int, int, Bundle, Class, int)}, this
     * method is private.
     *
     * @param window The window to unfocus.
     * @return True if focus changed successfully, false if it failed.
     */
    boolean unfocus(Window window);

    /**
     * Courtesy method for your implementation to use if you want to. Gets a
     * unique id to assign to a new window.
     *
     * @return The unique id.
     */
    int getUniqueId();

    /**
     * Return the window that currently has focus.
     *
     * @return The window that has focus.
     */
    Window getFocusedWindow();

    /**
     * Sets the window that currently has focus.
     */
    void setFocusedWindow(Window window);

    /**
     * Internal touch handler for handling moving the window.
     *
     * @param id
     * @param window
     * @param view
     * @param event
     * @return
     * @see {@link View#onTouchEvent(MotionEvent)}
     */
    boolean onTouchHandleMove(int id, Window window, View view,
                              MotionEvent event);

    /**
     * Internal touch handler for handling resizing the window.
     *
     * @param id
     * @param window
     * @param view
     * @param event
     * @return
     * @see {@link View#onTouchEvent(MotionEvent)}
     */
    boolean onTouchHandleResize(int id, Window window, View view,
                                MotionEvent event);

    /**
     * Update the window corresponding to this id with the given params.
     *
     * @param id     The id of the window.
     * @param params The updated layout params to apply.
     */
    void updateViewLayout(int id, BaseStandOutLayoutParams params);

    Context getContext();

    Class<? extends Service> getImplClass();
}
