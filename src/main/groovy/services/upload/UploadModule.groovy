package services.upload

import com.google.inject.AbstractModule


class UploadModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(TradeUploadService).to(TradeUploadServiceImpl)
    }
}
