package services.upload

import entity.Trade
import services.upload.strategy.PersistStrategy


interface TradeUploadService {

    Iterable<Trade> fromExcel(InputStream fis, PersistStrategy strategy)

}