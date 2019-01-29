// Generated code from Butter Knife. Do not modify!
package codelabs.com.service_valley.Activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import codelabs.com.service_valley.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class EditProfileActivity_ViewBinding implements Unbinder {
  private EditProfileActivity target;

  private View view2131296379;

  private View view2131296611;

  @UiThread
  public EditProfileActivity_ViewBinding(EditProfileActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public EditProfileActivity_ViewBinding(final EditProfileActivity target, View source) {
    this.target = target;

    View view;
    target.pageTitleToolbar = Utils.findRequiredViewAsType(source, R.id.pageTitleToolbar, "field 'pageTitleToolbar'", TextView.class);
    target.sideMenu = Utils.findRequiredViewAsType(source, R.id.sideMenu, "field 'sideMenu'", ImageButton.class);
    view = Utils.findRequiredView(source, R.id.homeBtn, "field 'homeBtn' and method 'OnHomeBtnClicked'");
    target.homeBtn = Utils.castView(view, R.id.homeBtn, "field 'homeBtn'", ImageButton.class);
    view2131296379 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.OnHomeBtnClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.updateProfile, "field 'updateProfile' and method 'OnUpdateClick'");
    target.updateProfile = Utils.castView(view, R.id.updateProfile, "field 'updateProfile'", TextView.class);
    view2131296611 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.OnUpdateClick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    EditProfileActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.pageTitleToolbar