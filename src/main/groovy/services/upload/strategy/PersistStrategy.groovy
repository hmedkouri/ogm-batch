package services.upload.strategy

import entity.Trade

interface PersistStrategy {

    Iterable<Trade> persist(List<Trade> trades)
}