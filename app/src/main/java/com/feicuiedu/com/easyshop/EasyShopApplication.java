package com.feicuiedu.com.easyshop;

import com.feicuiedu.apphx.HxBaseApplication;
import com.feicuiedu.apphx.HxModuleInitializer;
import com.feicuiedu.apphx.model.repository.DefaultLocalInviteRepo;
import com.feicuiedu.apphx.model.repository.DefaultLocalUsersRepo;
import com.feicuiedu.com.easyshop.model.CachePreferences;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class EasyShopApplication extends HxBaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        /*对ImageLoader初始化*/
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)/*开启内存缓存*/
                .cacheOnDisk(true)/*开启硬盘缓存*/
                .resetViewBeforeLoading(true)/*在ImageView加载前清除它上面之前的图片*/
                .build();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheSize(4 * 1024 * 1024)/*设置内存缓存的大小(4M)*/
                .defaultDisplayImageOptions(displayImageOptions)
                .build();
        ImageLoader.getInstance().init(configuration);

        /*初始化用户偏好*/
        CachePreferences.init(this);
    }

    @Override
    protected void initHxModule(HxModuleInitializer initializer) {
        /*对环信模块做初始化*/
        initializer.setLocalInviteRepo(DefaultLocalInviteRepo.getInstance(this))
                .setLocalUsersRepo(DefaultLocalUsersRepo.getInstance(this))
                .setRemoteUsersRepo(new RemoteUsersRepo())
                .init();
    }
}
