
package com.mindorks.framework.mvvm.ui.login;



public interface LoginNavigator {

    void handleError(Throwable throwable);

    void login(boolean loginAsProfessor);

    void openMainActivity();
}
