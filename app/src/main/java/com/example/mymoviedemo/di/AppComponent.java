package com.example.mymoviedemo.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {AndroidInjectionModule.class,AppModule.class,ActivityBindingModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder{
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }

    void inject(MyMovieDemoApp app);
}
